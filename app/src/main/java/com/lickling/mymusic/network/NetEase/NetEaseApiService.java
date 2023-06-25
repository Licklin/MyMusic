package com.lickling.mymusic.network.NetEase;

import com.lickling.mymusic.network.NetEase.bean.QrCodeCheckResponse;
import com.lickling.mymusic.network.NetEase.bean.QrCodeKeyRespone;
import com.lickling.mymusic.network.NetEase.bean.QrCodeObtainResponse;

import io.reactivex.rxjava3.core.Flowable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NetEaseApiService {

    @GET("/login/qr/key")
    Flowable<QrCodeKeyRespone> getQrKey(@Query("timestamp") long timestamp);

    @GET("/login/qr/create")
    Flowable<QrCodeObtainResponse> getQrCode(@Query("key") String qrKey,
                                             @Query("qrimg") String qrimg,
                                             @Query("timestamp") long timestamp);

    @GET("/login/qr/check")
    Flowable<ResponseBody> checkQrCodeStatus_rawReturn(@Query("key") String qrKey,
                                                       @Query("timestamp") long timestamp);

    @GET("/login/qr/check")
    Flowable<QrCodeCheckResponse> checkQrCodeStatus(@Query("key") String qrKey,
                                                    @Query("timestamp") long timestamp);


}
