package com.zncm.timepill.utils;


import com.zncm.timepill.global.TpConstants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

//时间工具类
public class TimeUtils {


    public static String getNoteBookDate(String time) {
        String ret = time;
        try {
            //2014-08-08 13:14:14
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);//年
            String note_book_year = ret.substring(0, 4);
            String note_book_month = ret.substring(5, 7);
            String note_book_day = ret.substring(8, 10);
            if (year == Integer.parseInt(note_book_year)) {
                ret = note_book_month + "." + note_book_day;
            } else {
                ret = note_book_year + "." + note_book_month;
            }

        } catch (Exception e) {
        }
        return ret;
    }

    public static String getNoteBookYear(String time) {
        String ret = time;
        try {
            String note_book_year = ret.substring(0, 4);
            ret = note_book_year;
        } catch (Exception e) {
        }
        return ret;
    }


    public static String getTimeShow(String timeStr, boolean bHM) {
        Long longTime = null;
        try {
            longTime = Long.parseLong(timeStr);
        } catch (Exception e) {
        }
        Date tmp = new Date(longTime);
        StringBuffer sbTime = new StringBuffer();
        String today;
        if (longTime < getDayStart()) {
            sbTime.append(getMDDate(tmp));
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(tmp);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        if (hour >= 6 && hour < 12) {
            // 0:00:00 - 11:59:59 算上午
            today = "上午 ";
        } else if (hour >= 12 && hour < 18) {
            // 12:00:00 - 17:59:59 算下午
            today = "下午 ";
        } else if (hour >= 0 && hour < 6) {
            today = "凌晨 ";
        } else {
            // 18:00:00 - 23:59:59 算晚上
            today = "夜晚  ";
        }
        if (bHM) {
            sbTime.append(today + getTimeHM(tmp));
        } else {
            if (longTime >= getDayStart()) {
                sbTime.append(today + getTimeHM(tmp));
            }
        }
        return sbTime.toString();
    }


    public static String convertSToHMS(int second) {
        int h = 0;
        int d = 0;
        int s = 0;
        int temp = second % 3600;
        if (second > 3600) {
            h = second / 3600;
            if (temp != 0) {
                if (temp > 60) {
                    d = temp / 60;
                    if (temp % 60 != 0) {
                        s = temp % 60;
                    }
                } else {
                    s = temp;
                }
            }
        } else {
            d = second / 60;
            if (second % 60 != 0) {
                s = second % 60;
            }
        }

        return h + ":" + d + ":" + s;
    }


    public static String getDateHMFromLong(long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String dt = sdf.format(time);
        return dt;
    }

    public static String getYearFirstDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_YEAR, calendar.getActualMinimum(Calendar.DAY_OF_YEAR));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(calendar.getTime());
    }

    public static String getMonthFirstDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(calendar.getTime());
    }

    public static String getWeekFirstDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(calendar.getTime());
    }

    public static String getFirstDayOfWeek() {
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTime(new Date());
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek()); // Monday
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(c.getTime());
    }

    public static String getToDay() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(new Date().getTime());
    }

    //今天0点毫秒数
    public static long getDayStart() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    public static final Date increaseDate(Date aDate, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(aDate);
        cal.add(Calendar.DAY_OF_MONTH, days);
        return cal.getTime();
    }

    public static String[] calcToday(String begin, String end, String now, GregorianCalendar calendar) {
        String[] temp = new String[2];
        begin = now;
        end = now;
        temp[0] = begin;
        temp[1] = end;
        return temp;
    }

    public static String[] calcYesterday(String begin, String end, String now, GregorianCalendar calendar) {
        String[] temp = new String[2];
        calendar.add(GregorianCalendar.DATE, -1);
        begin = new java.sql.Date(calendar.getTime().getTime()).toString();
        end = begin;

        temp[0] = begin;
        temp[1] = end;
        return temp;
    }

    public static String[] calcThisWeek(String begin, String end, String now, GregorianCalendar calendar) {
        String[] temp = new String[2];
        end = now;
        int minus = calendar.get(GregorianCalendar.DAY_OF_WEEK) - 2;
        if (minus < 0) {
        } else {

            calendar.add(GregorianCalendar.DATE, -minus);
            begin = new java.sql.Date(calendar.getTime().getTime()).toString();
        }

        temp[0] = begin;
        temp[1] = end;
        return temp;
    }

    public static String[] calcThisMonth(String begin, String end, String now, GregorianCalendar calendar) {
        String[] temp = new String[2];
        end = now;
        int dayOfMonth = calendar.get(GregorianCalendar.DATE);
        calendar.add(GregorianCalendar.DATE, -dayOfMonth + 1);
        begin = new java.sql.Date(calendar.getTime().getTime()).toString();

        temp[0] = begin;
        temp[1] = end;
        return temp;
    }

    /**
     * String转换为时间
     *
     * @param str
     * @return
     */
    public static Date ParseDate(String str) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date addTime = null;
        try {
            addTime = dateFormat.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return addTime;
    }

    public static String parseDateLong(String str) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date addTime = null;
        try {
            addTime = dateFormat.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return addTime.getTime() + "";
    }

    public static Long dateStrToLong(String str) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date addTime = null;
        try {
            addTime = dateFormat.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return addTime.getTime();
    }

    // 相隔天数计算
    public static int diffDays(String day1, String day2) {
        long diff = dateStrToLong(day2) - dateStrToLong(day1);
        int day = (int) (diff * 1.0f / TpConstants.DAY);
        return day;
    }

    public static int diffNowDays(Long day2) {
        long diff = day2 - dateStrToLong(getDateYDM());
        int day = (int) (diff * 1.0f / TpConstants.DAY);
        return day;
    }

    // 日期推算
    public static String afterDays(String day1, int nDay) {

        long newLong = dateStrToLong(day1) + nDay * TpConstants.DAY;
        return getDateYDMShow(new Date(newLong));
    }

    public static String getDateYDMShow(Date inputDate) {
        return new SimpleDateFormat("yyyy-MM-dd").format(inputDate);
    }

    /**
     * 将日期转换为字符串
     *
     * @param date
     * @return
     */
    public static String ParseDateToString(Date date) {
        return ParseDateToString(date, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 将日期转换为字符串（重载）
     *
     * @param date
     * @param format :时间格式，必须符合yyyy-MM-dd hh:mm:ss
     * @return
     */
    public static String ParseDateToString(Date date, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);

        return dateFormat.format(date);
    }

    /**
     * 将UMT时间转换为本地时间
     *
     * @param str
     * @return
     * @throws java.text.ParseException
     */
    public static Date ParseUTCDate(String str) {
        // 格式化2012-03-04T23:42:00+08:00
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.CHINA);
        try {
            Date date = formatter.parse(str);

            return date;
        } catch (ParseException e) {
            // 格式化Sat, 17 Mar 2012 11:37:13 +0000
            // Sat, 17 Mar 2012 22:13:41 +0800
            try {
                SimpleDateFormat formatter2 = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.CHINA);
                Date date2 = formatter2.parse(str);

                return date2;
            } catch (ParseException ex) {
                return null;
            }
        }
    }

    private static final String DAY = "天";
    private static final String HOUR = "小时";
    private static final String MINUTE = "分钟";
    private static final String UNKNOWN = "刚刚";
    private static final String SUFFIX = "前";

    public static String getDisplayDate(Date inputDate) {
        Date localDate = new Date();
        long l1 = (localDate.getTime() - inputDate.getTime()) / 1000L;
        while (true) {
            if (l1 <= 60L) {
                return UNKNOWN;
            }
            long l2 = l1 / 60L;
            if (l2 < 60L) {
                return l2 + MINUTE + SUFFIX;
            }

            long l3 = l2 / 60L;
            if (l3 < 24L) {
                return l3 + HOUR + SUFFIX;
            }
            long l4 = l3 / 24L;

            if (l4 > 7 && l4 < 365) {
                return getTimeMDHM(inputDate);
            } else if (l4 >= 365) {
                return getTimeYMDHM(inputDate);
            } else {
                return l4 + DAY + SUFFIX;
            }

        }
    }

    public static String getTimeMDHM(Date inputDate) {
        return new SimpleDateFormat("MM-dd HH:mm").format(inputDate);
    }


    public static String getTimeHMS(Date inputDate) {
        return new SimpleDateFormat("MM-dd HH:mm").format(inputDate);
    }


    public static boolean isAddTime(Date inputDate) {

        boolean is_add_time = false;

        if (is_add_time) {

        }
        return is_add_time;
    }

