package com.lickling.mymusic.network.NetEase;

import com.lickling.mymusic.network.NetEase.bean.CloudSearchPlayListResponse;
import com.lickling.mymusic.network.NetEase.bean.CloudSearchSingleSongResponse;
import com.lickling.mymusic.network.NetEase.bean.LikeListResponse;
import com.lickling.mymusic.network.NetEase.bean.LoginStatusResponse;
import com.lickling.mymusic.network.NetEase.bean.QrCodeCheckResponse;
import com.lickling.mymusic.network.NetEase.bean.QrCodeKeyRespone;
import com.lickling.mymusic.network.NetEase.bean.QrCodeObtainResponse;
import com.lickling.mymusic.network.NetEase.bean.SongUrlResponse;
import com.lickling.mymusic.network.NetEase.bean.UserPlaylistResponse;

import io.reactivex.rxjava3.core.Flowable;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface NetEaseApiService {

    @GET("/login/qr/key")
    Flowable<QrCodeKeyRespone> getQrKey(@Query("timestamp") long timestamp);

    @GET("/login/qr/create")
    Flowable<QrCodeObtainResponse> getQrCode(@Query("key") String qrKey,
                                             @Query("qrimg") String qrimg,
                                             @Query("timestamp") long timestamp);

    @GET("/login/qr/check")
    Flowable<QrCodeCheckResponse> checkQrCodeStatus(@Query("key") String qrKey,
                                                    @Query("timestamp") long timestamp);

    @GET("/login/status")
    Flowable<LoginStatusResponse> getLoginStatus(@Query("timestamp") long timestamp);

    @POST("/user/playlist")
    Flowable<UserPlaylistResponse> getUserPlayList(@Query("uid") String uid,
                                                   @Query("limit") int limit,
                                                   @Query("offset") int offset,
                                                   @Query("timestamp") long timestamp);

    @GET("/likelist")
    Flowable<LikeListResponse> getLikeList(@Query("uid") String uid);

    @GET("/cloudsearch")
    Flowable<CloudSearchSingleSongResponse> getCloudSearchSingleSong(@Query("keywords") String KeyWords,
                                                                     @Query("limit") int limit,
                                                                     @Query("offset") int offset,
                                                                     @Query("type") String type);

    @GET("/cloudsearch")
    Flowable<CloudSearchPlayListResponse> getCloudSearchPlayList(@Query("keywords") String KeyWords,
                                                                 @Query("limit") int limit,
                                                                 @Query("offset") int offset,
                                                                 @Query("type") String type);

    @GET("/song/url")
    Flowable<SongUrlResponse> getSongUrl(@Query("id") String id,
                                         @Query("br") String br);

    @GET("/song/url")
    Flowable<SongUrlResponse> getSongUrl(@Query("id") String id);

}
