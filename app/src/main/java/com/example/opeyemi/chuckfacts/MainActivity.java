package com.example.opeyemi.chuckfacts;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {


    OkHttpClient client = new OkHttpClient();

    private TextView joke;
    String jokeText ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        joke = (TextView)findViewById(R.id.joke);
        Button refresh = (Button)findViewById(R.id.refresh);

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Thread requestThread = new Thread(){
                    @Override
                    public void run() {

                        jokeText = getJoke();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                joke.setText(jokeText);

                            }
                        });

                    }
                };

                requestThread.start();
            }
        });



        Thread requestThread = new Thread(){
            @Override
            public void run() {

               jokeText = getJoke();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        joke.setText(jokeText);

                    }
                });

            }
        };

        requestThread.start();

    }

    String run(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }



    private String getJoke(){

        String text = "";
        try {
            String json =  run("https://api.chucknorris.io/jokes/random");
            JSONObject jsonObj = new JSONObject(json);

            text = jsonObj.getString("value");


        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return text;
    }



}
