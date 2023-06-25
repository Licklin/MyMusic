package com.lickling.mymusic.network.NetEase;

import android.annotation.SuppressLint;

import com.google.gson.GsonBuilder;
import com.lickling.mymusic.network.NetEase.bean.QrCodeKeyRespone;
import com.lickling.mymusic.network.NetEase.bean.QrCodeObtainResponse;

import org.jetbrains.annotations.NotNull;

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
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetEaseApiHandler {

    private Map<String, List<Cookie>> _cookies = new HashMap<>();
    private String _qrCodeKey = null;
    private OkHttpClient _httpClient;
    private Retrofit _retrofit;

    protected String _BASE_URL = "http://localhost:4000";

    public boolean __DEBUG__ = true;
    public NetEaseApiService _client;

    public NetEaseApiHandler() {
        this.__init__(_BASE_URL);
    }

    public NetEaseApiHandler(String BASE_URL) {
        this.__init__(BASE_URL);
    }

    public Consumer<Throwable> defErrorHandler() {
        return new Consumer<Throwable>() { // 添加 onError 处理程序
            @Override
            public void accept(Throwable throwable) throws Throwable {
                // 在这里处理异常情况
                System.out.println("[NetEaseTest subscribe: Error] " + throwable.getMessage());
            }
        };
    }

    private void __init__(String BASE_URL) {
        this._BASE_URL = BASE_URL;
        // 创建 OKHttpClient 实例, 包含了Cookie Jar, 可以登陆
        _httpClient = new OkHttpClient.Builder()
                .cookieJar(new CookieJar() {
                    @Override
                    public void saveFromResponse(@NotNull HttpUrl httpUrl, @NotNull List<Cookie> list) {
                        _cookies.put(httpUrl.host(), list);
                    }

                    @NotNull
                    @Override
                    public List<Cookie> loadForRequest(@NotNull HttpUrl httpUrl) {
                        List<Cookie> cookies = NetEaseApiHandler.this._cookies.get(httpUrl.host());
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

    private void setQrCodeKey(String qrCodeKey) {
        // 只使用这个来设置this._qrCodeKey
        this._qrCodeKey = qrCodeKey;
    }

    private String getQrCodeKey() {
        // 只使用这个来获取this._qrCodeKey, 用完就设为空
        if (this._qrCodeKey == null)
            return null;
        String tmp = this._qrCodeKey;
//        this._qrCodeKey = null;
        return tmp;
    }

    @SuppressLint("CheckResult")
    public Flowable<String> getQrCode() {
        // 异步获取二维码的函数的部分, 需要在主线程订阅.
        long timestamp = System.currentTimeMillis();
        return _client.getQrKey(timestamp)
                .timeout(7000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .flatMap(new Function<QrCodeKeyRespone, Flowable<QrCodeObtainResponse>>() {
                    @Override
                    public Flowable<QrCodeObtainResponse> apply(QrCodeKeyRespone qrCodeKeyRespone) throws Throwable {
                        // 检测发来的QrKey, 然后请求二维码生成接口
                        if (__DEBUG__)
                            System.out.println("[NetEaseTest flatMap: qrCodeKey Obj ]" + qrCodeKeyRespone);
                        String key = qrCodeKeyRespone.getUniKey() == null ? "" : qrCodeKeyRespone.getUniKey();
                        setQrCodeKey(key);
                        return _client.getQrCode(key, "true", timestamp);
                    }
                })
                .map(new Function<QrCodeObtainResponse, String>() {
                    @Override
                    public String apply(QrCodeObtainResponse qrCodeObtainResponse) throws Throwable {
                        String QRCodeBase64 = "";
                        if (qrCodeObtainResponse != null) {

                            if (__DEBUG__)
                                System.out.println("[NetEaseTest map: qrImageUrl] " + qrCodeObtainResponse);

                            // 获取Base64编码, 然后去除头部信息
                            QRCodeBase64 = qrCodeObtainResponse.data.qrimg;
                            QRCodeBase64 = QRCodeBase64.substring(QRCodeBase64.indexOf(",") + 1);

                            return QRCodeBase64;
                        }
                        return null;
                    }
                })
                //.observeOn(AndroidSchedulers.mainThread())
                ;
    }

    public Flowable<ResponseBody> checkQrCodeStatus_rawReturn() {
        long timestamp = System.currentTimeMillis();
        String qrCodeKey = null;

        while (qrCodeKey == null) {
            qrCodeKey = getQrCodeKey();
        }

        System.out.println("[qrCodeKey] " + qrCodeKey);
        return _client.checkQrCodeStatus_rawReturn(qrCodeKey, timestamp);
    }

}
