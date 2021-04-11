package com.example.programm.myapplication_2.predictor;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by teacher on 12.05.2016.
 */
public interface PredictService {
    @GET("/api/v1/predict.json/complete")
    Call<PredictResp> complete(
            @Query("key") String key,
            @Query("lang") String lang,
            @Query("q") String query
    );
}
