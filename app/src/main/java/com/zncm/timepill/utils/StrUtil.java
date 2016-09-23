package com.zncm.timepill.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.text.ClipboardManager;
import android.widget.EditText;

import com.zncm.timepill.global.TpApplication;
import com.zncm.timepill.utils.XUtil;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//字符串工具类
@SuppressWarnings("deprecation")
public class StrUtil {
    public static long getFolderSize(File file) {
        long size = 0;
        try {
            File flist[] = file.listFiles();
            if (flist != null) {
                for (int i = 0; i < flist.length; i++) {
                    if (flist[i].isDirectory()) {
                        size = size + getFolderSize(flist[i]);
                    } else {
                        size = size + flist[i].length();
                    }
                }
            }
        } catch (Exception e) {
        }
        return size;
    }

    public static String formatFileSize(long size) {
        long SIZE_KB = 1024;
        long SIZE_MB = SIZE_KB * 1024;
        long SIZE_GB = SIZE_MB * 1024;
        if (size < SIZE_KB) {
            return String.format("%d B", (int) size);
        } else if (size < SIZE_MB) {
            return String.format("%.2f KB", (float) size / SIZE_KB);
        } else if (size < SIZE_GB) {
            return String.format("%.2f MB", (float) size / SIZE_MB);
        } else {
            return String.format("%.2f GB", (float) size / SIZE_GB);
        }
    }


    public static String timeYear(String time) {
        String ret = "";
        if (notEmptyOrNull(time) && time.contains(" ")) {
            ret = time.substring(0, time.indexOf(" "));
        }
        return ret;
    }

    public static String timeDay(String time) {
        String ret = "";
        if (notEmptyOrNull(time) && time.contains(" ")) {
            ret = time.substring(time.indexOf(" ") + 1);
        }
        return ret;
    }

    public static String timeDate(String time) {
        String ret = "";
        if (notEmptyOrNull(time) && time.contains(" ")) {
            ret = time.substring(0, time.indexOf(" "));
        }
        return ret;
    }


    public static void etSave(EditText... ets) {
        for (int i = 0; i < ets.length; i++) {
            if (notEmptyOrNull(ets[i].getText().toString().trim()) && !ets[i].getText().toString().trim().contains("@")) {
                TpApplication.setHistoryEtMap(String.valueOf(ets[i].getId()), ets[i].getText().toString().trim());
            }
        }

    }

    public static void etSave(String key, EditText... ets) {
        for (int i = 0; i < ets.length; i++) {
            if (notEmptyOrNull(ets[i].getText().toString().trim()) && !ets[i].getText().toString().trim().contains("@")) {
                TpApplication.setHistoryEtMap(String.valueOf(ets[i].getId()) + key, ets[i].getText().toString()
                        .trim());
            }
        }

    }

    public static void etResume(EditText... ets) {
        for (int i = 0; i < ets.length; i++) {

            if (TpApplication.getHistoryEtMap() != null) {
                String input = TpApplication.getHistoryEtMap().get(String.valueOf(ets[i].getId()));
                if (notEmptyOrNull(input)) {
                    ets[i].setText(input);
                }
            }

        }
    }

    public static void etResume(String key, EditText... ets) {
        if (TpApplication.getHistoryEtMap() != null) {
            for (int i = 0; i < ets.length; i++) {
                String input = TpApplication.getHistoryEtMap().get(String.valueOf(ets[i].getId() + key));
                if (notEmptyOrNull(input)) {
                    ets[i].setText(input);
                }
            }
        }

    }

    public static void etClear(EditText... ets) {
        for (int i = 0; i < ets.length; i++) {
            ets[i].setText("");
            TpApplication.setHistoryEtMap(String.valueOf(ets[i].getId()), "");
        }

    }

    public static void etClear(String key, EditText... ets) {
        for (int i = 0; i < ets.length; i++) {
            ets[i].setText("");
            TpApplication.setHistoryEtMap(String.valueOf(ets[i].getId()) + key, "");
        }

    }


    public static String replaceBlank(String str) {
//        String dest = "";
//        if (str != null) {
//            //留一个空格
//            Pattern p = Pattern.compile("\\s{2,}|\t|\r|");
//            //去掉全部空格
//            //  Pattern p = Pattern.compile("\\s*|\t|\r|\n");
//            Matcher m = p.matcher(str);
//            dest = m.replaceAll("");
//            //换行换成空格
//            dest = dest.replaceAll("\n", " ");
//        }
        return str;
    }


    public static String toDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    public static String toEDay(int day) {
        SimpleDateFormat sdf = new SimpleDateFormat("E");
        long dayLong = 24 * 60 * 60 * 1000;
        return sdf.format(new Date().getTime() + day * dayLong);
    }

