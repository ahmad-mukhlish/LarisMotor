package com.yayanheryanto.larismotor.view.sales;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.yayanheryanto.larismotor.R;
import com.yayanheryanto.larismotor.retrofit.ApiClient;
import com.yayanheryanto.larismotor.retrofit.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.yayanheryanto.larismotor.config.config.ID_USER;
import static com.yayanheryanto.larismotor.config.config.MY_PREFERENCES;
import static com.yayanheryanto.larismotor.config.config.NAMA_USER;

public class InsentifHasilSalesActivity extends AppCompatActivity {

    TextView salesTxt, dariTxt, hinggaTxt, jumlahMobarTxt, jumlahMokasTxt;
    String id, dariSql, hinggaSql;
    private SharedPreferences pref;
    int jumlahMobar, jumlahMokas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insentif_hasil_sales);

        salesTxt = findViewById(R.id.sales);
        dariTxt = findViewById(R.id.dari);
        hinggaTxt = findViewById(R.id.hingga);
        jumlahMobarTxt = findViewById(R.id.jumlah_mobar_sales);
        jumlahMokasTxt = findViewById(R.id.jumlah_mokas_sales);

        Bundle bundle = getIntent().getExtras();

        pref = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        id = pref.getString(ID_USER, "");

        salesTxt.setText(pref.getString(NAMA_USER, ""));
        dariTxt.setText(bundle.getString("dari"));
        hinggaTxt.setText(bundle.getString("hingga"));
        dariSql = bundle.getString("dariSql");
        hinggaSql = bundle.getString("hinggaSql");

        getJumlahMobar();
        getJumlahMokas();

    }

    public void getJumlahMobar() {



        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Integer> call = apiInterface.getJumlahMobarInsentif(id,dariSql,hinggaSql );
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                jumlahMobar = response.body();
                jumlahMobarTxt.setText(jumlahMobar+"");
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        });

    }

    public void getJumlahMokas() {


        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Integer> call = apiInterface.getJumlahMokasInsentif(id,dariSql,hinggaSql );
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                jumlahMokas = response.body();
                jumlahMokasTxt.setText(jumlahMokas+"");


            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        });

    }
}
