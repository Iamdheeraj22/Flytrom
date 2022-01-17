package com.flytrom.learning.utils

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.flytrom.learning.interfaces.Apis
import com.flytrom.learning.internet.ConnectionStateMonitor
import com.flytrom.learning.utils.AppVisibilityDetector.AppVisibilityCallback
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import timber.log.Timber.DebugTree
import java.util.concurrent.TimeUnit

class AppControllerKotlin : Application() {

    private val TAG = AppControllerKotlin::class.java.simpleName
    private var context: AppControllerKotlin? = null
    private var retrofit: Retrofit? = null
    private var isonline = false
    private var internetOn = false

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        context = AppControllerKotlin()
        Timber.plant(DebugTree())
        val monitor = ConnectionStateMonitor()
        monitor.enable(this)
        AppVisibilityDetector.init(this, object : AppVisibilityCallback {
            override fun onAppGotoForeground() {
                isonline = true
            }

            override fun onAppGotoBackground() {
                isonline = false
            }
        })
    }

    fun getInstance(): AppControllerKotlin? {
        return context
    }

    fun isInternetOn(): Boolean {
        return internetOn
    }

    fun setInternetOn(isInternetOn: Boolean) {
        internetOn = isInternetOn
    }

    fun isIsonline(): Boolean {
        return isonline
    }

    fun getApis(): Apis? {
        if (retrofit == null) {
            val httpClient = OkHttpClient.Builder()
            httpClient.addInterceptor(BasicAuthInterceptor(Constants.USERNAME, Constants.PASSWORD))
            httpClient.readTimeout(30, TimeUnit.SECONDS)
            val gson = GsonBuilder()
                    .setLenient()
                    .create()
            retrofit = Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .baseUrl(Constants.BASE_URL)
                    .client(httpClient.build())
                    .build()
        }
        return retrofit?.create(Apis::class.java)
    }

    fun getRetrofit(): Retrofit? {
        if (retrofit == null) {
            getApis()
        }
        return retrofit
    }
}