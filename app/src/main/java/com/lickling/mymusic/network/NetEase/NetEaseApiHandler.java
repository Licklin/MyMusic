package com.lickling.mymusic.network.NetEase;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.google.gson.GsonBuilder;
import com.lickling.mymusic.network.NetEase.bean.QrCodeKeyRespone;
import com.lickling.mymusic.network.NetEase.bean.QrCodeResponse;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetEaseApiHandler {

    boolean __DEBUG__ = true;

    protected String BASE_URL = "http://localhost:4000";
    private final String PREFIX = "[WanAndroidUnitTest] ";

    private Map<String, List<Cookie>> cookies = new HashMap<>();

    private OkHttpClient _httpClient;
    private Retrofit _retrofit;
    public NetEaseApiService _client;

    public NetEaseApiHandler() {
        this.__init__(BASE_URL);
    }

    public NetEaseApiHandler(String BASE_URL) {
        this.__init__(BASE_URL);
    }

    private void __init__(String BASE_URL) {
        this.BASE_URL = BASE_URL;
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
                        List<Cookie> cookies = NetEaseApiHandler.this.cookies.get(httpUrl.host());
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
        _client = _retrofit.create(NetEaseApiService.class);
    }

    //----类方法----

    @SuppressLint("CheckResult")
    public void getQrKey() {
        long timestamp = System.currentTimeMillis();
        _client.getQrKey(timestamp)
                .timeout(10000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .map(new Function<QrCodeKeyRespone, String>() {
                    @Override
                    public String apply(QrCodeKeyRespone qrCodeKeyRespone) throws Throwable {
                        if (__DEBUG__)
                            System.out.println("[NewEaseTest: qrCodeKey Obj] " + qrCodeKeyRespone);
                        return qrCodeKeyRespone.getUniKey() == null ? "" : qrCodeKeyRespone.getUniKey();
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String qrCodeKey) throws Throwable {
                        if (__DEBUG__)
                            System.out.println("[NewEaseTest subscribe: Consumer] " + qrCodeKey);
                    }
                }, new Consumer<Throwable>() { // 添加 onError 处理程序
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        // 在这里处理异常情况
                        if (__DEBUG__)
                            System.out.println("[NewEaseTest subscribe: Error] " + throwable.getMessage());
                    }
                });
    }

    @SuppressLint("CheckResult")
    public void getQrCode() {
        long timestamp = System.currentTimeMillis();
        _client.getQrKey(timestamp)
                .timeout(10000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .flatMap(new Function<QrCodeKeyRespone, Flowable<QrCodeResponse>>() {
                    @Override
                    public Flowable<QrCodeResponse> apply(QrCodeKeyRespone qrCodeKeyRespone) throws Throwable {
                        // 检测发来的QrKey, 然后请求二维码生成接口
                        if (__DEBUG__)
                            System.out.println("[NewEaseTest flatMap: qrCodeKey Obj ]" + qrCodeKeyRespone);
                        String key = qrCodeKeyRespone.getUniKey() == null ? "" : qrCodeKeyRespone.getUniKey();
                        return _client.getQrCode(key, "true", timestamp);
                    }
                })
                .map(new Function<QrCodeResponse, Bitmap>() {
                    @Override
                    public Bitmap apply(QrCodeResponse qrCodeResponse) throws Throwable {
                        String qrImageBase64 = "";
                        if (qrCodeResponse != null) {
                            qrImageBase64 = qrCodeResponse.data.qrimg;
                            // 去除头部信息
                            String base64Data = qrImageBase64.substring(qrImageBase64.indexOf(",") + 1);

                            if (__DEBUG__)
                                System.out.println("[NewEaseTest subscribe: qrImageBase64] " + base64Data);

                            // 解码为字节数组
                            byte[] decodedBytes = Base64.decode(base64Data, Base64.DEFAULT);

                            // 解码为Bitmap
                            Bitmap QrBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);

                            return QrBitmap;
                        }
                        return null;
                    }
                })
                .subscribe(new Consumer<Bitmap>() {
                    @Override
                    public void accept(Bitmap QRCode) throws Throwable {
                        if (__DEBUG__)
                            System.out.println("[NewEaseTest subscribe: Alive] ");
                        // do something?
                    }
                }, new Consumer<Throwable>() { // 添加 onError 处理程序
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        // 在这里处理异常情况
                        if (__DEBUG__)
                            System.out.println("[NewEaseTest subscribe: Error] " + throwable.getMessage());
                    }
                });
    }


}
