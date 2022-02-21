package cn.roy.logcanary.op.component;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

import cn.roy.logcanary.op.LogCanaryDelegate;
import cn.roy.logcanary.op.R;
import cn.roy.logcanary.op.adapter.CommonAdapter;
import cn.roy.logcanary.op.adapter.base.ViewHolder;
import cn.roy.logcanary.op.bean.LogBean;
import cn.roy.logcanary.op.util.AppOpsManagerUtil;
import cn.roy.logcanary.op.view.LogListAdapter;

/**
 * @Description: 悬浮日志页面
 * @Author: Roy Z
 * @Date: 2021/07/22
 * @Version: v1.0
 */
public class LogListActivity extends AppCompatActivity {
    private TextView tvLevelFilter;
    private EditText etFilter;
    private RecyclerView recyclerView;

    private LogCanaryDelegate logCanaryDelegate;
    private List<LogBean> logBeanList;
    private int filterLevel = 0;
    private String filterText = "";
    private List<LogBean> logListAdapterData;
    private LogListAdapter logListAdapter;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null) {
                LogBean data = intent.getParcelableExtra("data");
                if (data != null) {
                    logBeanList.add(data);
                    if (data.getLogLevel() >= filterLevel
                            && TextUtils.isEmpty(filterText) || data.containKey(filterText)) {
                        logListAdapterData.add(data);
                        if (logListAdapter != null) {
                            logListAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            // 清除透明状态栏
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS |
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            // 需要设置这个flag才能调用setStatusBarColor来设置状态栏颜色
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            // 设置状态栏颜色
            window.setStatusBarColor(Color.BLACK);
            // 设置导航栏颜色
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_log_list);

        logCanaryDelegate = LogCanaryDelegate.getInstance();
        initDate();
        initView();
        IntentFilter intentFilter = new IntentFilter(LogCanaryDelegate.INTENT_FILTER_LOG_EVENT);
        registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(broadcastReceiver);
    }

    private void initDate() {
        logBeanList = new ArrayList<>();
        logBeanList.addAll(logCanaryDelegate.getAllLogList());
        logListAdapterData = new ArrayList<>();
        logListAdapterData.addAll(logBeanList);
    }

    private void initView() {
        tvLevelFilter = findViewById(R.id.tv_level);
        tvLevelFilter.setOnClickListener(v -> {
            showLevelFilter();
        });
        etFilter = findViewById(R.id.etFilter);
        etFilter.setOnEditorActionListener((v, actionId, event) -> {
            String keyword = etFilter.getText().toString().trim();
            if (actionId == EditorInfo.IME_ACTION_SEARCH && !TextUtils.isEmpty(keyword)) {
                filterText = keyword;
                getFilteredData();
            }
            return true;
        });
        etFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!filterText.equals(s.toString())) {
                    filterText = s.toString();
                    getFilteredData();
                }
            }
        });
        findViewById(R.id.ic_action_more).setOnClickListener(v -> {
            showOperationDialog();
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL,
                false));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(new ColorDrawable(Color.parseColor("#999999")));
        recyclerView.addItemDecoration(dividerItemDecoration);
        logListAdapter = new LogListAdapter(this, logListAdapterData);
        recyclerView.setAdapter(logListAdapter);
    }

    private PopupWindow levelFilterPopupWindow;

    private void showLevelFilter() {
        if (levelFilterPopupWindow == null) {
            levelFilterPopupWindow = new PopupWindow(this);
            RecyclerView recyclerView = new RecyclerView(this);
            recyclerView.setLayoutManager(new LinearLayoutManager(this,
                    LinearLayoutManager.VERTICAL,
                    false));
            List<String> levels = new ArrayList<>();
            levels.add("Verbose");
            levels.add("Debug");
            levels.add("Info");
            levels.add("Warn");
            levels.add("Error");
            CommonAdapter<String> adapter = new CommonAdapter<String>(this,
                    R.layout.item_log_list_level_filter, levels) {
                @Override
                protected void convert(ViewHolder viewHolder, String level, int position) {
                    ((TextView) viewHolder.itemView).setText(level);
                    viewHolder.itemView.setOnClickListener(v -> {
                        filterLevel = position;
                        tvLevelFilter.setText(level);
                        levelFilterPopupWindow.dismiss();
                        getFilteredData();
                    });
                }
            };
            recyclerView.setAdapter(adapter);
            levelFilterPopupWindow.setContentView(recyclerView);
            levelFilterPopupWindow.setWidth(tvLevelFilter.getWidth() + 20);
            levelFilterPopupWindow.setBackgroundDrawable(
                    getResources().getDrawable(R.drawable.shape_black_5dp));
            levelFilterPopupWindow.setOutsideTouchable(true);
        }
        if (!levelFilterPopupWindow.isShowing()) {
            levelFilterPopupWindow.showAsDropDown(tvLevelFilter, 0, 10);
        }
    }

    private void getFilteredData() {
        List<LogBean> data = new ArrayList<>();
        for (LogBean item : logBeanList) {
            if (item.getLogLevel() >= filterLevel) {
                if (TextUtils.isEmpty(filterText) || item.containKey(filterText)) {
                    data.add(item);
                }
            }
        }
        logListAdapterData.clear();
        logListAdapterData.addAll(data);
        logListAdapter.notifyDataSetChanged();
    }

    private BottomSheetDialog bottomSheetDialog;

    private void showOperationDialog() {
        if (bottomSheetDialog == null) {
            bottomSheetDialog = new BottomSheetDialog(this);
            bottomSheetDialog.setContentView(R.layout.dialog_float_log_manage);
            bottomSheetDialog.setCancelable(true);
            bottomSheetDialog.setCanceledOnTouchOutside(true);
            // 悬浮窗权限管理
            bottomSheetDialog.findViewById(R.id.vApplyPermission).setOnClickListener(v -> {
                bottomSheetDialog.hide();
                requestDrawOverlays();
            });
            // 缓存日志条数设置
            EditText etMaxCount = bottomSheetDialog.findViewById(R.id.etMaxCount);
            etMaxCount.setOnEditorActionListener((textView, i, keyEvent) -> {
                        if (i == EditorInfo.IME_ACTION_DONE) {
                            logCanaryDelegate.setMaxLogCount(Integer.valueOf(etMaxCount.getText().toString()));
                            bottomSheetDialog.hide();
                        }
                        return false;
                    }
            );
            // 日志功能总开关
            Switch sLogSwitch = bottomSheetDialog.findViewById(R.id.sLogSwitch);
            sLogSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
                logCanaryDelegate.setEnable(b);
            });
            // 同步保存日志开关
            Switch sSyncSaveLog = bottomSheetDialog.findViewById(R.id.sSyncSaveLog);
            sSyncSaveLog.setOnCheckedChangeListener((compoundButton, b) -> {
                logCanaryDelegate.setSyncSaveLog(b);
            });
            // 清除数据
            bottomSheetDialog.findViewById(R.id.vCleanData).setOnClickListener(v -> {
                bottomSheetDialog.hide();
                logBeanList.clear();
                logListAdapterData.clear();
                logListAdapter.notifyDataSetChanged();
                logCanaryDelegate.clear();
            });
            // 关闭页面
            bottomSheetDialog.findViewById(R.id.vExit).setOnClickListener(v -> {
                bottomSheetDialog.hide();
                finish();
            });
            // 取消
            bottomSheetDialog.findViewById(R.id.tvCancel).setOnClickListener(v -> {
                bottomSheetDialog.hide();
            });
            View parentView = bottomSheetDialog.getDelegate()
                    .findViewById(com.google.android.material.R.id.design_bottom_sheet);
            parentView.setBackgroundColor(Color.TRANSPARENT);
        }
        TextView tvFloatWindowPermissionStatus = bottomSheetDialog.findViewById(R.id.tvFloatWindowPermissionStatus);
        if (AppOpsManagerUtil.checkDrawOverlays(this)) {
            tvFloatWindowPermissionStatus.setText("已开启");
        } else {
            tvFloatWindowPermissionStatus.setText("已关闭");
        }
        // 最大缓存条数
        EditText etMaxCount = bottomSheetDialog.findViewById(R.id.etMaxCount);
        etMaxCount.setText(String.valueOf(logCanaryDelegate.getMaxLogCount()));
        etMaxCount.setSelection(etMaxCount.getText().length());
        // 日志功能总开关
        Switch sLogSwitch = bottomSheetDialog.findViewById(R.id.sLogSwitch);
        sLogSwitch.setChecked(logCanaryDelegate.isEnable());
        // 同步保存日志开关
        Switch sSyncSaveLog = bottomSheetDialog.findViewById(R.id.sSyncSaveLog);
        sSyncSaveLog.setChecked(logCanaryDelegate.isSyncSaveLog());
        // 日志条数
        TextView tvLogCount = bottomSheetDialog.findViewById(R.id.tvLogCount);
        tvLogCount.setText(String.valueOf(logCanaryDelegate.getLogCount()));
        bottomSheetDialog.show();
    }

    private static final int OVERLAYS_CODE = 10001;

    @TargetApi(23)
    private void requestDrawOverlays() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, OVERLAYS_CODE);
    }
}
