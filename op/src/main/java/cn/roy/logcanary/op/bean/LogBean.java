package cn.roy.logcanary.op.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * @Description 日志bean
 * @Author Roy Z
 * @Date 2018/4/16
 * @Version V1.0.0
 */
public class LogBean implements Parcelable {
    public static final int VERBOSE = 0;
    public static final int DEBUG = 1;
    public static final int INFO = 2;
    public static final int WARN = 3;
    public static final int ERROR = 4;

    private int logLevel;
    private String logTag;
    //private transient String logText;// 不需要序列化此字段
    private String logText;
    private long date;

    public LogBean(int logLevel, String logTag, String logText) {
        this.logLevel = logLevel;
        this.logTag = logTag;
        this.logText = logText;

        this.date = System.currentTimeMillis();
    }

    protected LogBean(Parcel in) {
        logLevel = in.readInt();
        logTag = in.readString();
        logText = in.readString();
        date = in.readLong();
    }

    public static final Creator<LogBean> CREATOR = new Creator<LogBean>() {
        @Override
        public LogBean createFromParcel(Parcel in) {
            return new LogBean(in);
        }

        @Override
        public LogBean[] newArray(int size) {
            return new LogBean[size];
        }
    };

    public int getLogLevel() {
        return logLevel;
    }

    public String getLogLevelStr() {
        String s = "V";
        switch (logLevel) {
            case 1:
                s = "D";
                break;
            case 2:
                s = "I";
                break;
            case 3:
                s = "W";
                break;
            case 4:
                s = "E";
                break;
            default:
                break;
        }
        return s;
    }

    public void setLogLevel(int logLevel) {
        this.logLevel = logLevel;
    }

    public String getLogTag() {
        return logTag;
    }

    public void setLogTag(String logTag) {
        this.logTag = logTag;
    }

    public String getLogText() {
        return logText;
    }

    public void setLogText(String logText) {
        this.logText = logText;
    }

    public long getDate() {
        return date;
    }

    public boolean containKey(String key) {
        return logTag.contains(key) || logText.contains(key);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(logLevel);
        dest.writeString(logTag);
        dest.writeString(logText);
        dest.writeLong(date);
    }
}
