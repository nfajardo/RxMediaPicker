package com.nfajardo.rxmediapicker;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class HiddenActivity extends Activity {

    private static final String KEY_CAMERA_PICTURE_URL = "cameraPictureUrl";
    private static final String KEY_CAMERA_VIDEO_URL = "cameraVideoUrl";

    public static final String SOURCE = "image_source";
    public static final String ALLOW_MULTIPLE_IMAGES = "allow_multiple_images";
    public static final String ALLOW_MULTIPLE_VIDEOS = "allow_multiple_videos";


    private static final int SELECT_PHOTO = 100;
    private static final int TAKE_PHOTO = 101;

    private static final int SELECT_VIDEO = 200;
    private static final int TAKE_VIDEO = 201;

    private Uri cameraPictureUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            handleIntent(getIntent());
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(KEY_CAMERA_PICTURE_URL, cameraPictureUrl);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        cameraPictureUrl = savedInstanceState.getParcelable(KEY_CAMERA_PICTURE_URL);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            handleIntent(getIntent());
        } else {
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case SELECT_PHOTO:
                    handleGalleryResult(data);
                    break;
                case TAKE_PHOTO:
                    RxMediaPicker.with(this).onImagePicked(cameraPictureUrl);
                    break;
                case SELECT_VIDEO:
                    handleGalleryVideoResult(data);
                    break;
                case TAKE_VIDEO:
                    RxMediaPicker.with(this).onVideoPicked(cameraPictureUrl);
                    break;
            }
        }
        finish();
    }

    private void handleGalleryResult(Intent data) {
        if (getIntent().getBooleanExtra(ALLOW_MULTIPLE_IMAGES, false)) {
            ArrayList<Uri> imageUris = new ArrayList<>();
            ClipData clipData = data.getClipData();
            if (clipData != null) {
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    imageUris.add(clipData.getItemAt(i).getUri());
                }
            } else {
                imageUris.add(data.getData());
            }
            RxMediaPicker.with(this).onImagesPicked(imageUris);
        } else {
            RxMediaPicker.with(this).onImagePicked(data.getData());
        }
    }
    private void handleGalleryVideoResult(Intent data) {
        if (getIntent().getBooleanExtra(ALLOW_MULTIPLE_IMAGES, false)) {
            ArrayList<Uri> imageUris = new ArrayList<>();
            ClipData clipData = data.getClipData();
            if (clipData != null) {
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    imageUris.add(clipData.getItemAt(i).getUri());
                }
            } else {
                imageUris.add(data.getData());
            }
            RxMediaPicker.with(this).onVideosPicked(imageUris);
        } else {
            RxMediaPicker.with(this).onVideoPicked(data.getData());
        }
    }

    private void handleIntent(Intent intent) {
        if (!checkPermission()) {
            return;
        }

        Sources sourceType = Sources.values()[intent.getIntExtra(SOURCE, 0)];
        int chooseCode = 0;
        Intent pictureChooseIntent = null;

        switch (sourceType) {
            case CAMERAIMAGE:
                cameraPictureUrl = createImageUri();
                pictureChooseIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                pictureChooseIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraPictureUrl);
                chooseCode = TAKE_PHOTO;
                break;
            case GALLERYIMAGE:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    pictureChooseIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    pictureChooseIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,
                            getIntent().getBooleanExtra(ALLOW_MULTIPLE_IMAGES, false));
                    pictureChooseIntent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
                } else {
                    pictureChooseIntent = new Intent(Intent.ACTION_GET_CONTENT);
                }
                pictureChooseIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                pictureChooseIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                pictureChooseIntent.setType("image/*");
                chooseCode = SELECT_PHOTO;
                break;
            case CAMERAVIDEO:
                cameraPictureUrl = createImageUri();
                pictureChooseIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                pictureChooseIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraPictureUrl);
                chooseCode = TAKE_VIDEO;
                break;
            case GALLERYVIDEO:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    pictureChooseIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    pictureChooseIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,
                            getIntent().getBooleanExtra(ALLOW_MULTIPLE_VIDEOS, false));
                    pictureChooseIntent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
                } else {
                    pictureChooseIntent = new Intent(Intent.ACTION_GET_CONTENT);
                }
                pictureChooseIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                pictureChooseIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                pictureChooseIntent.setType("video/*");
                chooseCode = SELECT_VIDEO;
                break;
        }

        startActivityForResult(pictureChooseIntent, chooseCode);
    }

    private boolean checkPermission() {
        if (ContextCompat.checkSelfPermission(HiddenActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(HiddenActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    0);
            return false;
        } else {
            return true;
        }
    }

    private Uri createImageUri() {
        ContentResolver contentResolver = getContentResolver();
        ContentValues cv = new ContentValues();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        cv.put(MediaStore.Images.Media.TITLE, timeStamp);
        return contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cv);
    }

}
