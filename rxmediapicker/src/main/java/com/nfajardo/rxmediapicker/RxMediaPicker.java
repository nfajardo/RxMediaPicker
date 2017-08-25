package com.nfajardo.rxmediapicker;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public class RxMediaPicker {

    private static RxMediaPicker instance;

    public static synchronized RxMediaPicker with(Context context) {
        if (instance == null) {
            instance = new RxMediaPicker(context.getApplicationContext());
        }
        return instance;
    }

    private Context context;
    private PublishSubject<Uri> publishSubject;
    private PublishSubject<List<Uri>> publishSubjectMultipleImages;

    private RxMediaPicker(Context context) {
        this.context = context;
    }

    public Observable<Uri> getActiveSubscription() {
        return publishSubject;
    }

    public Observable<Uri> requestImage(Sources imageSource) {
        publishSubject = PublishSubject.create();
        startImagePickHiddenActivity(imageSource.ordinal(), false);
        return publishSubject;
    }
    public Observable<Uri> requestVideo(Sources videoSource) {
        publishSubject = PublishSubject.create();
        startVideoPickHiddenActivity(videoSource.ordinal(), false);
        return publishSubject;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public Observable<List<Uri>> requestMultipleImages() {
        publishSubjectMultipleImages = PublishSubject.create();
        startImagePickHiddenActivity(Sources.GALLERYIMAGE.ordinal(), true);
        return publishSubjectMultipleImages;
    }

    void onImagePicked(Uri uri) {
        if (publishSubject != null) {
            publishSubject.onNext(uri);
            publishSubject.onComplete();
        }
    }

    void onVideoPicked(Uri uri) {
        if (publishSubject != null) {
            publishSubject.onNext(uri);
            publishSubject.onComplete();
        }
    }

    void onImagesPicked(List<Uri> uris) {
        if (publishSubjectMultipleImages != null) {
            publishSubjectMultipleImages.onNext(uris);
            publishSubjectMultipleImages.onComplete();
        }
    }
    void onVideosPicked(List<Uri> uris) {
        if (publishSubjectMultipleImages != null) {
            publishSubjectMultipleImages.onNext(uris);
            publishSubjectMultipleImages.onComplete();
        }
    }

    private void startImagePickHiddenActivity(int imageSource, boolean allowMultipleImages) {
        Intent intent = new Intent(context, HiddenActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(HiddenActivity.ALLOW_MULTIPLE_IMAGES, allowMultipleImages);
        intent.putExtra(HiddenActivity.SOURCE, imageSource);
        context.startActivity(intent);
    }
    private void startVideoPickHiddenActivity(int imageSource, boolean allowMultipleImages) {
        Intent intent = new Intent(context, HiddenActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(HiddenActivity.ALLOW_MULTIPLE_VIDEOS, allowMultipleImages);
        intent.putExtra(HiddenActivity.SOURCE, imageSource);
        context.startActivity(intent);
    }

}

