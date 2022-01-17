package com.flytrom.learning.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.flytrom.learning.beans.normal_beans.CustomModuleDataBean;
import com.flytrom.learning.beans.response_beans.auth.LoginBean;
import com.flytrom.learning.beans.response_beans.auth.UserDataBean;
import com.flytrom.learning.beans.response_beans.random_question.RandomQuestionBean;
import com.flytrom.learning.beans.response_beans.videos.VideoBean;
import com.flytrom.learning.beans.response_beans.random_question.GetRandomQuestionBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;


/**
 * Created by jatin on 2/21/2018.
 */

public class PrefUtils {

    private final String TAG = PrefUtils.class.getName();
    private final String SHARED_PREF_NAME = "FLYTROM";
    private final SharedPreferences _pref;
    private static PrefUtils _instance;

    private PrefUtils() {
        _pref = AppController.getInstance().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
    }

    public static PrefUtils getInstance() {
        if (_instance == null) {
            _instance = new PrefUtils();
        }
        return _instance;
    }


    public boolean clear() {
        SharedPreferences.Editor editor = _pref.edit();
        editor.clear();
        return editor.commit();
    }


    public boolean setUser(UserDataBean userBean) {
        SharedPreferences.Editor editor = _pref.edit();
        editor.putString(KEY.USER, new Gson().toJson(userBean));
        return editor.commit();
    }

    public boolean setBookmarkCounter(int counter) {
        SharedPreferences.Editor editor = _pref.edit();
        editor.putInt(KEY.BOOKMARK_COUNTER, counter);
        return editor.commit();
    }

    public int getBookmarkCounter() {
        return _pref.getInt(KEY.BOOKMARK_COUNTER, 0);
    }

    public UserDataBean getUser() {
        try {
            String s = _pref.getString(KEY.USER, null);
            if (s == null)
                return null;
            return new Gson().fromJson(s, UserDataBean.class);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean setCustomModuleData(CustomModuleDataBean bean) {
        SharedPreferences.Editor editor = _pref.edit();
        editor.putString(KEY.CUSTOM_MODULE_DATA, new Gson().toJson(bean));
        return editor.commit();
    }

    public CustomModuleDataBean getCustomModuleData() {
        try {
            String s = _pref.getString(KEY.CUSTOM_MODULE_DATA, null);
            if (s == null)
                return null;
            return new Gson().fromJson(s, CustomModuleDataBean.class);
        } catch (Exception e) {
            return null;
        }
    }


    public boolean setCurrentDate(String currentDate) {
        SharedPreferences.Editor editor = _pref.edit();
        editor.putString(KEY.CURRENT_DATE, currentDate);
        return editor.commit();
    }

    public String getCurrentDate() {
        return _pref.getString(KEY.CURRENT_DATE, null);
    }


    public boolean setRandomMCQ(RandomQuestionBean questionBean) {
        SharedPreferences.Editor editor = _pref.edit();
        editor.putString(KEY.RANDOM_QUESTION, new Gson().toJson(questionBean));
        return editor.commit();
    }

    public RandomQuestionBean getRandomMCQ() {
        try {
            String s = _pref.getString(KEY.RANDOM_QUESTION, null);
            if (s == null)
                return null;
            return new Gson().fromJson(s, RandomQuestionBean.class);
        } catch (Exception e) {
            return null;
        }
    }

    public List<VideoBean> getDownloadsVideos() {
        try {
            String s = _pref.getString(KEY.DOWNLOADED_VIDEOS, null);
            if (s == null)
                return null;
            Type type = new TypeToken<List<VideoBean>>() {
            }.getType();
            return new Gson().fromJson(s, type);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean setDownloadsVideos(List<VideoBean> list) {
        SharedPreferences.Editor editor = _pref.edit();
        Type type = new TypeToken<List<VideoBean>>() {
        }.getType();
        editor.putString(KEY.DOWNLOADED_VIDEOS, new Gson().toJson(list, type));
        return editor.commit();
    }

    public void setTimezone(String timezone) {
        SharedPreferences.Editor editor = _pref.edit();
        editor.putString(KEY.TIMEZONE, timezone);
        editor.commit();
    }

    public String getTimezone() {
        return _pref.getString(KEY.TIMEZONE, "");
    }

    public int getLock() {
        return _pref.getInt(KEY.LOCK, 0);
    }

    public boolean setLock(int lock) {
        SharedPreferences.Editor editor = _pref.edit();
        editor.putInt(KEY.LOCK, lock);
        return editor.commit();
    }

    public void setLatitude(String latitude) {
        SharedPreferences.Editor editor = _pref.edit();
        editor.putString(KEY.LATITUDE, latitude);
        editor.apply();
    }

    public String getLatitude() {
        return _pref.getString(KEY.LATITUDE, "30.704649");
    }

    public void setLongtitude(String longtitude) {
        SharedPreferences.Editor editor = _pref.edit();
        editor.putString(KEY.LONGTITUDE, longtitude);
        editor.apply();
    }

    public String getLongtitude() {
        return _pref.getString(KEY.LONGTITUDE, "76.717873");
    }


    public boolean setNotification(boolean value) {
        SharedPreferences.Editor editor = _pref.edit();
        editor.putBoolean(KEY.NOTIFICATION, value);
        return editor.commit();
    }

    public boolean getNotification() {
        return _pref.getBoolean(KEY.NOTIFICATION, false);
    }


    public boolean setRemember() {
        SharedPreferences.Editor editor = _pref.edit();
        editor.putBoolean(KEY.REMEMBER, true);
        return editor.commit();
    }

    public boolean getRemember() {
        return _pref.getBoolean(KEY.REMEMBER, false);
    }

    public void setVibrationMode(boolean value) {
        SharedPreferences.Editor editor = _pref.edit();
        editor.putBoolean(KEY.VIBRATION, value);
        editor.apply();
    }

    public boolean isVibrationModeOn() {
        return _pref.getBoolean(KEY.VIBRATION, true);
    }

    public void setNotificationText(String value) {
        SharedPreferences.Editor editor = _pref.edit();
        editor.putString(KEY.NOTIFICATION_TEXT, value);
        editor.apply();
    }

    public String getNotificationText() {
        return _pref.getString(KEY.NOTIFICATION_TEXT, null);
    }

    public void setSelectedSubject(String title) {
        SharedPreferences.Editor editor = _pref.edit();
        editor.putString(KEY.SELECTED_SUBJECT, title);
        editor.apply();
    }

    public String getSelectedSubject() {
        return _pref.getString(KEY.SELECTED_SUBJECT, null);
    }

    public static final class KEY {
        public static final String USER = "userData";
        public static final String BOOKMARK_COUNTER = "bookmark_counter";
        public static final String RANDOM_QUESTION = "random_question";
        public static final String CUSTOM_MODULE_DATA = "custom_module_data";
        public static final String DOWNLOADED_VIDEOS = "downloaded_videos";
        public static final String USER_SETTINGS = "user_settings";
        public static final String SHOW_LOCK_SCREEN = "show_lock_screen";
        public static final String REMEMBER = "remember";
        public static final String MY_SELECTED_OPTION = "my_selected_option";
        public static final String NOTIFICATION = "notification";
        public static final String TIMEZONE = "timezone";
        public static final String LATITUDE = "latitude";
        public static final String LONGTITUDE = "longtitude";
        public static final String LOCK = "lock";
        public static final String VIBRATION = "vibration";
        public static final String NOTIFICATION_TEXT = "notification_text";
        public static final String SELECTED_SUBJECT = "selected_subject";
        static final String CURRENT_DATE = "current_date";

    }
}
