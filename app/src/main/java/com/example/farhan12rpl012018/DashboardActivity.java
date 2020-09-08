package com.example.farhan12rpl012018;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.HashMap;

public class DashboardActivity extends AppCompatActivity {
    LinearLayout bgll;
    private CardView btnlistuser, btnstore, btnbike, btnlogout;
    private SharedPreferences preferences;
    private TextView txtcity, txtinetsign, txttemp;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Resources resources;
    private String id, name, role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        btnlistuser = findViewById(R.id.btnlistuser);
        btnlogout = findViewById(R.id.btnlogout);
        txtcity = findViewById(R.id.txtcity);
        txtinetsign = findViewById(R.id.txtinetsign);
        txttemp = findViewById(R.id.txttemp);
        swipeRefreshLayout = findViewById(R.id.refresh);
        bgll = findViewById(R.id.bgll);
        preferences = getSharedPreferences("pref", MODE_PRIVATE);
        id = preferences.getString("id", "");
        role = preferences.getString("role", "");

        getWeatherData();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getWeatherData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        if (role.equalsIgnoreCase("Admin")) {
            System.out.println("respon " + role);
            btnlistuser.setVisibility(View.VISIBLE);
            btnlistuser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(DashboardActivity.this, ListCustomerActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();
                startActivity(intent);
                finish();
            }
        });
        swipeRefreshLayout.setRefreshing(false);
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

    private void getWeatherData(){
        resources = getResources();

        HashMap<String, String> headers = new HashMap<>();
        headers.put("x-rapidapi-host", "community-open-weather-map.p.rapidapi.com");
        headers.put("x-rapidapi-key", "a3c0b4589emsh151e5311695a642p1cab19jsn28a1ae513a7b");

        AndroidNetworking.get("https://community-open-weather-map.p.rapidapi.com/weather?units=metric&mode=json&q=kudus,id")
                .addHeaders(headers)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
            @Override
            public void onResponse(JSONObject response) {
                System.out.println(response);
                String city = response.optString("name");
                JSONObject main = response.optJSONObject("main");
                JSONArray weather1 = response.optJSONArray("weather");
                String temp = main.optString("temp");
                double tmp = Double.parseDouble(temp);
                temp = Integer.toString((int)tmp);
                JSONObject weather = weather1.optJSONObject(0);
                String icon = weather.optString("icon");
                if (icon.toLowerCase().contains("n")) {

                    bgll.setBackground(resources.getDrawable(R.drawable.dark_bg));
                    System.out.println("contains n");

                } else {

                    bgll.setBackground(resources.getDrawable(R.drawable.light_bg));
                    System.out.println("no n");

                }
                txtcity.setText(city);
                txttemp.setText(temp + "\u00B0" + "C");
                new DownloadImageTask((ImageView) findViewById(R.id.imgcloud))
                        .execute("http://openweathermap.org/img/wn/"+ icon +"@2x.png");
            }

            @Override
            public void onError(ANError anError) {
                Log.d("Soy", "onError: " + anError.getErrorBody());
                Log.d("Soy", "onError: " + anError.getLocalizedMessage());
                Log.d("Soy", "onError: " + anError.getErrorDetail());
                Log.d("Soy", "onError: " + anError.getResponse());
                Log.d("Soy  ", "onError: " + anError.getErrorCode());
                txtinetsign.setVisibility(View.VISIBLE);
            }
        });
    }
}
