package com.lickling.mymusic.network.wanAndroid;

import android.annotation.SuppressLint;

import com.google.gson.GsonBuilder;
import com.lickling.mymusic.network.wanAndroid.bean.LoginResponse;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class WanAndroidApiHandler {

    private static final String BASE_URL = "https://www.wanandroid.com/";
    private static final String PREFIX = "[WanAndroidUnitTest] ";

    private Map<String, List<Cookie>> cookies = new HashMap<>();

    private OkHttpClient _httpClient;
    private Retrofit _retrofit;
    private WanAndroidApiService _client;

    public WanAndroidApiHandler() {
        // 创建 OKHttpClient 实例, 包含了Cookie Jar, 可以登陆
        _httpClient = new OkHttpClient.Builder()
                .cookieJar(new CookieJar() {
                    @Override
                    public void saveFromResponse(@NotNull HttpUrl httpUrl, @NotNull List<Cookie> list) {
                        cookies.put(httpUrl.host(), list);
                    }

                    @NotNull
                    @Override
                    public List<Cookie> loadForRequest(@NotNull HttpUrl httpUrl) {
                        List<Cookie> cookies = WanAndroidApiHandler.this.cookies.get(httpUrl.host());
                        return cookies == null ? new ArrayList<>() : cookies;
                    }
                })
                .build();

        // 创建 Retrofit 实例
        _retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .callFactory(_httpClient)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();

        // 创建 APIService 实例
        _client = _retrofit.create(WanAndroidApiService.class);
    }


    @SuppressLint("CheckResult")
    //发起网络请求
    public void aSyncLogin(String username, String password) {
        this._client.wanAndroidLogin(username, password)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(new Consumer<LoginResponse>() {
                    @Override
                    public void accept(LoginResponse response) throws Throwable {
                        if (response.errorCode != 0) {
                            System.out.println(PREFIX + "Encountered error: ");
                            System.out.println(PREFIX + "Response code is " + response.errorCode);
                            System.out.println(PREFIX + "Response message is " + response.errorMsg);
                        } else {
                            System.out.println(PREFIX + "Login succeed");
                        }
                    }
                });
    }

    @SuppressLint("CheckResult")
    // 获取收藏文章
    public void aSyncGetArticle(int pageNum) {
        this._client.getArticle(0)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Throwable {
                        System.out.println(PREFIX + responseBody.string());
                    }
                });
    }

}
