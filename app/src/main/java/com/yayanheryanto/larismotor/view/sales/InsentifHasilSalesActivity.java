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
import com.yayanheryanto.larismotor.view.owner.InsentifHasilOwnerActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.yayanheryanto.larismotor.config.config.ID_USER;
import static com.yayanheryanto.larismotor.config.config.MY_PREFERENCES;
import static com.yayanheryanto.larismotor.config.config.NAMA_USER;

public class InsentifHasilSalesActivity extends AppCompatActivity {

    private TextView salesTxt, dariTxt, hinggaTxt, jumlahMobarTxt, jumlahMokasTxt,
            nominalMobarTxt, nominalMokasTxt, lainTxt, persentaseMobarTxt;

    private String id, dariSql, hinggaSql;
    private SharedPreferences pref;
    private int jumlahMobar, jumlahMokas, lain, persentaseMobar;
    private KonfigInsentif konfigInsentif;



    private ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insentif_hasil_sales);

        initProgressDialog();

        salesTxt = findViewById(R.id.sales);
        dariTxt = findViewById(R.id.dari);
        hinggaTxt = findViewById(R.id.hingga);
        jumlahMobarTxt = findViewById(R.id.jumlah_mobar_sales);
        jumlahMokasTxt = findViewById(R.id.jumlah_mokas_sales);
        nominalMobarTxt = findViewById(R.id.nominal_mobar_sales);
        nominalMokasTxt = findViewById(R.id.nominal_mokas_sales);
        lainTxt = findViewById(R.id.lain_sales);
        persentaseMobarTxt = findViewById(R.id.persentase_mobar_sales);

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

        int hasil = jumlahMobar * Integer.parseInt(konfigInsentif.getInsentifMobar()) ;
        nominalMobarTxt.setText(hasil+"");

    }

    private void getNominalMokas() {

        int hasil = 0 ;

        if (jumlahMokas < 6)
        {hasil = jumlahMokas * Integer.parseInt(konfigInsentif.getInsentifMokas15());}
        else if (jumlahMokas > 5 && jumlahMokas < 11)
        {hasil = jumlahMokas * Integer.parseInt(konfigInsentif.getInsentifMokas610());}
        else if (jumlahMokas > 10 && jumlahMokas < 16)
        {hasil = jumlahMokas * Integer.parseInt(konfigInsentif.getInsentifMokas1115());}
        else if (jumlahMokas > 15 && jumlahMokas < 21)
        {hasil = jumlahMokas * Integer.parseInt(konfigInsentif.getInsentifMokas1620());}
        else if (jumlahMokas > 20)
        {hasil = jumlahMokas * Integer.parseInt(konfigInsentif.getInsentifMokas21());}

        nominalMokasTxt.setText(hasil+"");



    }

    public void getLain() {


        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Integer> call = apiInterface.getLain(id);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                lain = response.body();
                lainTxt.setText(lain + "");
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
                persentaseMobarTxt.setText(persentaseMobar + "");
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        });

    }

}
