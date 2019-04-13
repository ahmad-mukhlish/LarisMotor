package com.yayanheryanto.larismotor.view.owner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yayanheryanto.larismotor.R;
import com.yayanheryanto.larismotor.model.KonfigInsentif;
import com.yayanheryanto.larismotor.model.Sales;
import com.yayanheryanto.larismotor.retrofit.ApiClient;
import com.yayanheryanto.larismotor.retrofit.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InsentifHasilOwnerActivity extends AppCompatActivity {

    EditText lain;
    ImageView edit, hapus;
    boolean klik;
    TextView salesTxt, dariTxt, hinggaTxt,
            jumlahMokasTxt, jumlahMobarTxt, nominalMobarTxt, nominalMokasTxt;
    View batas;
    RelativeLayout holder, holderMargin;
    int jumlahMobar, jumlahMokas;
    String dariSql, hinggaSql;
    Sales salesNow;
    KonfigInsentif konfigInsentif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insentif_hasil_owner);

        Bundle bundle = getIntent().getExtras();
        salesNow = bundle.getParcelable("sales");
        dariSql = bundle.getString("dariSql");
        hinggaSql = bundle.getString("hinggaSql");

        lain = findViewById(R.id.lain);
        edit = findViewById(R.id.edit);
        hapus = findViewById(R.id.hapus);
        salesTxt = findViewById(R.id.sales);
        dariTxt = findViewById(R.id.dari);
        hinggaTxt = findViewById(R.id.hingga);
        batas = findViewById(R.id.batas);
        holder = findViewById(R.id.holder);
        holderMargin = findViewById(R.id.holder_margin);
        jumlahMobarTxt = findViewById(R.id.jumlah_mobar);
        jumlahMokasTxt = findViewById(R.id.jumlah_mokas);
        nominalMobarTxt = findViewById(R.id.nominal_mobar);
        nominalMokasTxt = findViewById(R.id.nominal_mokas);


        salesTxt.setText(salesNow.getNama());
        dariTxt.setText(bundle.getString("dari"));
        hinggaTxt.setText(bundle.getString("hingga"));

        klik = false;

        setMargins(holderMargin, 0, 2, 2, 2);


        holderMargin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!klik) {
                    lain.setEnabled(true);
                    lain.requestFocus();
                    lain.setSelection(lain.getText().length());
                    edit.setImageResource(R.drawable.ic_save_256);
                    batas.setVisibility(View.VISIBLE);
                    holder.setVisibility(View.VISIBLE);
                    klik = true;
                    setMargins(holderMargin, 0, 2, 2, 0);

                } else {
                    setMargins(holderMargin, 0, 2, 2, 2);
                    edit.setImageResource(R.drawable.ic_pencil_edit_button);
                    batas.setVisibility(View.GONE);
                    holder.setVisibility(View.GONE);
                    lain.setEnabled(false);
                    lain.clearFocus();
                    simpan();
                    klik = false;

                }

            }
        });

        hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                lain.setText("0");

            }
        });


        getJumlahMobar();
        getJumlahMokas();
        getKonfig();



    }

    private void simpan() {
        //save ke database
    }

    public void getJumlahMobar() {


        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Log.v("cik", salesNow.getIdUser() + " " + dariSql + " " + hinggaSql);
        Call<Integer> call = apiInterface.getJumlahMobarInsentif(salesNow.getIdUser() + "", dariSql, hinggaSql);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                jumlahMobar = response.body();
                jumlahMobarTxt.setText(jumlahMobar + "");
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        });

    }

    public void getJumlahMokas() {


        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Log.v("cikan", salesNow.getIdUser() + " " + dariSql + " " + hinggaSql);
        Call<Integer> call = apiInterface.getJumlahMokasInsentif(salesNow.getIdUser() + "", dariSql, hinggaSql);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                jumlahMokas = response.body();
                jumlahMokasTxt.setText(jumlahMokas + "");


            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        });

    }

    private void setMargins(View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();

            int sizeInDP = 1;

            int marginInDp = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, sizeInDP, getResources()
                            .getDisplayMetrics());

            p.setMargins(left * marginInDp, top * marginInDp,
                    right * marginInDp, bottom * marginInDp);

            view.requestLayout();
        }
    }

    private void getKonfig() {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<KonfigInsentif>> call = apiInterface.getKonfigInsentif();
        call.enqueue(new Callback<List<KonfigInsentif>>() {
            @Override
            public void onResponse(Call<List<KonfigInsentif>> call, Response<List<KonfigInsentif>> response) {
                konfigInsentif = response.body().get(0);
                getNominalMobar();
                getNominalMokas();
            }

            @Override
            public void onFailure(Call<List<KonfigInsentif>> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(InsentifHasilOwnerActivity.this, "Terjadi Kesalahan Tidak Terduga", Toast.LENGTH_SHORT).show();
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
}
