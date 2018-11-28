package com.example.jmucientes.popularmovies.util;

import android.net.Uri;
import android.support.annotation.NonNull;

public class ImageUriUtils {
    private static final String HTTP_SCHEME = "http";

    private static final String PATH = "t/p";
    private static final String IMAGES_AUTH = "image.tmdb.org";

    // TODO This should be an ENUM to guarentee a valid size
    public static final String IMAGE_SIZE_W_185 = "w185";
    public static final String IMAGE_SIZE_W_342 = "w342";
    public static final String IMAGE_SIZE_W_500 = "w500";


    /**
     * The base URL will look like: http://image.tmdb.org/t/p/.
     * Then you will need a ‘size’, which will be one of the following: "w92", "w154", "w185", "w342", "w500", "w780", or "original". For most phones we recommend using “w185”.
     * And finally the poster path returned by the query, in this case “/nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg”
     * @param imagePath String
     * @return Uri
     */
    @NonNull
    public static Uri getFullyQualifiedImageUri(@NonNull String imagePath) {
        return getFullyQualifiedImageUri(imagePath, IMAGE_SIZE_W_185);
    }

    @NonNull
    public static Uri getFullyQualifiedImageUri(@NonNull String imagePath, @NonNull String imageSize) {
        if (imagePath.length() > 0 && '/' == (imagePath.charAt(0))) { //Remove extra slash
            imagePath = imagePath.substring(1, imagePath.length());
        }
        Uri.Builder builder = new Uri.Builder();
        return builder.scheme(HTTP_SCHEME)
                .authority(IMAGES_AUTH)
                .path(PATH)
                .appendPath(imageSize)
                .appendPath(imagePath)
                .build();
    }

}
