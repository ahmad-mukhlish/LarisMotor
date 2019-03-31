package com.yayanheryanto.larismotor.view.owner;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.yayanheryanto.larismotor.R;
import com.yayanheryanto.larismotor.adapter.MotorAdapter;
import com.yayanheryanto.larismotor.model.Merk;
import com.yayanheryanto.larismotor.model.Motor;
import com.yayanheryanto.larismotor.model.Tipe;
import com.yayanheryanto.larismotor.retrofit.ApiClient;
import com.yayanheryanto.larismotor.retrofit.ApiInterface;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.yayanheryanto.larismotor.config.config.ID_USER;
import static com.yayanheryanto.larismotor.config.config.MY_PREFERENCES;

public class MotorActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private MotorAdapter adapter;
    private LinearLayoutManager layoutManager;
    private ProgressDialog dialog;
    private SearchView searchView;
    private MenuItem menuItem;

    private Spinner spinnerMerk;
    private Spinner spinnerTipe;
    private Spinner spinnerTahun;
    private Spinner spinnerStatus;

    private ArrayAdapter<String> merkAdapter;
    private ArrayAdapter<String> tipeAdapter;
    private List<Tipe> tipes;
    private int idTipe, idMerk;


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

    private void getMotor() {
        dialog.show();
        SharedPreferences pref = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        String id = pref.getString(ID_USER, "");
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<Motor>> call = apiInterface.getMotor(id);
        call.enqueue(new Callback<List<Motor>>() {
            @Override
            public void onResponse(Call<List<Motor>> call, Response<List<Motor>> response) {
                dialog.dismiss();
                List<Motor> list = response.body();

                adapter = new MotorAdapter(getApplicationContext(), list, MotorActivity.this);
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
        searchView = (SearchView) menuItem.getActionView();
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

            case R.id.filter:
                dialogueFilter();
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


    private void dialogueFilter() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MotorActivity.this);
        View rootDialog = LayoutInflater.from(MotorActivity.this).inflate(R.layout.dialogue_filter, null);


        spinnerMerk = rootDialog.findViewById(R.id.spinner_merk);
        spinnerTipe = rootDialog.findViewById(R.id.spinner_tipe);
        spinnerTahun = rootDialog.findViewById(R.id.spinner_tahun);

        final String[] status = {"Pilih Status", "Tersedia", "Sold Out"};
        spinnerStatus = rootDialog.findViewById(R.id.spinner_status);
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, status);
        spinnerStatus.setAdapter(statusAdapter);

        getMerk();
        getTahun();

        builder.setView(rootDialog);
        final AlertDialog dialog = builder.create();
        dialog.show();

        Button ok = rootDialog.findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                setFilter();
            }
        });


    }

    private void getMerk() {

        final List<Merk> merks = new ArrayList<>();


        final ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<Merk>> call = apiInterface.getMerk();
        call.enqueue(new Callback<List<Merk>>() {
            @Override
            public void onResponse(Call<List<Merk>> call, Response<List<Merk>> response) {

                for (Merk merkNow : response.body()) {

                    merks.add(merkNow);

                }
                String[] merkArray = new String[merks.size() + 1];

                int i = 1;

                merkArray[0] = "Pilih Merk";

                for (Merk merkNow : merks) {

                    merkArray[i] = merkNow.getNamaMerk();
                    i++;
                }

                merkAdapter = new ArrayAdapter<>(MotorActivity.this, android.R.layout.simple_spinner_item, merkArray);
                spinnerMerk.setAdapter(merkAdapter);


            }

            @Override
            public void onFailure(Call<List<Merk>> call, Throwable t) {

            }
        });

        spinnerMerk.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {

                tipes = new ArrayList<>();

                if (position == 0) {

                    String[] tipeArray = tipeArray = new String[1];

                    tipeArray[0] = "Silakan Pilih Merk Terlebih Dahulu";
                    tipeAdapter = new ArrayAdapter<>(MotorActivity.this, android.R.layout.simple_spinner_item, tipeArray);
                    spinnerTipe.setAdapter(tipeAdapter);


                } else {
                    Call<List<Tipe>> call2 = apiInterface.getTipe(String.valueOf(merks.get(position - 1).getIdMerk()));
                    call2.enqueue(new Callback<List<Tipe>>() {
                        @Override
                        public void onResponse(Call<List<Tipe>> call, Response<List<Tipe>> response) {

                            for (Tipe tipeNow : response.body()) {

                                tipes.add(tipeNow);

                            }

                            int i = 1;

                            String[] tipeArray;

                            if (tipes.isEmpty()) {
                                tipeArray = new String[1];
                                tipeArray[0] = "Tipe tidak tersedia";

                            } else {


                                tipeArray = new String[tipes.size() + 1];
                                tipeArray[0] = "Pilih Tipe";

                                for (Tipe tipeNow : tipes) {

                                    tipeArray[i] = tipeNow.getNamaTipe();
                                    i++;
                                }
                            }
                            tipeAdapter = new ArrayAdapter<>(MotorActivity.this, android.R.layout.simple_spinner_item, tipeArray);
                            spinnerTipe.setAdapter(tipeAdapter);
                            idMerk = merks.get(position - 1).getIdMerk();

                        }

                        @Override
                        public void onFailure(Call<List<Tipe>> call, Throwable t) {

                        }
                    });
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spinnerTipe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (tipes.size() != 0 && i != 0) {
                    idTipe = tipes.get(i - 1).getIdTipe();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    private void getTahun() {

        int tahunNow = Calendar.getInstance().get(Calendar.YEAR);
        int tahunAwal = 1985;


        String[] tahunArray = new String[tahunNow - tahunAwal + 2];

        tahunArray[0] = "Pilih Tahun";

        int j = 1;
        for (int i = tahunAwal; i <= tahunNow; i++) {

            tahunArray[j] = i + "";
            j++;

        }

        ArrayAdapter<String> tahunAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tahunArray);
        spinnerTahun.setAdapter(tahunAdapter);

    }

    private void setFilter() {


        String status, merk, tipe, tahun;

        if (spinnerStatus.getSelectedItemPosition() == 0) {
            status = "-1";
        } else if (spinnerStatus.getSelectedItemPosition() == 1) {
            status = "0";
        } else {
            status = "1";
        }

        if (spinnerMerk.getSelectedItemPosition() != 0) {
            merk = idMerk + "";
        } else {
            merk = "-1";
        }

        if (spinnerTipe.getSelectedItemPosition() != 0) {
            tipe = idTipe + "";
        } else {
            tipe = "-1";
        }

        if (spinnerTahun.getSelectedItemPosition() != 0) {
            tahun = spinnerTahun.getSelectedItem().toString();
        } else {
            tahun = "-1";
        }

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        SharedPreferences pref = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        String id = pref.getString(ID_USER, "");
        Call<List<Motor>> call = apiInterface.getFilteredMotor(id, status, merk, tipe, tahun);
        call.enqueue(new Callback<List<Motor>>() {
            @Override
            public void onResponse(Call<List<Motor>> call, Response<List<Motor>> response) {

                if (response.body() == null) {
                    Toast.makeText(MotorActivity.this, "Silakan filter data Anda", Toast.LENGTH_SHORT).show();

                } else if (response.body().isEmpty()) {

                    Toast.makeText(MotorActivity.this, "Data yang Anda filter tidak tersediad", Toast.LENGTH_SHORT).show();


                } else {
                    List<Motor> list = response.body();
                    adapter = new MotorAdapter(getApplicationContext(), list, MotorActivity.this);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                }

            }

            @Override
            public void onFailure(Call<List<Motor>> call, Throwable t) {
                Toast.makeText(MotorActivity.this, "Terjadi Kesalahan Tidak Terduga", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(MotorActivity.this, OwnerMenuActivity.class));
    }
}
