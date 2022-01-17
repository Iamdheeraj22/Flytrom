package com.flytrom.learning.room_db.db;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.flytrom.learning.beans.response_beans.bookmark.BookmarkedQuestionBean;
import com.flytrom.learning.beans.response_beans.custom_module.CustomModuleBean;
import com.flytrom.learning.beans.response_beans.others.OptionsBean;
import com.flytrom.learning.beans.response_beans.videos.VideoBean;
import com.flytrom.learning.beans.response_beans.q_bank.ChapterBean;
import com.flytrom.learning.beans.response_beans.random_question.RandomQuestionBean;
import com.flytrom.learning.beans.response_beans.subject.SubjectBean;
import com.flytrom.learning.beans.response_beans.test_bean.QuestionBean;
import com.flytrom.learning.beans.response_beans.test_bean.TestBean;
import com.flytrom.learning.room_db.async.AppDatabaseAsync;
import com.flytrom.learning.room_db.dao.DaoAccess;
import com.flytrom.learning.utils.Constants;

@Database(entities = {
        QuestionBean.class,
        OptionsBean.class,
        VideoBean.class,
        TestBean.class,
        ChapterBean.class,
        BookmarkedQuestionBean.class,
        CustomModuleBean.class,
        SubjectBean.class,
        RandomQuestionBean.class},
        version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract DaoAccess daoAccess();

    private static final String DB_NAME = Constants.APP_DB_NAME;
    private static AppDatabase instance;

    public static AppDatabase getDatabase(final Context context) {
        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, DB_NAME)
                            .addCallback(sRoomDatabaseCallback)
                            .build();

                }
            }
        }
        return instance;
    }

    private static Callback sRoomDatabaseCallback =
            new Callback() {
                @Override
                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                    super.onOpen(db);
                    new AppDatabaseAsync(instance).execute();
                }
            };
}
