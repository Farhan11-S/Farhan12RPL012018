package com.example.farhan12rpl012018;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
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

public class RegisterActivity extends AppCompatActivity {
    TextView readyaccount;
    private Button btnregister;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String ID = "id";
    public static final String USERNAME = "username";
    private SharedPreferences preferences;
    private String URL = "http://192.168.6.71/tugasapi/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btnregister = findViewById(R.id.btnregis);
        final EditText txtemail = findViewById(R.id.txtemail);
        final EditText txtusername1 = findViewById(R.id.txtusername);
        final EditText txtnohp = findViewById(R.id.txtnomerhp);
        final EditText txtnoktp = findViewById(R.id.txtnoktp);
        final EditText txtalamat = findViewById(R.id.txtalamat);
        final EditText txtpass1 = findViewById(R.id.txtpass);

        readyaccount = (TextView) findViewById(R.id.txtlogin);
        readyaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });
        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = txtemail.getText().toString();
                String username1 = txtusername1.getText().toString();
                String nohp = txtnohp.getText().toString();
                String noktp = txtnoktp.getText().toString();
                String alamat = txtalamat.getText().toString();
                String pass = txtpass1.getText().toString();

                HashMap<String, String> body = new HashMap<>();
                body.put("noktp", noktp);
                body.put("email", email);
                body.put("password", pass);
                body.put("nama", username1);
                body.put("nohp", nohp);
                body.put("alamat", alamat);
                body.put("roleuser", "1");
                AndroidNetworking.post(URL + "register.php")
                        .addBodyParameter(body)
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d("ABR", "respon : " + response);
                                String status = response.optString("STATUS");
                                String message = response.optString("MESSAGE");
                                if (status.equalsIgnoreCase("SUCCESS")) {
                                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();

                                }
                            }

                            @Override
                            public void onError(ANError anError) {
                                Toast.makeText(RegisterActivity.this, "Kesalahan Internal", Toast.LENGTH_SHORT).show();
                                Log.d("Soy", "onError: " + anError.getErrorBody());
                                Log.d("Soy", "onError: " + anError.getLocalizedMessage());
                                Log.d("Soy", "onError: " + anError.getErrorDetail());
                                Log.d("Soy", "onError: " + anError.getResponse());
                                Log.d("Soy", "onError: " + anError.getErrorCode());
                            }
                        });
            }
        });
    }
}
