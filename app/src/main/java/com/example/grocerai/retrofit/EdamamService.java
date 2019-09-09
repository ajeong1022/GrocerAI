package com.example.grocerai.retrofit;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface EdamamService {

    @GET("search")
    Call<RecipeSearchResult> searchRecipeByTitle(@Query("q") String title,
                                                 @Query("app_id") String id,
                                                 @Query("app_key") String key);
}
