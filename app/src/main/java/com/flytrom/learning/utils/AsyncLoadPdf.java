package com.flytrom.learning.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.flytrom.learning.interfaces.OnTaskCompleted;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class AsyncLoadPdf extends AsyncTask<String, String, String> {

    private String resp;
    private String name;
    private OnTaskCompleted listener;
    @SuppressLint("StaticFieldLeak")
    private Context context;

    public AsyncLoadPdf(Context context,OnTaskCompleted listener, String name) {
        this.context = context;
        this.listener = listener;
        this.name = name;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            URL url = new URL(params[0]); //you can write here any link
            URLConnection uCon = url.openConnection();
            InputStream is = uCon.getInputStream();

            BufferedInputStream bis = new BufferedInputStream(is);
            ByteArrayBuffer baf = new ByteArrayBuffer(50);
            int current = 0;
            while ((current = bis.read()) != -1) {
                baf.append((byte) current);
            }
            UtilMethods.createDirectory(context,baf, params[1]);

        } catch (IOException e) {
            Log.d("ImageManager", "Error: " + e);
        }
        return resp;
    }


    @Override
    protected void onPostExecute(String result) {
        listener.onTaskCompletedPdf(name);
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onProgressUpdate(String... text) {

    }
}