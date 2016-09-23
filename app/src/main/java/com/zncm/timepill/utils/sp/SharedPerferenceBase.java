package com.zncm.timepill.utils.sp;

import android.content.SharedPreferences;


import com.zncm.timepill.global.TpApplication;

import java.util.Map;

//对系统SharedPerference的封装
public class SharedPerferenceBase {

    public static SharedPreferences getPreferences(String fileString) {
        return TpApplication.getInstance().getPreferences(fileString);
    }

    /**
     * @param perference
     * @param keyString
     * @param defaultBoolean
     * @return boolean
     * @throws
     * @Title: getBoolean
     */
    public static boolean getBoolean(SharedPreferences perference,
                                     String keyString, Boolean defaultBoolean) {
        return perference.getBoolean(keyString, defaultBoolean.booleanValue());
    }

    /**
     * @param perPreference
     * @param keyString
     * @param booleanValue
     * @return void
     * @throws
     * @Title: putBoolean
     */
    public static void putBoolean(SharedPreferences perPreference,
                                  String keyString, Boolean booleanValue) {
        perPreference.edit().putBoolean(keyString, booleanValue).commit();
    }

    /**
     * @param perference
     * @param keyString
     * @param defaultFloat
     * @return float
     * @throws
     * @Title: getFloat
     */
    public static float getFloat(SharedPreferences perference,
                                 String keyString, Float defaultFloat) {
        return perference.getFloat(keyString, defaultFloat.floatValue());
    }

    /**
     * @param perference
     * @param keyString
     * @param floatValue
     * @return void
     * @throws
     * @Title: putFloat
     */
    public static void putFloat(SharedPreferences perference, String keyString,
                                Float floatValue) {
        perference.edit().putFloat(keyString, floatValue.floatValue()).commit();
    }

    /**
     * @param perference
     * @param keyString
     * @param defaultInteger
     * @return int
     * @throws
     * @Title: getInt
     */
    public static int getInt(SharedPreferences perference, String keyString,
                             Integer defaultInteger) {
        return perference.getInt(keyString, defaultInteger.intValue());
    }

    /**
     * @param perference
     * @param keyString
     * @param integerVaule
     * @return void
     * @throws
     * @Title: putInt
     */
    public static void putInt(SharedPreferences perference, String keyString,
                              Integer integerVaule) {
        perference.edit().putInt(keyString, integerVaule.intValue()).commit();
    }

    /**
     * @param perference
     * @param keyString
     * @param defaultLong
     * @return Long
     * @throws
     * @Title: getLong
     */
    public static Long getLong(SharedPreferences perference, String keyString,
                               Long defaultLong) {
        return Long.valueOf(perference.getLong(keyString,
                defaultLong.longValue()));
    }

    /**
     * @param perference
     * @param keyString
     * @param longVaule
     * @return void
     * @throws
     * @Title: putLong
     */
    public static void putLong(SharedPreferences perference, String keyString,
                               Long longVaule) {
        perference.edit().putLong(keyString, longVaule.longValue()).commit();
    }

    /**
     * @param perference
     * @param keyString
     * @param defaultString
     * @return String
     * @throws
     * @Title: getString
     */
    public static String getString(SharedPreferences perference,
                                   String keyString, String defaultString) {
        return perference.getString(keyString, defaultString);
    }

    /**
     * @param perference
     * @param keyString
     * @param stringVaule
     * @return void
     * @throws
     * @Title: putString
     */
    public static void putString(SharedPreferences perference,
                                 String keyString, String stringVaule) {
        perference.edit().putString(keyString, stringVaule).commit();
    }

    /**
     * @param perference
     * @return Map<String,?>
     * @throws
     * @Title: getAllKeyValues
     */
    public static Map<String, ?> getAllKeyValues(SharedPreferences perference) {
        return perference.getAll();
    }

    /**
     * @param perference
     * @return void
     * @throws
     * @Title: clearAllKeyValues
     */
    public static void clearAllKeyValues(SharedPreferences perference) {
        perference.edit().clear().commit();
    }

    /**
     * @param perPreference
     * @param keyString
     * @return boolean
     * @throws
     * @Title: containKey
     */
    public static boolean containKey(SharedPreferences perPreference,
                                     String keyString) {
        return perPreference.contains(keyString);
    }

    /**
     * @param perference
     * @param keyString
     * @return void
     * @throws
     * @Title: removeKey
     */
    public static void removeKey(SharedPreferences perference, String keyString) {
        perference.edit().remove(keyString).commit();
    }
}
