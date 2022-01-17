package com.flytrom.learning;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flytrom.learning.activities.video_menu.PlayVideoActivity;
import com.flytrom.learning.utils.OnDownloadListner;

import org.androidannotations.annotations.sharedpreferences.SharedPref;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

public class DownloadFileTask extends AsyncTask<String, Integer, String> {

    private Context context;
    private PowerManager.WakeLock mWakeLock;
        ProgressDialog mProgressDialog;
    ProgressBar progressBar;
    TextView text_percent;
    TextView text_download;
    LinearLayout linear_download;
    RelativeLayout progressLayout;
    ImageView imageDownlaod;
    OnDownloadListner callback;

    public DownloadFileTask(Context context) {
        this.context = context;
    }


    public DownloadFileTask(Context context, ProgressBar progressBar, TextView text_percent, LinearLayout linear_download, TextView text_download, RelativeLayout progressLayout, ImageView imageDownlaod, OnDownloadListner callback) {
        this.context = context;
        this.progressBar = progressBar;
        this.text_percent = text_percent;
        this.text_download = text_download;
        this.linear_download = linear_download;
        this.progressLayout = progressLayout;
        this.imageDownlaod = imageDownlaod;
        this.callback = callback;
    }

    public String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        Log.e("DownloadFileTask", "getMimeType: " + type);
        return type;
    }

    @Override
    protected String doInBackground(String... sUrl) {
        InputStream input = null;
        OutputStream output = null;
        HttpURLConnection connection = null;
        String mimeType = getMimeType(sUrl[0]);
        if (mimeType.contains("/")) {
            mimeType = mimeType.split("/")[1];
        }
        String fileName = (new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())).format(new Date()) + "." + mimeType;
        Log.e("DownloadFileTask", "doInBackground_fileName: " + fileName);
        try {
            URL url = new URL(sUrl[0]);
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            // expect HTTP 200 OK, so we don't mistakenly save error report
            // instead of the file
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return "Server returned HTTP " + connection.getResponseCode()
                        + " " + connection.getResponseMessage();
            }

            // this will be useful to display download percentage
            // might be -1: server did not report the length
            int fileLength = connection.getContentLength();

            // download the file
            File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File file = new File(folder, fileName);
            input = connection.getInputStream();
            output = new FileOutputStream(file);

            byte data[] = new byte[4096];
            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {
                // allow canceling with back button
                if (isCancelled()) {
                    input.close();
                    return null;
                }
                total += count;
                // publishing the progress....
                if (fileLength > 0) // only if total length is known
                    publishProgress((int) (total * 100 / fileLength));
                output.write(data, 0, count);
            }
        } catch (Exception e) {
            return e.toString();
        } finally {
            try {
                if (output != null)
                    output.close();
                if (input != null)
                    input.close();
            } catch (IOException ignored) {
            }

            if (connection != null)
                connection.disconnect();
        }
        return null;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // take CPU lock to prevent CPU from going off if the user
        // presses the power button during download
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                getClass().getName());
        mWakeLock.acquire(10 * 60 * 1000L /*10 minutes*/);
        if (progressBar == null) {
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setMessage("Downloading file...");
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgressDialog.setCancelable(true);
            mProgressDialog.show();
        }
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        super.onProgressUpdate(progress);
        // if we get here, length is known, now set indeterminate to false
        if (progressBar != null) {
            progressBar.setMax(100);
            progressBar.setProgress(progress[0]);
            text_percent.setText(progress[0]+"%");
            Log.i("Downloading video",progress[0]+"%");
            Log.d("downloadpercentage",progress[0]+"%");
        }else if (mProgressDialog != null) {
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setMax(100);
            mProgressDialog.setProgress(progress[0]);
        }
    }

    @Override
    protected void onPostExecute(String result) {
        mWakeLock.release();
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
        if (result != null)
            Toast.makeText(context, "Download error: " + result, Toast.LENGTH_LONG).show();
        else
            Toast.makeText(context, "File downloaded", Toast.LENGTH_SHORT).show();

        linear_download.setVisibility(View.VISIBLE);
        progressLayout.setVisibility(View.GONE);
        text_download.setText("Downloaded");
        imageDownlaod.setImageResource(R.drawable.tick);

        callback.downloadVideo("yes");

    }

}
