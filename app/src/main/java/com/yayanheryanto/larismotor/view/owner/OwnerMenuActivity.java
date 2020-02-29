package com.yayanheryanto.larismotor.view.owner;

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
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.yayanheryanto.larismotor.R;
import com.yayanheryanto.larismotor.adapter.MenuAdapter;
import com.yayanheryanto.larismotor.model.Menu;
import com.yayanheryanto.larismotor.retrofit.ApiClient;
import com.yayanheryanto.larismotor.retrofit.ApiInterface;
import com.yayanheryanto.larismotor.view.LoginActivity;
import com.yayanheryanto.larismotor.view.laporan.FilterActivity;
import com.yayanheryanto.larismotor.view.pending.PendingTransaksiActivity;
import com.yayanheryanto.larismotor.view.transaksi.TransaksiActivity;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.yayanheryanto.larismotor.config.config.ACCESTOKEN;
import static com.yayanheryanto.larismotor.config.config.ID_USER;
import static com.yayanheryanto.larismotor.config.config.MY_PREFERENCES;
import static com.yayanheryanto.larismotor.config.config.NAMA_USER;

public class OwnerMenuActivity extends AppCompatActivity implements View.OnClickListener {

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
        setContentView(R.layout.activity_owner_menu);


        pref = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        id = pref.getString(ID_USER, "");
        token = pref.getString(ACCESTOKEN, "");
        String namaUser = pref.getString(NAMA_USER, "");
        //Toast.makeText(this, token, Toast.LENGTH_SHORT).show();

        txName = findViewById(R.id.txName);
        image = findViewById(R.id.image);
        image.setOnClickListener(this);

        txName.setText(namaUser);

        ScrollView scrollView = findViewById(R.id.scrollView);
        scrollView.setFocusableInTouchMode(true);
        scrollView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);

        recyclerView = findViewById(R.id.recyclerview);
        list = new ArrayList<Menu>();
        list.add(new Menu(R.drawable.user, "Customer", CustomerActivity.class));
        list.add(new Menu(R.drawable.motorbike, "Motor", MotorActivity.class));
        list.add(new Menu(R.drawable.transaction, "Transaksi", TransaksiActivity.class));
        list.add(new Menu(R.drawable.time, "Pending Transaksi", PendingTransaksiActivity.class));
        list.add(new Menu(R.drawable.money, "Atur Insentif", InsentifConfigAcitivity.class));
        list.add(new Menu(R.drawable.report, "Laporan Penjualan", FilterActivity.class));
        list.add(new Menu(R.drawable.master, "Master", MasterActivity.class));
        list.add(new Menu(R.drawable.sales, "Sales", SalesActivity.class));

        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(this, 2);
        adapter = new MenuAdapter(this, list);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        closeAndroidPDialog();

    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.menu_owner, menu);

        return true;
    }

    private void closeAndroidPDialog() {
        try {
            Class aClass = Class.forName("android.content.pm.PackageParser$Package");
            Constructor declaredConstructor = aClass.getDeclaredConstructor(String.class);
            declaredConstructor.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Class cls = Class.forName("android.app.ActivityThread");
            Method declaredMethod = cls.getDeclaredMethod("currentActivityThread");
            declaredMethod.setAccessible(true);
            Object activityThread = declaredMethod.invoke(null);
            Field mHiddenApiWarningShown = cls.getDeclaredField("mHiddenApiWarningShown");
            mHiddenApiWarningShown.setAccessible(true);
            mHiddenApiWarningShown.setBoolean(activityThread, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

                if (response.body().equalsIgnoreCase("success")) {

                    editor = pref.edit();
                    editor.putString(ACCESTOKEN, "");
                    editor.putString(ID_USER, "");
                    editor.putString(NAMA_USER, "");
                    editor.commit();
                    Intent intent = new Intent(OwnerMenuActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
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
            Intent intent = new Intent(OwnerMenuActivity.this, EditProfileOwnerActivity.class);
            intent.putExtra("ID_USER", id);
            startActivity(intent);
        }
    }

}
