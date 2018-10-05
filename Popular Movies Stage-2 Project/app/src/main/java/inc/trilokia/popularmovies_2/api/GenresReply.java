package inc.trilokia.popularmovies_2.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GenresReply<T> {

    @SerializedName("genres")
    private List<T> genres;

    public List<T> getGenres() {
        return genres;
    }

    public GenresReply(List<T> genres) {
        this.genres = genres;
    }

}