//    public static Date stringToDate(String strDate) {
//        Date date = null;
//        try {
//            date = DateFormat.getDateInstance().parse(strDate);
//        } catch (ParseException e) {
//            CheckedExceptionHandler.handleException(e);
//        }
//        return date;
//    }

    /**
     * 通过string获取时间，若获取不到，则返回当前时间
     *
     * @Description
     */

    public static String dateToEDate(Date date) {
        String eDate = "";
        SimpleDateFormat dateFm = new SimpleDateFormat("EEEE");
        eDate = dateFm.format(date);
        return eDate;
    }

    public static String getYesterdayDate() {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date().getTime() - 24 * 60 * 60 * 1000);
    }

    public static String getDate() {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }

    public static String getDateYDM() {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }

    public static String getDateM() {
        return new SimpleDateFormat("MM").format(new Date());
    }

    public static String getDateMD() {
        return new SimpleDateFormat("MM-dd").format(new Date());
    }

    public static String getDateM_D_E() {
        return new SimpleDateFormat("MM/dd E").format(new Date());
    }

    public static String getDateMD(Long timeLong) {
        return new SimpleDateFormat("MM-dd").format(new Date(timeLong));
    }

    public static String getDateYDME() {
        return new SimpleDateFormat("yyyy-MM-dd E").format(new Date());
    }

    public static String getDateYDMEChinese() {
        return new SimpleDateFormat("yyyy年MM月dd日 E").format(new Date());
    }

    public static String getDateYDMESimple() {
        return new SimpleDateFormat("yyyy/MM/dd E").format(new Date());
    }

    public static String getTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    public static String getDateFull(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

    public static String getTimeHS() {
        return new SimpleDateFormat("HH:mm").format(new Date());
    }

    public static String getTimeH(String timeStr) {
        return new SimpleDateFormat("HH").format(new Date(Long.parseLong(timeStr)));
    }

    public static String getTimeS(String timeStr) {
        return new SimpleDateFormat("mm").format(new Date(Long.parseLong(timeStr)));
    }

    public static String getTime1(Date inputDate) {
        return new SimpleDateFormat("MM-dd-yyyy HH:mm").format(inputDate);
    }

    public static String getTime2(Date inputDate) {
        return new SimpleDateFormat("dd-MM-yyyy HH:mm").format(inputDate);
    }

    public static String getTime3(Date inputDate) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(inputDate);
    }

    public static String getTime4(Date inputDate) {
        return new SimpleDateFormat("EE,MM月dd日,yyyy年 HH时 mm分").format(inputDate);
    }

    public static String getTime5(Date inputDate) {
        return new SimpleDateFormat("EE,dd日 MM月,yyyy年 HH时 mm分").format(inputDate);
    }

    public static String getTime6(Date inputDate) {
        return new SimpleDateFormat("yyyy年 MM月 dd日,EE HH时 mm分").format(inputDate);
    }

    public static String getTimeYMDHM(Date inputDate) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(inputDate);
    }

    public static String getTimeHM() {
        return new SimpleDateFormat("HH:mm").format(new Date());
    }

    public static String getTimeYMDHMS(Date inputDate) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(inputDate);
    }

    public static String getTimeYMDHMS() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    public static String getPhotoTime() {
        return new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date());
    }

    public static String getBackUpTime() {
        return new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(new Date());
    }

    public static String getTimeYMD(Date inputDate) {
        return new SimpleDateFormat("yyyy-MM-dd").format(inputDate);
    }

    public static String getTimeYMDE(Date inputDate) {
        return new SimpleDateFormat("MM-dd\nE").format(inputDate);
    }

    public static String getTimeHM(Date inputDate) {
        return new SimpleDateFormat("HH:mm").format(inputDate);
    }

    public static String getLongTime() {
        return (new Date().getTime()) + "";
    }

    public static Long getTimeLong() {
        return (new Date().getTime());
    }

    public static String getFileSaveTime() {
        return new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    }

    public static String getTime(Date inputDate) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(inputDate);
    }

    public static String getChineseDate(Date inputDate) {
        String show = "";
//        Date nowDate = new Date();
//        if (getDateYDM(inputDate).equals(getDateYDM(nowDate))) {
//            // 今天
//            Calendar cal = Calendar.getInstance();
//            cal.setTime(inputDate);
//            int hour = cal.get(Calendar.HOUR_OF_DAY);
//            if (hour >= 6 && hour < 12) {
//                // 0:00:00 - 11:59:59 算上午
//                show = "上午 " + getTimeHS(inputDate);
//            } else if (hour >= 12 && hour < 18) {
//                // 12:00:00 - 17:59:59 算下午
//                show = "下午 " + getTimeHS(inputDate);
//            } else if (hour >= 0 && hour < 6) {
//                show = "凌晨 " + getTimeHS(inputDate);
//            } else {
//                // 18:00:00 - 23:59:59 算晚上
//                show = "晚上 " + getTimeHS(inputDate);
//            }
//        } else if (getLastWeekDay().getTime() < inputDate.getTime()) {
//            show = dateToEDate(inputDate).replace("星期", "周") + " " + getTimeHS(inputDate);
//        } else if (inputDate.getTime() > TimeUtils.stringToDate(TimeUtils.getYearFirstDay()).getTime()) {
//            show = getFullNoYearDate2(inputDate);
//        } else {
//            show = getFullDate2(inputDate);
//        }
        show = getFullNoYearDate2(inputDate);
        return show;
    }


    public static String getMDotDEDate(Date inputDate) {
        return new SimpleDateFormat("MM.dd E").format(inputDate);
    }

    public static String getMonth(Date inputDate) {
        return new SimpleDateFormat("MM").format(inputDate);
    }

    public static String getDay(Date inputDate) {
        return new SimpleDateFormat("dd").format(inputDate);
    }

    public static Date timeStrToLong(String time) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            date = sdf.parse(time);
        } catch (Exception e) {
            //2014-06-28T11:25:09+0800 搜索结果的时间
            sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
            try {
                date = sdf.parse(time);
            } catch (ParseException e1) {
                e1.printStackTrace();
            }
        }
        return date;
    }


    public static String getMDDate(Date inputDate) {
        return new SimpleDateFormat("MM月dd日").format(inputDate);
    }

    public static String getMDEDate(Date inputDate) {
        return new SimpleDateFormat("MM月dd日 E").format(inputDate);
    }

    public static String getDateYDM(Date inputDate) {
        return new SimpleDateFormat("yyyy年MM月dd日").format(inputDate);
    }

    public static String getDateYDM2(Date inputDate) {
        return new SimpleDateFormat("yyyy-MM-dd").format(inputDate);
    }

    public static String getYMDEDateYDM(Date inputDate) {
        return new SimpleDateFormat("yyyy年MM月dd日 E").format(inputDate);
    }

    public static String getFullDate(Date inputDate) {
        return new SimpleDateFormat("yyyy年MM月dd日 E HH:mm").format(inputDate);
    }

    public static String getFullNoYearDate(Date inputDate) {
        return new SimpleDateFormat("MM月dd日 E HH:mm").format(inputDate);
    }

    public static String getFullDate2(Date inputDate) {
        return new SimpleDateFormat("yyyy/MM/dd HH:mm").format(inputDate);
    }

    public static String getFullNoYearDate2(Date inputDate) {
        return new SimpleDateFormat("MM/dd HH:mm").format(inputDate);
    }

    public static String getTimeHS(Date inputDate) {
        return new SimpleDateFormat("HH:mm").format(inputDate);
    }

    public static String getWeekDate(Date inputDate) {
        return new SimpleDateFormat("E HH:mm").format(inputDate);
    }

    public static String getMonthDate(Date inputDate) {
        return new SimpleDateFormat("dd日 HH:mm").format(inputDate);
    }

    public static String getYearDate(Date inputDate) {
        return new SimpleDateFormat("MM月dd日 HH:mm").format(inputDate);
    }


    public static String getMD(Date inputDate) {
        return new SimpleDateFormat("MM/dd").format(inputDate);
    }

    public static String getYMD(Date inputDate) {
        return new SimpleDateFormat("yyyy/MM/dd").format(inputDate);
    }

    public static Date getLastWeekDay() {
        Date lastWeekDay;
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, 1);
        lastWeekDay = cal.getTime();
        return lastWeekDay;
    }

    public static String getInsrtTime() {
        return new SimpleDateFormat("【MM月dd日 HH:mm】").format(new Date());
    }

}
