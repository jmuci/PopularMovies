package com.example.jmucientes.popularmovies.model;

/**
 * This class will represent a VideoTrailer Trailer.
 */
public class VideoTrailer {

    public static final String SITE_YOU_TUBE = "YouTube";
    private final String YOU_TUBE_URL = "https://www.youtube.com/watch?v=";

    final private String mId;
    final private String mKey;
    final private String mName;
    final private String mSite;


    public VideoTrailer(String id, String key, String name, String site) {
        mId = id;
        mKey = key;
        mName = name;
        mSite = site;
    }

    public String getId() {
        return mId;
    }

    public String getKey() {
        return mKey;
    }

    public String getName() {
        return mName;
    }

    public String getSite() {
        return mSite;
    }

    public String getYouTubeLink() {
        if (SITE_YOU_TUBE.equals(mSite)) {
            return YOU_TUBE_URL + mKey;
        }
        return "Error: No YouTube link available";
    }

    @Override
    public String toString() {
        return "VideoTrailer{" +
                "mId='" + mId + '\'' +
                ", mKey='" + mKey + '\'' +
                ", mName='" + mName + '\'' +
                ", mSite='" + mSite + '\'' +
                '}';
    }
}

