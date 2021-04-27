package com.example.dayofspace;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MainActivity extends AppCompatActivity {

    ImageView picture;
    TextView infoText;
    Button speechButton;
    SpaceResponse spaceResponse;
    String token = "";

    private final String ADDRESS = "https://api.nasa.gov/planetary/apod?api_key=DEMO_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        picture         =   findViewById(R.id.picture);
        infoText        =   findViewById(R.id.scienceInfo);
        speechButton    =   findViewById(R.id.speechButton);

        SpaceTask spaceTask = new SpaceTask();
        spaceTask.execute();

    }

    public void getVoices(View view) {
        String text = infoText.getText().toString();
        Retrofit tokenRetrofit = new Retrofit.Builder()
                .baseUrl(AzureTokenAPI.tokenURI)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        AzureTokenAPI azureTokenAPI = tokenRetrofit.create(AzureTokenAPI.class);
        Call<String> callToken = azureTokenAPI.getToken();
        callToken.enqueue(new TokenCallback());


    }

    class SpaceTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            OkHttpClient spaceClient = new OkHttpClient();
            Request pictureInfo;
            Response pictureResponse;
            Gson gson = new Gson();

            HttpUrl spaceHttp = HttpUrl.parse(ADDRESS).newBuilder().build();
            pictureInfo = new Request.Builder().url(spaceHttp).build();
            try {
                pictureResponse = spaceClient.newCall(pictureInfo).execute();
                spaceResponse = new SpaceResponse();
                spaceResponse = gson.fromJson(pictureResponse.body().string(), SpaceResponse.class);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(spaceResponse.media_type.equals("image")) {
                Picasso.get()
                        .load(spaceResponse.url)
                        .placeholder(R.drawable.space)
                        .into(picture);
            }else{
                //запуск youtube через интент или создание своего медиаплейера
            }
            //создадим объект retrofit
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(AzureTranslateAPI.address)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            //создадим объект интерфейса
            AzureTranslateAPI api = retrofit.create(AzureTranslateAPI.class);
            //объявим массив
            BodyTranslate [] bodyTranslates = new BodyTranslate[1];
            bodyTranslates[0] = new BodyTranslate();
            bodyTranslates[0].Text = spaceResponse.explanation;
            Call<ResponseTranslate[]> call = api.requestTranslate(bodyTranslates);
            call.enqueue(new TranslateCallback());
            //infoText.setText(spaceResponse.explanation);

        }
    }


    private class TranslateCallback implements retrofit2.Callback<ResponseTranslate[]> {
        @Override
        public void onResponse(Call<ResponseTranslate[]> call,
                               retrofit2.Response<ResponseTranslate[]> response) {
            if(response.isSuccessful()){
                String s = "";
                s = response.body()[0].translations.get(0).text;
                infoText.setText(s);
            }else {
                Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_SHORT)
                        .show();
            }
        }

        @Override
        public void onFailure(Call<ResponseTranslate[]> call, Throwable t) {
            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private class TokenCallback implements retrofit2.Callback<String> {
        @Override
        public void onResponse(Call<String> call, retrofit2.Response<String> response) {
            if(response.isSuccessful()){
                token = response.body();
                //Toast.makeText(getApplicationContext(), token, Toast.LENGTH_SHORT)
               //         .show();
                //Следующий объект retrofit
                Retrofit voicesRetrofit = new Retrofit.Builder()
                        .baseUrl(AzureVoicesAPI.voiceURI)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                AzureVoicesAPI azureVoicesAPI = voicesRetrofit.create(AzureVoicesAPI.class);

                Call<ArrayList<Dictor>> callDictors = azureVoicesAPI.getDictorsList(token);
                callDictors.enqueue(new DictorsCallback());
            }else {
                Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_SHORT)
                        .show();
            }
        }

        @Override
        public void onFailure(Call<String> call, Throwable t) {
            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private class DictorsCallback implements retrofit2.Callback<ArrayList<Dictor>> {
        @Override
        public void onResponse(Call<ArrayList<Dictor>> call,
                               retrofit2.Response<ArrayList<Dictor>> response) {
            ArrayList<Dictor> dictors = new ArrayList<>();
            if(response.isSuccessful()) {
                dictors = response.body();
                Toast.makeText(getApplicationContext(), dictors.get(0).ShortName,
                        Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getApplicationContext(),
                        Integer.toString(response.code()),
                        Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Call<ArrayList<Dictor>> call, Throwable t) {

        }
    }
}
