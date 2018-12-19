package com.yayanheryanto.larismotor.view.owner;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.yayanheryanto.larismotor.R;
import com.yayanheryanto.larismotor.adapter.MotorAdapter;
import com.yayanheryanto.larismotor.model.Motor;
import com.yayanheryanto.larismotor.retrofit.ApiClient;
import com.yayanheryanto.larismotor.retrofit.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MotorActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private MotorAdapter adapter;
    private LinearLayoutManager layoutManager;
    private ProgressDialog dialog;
    private SearchView searchView;
    private MenuItem menuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_motor);

        initProgressDialog();
        recyclerView = findViewById(R.id.rvMotor);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        getMotor();
    }


    private void initProgressDialog() {
        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading");
        dialog.setMessage("Sedang Memproses..");
        dialog.setCancelable(false);
    }

    private void getMotor(){
        dialog.show();
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<Motor>> call = apiInterface.getMotor();
        call.enqueue(new Callback<List<Motor>>() {
            @Override
            public void onResponse(Call<List<Motor>> call, Response<List<Motor>> response) {
                dialog.dismiss();
                List<Motor> list = response.body();

                adapter = new MotorAdapter(getApplicationContext(),list, MotorActivity.this);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<List<Motor>> call, Throwable t) {
                dialog.dismiss();
                t.printStackTrace();
                Toast.makeText(MotorActivity.this, "Terjadi Kesalahan Tidak Terduga", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_motor, menu);
        menuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView)menuItem.getActionView();
        searchView.setIconifiedByDefault(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.tambah:
                Intent intent = new Intent(MotorActivity.this, AddMotorActivity.class);
                startActivity(intent);
                return true;

            case R.id.action_search:
                Intent intent1 = new Intent(MotorActivity.this, CariMotorActivity.class);
                startActivity(intent1);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        searchView.setIconifiedByDefault(true);
        menuItem.collapseActionView();
        searchView.onActionViewExpanded();
    }
}
