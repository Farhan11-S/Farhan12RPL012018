package com.example.farhan12rpl012018;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.PermissionRequest;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;

public class CreateBikeActivity extends AppCompatActivity {

    private Button btnAdd, btnAddImage;
    private EditText etKodesepeda, etMerksepeda, etWarnasepeda, etHargasepeda;
    public static final int RESULT_GALLERY = 0;
    private File imageFile;
    private Uri imageUri;
    private ImageView imgPreview;
    private Resources resources;
    private String URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_bike);

        btnAdd = findViewById(R.id.btn_add);
        btnAddImage = findViewById(R.id.btn_image);
        etKodesepeda = findViewById(R.id.et_kodesepeda);
        etMerksepeda = findViewById(R.id.et_merksepeda);
        etWarnasepeda = findViewById(R.id.et_warnasepeda);
        etHargasepeda = findViewById(R.id.et_hargasepeda);
        imgPreview = findViewById(R.id.imgPreview);
        resources = getResources();
        URL = resources.getString(R.string.main_url);

        btnAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(
                        CreateBikeActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) ==
                        PackageManager.PERMISSION_GRANTED) {
                    // You can use the API that requires the permission.
                    Intent galleryIntent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, RESULT_GALLERY);
                } else {
                    // You can directly ask for the permission.
                    // The registered ActivityResultCallback gets the result of this request.

                    requestPermissions(new String[] { Manifest.permission.READ_EXTERNAL_STORAGE }, 1);
                }
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String kode = etKodesepeda.getText().toString();
                String merk = etMerksepeda.getText().toString();
                String warna = etWarnasepeda.getText().toString();
                String harga = etHargasepeda.getText().toString();

                HashMap<String, String> body = new HashMap<>();
                body.put("kode", kode);
                body.put("merk", merk);
                body.put("warna", warna);
                body.put("hargasewa", harga);

                AndroidNetworking.upload(URL + "insert_master.php")
                        .addMultipartFile("image",imageFile)
                        .addMultipartParameter(body)
                        .setTag("test")
                        .setPriority(Priority.MEDIUM)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // do anything with response
                                Log.d("ABR", "respon : " + response);
                                String status = response.optString("STATUS");
                                String message = response.optString("MESSAGE");
                                if (status.equalsIgnoreCase("SUCCESS")) {
                                    Toast.makeText(CreateBikeActivity.this, message, Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(CreateBikeActivity.this, StoreActivity.class);
                                    startActivity(intent);
                                    finish();
                                    finishAffinity();
                                } else {
                                    Toast.makeText(CreateBikeActivity.this, message, Toast.LENGTH_SHORT).show();

                                }
                            }

                            @Override
                            public void onError(ANError error) {
                                // handle error
                                Log.d("anError", error.getLocalizedMessage());
                            }
                        });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RESULT_GALLERY :
                if (null != data) {
                    imageUri = data.getData();
                    String imagepath = getRealPathFromURI(imageUri,this);
                    imageFile = new File(imagepath);
                    //Do whatever that you desire here. or leave this blank
                    imgPreview.setImageURI(imageUri);
                    imgPreview.setVisibility(View.VISIBLE);
                }
                break;
            default:
                break;
        }

    }

    public String getRealPathFromURI(Uri contentURI, Activity context) {
        String[] projection = { MediaStore.Images.Media.DATA };
        @SuppressWarnings("deprecation")
        Cursor cursor = context.managedQuery(contentURI, projection, null,
                null, null);
        if (cursor == null)
            return null;
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        if (cursor.moveToFirst()) {
            String s = cursor.getString(column_index);
            // cursor.close();
            return s;
        }
        // cursor.close();
        return null;
    }
}
