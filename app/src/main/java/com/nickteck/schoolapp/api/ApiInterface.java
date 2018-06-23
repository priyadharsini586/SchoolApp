package com.nickteck.schoolapp.api;

import com.nickteck.schoolapp.model.AboutMyChildDetails;
import com.nickteck.schoolapp.model.AnnoncementDetails;
import com.nickteck.schoolapp.model.LoginDetails;
import com.nickteck.schoolapp.model.ParentDetails;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by admin on 6/14/2018.
 */

public interface ApiInterface {

    @FormUrlEncoded
    @POST("parent_check.php")
    Call<LoginDetails> checkMobileNo(@Field("x") JSONObject object);

    @FormUrlEncoded
    @POST("get_opt.php")
    Call<LoginDetails> checkOpt(@Field("x") JSONObject object);

    @FormUrlEncoded
    @POST("verify_otp.php")
    Call<LoginDetails> verifyOtp(@Field("x") JSONObject object);

    @FormUrlEncoded
    @POST("parent_details.php")
    Call<ParentDetails> getParentDetails(@Field("x") JSONObject object);

    @FormUrlEncoded
    @POST("about_child.php")
    Call<AboutMyChildDetails> getChildAboutDetails(@Field("x") JSONObject object);

    @FormUrlEncoded
    @POST("about_all_child.php")
    Call<AboutMyChildDetails> getAllChildAboutDetails(@Field("x") JSONObject object);

    @FormUrlEncoded
    @POST("announcement.php")
    Call<AnnoncementDetails> getAnnoncementDetails(@Field("x") JSONObject object);

    @FormUrlEncoded
    @POST("read_about_child.php")
    Call<AboutMyChildDetails> readStatus (@Field("x") JSONObject object);

    @FormUrlEncoded
    @POST("about_child_count.php")
    Call<AboutMyChildDetails> getMsgCount (@Field("x") JSONObject object);

    @FormUrlEncoded
    @POST("read_announcement.php")
    Call<AboutMyChildDetails> readAnnoncementStatus (@Field("x") JSONObject object);


}
