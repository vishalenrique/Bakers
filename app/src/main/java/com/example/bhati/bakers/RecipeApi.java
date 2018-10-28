package com.example.bhati.bakers;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class RecipeApi {

    private static final String url = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";
    public static RecipeService sRecipeService;

    public static RecipeService getService(){
        if(sRecipeService == null){
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            sRecipeService = retrofit.create(RecipeService.class);
        }
        return sRecipeService;
    }

    public interface RecipeService{
        @GET("baking.json")
       Call<List<Recipe>> getRecipeList();
    }
}
