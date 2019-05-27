package com.yayanheryanto.larismotor.view.owner;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.yayanheryanto.larismotor.R;
import com.yayanheryanto.larismotor.adapter.MotorAdapter;
import com.yayanheryanto.larismotor.adapter.MotorSalesAdapter;
import com.yayanheryanto.larismotor.model.Motor;
import com.yayanheryanto.larismotor.retrofit.ApiClient;
import com.yayanheryanto.larismotor.retrofit.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.yayanheryanto.larismotor.config.config.ID_USER;
import static com.yayanheryanto.larismotor.config.config.MY_PREFERENCES;

public class CariMotorActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private MotorAdapter adapter;
    private MotorSalesAdapter salesAdapter;
    private LinearLayoutManager layoutManager;
    private ProgressDialog dialog;
    private SearchView searchView;
    private MenuItem menuItem;
    private boolean owner = true ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cari_motor);

        initProgressDialog();
        recyclerView = findViewById(R.id.rvMotor);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        Bundle bundle = getIntent().getExtras();
        owner = bundle.getBoolean("owner");
    }


    private void initProgressDialog() {
        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading");
        dialog.setMessage("Sedang Memproses..");
        dialog.setCancelable(false);
    }

    private void getMotor(String no){
        dialog.show();
        dialog.show();
        SharedPreferences pref = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        String id = pref.getString(ID_USER, "");
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<Motor>> call;
        if (no.contains(" ")) {
            call = apiInterface.searchNoPol(id,no);
        }else{
            call = apiInterface.searchNoMesin(id,no);
        }
        call.enqueue(new Callback<List<Motor>>() {
            @Override
            public void onResponse(Call<List<Motor>> call, Response<List<Motor>> response) {
                dialog.dismiss();
                if (response.body().isEmpty()){
                    Toast.makeText(CariMotorActivity.this, "Data Motor Tidak Ditemukan", Toast.LENGTH_SHORT).show();
                }else {
                    List<Motor> list = response.body();

                    if (owner) {
                        adapter = new MotorAdapter(getApplicationContext(), list, CariMotorActivity.this);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    } else {
                        salesAdapter = new MotorSalesAdapter(getApplicationContext(), list, CariMotorActivity.this);
                        recyclerView.setAdapter(salesAdapter);
                        salesAdapter.notifyDataSetChanged();
                    }

                }

            }

            @Override
            public void onFailure(Call<List<Motor>> call, Throwable t) {
                dialog.dismiss();
                t.printStackTrace();
                Toast.makeText(CariMotorActivity.this, "Terjadi Kesalahan Tidak Terduga", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_cari_act, menu);

        MenuItem searchMenu = menu.findItem(R.id.action_search_act);
        SearchView searchView = (SearchView)searchMenu.getActionView();
        searchView.setQueryHint("Masukan No.Polisi atau No.Mesin");
        searchView.setIconified(false);
        searchMenu.expandActionView();
        searchView.setQuery("",false);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.getTrimmedLength(newText) > 0) {
                    getMotor(newText);
                }

                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}
