package inc.trilokia.popularmovies_2.models;

import com.google.gson.annotations.SerializedName;

public class Review {

    //Review Variables
    @SerializedName("author")
    private String author;
    @SerializedName("content")
    private String content;
    @SerializedName("url")
    private String url;

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getUrl() {
        return url;
    }

    public Review(String author, String content, String url) {
        this.author = author;
        this.content = content;
        this.url = url;
    }

}