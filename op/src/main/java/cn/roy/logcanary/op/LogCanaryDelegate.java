package cn.roy.logcanary.op;

import android.content.Context;
import android.content.Intent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

import cn.roy.logcanary.op.bean.LogBean;
import cn.roy.logcanary.op.bean.TagBean;
import cn.roy.logcanary.op.component.LogService;
import cn.roy.logcanary.op.util.AppOpsManagerUtil;
import cn.roy.logcanary.op.util.LoggerUtil;

/**
 * @Description: 代理
 * @Author: Roy Z
 * @Date: 2020/4/6 14:59
 * @Version: v1.0
 */
public final class LogCanaryDelegate {
    public static final String INTENT_FILTER_LOG_EVENT = "intent_filter_log_event";
    private static LogCanaryDelegate instance;

    public static LogCanaryDelegate getInstance() {
        if (instance == null) {
            synchronized (LogCanaryDelegate.class) {
                if (instance == null) {
                    instance = new LogCanaryDelegate();
                }
            }
        }
        return instance;
    }

    private Context context;
    private int maxLogCount = 1000;
    private boolean isEnable = true;// 总开关
    private boolean isSyncSaveLog = true;// 同步保存日志开关
    private final LinkedBlockingQueue<LogBean> logBeanLinkedBlockingQueue;
    private final ReentrantLock lock;
    private final List<LogBean> originData;// TODO 存在竞态条件，需要考虑同步问题
    private final Map<String, TagBean> tagBeanMap;
    private final Map<Integer, Set<String>> levelTagMap;
    private volatile Worker mWorker;

    private LogCanaryDelegate() {
        lock = new ReentrantLock();
        logBeanLinkedBlockingQueue = new LinkedBlockingQueue<>();
        originData = new ArrayList<>();
        tagBeanMap = new HashMap<>();
        levelTagMap = new HashMap<>();
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public int getMaxLogCount() {
        return maxLogCount;
    }

    public void setMaxLogCount(int maxLogCount) {
        this.maxLogCount = maxLogCount;
    }

    public boolean isEnable() {
        return isEnable;
    }

    public void setEnable(boolean enable) {
        isEnable = enable;
    }

    public boolean isSyncSaveLog() {
        return isSyncSaveLog;
    }

    public void setSyncSaveLog(boolean syncSaveLog) {
        isSyncSaveLog = syncSaveLog;
    }

    public void addLog(int level, String tag, String msg) {
        if (isSyncSaveLog) {
            Logger logger = LoggerFactory.getLogger(tag);
            switch (level) {
                case LogBean.VERBOSE:
                    logger.trace(msg);
                    break;
                case LogBean.DEBUG:
                    logger.debug(msg);
                    break;
                case LogBean.INFO:
                    logger.info(msg);
                    break;
                case LogBean.WARN:
                    logger.warn(msg);
                    break;
                case LogBean.ERROR:
                    logger.error(msg);
                    break;
                default:
                    break;
            }
        }
        if (isEnable) {
            // TODO 序列化数据过长处理
            int length = msg.length();
            int step = 1024;
            if (length > step) {
                int size = length / step + 1;
                for (int i = 0; i < size; i++) {
                    int start = step * i;
                    int end = step * (i + 1);
                    if (end > length) {
                        end = length - 1;
                    }
                    String subMsg = msg.substring(start, end);
                    LogBean bean = new LogBean(level, tag, subMsg);
                    logBeanLinkedBlockingQueue.offer(bean);
                }
            } else {
                LogBean bean = new LogBean(level, tag, msg);
                logBeanLinkedBlockingQueue.offer(bean);
            }

            if (mWorker == null) {
                synchronized (this) {
                    if (mWorker == null) {
                        mWorker = new Worker();
                    }
                    new Thread(mWorker).start();
                }
            }
        }
    }

    public int getLogCount() {
        return originData.size();
    }

    public Set<String> getLogTagList(Set<Integer> levels) {
        Set<String> tags = new HashSet<>();
        for (Integer level : levels) {
            Set<String> tagSet = levelTagMap.get(level);
            if (tagSet != null && !tagSet.isEmpty()) {
                tags.addAll(tagSet);
            }
        }
        return tags;
    }

    public List<LogBean> getLogListByLevels(Set<Integer> levels) {
        List<LogBean> logBeans = new ArrayList<>();
        lock.lock();
        try {
            for (LogBean bean : originData) {
                if (levels.contains(bean.getLogLevel())) {
                    logBeans.add(bean);
                }
            }
        } finally {
            lock.unlock();
        }
        return logBeans;
    }

    public List<LogBean> getLogList(Set<Integer> levels, Set<String> tags) {
        List<LogBean> logBeans = new ArrayList<>();
        for (String tag : tags) {
            TagBean tagBean = tagBeanMap.get(tag);
            if (tagBean != null) {
                List<LogBean> logBeanList = tagBean.logBeanList;
                if (!logBeanList.isEmpty()) {
                    for (LogBean bean : logBeanList) {
                        if (levels.contains(bean.getLogLevel())) {
                            logBeans.add(bean);
                        }
                    }
                }
            }
        }
        return logBeans;
    }

    public List<LogBean> getAllLogList() {
        return new ArrayList<>(originData);
    }

    public void clear() {
        logBeanLinkedBlockingQueue.clear();
        originData.clear();
        tagBeanMap.clear();
        levelTagMap.clear();
    }

    public void close() {
        isEnable = false;
        if (mWorker != null) {
            mWorker.stop();
            mWorker = null;
        }
    }

    private class Worker implements Runnable {
        private boolean run = true;

        public void stop() {
            this.run = false;
        }

        @Override
        public void run() {
            while (run) {
                try {
                    LogBean logBean = logBeanLinkedBlockingQueue.take();// 当无对象的时候，该方法会堵塞
                    originData.add(logBean);
                    int size = originData.size();
                    if (size > maxLogCount) {
                        lock.lock();
                        try {
                            int removeSize = size - maxLogCount + 1;
                            List<LogBean> removeLogBeans = originData.subList(0, removeSize);
                            originData.removeAll(removeLogBeans);
                        } finally {
                            lock.unlock();
                        }
                    }
                    String tag = logBean.getLogTag();
                    int level = logBean.getLogLevel();
                    // 维护tagBeanMap
                    TagBean tagBean = tagBeanMap.get(tag);
                    if (tagBean == null) {
                        tagBean = new TagBean();
                        tagBean.tagName = tag;
                        tagBean.logBeanList = new ArrayList<>();
                        tagBean.logBeanList.add(logBean);
                        tagBeanMap.put(tag, tagBean);
                    } else {
                        List<LogBean> logBeanList = tagBean.logBeanList;
                        logBeanList.add(logBean);
                    }
                    // 维护levelTagMap
                    Set<String> tagSet = levelTagMap.get(level);
                    if (tagSet == null) {
                        tagSet = new HashSet<>();
                        levelTagMap.put(level, tagSet);
                    }
                    tagSet.add(tag);
                    // 广播通知
                    sendBroadcast(logBean);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private void sendBroadcast(LogBean bean) {
            if (context == null) {
                return;
            }
            // 广播数据
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction(INTENT_FILTER_LOG_EVENT);
            broadcastIntent.putExtra("data", bean);
            context.sendBroadcast(broadcastIntent);
            // 判断是否有权限
            if (AppOpsManagerUtil.checkDrawOverlays(context)) {
                // 构建日志实体，显示在悬浮窗口
                Intent intent = new Intent(context, LogService.class);
                intent.putExtra("data", bean);
                context.startService(intent);
            }
        }
    }

}
