# RxMediaPicker
An easy way to get image or Video from Gallery or Camera with request runtime permission on Android M using RxJava2. Based on RxImagePicker(@MLSDev)

Includes Sample

## Example Image

```java
RxImagePicker.with(context).requestImage(Sources.GALLERYIMAGE).subscribe(new Consumer<Uri>() {
                    @Override
                    public void accept(@NonNull Uri uri) throws Exception {
                        //Get image by uri using one of image loading libraries. I love Picasso
                    }
                });
```

## Example Video Gallery

```java
RxImagePicker.with(context).requestVideo(Sources.GALLERYVIDEO).subscribe(new Consumer<Uri>() {
                    @Override
                    public void accept(@NonNull Uri uri) throws Exception {
                        //Get video by uri.
                    }
                });
```

## Example Video Camera

```java
RxImagePicker.with(context).requestVideo(Sources.CAMERAVIDEO).subscribe(new Consumer<Uri>() {
                    @Override
                    public void accept(@NonNull Uri uri) throws Exception {
                        //Get video by uri.
                    }
                });
```


## Developer
* Nelson FB 

# License
RxMediaPicker is released under the MIT license. See LICENSE for details.
