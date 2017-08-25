package com.nfajardo.sample;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.VideoView;



import com.nfajardo.rxmediapicker.RxMediaPicker;
import com.nfajardo.rxmediapicker.Sources;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity {

    Button btn_image;
    Button btn_video;
    Button btn_video_camera;
    ImageView imgResult;
    VideoView videoResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_image = (Button)findViewById(R.id.btn_image);
        btn_video_camera = (Button)findViewById(R.id.btn_video_camera);
        btn_video = (Button)findViewById(R.id.btn_video);
        imgResult = (ImageView)findViewById(R.id.imgResult);
        videoResult = (VideoView) findViewById(R.id.videoResult);

        btn_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RxMediaPicker.with(MainActivity.this).requestImage(Sources.GALLERYIMAGE).subscribe(new Consumer<Uri>() {
                    @Override
                    public void accept(@NonNull Uri uri) throws Exception {
                        //Get image by uri using one of image loading libraries. I use Glide in sample app.


                        imgResult.setImageURI(uri);

                    }
                });
            }
        });

        btn_video_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RxMediaPicker.with(MainActivity.this).requestVideo(Sources.CAMERAVIDEO).subscribe(new Consumer<Uri>() {
                    @Override
                    public void accept(@NonNull Uri uri) throws Exception {
                        //Get image by uri using one of image loading libraries. I use Glide in sample app.

                        videoResult.setVideoPath(uri.toString());
                        videoResult.start();


                    }
                });
            }
        });


        btn_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RxMediaPicker.with(MainActivity.this).requestVideo(Sources.GALLERYVIDEO).subscribe(new Consumer<Uri>() {
                    @Override
                    public void accept(@NonNull Uri uri) throws Exception {
                        //Get image by uri using one of image loading libraries. I use Glide in sample app.

                        videoResult.setVideoPath(uri.toString());
                        videoResult.start();


                    }
                });
            }
        });

    }
}
