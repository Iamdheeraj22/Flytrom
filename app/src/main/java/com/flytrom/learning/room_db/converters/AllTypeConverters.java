package com.flytrom.learning.room_db.converters;

import androidx.room.TypeConverter;

import com.flytrom.learning.beans.response_beans.others.OptionsBean;
import com.flytrom.learning.beans.response_beans.others.SlidesBean;
import com.flytrom.learning.beans.response_beans.others.SolveMCQBean;
import com.flytrom.learning.beans.response_beans.others.TopicsBean;
import com.flytrom.learning.beans.response_beans.q_bank.ChapterBean;
import com.flytrom.learning.beans.response_beans.random_question.FactsBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

public class AllTypeConverters {

    private static Gson gson = new Gson();

    /*** Options TypeConverts*/
    @TypeConverter
    public static List<OptionsBean> stringToQuestionBean(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<OptionsBean>>() {
        }.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String questionBeanToString(List<OptionsBean> someObjects) {
        return gson.toJson(someObjects);
    }

    /*** SolveMCQBean TypeConverts*/
    @TypeConverter
    public static List<SolveMCQBean> stringToSolveMCQBean(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<SolveMCQBean>>() {
        }.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String solveMCQBeanToString(List<SolveMCQBean> someObjects) {
        return gson.toJson(someObjects);
    }

    /*** Facts TypeConverts*/
    @TypeConverter
    public static FactsBean stringToFactsBean(String data) {
        if (data == null) {
            return new FactsBean();
        }

        Type bean = new TypeToken<FactsBean>() {
        }.getType();

        return gson.fromJson(data, bean);
    }

    @TypeConverter
    public static String factsBeanToString(FactsBean someObjects) {
        return gson.toJson(someObjects);
    }

    /*** Topics TypeConverts*/
    @TypeConverter
    public static List<TopicsBean> stringToTopicsBean(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<TopicsBean>>() {
        }.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String topicsBeanToString(List<TopicsBean> someObjects) {
        return gson.toJson(someObjects);
    }

    /*** Slides TypeConverts*/
    @TypeConverter
    public static List<SlidesBean> stringToSlidesBean(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<SlidesBean>>() {
        }.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String slidesBeanToString(List<SlidesBean> someObjects) {
        return gson.toJson(someObjects);
    }

    /*** GetChapter TypeConverts*/
    @TypeConverter
    public static List<ChapterBean> stringToChapterBean(String data) {
        if (data == null) {
            return Collections.emptyList();
        }

        Type listType = new TypeToken<List<ChapterBean>>() {
        }.getType();

        return gson.fromJson(data, listType);
    }

    @TypeConverter
    public static String chapterBeanToString(List<ChapterBean> someObjects) {
        return gson.toJson(someObjects);
    }
}
