package com.example.farhan12rpl012018;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.farhan12rpl012018.adapter.StoreAdapter;
import com.example.farhan12rpl012018.model.StoreModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StoreActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<StoreModel> models = new ArrayList<>();
    private StoreAdapter storeAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SharedPreferences sharedPreferences;
    private Toolbar toolbar;
    private Resources resources;
    private String URL;
    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store);

        toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recyclerstore);
        floatingActionButton = findViewById(R.id.fab1);
        storeAdapter = new StoreAdapter(getBaseContext(),models);
        getData();
        recyclerView.setLayoutManager(new LinearLayoutManager(StoreActivity.this));
        recyclerView.setAdapter(storeAdapter);

        swipeRefreshLayout = findViewById(R.id.refreshstore);

        swipeRefreshLayout.setRefreshing(false);

        sharedPreferences = getSharedPreferences("pref", MODE_PRIVATE);
        String role = sharedPreferences.getString("role", "");

        if (role.equalsIgnoreCase("Admin")) {
            System.out.println("respon " + role);
            floatingActionButton.setVisibility(View.VISIBLE);
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                models.clear();
                getData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StoreActivity.this, CreateBikeActivity.class);
                startActivity(intent);
            }
        });
    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void getData(){
        resources = getResources();
        URL = resources.getString(R.string.main_url);
        sharedPreferences = getSharedPreferences("pref", MODE_PRIVATE);
        final String id = sharedPreferences.getString("id", "");
        AndroidNetworking.post(URL + "show_master.php")
                .addBodyParameter("id", id)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // do anything with response
                        try {
                            String status = response.getString("STATUS");
                            String message = response.getString("MESSAGE");
                            if (status.equals("SUCCESS")) {
                                JSONObject data = response.getJSONObject("PAYLOAD");
                                JSONArray customers = data.getJSONArray("DATA");
                                for (int i = 0; i<customers.length(); i++){
                                    JSONObject payload = customers.getJSONObject(i);
                                    String u_id = payload.optString("ID");
                                    String u_kode = payload.optString("KODE");
                                    String u_merk = payload.optString("MERK");
                                    String u_jenis = payload.optString("JENIS");
                                    String u_warna = payload.optString("WARNA");
                                    String u_hargasewa = payload.optString("HARGASEWA");
                                    String u_foto = payload.optString("FOTO");

                                    StoreModel cum = new StoreModel();
                                    cum.setId(u_id);
                                    cum.setKode(u_kode);
                                    cum.setMerk(u_merk);
                                    cum.setJenis(u_jenis);
                                    cum.setWarna(u_warna);
                                    cum.setHargasewa(u_hargasewa);
                                    cum.setFoto(u_foto);

                                    models.add(cum);
                                }

                                Toast.makeText(StoreActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(StoreActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("Soy", String.valueOf(response));
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Log.i("anError", error.toString());
                        Log.d("anError", error.getLocalizedMessage());
                    }
                });
    }
}
