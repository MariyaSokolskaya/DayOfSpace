package com.example.dayofspace;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface AzureTranslateAPI {
    String address = "https://api.cognitive.microsofttranslator.com";
    String api_key = "891c1008d9a84e9c90fbe6e59fd1d62b";

    @POST("/translate?api-version=3.0&to=ru")
    @Headers({
            "Ocp-Apim-Subscription-Key: " + api_key,
            "Ocp-Apim-Subscription-Region: westeurope",
            "Content-Type: application/json"
    })
    Call<ResponseTranslate[]> requestTranslate(@Body BodyTranslate [] bodyTranslates);
}
