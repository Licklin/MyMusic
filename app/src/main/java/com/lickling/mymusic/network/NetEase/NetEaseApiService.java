package com.lickling.mymusic.network.NetEase;

import com.lickling.mymusic.network.NetEase.bean.QrCodeKeyRespone;
import com.lickling.mymusic.network.NetEase.bean.QrCodeResponse;

import io.reactivex.rxjava3.core.Flowable;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NetEaseApiService {

    @GET("/login/qr/key")
    Flowable<QrCodeKeyRespone> getQrKey(@Query("timestamp") long timestamp);

    @GET("/login/qr/create")
    Flowable<QrCodeResponse> getQrCode(@Query("key") String qrKey, @Query("qrimg") String qrimg, @Query("timestamp") long timestamp);

}