    public static void copyText(Activity ctx, String text) {
        ClipboardManager cbm = (ClipboardManager) ctx.getSystemService(Context.CLIPBOARD_SERVICE);
        cbm.setText(text);
        XUtil.tShort("已复制");
    }

    public static int randomRGB() {
        String r, g, b;
        Random random = new Random();
        r = Integer.toHexString(random.nextInt(256)).toUpperCase();
        g = Integer.toHexString(random.nextInt(256)).toUpperCase();
        b = Integer.toHexString(random.nextInt(256)).toUpperCase();
        r = r.length() == 1 ? "0" + r : r;
        g = g.length() == 1 ? "0" + g : g;
        b = b.length() == 1 ? "0" + b : b;
        return Color.parseColor("#" + (r + g + b));
    }

    public static void sharePhoto(final Activity activity, String photoUri) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        File file = new File(photoUri);
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        shareIntent.setType("image/jpeg");
        activity.startActivity(Intent.createChooser(shareIntent, activity.getTitle()));
    }

    public static String replacePicturePath(String str) {
        String retStr = "";
        if (notEmptyOrNull(str) && str.contains(".")) {
            retStr = str.substring(0, str.lastIndexOf(".")) + "_fact_1"
                    + str.substring(str.lastIndexOf("."), str.length());
        }
        return retStr;
    }


    public static String replaceMiniPicturePath(String str) {

        String retStr = "";
        if (notEmptyOrNull(str) && str.contains(".")) {
            retStr = str.substring(0, str.lastIndexOf(".")) + "_fact_2"
                    + str.substring(str.lastIndexOf("."), str.length());
        }
        return retStr;
    }

    private final static String regxpForHtml = "<([^>]*)>"; // 过滤所有以<开头以>结尾的标签

    public static String filterHtml(String str) {
        Pattern pattern = Pattern.compile(regxpForHtml);
        Matcher matcher = pattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        boolean result1 = matcher.find();
        while (result1) {
            matcher.appendReplacement(sb, "");
            result1 = matcher.find();
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 格式化评论内容 1、将'@name'后加冒号
     *
     * @hchxxzx<br/> <a href="#2256199" title="查看所回复的评论">@</a>hchxxzx<br/>
     * 内容 2、格式化引用部分 <fieldset
     * class="comment_quote"><legend>引用</legend>鎏：<br/>
     * 大哥，看电视不是为了拆电视</fieldset><br/>
     * @param html
     * @return
     */
    static final Pattern patternAtNoLink = Pattern.compile("@([^<]+)");
    static final Pattern patternAt = Pattern.compile("<a(.+?)>@</a>([^<]+)");
    static final Pattern patternQuote = Pattern
            .compile("<fieldset(.+?)><legend>(.+?)</legend>(.+?)：?<br\\s?/>(.+?)</fieldset>");// 引用

    public static String formatCommentString(String html) {
        // 替换回复
        Matcher m = patternAtNoLink.matcher(html);
        String rs = new String(html);
        while (m.find()) {
            rs = m.replaceAll("@$1：");
        }
        m = patternAt.matcher(rs);
        while (m.find()) {
            rs = m.replaceAll("@$2：");
        }
        // 替换引用
        m = patternQuote.matcher(rs);
        while (m.find()) {
            rs = m.replaceAll("$1『");
            rs = m.replaceAll("@$2");
            rs = m.replaceAll("$3』");
        }
        // 替换图片
        rs = RemoveImgTag(rs);
        // 替换视频
        rs = RemoveVideoTag(rs);

        return rs;
    }

    static final Pattern patternImg = Pattern.compile("<img(.+?)src=\"(.+?)\"(.+?)(onload=\"(.+?)\")?([^\"]+?)>");

    public static boolean IsContainImg(String html) {
        Matcher m = patternImg.matcher(html);
        while (m.find()) {
            return true;
        }
        return false;
    }

    /**
     * 移除图片标记
     *
     * @param html
     * @return
     */
    public static String RemoveImgTag(String html) {
        Matcher m = patternImg.matcher(html);
        while (m.find()) {
            html = m.replaceAll("");
        }
        return html;
    }

    /**
     * 替换图片标记
     *
     * @param html
     * @return
     */
    static final Pattern patternImgSrc = Pattern.compile("<img(.+?)src=\"(.+?)\"(.+?)>");

    public static String ReplaceImgTag(String html) {
        Matcher m = patternImgSrc.matcher(html);
        while (m.find()) {
            html = m.replaceAll("【<a href=\"$2\">点击查看图片</a>】");
        }
        return html;
    }

    /**
     * 移除视频标记
     */
    static final Pattern patternVideo = Pattern
            .compile("<object(.+?)>(.*?)<param name=\"src\" value=\"(.+?)\"(.+?)>(.+?)</object>");

    public static String RemoveVideoTag(String html) {
        Matcher m = patternVideo.matcher(html);
        while (m.find()) {
            html = m.replaceAll("");
        }
        return html;
    }

    /**
     * 替换视频标记
     */
    static final Pattern patternVideoSrc = Pattern
            .compile("<object(.+?)>(.*?)<param name=\"src\" value=\"(.+?)\"(.+?)>(.+?)</object>");

    public static String ReplaceVideoTag(String html) {
        Matcher m = patternVideoSrc.matcher(html);
        while (m.find()) {
            html = m.replaceAll("【<a href=\"$3\">点击查看视频</a>】");
        }
        return html;
    }

    public static String delHTMLTag(String htmlStr) {
        String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; // 定义script的正则表达式
        String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // 定义style的正则表达式
        String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式

        Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
        Matcher m_script = p_script.matcher(htmlStr);
        htmlStr = m_script.replaceAll(""); // 过滤script标签

        Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
        Matcher m_style = p_style.matcher(htmlStr);
        htmlStr = m_style.replaceAll(""); // 过滤style标签

        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll(""); // 过滤html标签

        return htmlStr.trim(); // 返回文本字符串
    }

    public static void SendTo(Context ctx, String sendWhat) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, sendWhat);
        ctx.startActivity(shareIntent);
    }

    // 获取生日
    public static String getIdBirthDay(String id) {
        return id.substring(6, 14);
    }

    // 校验电话
    public static boolean verifyMobile(String text) {
        boolean flag = false;
        String reg = "^1[3458][0-9]{9}$";
        flag = text.matches(reg);
        return flag;
    }

    // 校验身份证
    public static boolean verifyIdCard(String text) {
        boolean flag = false;
        String reg1 = "[0-9]{17}x";
        String reg2 = "[0-9]{15}";
        String reg3 = "[0-9]{18}";
        flag = text.matches(reg1) || text.matches(reg2) || text.matches(reg3);
        return flag;
    }

    // list判null
    public static <T> boolean listNotNull(List<T> t) {
        if (t != null && t.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public static final String EMPTY_STRING = "";

    /**
     * ET默认选中最后一个字符
     *
     * @param ets
     * @Description
     * @author Dminter
     */

    public static void etSelectionLast(EditText... ets) {
        for (int i = 0; i < ets.length; i++) {
            ets[i].setSelection(ets[i].getText().toString().length());
        }

    }

    public static String etGetStr(EditText et) {
        String ret = et.getText().toString().trim();
        if (notEmptyOrNull(ret)) {
            return et.getText().toString();
        } else {
            return null;
        }

    }

    public static String getContentSummary(String content) {
        if (isEmptyOrNull(content)) {
            return "";
        } else if (content.length() >= 20) {
            return content.substring(0, 20);
        } else {
            return content;
        }

    }

    public static boolean isMinLen(String text, int len) {
        if (text != null && !text.equals("")) {
            if (text.length() >= len) {
                return true;
            } else {
                return false;
            }

        } else {
            return false;
        }

    }

    /**
     * 验证手机
     *
     * @param mobiles
     * @return
     * @Description
     */
    public static boolean isMobile(String mobiles) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * @param string
     * @return boolean
     * @throws
     * @Title: isEmptyOrNull
     */
    public static boolean isEmptyOrNull(String string) {
        if (string == null || string.trim().length() == 0 || string.equalsIgnoreCase("null")) {
            return true;
        } else {
            return false;
        }
    }


    public static boolean notEmptyOrNull(String string) {
        if (string != null && !string.equalsIgnoreCase("null") && string.trim().length() > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @Title: FormetFileSize
     */
    public static String formetFileSize(long fileSize) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileSize < 0) {
            fileSizeString = String.valueOf(fileSize);
        } else if (fileSize < 1024) {
            fileSizeString = df.format((double) fileSize) + "B";
        } else if (fileSize < 1048576) {
            fileSizeString = df.format((double) fileSize / 1024) + "K";
        } else if (fileSize < 1073741824) {
            fileSizeString = df.format((double) fileSize / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) fileSize / 1073741824) + "G";
        }
        return fileSizeString;
    }

    /**
     * @param bytes
     * @return String
     * @throws
     * @Title: bytesToHexString
     */
    public static String bytesToHexString(byte[] bytes) {
        // http://stackoverflow.com/questions/332079
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    /**
     * 获取简短类名
     *
     * @throws
     * @Title: getShortClassName
     */
    public static String getShortClassName(String className) {
        if (className == null)
            return null;
        int index = className.lastIndexOf(".");
        return className.substring(index + 1);
    }


    /**
     * 获取时间命名的文件名
     *
     * @return
     * @Description
     */
    public static String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyy-MM-dd-HH-mm-ss");
        return dateFormat.format(date);
    }

    /**
     * @param string
     * @param length
     * @return String
     * @throws
     * @Title: makeLongRepeatString
     */
    public static String makeLongRepeatString(String string, int length) {
        if (string == null) {
            return "";
        }
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < length; i++) {
            buffer.append(string);
        }
        return buffer.toString();
    }

    /**
     * @param str
     * @return boolean
     * @throws
     * @Title: isNumeric
     */
    // public static boolean isNumeric(String str){
    // for (int i = str.length();--i>=0;){
    // if (!Character.isDigit(str.charAt(i))){
    // return false;
    // }
    // }
    // return true;
    // }
    //
    // public static boolean isNumeric(String str){
    // Pattern pattern = Pattern.compile("[0-9]*");
    // return pattern.matcher(str).matches();
    // }
    public static boolean isNumeric(String str) {
        for (int i = str.length(); --i >= 0; ) {
            int chr = str.charAt(i);
            if (chr < 48 || chr > 57)
                return false;
        }
        return true;
    }

    public static boolean isChinese(String str) {
        boolean isChinese = false;
        if (str.length() < str.getBytes().length) {
            isChinese = true;
        } else {
            isChinese = false;
        }
        return isChinese;
    }

    public static ArrayList<String> TenRandom(int max) {
        ArrayList<String> temp = new ArrayList<String>();
        Random random = new Random();
        HashSet<Integer> set = new HashSet<Integer>();
        while (true) {
            int number = random.nextInt(max);
            set.add(number);
            if (set.size() == 15)
                break;
        }
        for (Iterator<Integer> it = set.iterator(); it.hasNext(); ) {
            temp.add(it.next().toString());
        }
        return temp;
    }

    /**
     * 超过100显示...
     *
     * @param num
     * @return
     * @Description
     */
    public static String getNumber(Integer num) {
        if (num != null) {
            int numVaule = num.intValue();
            if (numVaule <= 0) {
                return "0";
            } else if (numVaule > 0 && numVaule <= 100) {
                return num.toString();
            } else {
                return "...";
            }
        }
        return "0";
    }

    /**
     * 根据路径获取文件名
     *
     * @param path
     * @return
     * @Description
     */
    public static String getFileNameByPath(String path) {
        if (path == null)
            return null;
        int index = path.lastIndexOf("/");
        return path.substring(index + 1);
    }

    public static boolean contains(String str, char searchChar) {
        if ((str == null) || (str.length() == 0)) {
            return false;
        }

        return str.indexOf(searchChar) >= 0;
    }

    /**
     * 是否小写
     *
     * @param cr
     * @return
     * @Description
     */
    public static boolean isLowerChar(char cr) {
        if (cr >= 'a' && cr <= 'z') {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 是否小写
     *
     * @param cr
     * @return
     * @Description
     */
    public static boolean isUpperChar(char cr) {
        if (cr >= 'A' && cr <= 'Z') {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 去掉UTF-8存储的时候文件头的BOM
     *
     * @param str
     * @return
     * @Description
     */
    public static String getUnBomString(String str) {
        if (str != null && str.startsWith("\ufeff")) {
            str = str.substring(1);
        }
        return str.toString();
    }

    /**
     * 获取图片路径
     *
     * @param ctx
     * @param uri
     * @return
     */
    public static String getAbsoluteImagePath(Activity ctx, Uri uri) {
        if (uri.toString().startsWith("file://")) {
            // String str;
            // try {
            // str = java.net.URLEncoder.encode(uri.toString(), "UTF-8");
            // str = str.replaceAll("%2F", "/");// 需要把网址的特殊字符转过来
            // str = str.replaceAll("%3A", ":");
            return uri.toString().replace("file://", "");
            // } catch (UnsupportedEncodingException e) {
            // CheckedExceptionHandler.handleException(e);
            // return null;
            // }

        } else {
            return null;
        }
    }

    public static String subStr(String input, int len) {
        int inpuLen = input.length();
        if (len >= inpuLen) {
            return input;
        } else {
            return input.substring(0, len);
        }

    }

    public static String subEndStr(String input, int len) {
        int inpuLen = input.length();
        if (len >= inpuLen) {
            return input;
        } else {
            return input.substring(len);
        }

    }

    public static String subStrDot(String input, int len) {
        int inpuLen = input.length();
        if (len >= inpuLen) {
            return input;
        } else {
            return input.substring(0, len) + "...";
        }

    }
}
