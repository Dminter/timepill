package com.zncm.timepill.utils;

import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.UnknownHostException;

//log
public class LogContent {

    enum Type {
        Error, // 错误
        Warning, // 警告
        Information, // 信息
        Debug, // 调试
        Verbose; // 可见的
    }

    public static String EMPTY = "";
    public static String NULL = "null";

    private Throwable tr = null;
    private String message;
    private String tag;
    private Type type;

    public LogContent() {
    }

    public LogContent(Type type, String msg) {
        this.type = type;
        this.message = msg;
    }

    public LogContent(Type type, String msg, Throwable tr) {
        this.type = type;
        this.message = msg;
        this.tr = tr;
    }

    public void flush() {
        try {
            StackTraceElement stackTraceElement = getStackTraceElement(Thread
                    .currentThread().getStackTrace());
            tag = getTag(stackTraceElement);
            if (message == null)
                message = NULL;
            if (tr != null && !(tr instanceof UnknownHostException)) {
                message += getThrowableString(tr);
            } else {
                message += getMsgEnd(stackTraceElement);
            }
        } catch (Exception e) {
            tag = EMPTY;
            message = NULL;
            e.printStackTrace();
        }

        switch (type) {
            case Error:
                Log.e(tag, message);
                break;
            case Warning:
                Log.w(tag, message);
                break;
            case Information:
                Log.i(tag, message);
                break;
            case Debug:
                Log.d(tag, message);
                break;
            case Verbose:
                Log.v(tag, message);
                break;
            default:
                break;
        }
    }

    /**
     * 获取当前程序Tag
     *
     * @throws
     * @Title: getTag String
     */
    private static String getTag(StackTraceElement stackTraceElement) {
        return StrUtil.getShortClassName(stackTraceElement.getClassName())
                + "." + stackTraceElement.getMethodName();
    }

    /**
     * 获取文件位置
     *
     * @param stackTraceElement
     * @return String
     * @throws
     * @Title: getMsgEnd
     */
    private static String getMsgEnd(StackTraceElement stackTraceElement) {
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(StrUtil.makeLongRepeatString(" ", 60));
        strBuilder.append("at ");
        strBuilder.append(stackTraceElement.getClassName());
        strBuilder.append(".");
        strBuilder.append(stackTraceElement.getMethodName());
        strBuilder.append("(");
        strBuilder.append(stackTraceElement.getFileName());
        strBuilder.append(":");
        strBuilder.append(stackTraceElement.getLineNumber());
        strBuilder.append(")");
        return strBuilder.toString();
    }

    /**
     * @param tr
     * @return String
     * @throws
     * @Title: getThrowableString
     */
    public static String getThrowableString(Throwable tr) {
        if (tr == null) {
            return "";
        }

        Throwable t = tr;
        while (t != null) {
            t = t.getCause();
        }

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        tr.printStackTrace(pw);
        return sw.toString();
    }

    private static StackTraceElement getStackTraceElement(
            StackTraceElement stackTraceElements[]) {
        boolean doNext = false;
        for (int i = 0; i < stackTraceElements.length; i++) {
            StackTraceElement stackTraceElement = stackTraceElements[i];
            if (stackTraceElement != null) {
                String methodName = stackTraceElement.getMethodName();
                if (doNext) {
                    return stackTraceElement;
                }
                doNext = methodName.equals("i") || methodName.equals("e")
                        || methodName.equals("d") || methodName.equals("v")
                        || methodName.equals("w")
                        || methodName.equals("handleException");
            } else {
                return null;
            }
        }
        return null;
    }

}
