package com.example.dayofspace;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface AzureVoicesAPI {
    String voiceURI = "https://westeurope.tts.speech.microsoft.com";

    @GET("/cognitiveservices/voices/list")
    Call<ArrayList<Dictor>> getDictorsList(@Header("Authorization")
                                           String token);

    @POST("/cognitiveservices/v1")
    @Headers({
            "Content-Type: application/ssml+xml",
            "User-Agent: com.example.dayofspace",
            "X-Microsoft-OutputFormat: audio-16khz-32kbitrate-mono-mp3"
    })
    Call<ResponseBody> getVoice(@Header("Authorization")
                                      String token,
                                @Body VoiceChoice voiceChoice);
}
