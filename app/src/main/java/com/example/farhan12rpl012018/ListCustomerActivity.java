package com.example.farhan12rpl012018;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.farhan12rpl012018.adapter.CustomersAdapter;
import com.example.farhan12rpl012018.model.CustomersModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ListCustomerActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<CustomersModel> models = new ArrayList<>();
    private CustomersAdapter customersAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_customer);

        recyclerView = findViewById(R.id.recyclercustomer);
        customersAdapter = new CustomersAdapter(getBaseContext(),models);
        getData();
        recyclerView.setLayoutManager(new LinearLayoutManager(ListCustomerActivity.this));
        recyclerView.setAdapter(customersAdapter);

        swipeRefreshLayout = findViewById(R.id.refreshlist);

        swipeRefreshLayout.setRefreshing(false);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                models.clear();
                getData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void getData(){
        sharedPreferences = getSharedPreferences("pref", MODE_PRIVATE);
        final String id = sharedPreferences.getString("id", "");
        AndroidNetworking.post("http://192.168.43.183/tugasapi/show_user.php")
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
                                    String u_nama = payload.optString("NAMA");
                                    String u_email = payload.optString("EMAIL");
                                    String u_noktp = payload.optString("NOKTP");
                                    String u_nohp = payload.optString("NOHP");
                                    String u_alamat = payload.optString("ALAMAT");
                                    String u_role = payload.optString("ROLE");

                                    CustomersModel cum = new CustomersModel();
                                    cum.setId(u_id);
                                    cum.setEmail(u_email);
                                    cum.setNoktp(u_noktp);
                                    cum.setNohp(u_nohp);
                                    cum.setRole(u_role);
                                    cum.setProfile(R.drawable.bike_img);
                                    cum.setLocation(u_alamat);
                                    cum.setName(u_nama);

                                    models.add(cum);
                                }
                                customersAdapter.notifyDataSetChanged();

                                Toast.makeText(ListCustomerActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(ListCustomerActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("Soy", String.valueOf(response));
                    }
                    @Override
                    public void onError(ANError error) {
                        // handle error
                        Log.i("anError", "["+error+"]");
                        Log.d("anError", error.getLocalizedMessage());
                    }
                });
    }

    public void editData(String id, String username, String nohp, String noktp,String alamat) {
        sharedPreferences = getSharedPreferences("pref", MODE_PRIVATE);
        final String id_auth = sharedPreferences.getString("id", "");
        HashMap<String, String> body = new HashMap<>();
        body.put("id_auth", id_auth);
        body.put("id", id);
        body.put("noktp", noktp);
        body.put("nama", username);
        body.put("nohp", nohp);
        body.put("alamat", alamat);
        AndroidNetworking.post("http://192.168.43.183/tugasapi/edit_user.php")
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
                            Toast.makeText(ListCustomerActivity.this, message, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ListCustomerActivity.this, message, Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(ListCustomerActivity.this, "Kesalahan Internal", Toast.LENGTH_SHORT).show();
                        Log.d("Soy", "onError: " + anError.getErrorBody());
                        Log.d("Soy", "onError: " + anError.getLocalizedMessage());
                        Log.d("Soy", "onError: " + anError.getErrorDetail());
                        Log.d("Soy", "onError: " + anError.getResponse());
                        Log.d("Soy", "onError: " + anError.getErrorCode());
                    }
                });
    }

    public void deleteData(String id) {
        sharedPreferences = getSharedPreferences("pref", MODE_PRIVATE);
        final String id_auth = sharedPreferences.getString("id", "");
        HashMap<String, String> body = new HashMap<>();
        body.put("id_auth", id_auth);
        body.put("id", id);
        AndroidNetworking.post("http://192.168.43.183/tugasapi/delete_user.php")
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
                            Toast.makeText(ListCustomerActivity.this, message, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ListCustomerActivity.this, message, Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Toast.makeText(ListCustomerActivity.this, "Kesalahan Internal", Toast.LENGTH_SHORT).show();
                        Log.d("Soy", "onError: " + anError.getErrorBody());
                        Log.d("Soy", "onError: " + anError.getLocalizedMessage());
                        Log.d("Soy", "onError: " + anError.getErrorDetail());
                        Log.d("Soy", "onError: " + anError.getResponse());
                        Log.d("Soy", "onError: " + anError.getErrorCode());
                    }
                });
    }
}
