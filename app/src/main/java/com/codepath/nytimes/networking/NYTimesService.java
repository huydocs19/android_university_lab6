package com.codepath.nytimes.networking;

import com.codepath.nytimes.models.NYTimesAPIResponse;
import com.codepath.nytimes.models.NYTimesArticlesAPIResponse;
import com.codepath.nytimes.models.NYTimesPopularArticlesAPIResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NYTimesService {

    @GET("svc/books/v3/lists/{date}/{list}.json")
    Call<NYTimesAPIResponse> getBestSellingBooks(@Path("date") String date, @Path("list") String list, @Query("api-key") String apikey);

    @GET("svc/search/v2/articlesearch.json")
    Call<NYTimesArticlesAPIResponse> getArticlesByQuery(
            @Query("q") String query,
            @Query("page") int page,
            @Query("sort") String sortBy,
            @Query("fl") String filter,
            @Query("begin_date") String beginDate,
            @Query("api-key") String apikey);
    @GET("svc/mostpopular/v2/{category}/{period}.json")
    Call<NYTimesPopularArticlesAPIResponse> getPopularArticlesByQuery(
            @Path("category") String category,
            @Path("period") String period,
            @Query("api-key") String apikey);
}
