package com.bond.advice;

import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.io.IOException;

public class MainActivity extends FragmentActivity {

    private static final String BASE_URL = "http://fucking-great-advice.ru/api/random";

    private TextView adviceView;

    private OkHttpClient okHttpClient;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        okHttpClient = new OkHttpClient();
        handler = new Handler();

        adviceView = (TextView) findViewById(R.id.advice);
        TextView moreButton = (TextView) findViewById(R.id.more_button);

        moreButton.setOnClickListener(view -> getAdvice());

        getAdvice();
    }

    private String getAdvice() {

        Request request = new Request
                .Builder()
                .url(BASE_URL)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {

            @Override
            public void onResponse(Response response) throws IOException {
                String responseBody = response.body().string();

                try {
                    String advice = new JSONObject(responseBody)
                            .getString("text")
                            .replaceAll("&nbsp;"," ");

                    handler.post(() -> adviceView.setText(advice));
                } catch (Exception e) {
                    handler.post(() -> Toast.makeText(MainActivity.this,
                            "Попробуйте еще раз", Toast.LENGTH_LONG).show());
                }
            }

            @Override
            public void onFailure(Request request, IOException e) {
                handler.post(() -> Toast.makeText(MainActivity.this,
                        "Нет соединения. Попробуйте еще раз или чуть позже", Toast.LENGTH_LONG).show());
            }
        });

        return "";
    }
}