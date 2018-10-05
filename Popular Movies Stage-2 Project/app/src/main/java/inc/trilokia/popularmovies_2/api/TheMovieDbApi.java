package inc.trilokia.popularmovies_2.api;

import inc.trilokia.popularmovies_2.models.Genre;
import inc.trilokia.popularmovies_2.models.Movie;
import inc.trilokia.popularmovies_2.models.Review;
import inc.trilokia.popularmovies_2.models.Trailer;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TheMovieDbApi {

    @GET("genre/movie/list")
    Single<GenresReply<Genre>> getGenreList();


    @GET("movie/{popular}")
    Single<SearchReply<Movie>> discover(@Path("popular") String sortParam,
                                        @Query("language") String languageParam,
                                        @Query("vote_count.gte") String voteCountParam,
                                        @Query("page") int pageParam);


    @GET("search/movie")
    Single<SearchReply<Movie>> search(@Query("language") String languageParam,
                                      @Query("query") String queryParam,
                                      @Query("page") int pageParam);

    @GET("movie/{id}/videos")
    Single<TrailersReply<Trailer>> getTrailers(@Path("id") int id);

    @GET("movie/{id}/reviews")
    Single<ReviewsReply<Review>> getReviews(@Path("id") int id);

}