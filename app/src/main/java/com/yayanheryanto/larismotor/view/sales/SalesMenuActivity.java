package com.yayanheryanto.larismotor.view.sales;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yayanheryanto.larismotor.R;
import com.yayanheryanto.larismotor.adapter.MenuAdapter;
import com.yayanheryanto.larismotor.model.Menu;
import com.yayanheryanto.larismotor.retrofit.ApiClient;
import com.yayanheryanto.larismotor.retrofit.ApiInterface;
import com.yayanheryanto.larismotor.view.LoginActivity;
import com.yayanheryanto.larismotor.view.owner.CustomerActivity;
import com.yayanheryanto.larismotor.view.pending.PendingTransaksiActivity;
import com.yayanheryanto.larismotor.view.transaksi.TransaksiActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.yayanheryanto.larismotor.config.config.ACCESTOKEN;
import static com.yayanheryanto.larismotor.config.config.ID_USER;
import static com.yayanheryanto.larismotor.config.config.MY_PREFERENCES;
import static com.yayanheryanto.larismotor.config.config.NAMA_USER;

public class SalesMenuActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private List<Menu> list;
    private MenuAdapter adapter;
    private LinearLayoutManager layoutManager;
    private String id, token;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private TextView txName;
    private ImageView image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_menu);

        pref = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        id = pref.getString(ID_USER, "");
        token = pref.getString(ACCESTOKEN, "");
        String namaUser = pref.getString(NAMA_USER, "");

        txName = findViewById(R.id.txtName);
        image = findViewById(R.id.image);
        image.setOnClickListener(this);

        txName.setText(namaUser);

        recyclerView = findViewById(R.id.recyclerview);
        list = new ArrayList<Menu>();
        list.add(new Menu(R.drawable.user, "Customer", CustomerActivity.class));
        list.add(new Menu(R.drawable.motorbike, "Motor", MotorSalesActivity.class));
        list.add(new Menu(R.drawable.transaction, "Transaksi", TransaksiActivity.class));
        list.add(new Menu(R.drawable.time, "Pending Transaksi", PendingTransaksiActivity.class));
        list.add(new Menu(R.drawable.money, "Lihat Insentif Saya", InsentifConfigSalesActivity.class));

        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(this, 2);
        adapter = new MenuAdapter(this, list);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sales, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                logout();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logout() {
        //TODO insert API for delete token
        pref = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<String> call = apiInterface.logout(pref.getString(ID_USER, ""));
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if (response.body() != null) {

                    Log.v("coba", response.body());
                    if (response.body().equalsIgnoreCase("success")) {

                        editor = pref.edit();
                        editor.putString(ACCESTOKEN, "");
                        editor.putString(ID_USER, "");
                        editor.putString(NAMA_USER, "");
                        editor.commit();
                        Intent intent = new Intent(SalesMenuActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }


                } else {

                    try {
                        Log.v("coba", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view == image) {
            Intent intent = new Intent(SalesMenuActivity.this, EditProfileSalesActivity.class);
            intent.putExtra("ID_USER", id);
            startActivity(intent);

        }
    }
}
