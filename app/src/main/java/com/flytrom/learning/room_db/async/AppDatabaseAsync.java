package com.flytrom.learning.room_db.async;

import android.os.AsyncTask;

import com.flytrom.learning.room_db.dao.DaoAccess;
import com.flytrom.learning.room_db.db.AppDatabase;


public class AppDatabaseAsync extends AsyncTask<Void, Void, Void> {

    private final DaoAccess mDao;

    public AppDatabaseAsync(AppDatabase db) {
        mDao = db.daoAccess();
    }

    @Override
    protected Void doInBackground(final Void... params) {
       // mDao.deleteAll();
        return null;
    }

}
