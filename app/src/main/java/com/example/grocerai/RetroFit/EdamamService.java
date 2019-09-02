package com.example.grocerai.RetroFit;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface EdamamService {

    @GET("search")
    Call<RecipeSearchResult> searchRecipeByTitle(@Query("q") String title,
                                                 @Query("app_id") String id,
                                                 @Query("app_key") String key);
}
