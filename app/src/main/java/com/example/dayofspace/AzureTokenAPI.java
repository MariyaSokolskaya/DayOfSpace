package com.example.dayofspace;

import retrofit2.Call;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface AzureTokenAPI {
    String tokenURI = "https://westeurope.api.cognitive.microsoft.com";
    String apiKey = "8c085e691b2940e091e3a4bdf2804a60";

    @POST("/sts/v1.0/issueToken")
    @Headers({
            "Content-type: application/x-www-form-urlencoded",
            "Content-Length: 0",
            "Ocp-Apim-Subscription-Key: " + apiKey
    })
    Call<String> getToken();
}
