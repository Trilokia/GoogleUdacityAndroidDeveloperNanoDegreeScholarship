package inc.trilokia.popularmovies_2.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrailersReply<T> {

    @SerializedName("id")
    private int id;
    @SerializedName("results")
    private List<T> results;

    public int getId() {
        return id;
    }

    public List<T> getResults() {
        return results;
    }

    public TrailersReply(int id, List<T> results) {
        this.id = id;
        this.results = results;
    }

}