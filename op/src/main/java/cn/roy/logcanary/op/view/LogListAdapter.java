package cn.roy.logcanary.op.view;

import android.content.Context;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import cn.roy.logcanary.op.R;
import cn.roy.logcanary.op.adapter.CommonAdapter;
import cn.roy.logcanary.op.adapter.base.ViewHolder;
import cn.roy.logcanary.op.bean.LogBean;

/**
 * @Description 日志适配器
 * @Author Roy Z
 * @Date 2018/4/16
 * @Version V1.0.0
 */
public class LogListAdapter extends CommonAdapter<LogBean> {
    private SimpleDateFormat simpleDateFormat;

    private static final int[] colors = {R.color.colorLogVerbose, R.color.colorLogDebug,
            R.color.colorLogInfo, R.color.colorLogWarn, R.color.colorLogError};

    public LogListAdapter(Context context, List<LogBean> datas) {
        super(context, R.layout.item_log_detail, datas);

        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.CHINA);
    }

    @Override
    protected void convert(ViewHolder viewHolder, LogBean bean, int i) {
        TextView tvTag = viewHolder.getView(R.id.tv_tag);
        TextView tvLog = viewHolder.getView(R.id.tv_log);
        int type = bean.getLogLevel();
        String time = simpleDateFormat.format(bean.getDate());
        String text = String.format(Locale.CHINA, "[%s %s/%s]:", time, bean.getLogLevelStr(),
                bean.getLogTag());
        tvTag.setTextColor(mContext.getResources().getColor(colors[type]));
        tvLog.setTextColor(mContext.getResources().getColor(colors[type]));
        tvTag.setText(text);
        tvLog.setText(bean.getLogText());
    }
}
