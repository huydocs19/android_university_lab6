package com.codepath.nytimes.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ArticleMedia {
    @SerializedName("type")
    public String type;

    @SerializedName("media-metadata")
    public List<MetaData> mediaMetaData;


}