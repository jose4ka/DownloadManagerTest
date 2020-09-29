package com.example.downloadmanagertest.retrofit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface DownloadAPI {

    @GET
    Call<ResponseBody> downloadFromVK(@Url String fileUrl);
}
