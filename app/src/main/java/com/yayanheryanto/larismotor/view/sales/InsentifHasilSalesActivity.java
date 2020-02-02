package com.yayanheryanto.larismotor.view.sales;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.yayanheryanto.larismotor.R;
import com.yayanheryanto.larismotor.model.KonfigInsentif;
import com.yayanheryanto.larismotor.retrofit.ApiClient;
import com.yayanheryanto.larismotor.retrofit.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.yayanheryanto.larismotor.config.config.ID_USER;
import static com.yayanheryanto.larismotor.config.config.MY_PREFERENCES;
import static com.yayanheryanto.larismotor.config.config.NAMA_USER;
import static com.yayanheryanto.larismotor.helper.HelperClass.formatter;

public class InsentifHasilSalesActivity extends AppCompatActivity {

    private TextView salesTxt, dariTxt, hinggaTxt, jumlahMobarTxt, jumlahMokasTxt,
            nominalMobarTxt, nominalMokasTxt, lainTxt, persentaseMobarTxt, persentaseMokasTxt, totalTxt;

    private String id, dariSql, hinggaSql;
    private SharedPreferences pref;
    private int jumlahMobar, jumlahMokas, lain, persentaseMobar, persentaseMokas, nominalMobar, nominalMokas;
    private KonfigInsentif konfigInsentif;



    private ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insentif_hasil_sales);

        initProgressDialog();

        salesTxt = findViewById(R.id.nama_sales);
        dariTxt = findViewById(R.id.dari);
        hinggaTxt = findViewById(R.id.hingga);
        jumlahMobarTxt = findViewById(R.id.jumlah_mobar_sales);
        jumlahMokasTxt = findViewById(R.id.jumlah_mokas_sales);
        nominalMobarTxt = findViewById(R.id.nominal_mobar_sales);
        nominalMokasTxt = findViewById(R.id.nominal_mokas_sales);
        lainTxt = findViewById(R.id.lain_sales);
        persentaseMobarTxt = findViewById(R.id.persentase_mobar_sales);
        persentaseMokasTxt = findViewById(R.id.persentase_mokas_sales);
        totalTxt = findViewById(R.id.total_sales);

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
        getKonfig();

    }

    private void initProgressDialog() {
        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading");
        dialog.setMessage("Sedang Memproses..");
        dialog.setCancelable(false);
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

    private void getKonfig() {
        dialog.show();
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<KonfigInsentif>> call = apiInterface.getKonfigInsentif();
        call.enqueue(new Callback<List<KonfigInsentif>>() {
            @Override
            public void onResponse(Call<List<KonfigInsentif>> call, Response<List<KonfigInsentif>> response) {
                konfigInsentif = response.body().get(0);
                getNominalMobar();
                getNominalMokas();
                getLain();
                getPersentaseMobar();
                getPersentaseMokas();
                getTotal();
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<KonfigInsentif>> call, Throwable t) {
                dialog.dismiss();
                t.printStackTrace();
                Toast.makeText(InsentifHasilSalesActivity.this, "Terjadi Kesalahan Tidak Terduga", Toast.LENGTH_SHORT).show();
            }


        });
    }

    private void getNominalMobar() {

        nominalMobar = jumlahMobar * Integer.parseInt(konfigInsentif.getInsentifMobar()) ;
        nominalMobarTxt.setText(formatter(nominalMobar+""));
        getTotal();

    }

    private void getNominalMokas() {


        if (jumlahMokas < 6)
        {nominalMokas = jumlahMokas * Integer.parseInt(konfigInsentif.getInsentifMokas15());}
        else if (jumlahMokas > 5 && jumlahMokas < 11)
        {nominalMokas = jumlahMokas * Integer.parseInt(konfigInsentif.getInsentifMokas610());}
        else if (jumlahMokas > 10 && jumlahMokas < 16)
        {nominalMokas = jumlahMokas * Integer.parseInt(konfigInsentif.getInsentifMokas1115());}
        else if (jumlahMokas > 15 && jumlahMokas < 21)
        {nominalMokas = jumlahMokas * Integer.parseInt(konfigInsentif.getInsentifMokas1620());}
        else if (jumlahMokas > 20)
        {nominalMokas = jumlahMokas * Integer.parseInt(konfigInsentif.getInsentifMokas21());}

        nominalMokasTxt.setText(formatter(nominalMokas+""));
        getTotal();



    }

    public void getLain() {


        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Integer> call = apiInterface.getLain(id);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                lain = response.body();
                lainTxt.setText(formatter(lain + ""));
                getTotal();
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        });

    }

    public void getPersentaseMobar() {


        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Integer> call = apiInterface.getPersentaseMobar(id);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                persentaseMobar = response.body();
                persentaseMobarTxt.setText(formatter(persentaseMobar + ""));
                getTotal();
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        });

    }

    public void getPersentaseMokas() {


        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Integer> call = apiInterface.getPersentaseMokas(id, dariSql, hinggaSql);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                persentaseMokas = response.body();
                persentaseMokas = persentaseMokas * Integer.parseInt(konfigInsentif.getPersentase()) / 100;
                persentaseMokasTxt.setText(formatter(persentaseMokas + ""));
                getTotal();
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        });

    }

    public void getTotal() {


        int total = 0;
        total = nominalMobar + nominalMokas + persentaseMokas + persentaseMobar + lain ;

        Log.v("cek1",nominalMobar+"");
        Log.v("cek2",nominalMokas+"");
        Log.v("cek3",persentaseMokas+"");
        Log.v("cek4",persentaseMobar+"");
        Log.v("cek5",lain+"");


        totalTxt.setText(formatter(total+""));

    }

}
