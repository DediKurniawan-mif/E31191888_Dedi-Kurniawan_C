package com.example.crud_volley;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.crud_volley.Util.AppController;
import com.example.crud_volley.Util.ServerAPI;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class InsertData extends AppCompatActivity {
    EditText username, grup, nama, password;
    Button btnbatal, btnsimpan;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_data);

        /*get data from intent*/
        Intent data = getIntent();
        final int update = data.getIntExtra("update", 0);
        String intent_username = data.getStringExtra("username");
        String intent_grup = data.getStringExtra("grup");
        String intent_nama = data.getStringExtra("nama");
        String intent_password = data.getStringExtra("password");
        /*end get data from intent*/

        username = (EditText) findViewById(R.id.inp_username);
        grup = (EditText) findViewById(R.id.inp_grup);
        nama = (EditText) findViewById(R.id.inp_nama);
        password = (EditText) findViewById(R.id.inp_password);
        btnbatal = (Button) findViewById(R.id.btn_cancel);
        btnsimpan = (Button) findViewById(R.id.btn_simpan);
        pd = new ProgressDialog(InsertData.this);

        /*kondisi update / insert */
        if (update == 1) {
            btnsimpan.setText("Update Data");
            username.setText(intent_username);
            username.setVisibility(View.GONE);
            grup.setText(intent_grup);
            nama.setText(intent_nama);
            password.setText(intent_password);
        }

        btnsimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (update == 1) {
                    Update_data();
                } else {
                    simpanData();
                }
            }
        });

        btnbatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void Update_data() {
        pd.setMessage("Update Data");
        pd.setCancelable(false);
        pd.show();
        StringRequest updateReq = getStringRequest(ServerAPI.URL_UPDATE);
    }

    private StringRequest getStringRequest(String url) {
        return new StringRequest(Request.Method.POST, url, responseListener(), errorListener()) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("username", username.getText().toString());
                map.put("grup", grup.getText().toString());
                map.put("nama", nama.getText().toString());
                map.put("password", password.getText().toString());

                return map;
            }
        };
    }

    private Response.ErrorListener errorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.cancel();
                Toast.makeText(InsertData.this, "Gagal Insert Data",
                        Toast.LENGTH_SHORT).show();
            }
        };
    }

    private void simpanData() {
        pd.setMessage("Menyimpan Data");
        pd.setCancelable(false);
        pd.show();
        StringRequest sendData = getStringRequest(ServerAPI.URL_INSERT);
    }

    private Response.Listener<String> responseListener() {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                pd.cancel();
                try {
                    JSONObject res = new JSONObject(response);
                    Toast.makeText(InsertData.this, res.getString("message"),
                            Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    Toast.makeText(InsertData.this, "Terjadi kesalahan",
                            Toast.LENGTH_SHORT).show();
                    ;
                }
                startActivity(new Intent(InsertData.this, MainActivity.class));
            }
        };
    }
}

