package com.example.farhan12rpl012018;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {
    private boolean isFormFilled = false;

    private SharedPreferences preferences;
    private TextView txtRegis;
    private Button btnLogin;
    private EditText username, pass;
    private Resources resources;
    private String URL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtRegis = findViewById(R.id.txtregis);
        btnLogin = findViewById(R.id.btnlogin);
        username = findViewById(R.id.username);
        pass = findViewById(R.id.pass);
        resources = getResources();
        URL = resources.getString(R.string.main_url);

        txtRegis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFormFilled = true;

                final String s_username = username.getText().toString();
                final String s_pass = pass.getText().toString();

                if (s_username.isEmpty() || s_pass.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "MAKE SURE FORM IS FILLED", Toast.LENGTH_SHORT).show();
                    isFormFilled = false;
                }

                if (isFormFilled) {
                    HashMap<String, String> body = new HashMap<>();
                    body.put("email", s_username);
                    body.put("password", s_pass);
                    AndroidNetworking.post(URL + "login.php")
                            .addBodyParameter(body)
                            .setPriority(Priority.MEDIUM)
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.d("GZS", "respon : " + response);

                                    String status = response.optString("STATUS");
                                    String message = response.optString("MESSAGE");
                                    if (status.equalsIgnoreCase("SUCCESS")) {
                                        JSONObject payload = response.optJSONObject("PAYLOAD");
                                        String U_ID = payload.optString("LOGIN_ID");
                                        String U_NAME = payload.optString("LOGIN_NAME");
                                        String EMAIL = payload.optString("EMAIL");
                                        String NO_HP = payload.optString("NOMOR_HP");
                                        String NO_KTP = payload.optString("NOMOR_KTP");
                                        String ALAMAT = payload.optString("ALAMAT");
                                        String ROLE = payload.optString("ROLE");

                                        preferences = getSharedPreferences("pref", Context.MODE_PRIVATE);
                                        preferences.edit()
                                                .putString("id", U_ID)
                                                .putString("name", U_NAME)
                                                .putString("no_hp", NO_HP)
                                                .putString("email", EMAIL)
                                                .putString("no_ktp", NO_KTP)
                                                .putString("alamat", ALAMAT)
                                                .putString("role", ROLE)
                                                .apply();

                                        Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                                        startActivity(intent);
                                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                    else {
                                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onError(ANError anError) {
                                    Log.d("Soy", "onError: " + anError.getErrorBody());
                                    Log.d("Soy", "onError: " + anError.getLocalizedMessage());
                                    Log.d("Soy", "onError: " + anError.getErrorDetail());
                                    Log.d("Soy", "onError: " + anError.getResponse());
                                    Log.d("Soy  ", "onError: " + anError.getErrorCode());
                                }
                            });
                }

            }
        });
    }
}
