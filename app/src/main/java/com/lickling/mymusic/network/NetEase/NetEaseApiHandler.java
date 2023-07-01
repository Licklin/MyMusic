package com.lickling.mymusic.network.NetEase;

import android.annotation.SuppressLint;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.lickling.mymusic.bean.networkBean.CloudSearchPlayListResponse;
import com.lickling.mymusic.bean.networkBean.CloudSearchSingleSongResponse;
import com.lickling.mymusic.bean.networkBean.LikeListResponse;
import com.lickling.mymusic.bean.networkBean.LoginStatusResponse;
import com.lickling.mymusic.bean.networkBean.PlayListTrackAllResponse;
import com.lickling.mymusic.bean.networkBean.QrCodeCheckResponse;
import com.lickling.mymusic.bean.networkBean.QrCodeKeyRespone;
import com.lickling.mymusic.bean.networkBean.QrCodeObtainResponse;
import com.lickling.mymusic.bean.networkBean.SongUrlResponse;
import com.lickling.mymusic.bean.networkBean.UserPlaylistResponse;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
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
    private String TAG = "NetEaseApiHandler ";
    private static volatile Map<String, List<Cookie>> _cookies = new HashMap<>();
    public static volatile String _qrCodeKey = null;
    private OkHttpClient _httpClient;
    private Retrofit _retrofit;
    private final int DEF_TIME_OUT_MILLISECOND = 10000;

    protected String _BASE_URL = "http://192.168.31.31:3000";

    public boolean __DEBUG__ = true;
    public NetEaseApiService _client;

    public final String SINGLE_SONG = "1";
    public final String PLAY_LIST = "1000";

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

    //----类内方法----

    private void setQrCodeKey(String qrCodeKey) {
        // 只使用这个来设置this._qrCodeKey
        synchronized (this) {
            this._qrCodeKey = qrCodeKey;
//            System.out.println("setQrCodeKey" + this._qrCodeKey);
        }
    }

    private String getQrCodeKey() {
        // 只使用这个来获取this._qrCodeKey
        synchronized (this) {
            System.out.println("getQrCodeKey:" + (this._qrCodeKey == null));
            if (this._qrCodeKey != null) {
                System.out.println("getQrCodeKey:" + this._qrCodeKey);
                return this._qrCodeKey;
            }
            return null;
        }

    }

        public void saveStringAsJsonFile(UserPlaylistResponse jsonString, String filePath) {
        Gson gson = new Gson();
        String json = gson.toJson(jsonString);
        try {
            FileWriter fileWriter = new FileWriter(filePath);
            fileWriter.write(json);
            fileWriter.close();
            System.out.println("[saveStringAsJsonFile Saved] " + filePath);
        } catch (IOException e) {
            System.out.println("[saveStringAsJsonFile Error] " + e.getMessage());
        }
    }

    // ----过时的----

    public void saveCookiesVarToFile(String fileName) {
        Gson gson = new Gson();
        String json = gson.toJson(this._cookies);

        try (PrintWriter out = new PrintWriter(fileName)) {
            out.println(json);
            System.out.println("[saveCookiesVarToFile Should be done...] ");
        } catch (FileNotFoundException e) {
            System.out.println("[saveCookiesVarToFile Error] " + e.getMessage());
        }
    }

    public void loadCookiesStringFromFile(String fileName) {
        Gson gson = new Gson();
        Type mapType = new TypeToken<Map<String, List<Cookie>>>() {
        }.getType();

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String json = reader.readLine();
            this._cookies = gson.fromJson(json, mapType);
        } catch (FileNotFoundException e) {
            System.out.println("[loadCookiesStringFromFile No file error] " + e.getMessage());
        } catch (Exception e) {
            System.out.println("[loadCookiesStringFromFile Error] " + e.getMessage());
        }

    }

    //----类方法----
    public String cookie2Json() {
        Gson gson = new Gson();
        String json = gson.toJson(this._cookies);
        System.out.println("[saveStringAsJsonFile Returned] " + json);
        return json;
    }

    public void json2Cookie(String json) {
        Gson gson = new Gson();
        Type mapType = new TypeToken<Map<String, List<Cookie>>>() {
        }.getType();

        try {
            this._cookies = gson.fromJson(json, mapType);
        } catch (Exception e) {
            System.out.println("[loadCookiesStringFromFile Error] " + e.getMessage());
            this._cookies = new HashMap<>();
        }

//        Log.e("CLIENT", "json2Cookie: " + _cookies.toString());

    }

    public void printCookies() {
        System.out.println(_cookies.toString());
    }

    //----异步方法----

    @SuppressLint("CheckResult")
    public Flowable<String> getQrCode() {
        // 异步获取二维码的函数的部分, 需要在主线程订阅.
        long timestamp = System.currentTimeMillis();
        return _client.getQrKey(timestamp)
                .timeout(DEF_TIME_OUT_MILLISECOND, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Function<QrCodeKeyRespone, Flowable<QrCodeObtainResponse>>() {
                    @Override
                    public Flowable<QrCodeObtainResponse> apply(QrCodeKeyRespone qrCodeKeyRespone) throws Throwable {
                        // 检测发来的QrKey, 然后请求二维码生成接口
                        if (__DEBUG__)
                            System.out.println("[NetEaseTest flatMap: qrCodeKey Obj ]" + qrCodeKeyRespone);
                        String key = qrCodeKeyRespone.getUniKey() == null ? "" : qrCodeKeyRespone.getUniKey();
                        NetEaseApiHandler._qrCodeKey = key;
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

    public Flowable<QrCodeCheckResponse> checkQrCodeStatus() {
        long timestamp = System.currentTimeMillis();
        String qrCodeKey = "25ed2d0d-4983-4a6e-99e5-1f237f205817";
        if (getQrCodeKey() != null) {
            qrCodeKey = getQrCodeKey();
        }


        return _client.checkQrCodeStatus(qrCodeKey, timestamp)
                .timeout(DEF_TIME_OUT_MILLISECOND, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.newThread());
    }

    public Flowable<LoginStatusResponse> getLoginStatus() {
        return _client.getLoginStatus(System.currentTimeMillis())
                .timeout(DEF_TIME_OUT_MILLISECOND, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.computation());
    }

    public Flowable<LikeListResponse> getLikeList(String uid) {
        return _client.getLikeList(uid)
                .timeout(DEF_TIME_OUT_MILLISECOND, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.computation());
    }

    public Flowable<UserPlaylistResponse> getUserPlaylist(String uid, int limit, int offset) {
        return _client.getUserPlayList(uid, limit, offset, System.currentTimeMillis())
                .timeout(DEF_TIME_OUT_MILLISECOND, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.computation());
    }

    public Flowable<CloudSearchSingleSongResponse> getCloudSearchSingleSong(String keywords, int limit, int offset) {
        return _client.getCloudSearchSingleSong(keywords, limit, offset, this.SINGLE_SONG, System.currentTimeMillis())
                .timeout(DEF_TIME_OUT_MILLISECOND, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.computation());
    }

    public Flowable<CloudSearchPlayListResponse> getCloudSearchPlayList(String keywords, int limit, int offset) {
        return _client.getCloudSearchPlayList(keywords, limit, offset, this.PLAY_LIST)
                .timeout(DEF_TIME_OUT_MILLISECOND, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.computation());
    }

    public Flowable<SongUrlResponse> getSongUrl(String id, String br) {
        return _client.getSongUrl(id, br)
                .timeout(DEF_TIME_OUT_MILLISECOND, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.computation());
    }

    public Flowable<SongUrlResponse> getSongUrl(String id) {
        return _client.getSongUrl(id)
                .timeout(DEF_TIME_OUT_MILLISECOND, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.computation());
    }

    public Flowable<PlayListTrackAllResponse> getPlayListTrackAll(String id, int limit, int offset) {
        return _client.getPlayListTrackAll(id, limit, offset, System.currentTimeMillis())
                .timeout(DEF_TIME_OUT_MILLISECOND, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.computation());
    }

    public Flowable<ResponseBody> logOut() {
        return _client.logout(System.currentTimeMillis())
                .timeout(DEF_TIME_OUT_MILLISECOND, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.computation());
    }
}
