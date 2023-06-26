package com.lickling.mymusic;

import android.annotation.SuppressLint;

import com.google.gson.GsonBuilder;
import com.lickling.mymusic.network.wanAndroid.WanAndroidApiService;
import com.lickling.mymusic.network.wanAndroid.bean.LoginResponse;

import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.reactivestreams.Publisher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class WanAndroidUnitTest {
    Map<String, List<Cookie>> cookies = new HashMap<>();

    private static final String BASE_URL = "https://www.wanandroid.com/";
    private static final String PREFIX = "[WanAndroidUnitTest] ";

    @SuppressLint("CheckResult")
    @Test
    public void WanAndroidUnitTestMain() {
        // 创建 OKHttpClient 实例, 包含了Cookie Jar, 可以登陆
        OkHttpClient wanAndroidHttpClient = new OkHttpClient.Builder()
                .cookieJar(new CookieJar() {
                    @Override
                    public void saveFromResponse(@NotNull HttpUrl httpUrl, @NotNull List<Cookie> list) {
                        cookies.put(httpUrl.host(), list);
                    }

                    @NotNull
                    @Override
                    public List<Cookie> loadForRequest(@NotNull HttpUrl httpUrl) {
                        List<Cookie> cookies = WanAndroidUnitTest.this.cookies.get(httpUrl.host());
                        return cookies == null ? new ArrayList<>() : cookies;
                    }
                })
                .build();
        // 创建 Retrofit 实例
        Retrofit wanAndroidRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .callFactory(wanAndroidHttpClient)
                //.client(wanAndroidHttpClient)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();

        // 创建 WanAndroidApiService 实例
        WanAndroidApiService wanAndroidClient = wanAndroidRetrofit.create(WanAndroidApiService.class);

        // 发起网络请求
        wanAndroidClient.wanAndroidLogin("_temp_acc_", "__temp__passcode__")
                .timeout(3000, TimeUnit.MILLISECONDS)
                .flatMap(new Function<LoginResponse, Publisher<ResponseBody>>() {
                    @Override
                    public Publisher<ResponseBody> apply(LoginResponse responseBean) throws Throwable {
                        System.out.println(PREFIX + responseBean.errorCode);
                        // 执行获取文章列表请求
                        return wanAndroidClient.getArticle(0);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.newThread())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Throwable {
                        System.out.println(PREFIX + responseBody.string());
                    }
                }, new Consumer<Throwable>() { // 添加 onError 处理程序
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        // 在这里处理异常情况
                        System.out.println(PREFIX + "Error: " + throwable.getMessage());
                    }
                });

        while (true) {
        }
    }
}