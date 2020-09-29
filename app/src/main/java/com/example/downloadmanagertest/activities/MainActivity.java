package com.example.downloadmanagertest.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;

import com.example.downloadmanagertest.R;
import com.example.downloadmanagertest.retrofit.NetworkService;

import java.io.File;
import java.io.FileOutputStream;

import org.apache.commons.io.IOUtils;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private Button btnDownloadToDownloads, btnDownloadToInternal, btnDownloadByRetrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnDownloadToDownloads = findViewById(R.id.btnDownloadToDownloads);
        btnDownloadToDownloads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermissions()) startDownloadManagerToDownloads();
                else requestPerms();
            }
        });

        btnDownloadToInternal = findViewById(R.id.btnDownloadToInternalStorage);
        btnDownloadToInternal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermissions()) startDownloadManagerToInternalStorage();
                else requestPerms();
            }
        });

        btnDownloadByRetrofit = findViewById(R.id.btnDownloadByRetrofit);
        btnDownloadByRetrofit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermissions()) startDownloadByRetrofit();
                else requestPerms();
            }
        });



        if (!checkPermissions())requestPerms();


    }



    private void startDownloadManagerToDownloads(){
        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse("https://sun9-4.userapi.com/c855524/v855524015/255cf7/M8oLTrBszSU.jpg");

        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle("VK Photo");
        request.setDescription("Downloading");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "VKPhoto.jpg");
        downloadManager.enqueue(request);
    }



    private void startDownloadManagerToInternalStorage(){
        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse("https://sun9-4.userapi.com/c855524/v855524015/255cf7/M8oLTrBszSU.jpg");

        try {
            File file = new File(getFilesDir(), "VKPhoto.jpg");
            file.createNewFile();
            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setTitle("VK Photo");
            request.setDescription("Downloading");
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationUri(FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName()+".provider", file));
            downloadManager.enqueue(request);
        }
        catch (Exception e){}
    }



    private void startDownloadByRetrofit(){
        NetworkService.getInstance().getDownloadAPI()
                .downloadFromVK("M8oLTrBszSU.jpg").enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    File path = Environment.getExternalStorageDirectory();
                    File file = new File(path, "VKPhotoRetrofit.jpg");
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    IOUtils.write(response.body().bytes(), fileOutputStream);
                }
                catch (Exception e){

                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }




    private void requestPerms(){
        String[] perm = new String[]{Manifest.permission.INTERNET, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            ActivityCompat.requestPermissions(this,perm,123);
        }
    }
    

    private boolean checkPermissions(){

        if((ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.INTERNET) == PackageManager.PERMISSION_DENIED) ||
                (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)){
            return false;
        }
        else {
            return true;
        }
    }

}