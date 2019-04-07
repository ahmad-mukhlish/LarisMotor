package com.yayanheryanto.larismotor.view.sales;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.yayanheryanto.larismotor.R;
import com.yayanheryanto.larismotor.model.KonfigInsentif;
import com.yayanheryanto.larismotor.model.Sales;
import com.yayanheryanto.larismotor.retrofit.ApiClient;
import com.yayanheryanto.larismotor.retrofit.ApiInterface;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.yayanheryanto.larismotor.config.config.ACCESTOKEN;
import static com.yayanheryanto.larismotor.config.config.MY_PREFERENCES;

public class InsentifConfigSalesActivity extends AppCompatActivity implements com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener {

    private ImageView dari, ke ;
    private ProgressDialog dialog;
    private EditText dariTxt, keTxt, persentaseTxt, mobarTxt, limaTxt, sepuluhTxt, limaBelasTxt,
            duaPuluhTxt, duaSatuTxt;
    public static
    String tanggalDari = "kosong", tanggalKe = "kosong", sales = "-1", kondisi = "-1", caraBayar = "-1";

    private int jenis;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insentif_config_sales);

        SimpleDateFormat df = new SimpleDateFormat("dd MMMM yyyy", new Locale("ID"));
        final SimpleDateFormat sql = new SimpleDateFormat("yyyy-MM-dd", new Locale("ID"));

        dari = findViewById(R.id.dari_picker);
        ke = findViewById(R.id.ke_picker);

        dariTxt = findViewById(R.id.dari);
        keTxt = findViewById(R.id.ke);
        persentaseTxt = findViewById(R.id.persentase);
        mobarTxt = findViewById(R.id.insentif_mobar);
        limaTxt = findViewById(R.id.insentif_mokas15);
        sepuluhTxt = findViewById(R.id.insentif_mokas610);
        limaBelasTxt = findViewById(R.id.insentif_mokas1115);
        duaPuluhTxt = findViewById(R.id.insentif_mokas1620);
        duaSatuTxt = findViewById(R.id.insentif_mokas2125);



        initProgressDialog();
        getKonfig();

        if (!tanggalDari.equals("kosong")) {

            try {
                dariTxt.setText(df.format(sql.parse(tanggalDari)));
                keTxt.setText(df.format(sql.parse(tanggalKe)));

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        Button hasil = findViewById(R.id.hasil);
        hasil.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {




                try {
                    if (tanggalDari.equals("kosong") || tanggalKe.equals("kosong") || (sql.parse(tanggalDari).after(sql.parse(tanggalKe)))) {
                        Toast.makeText(InsentifConfigSalesActivity.this, "Data tanggal belum valid...", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(InsentifConfigSalesActivity.this, InsentifHasilSalesActivity.class);
                        startActivity(intent);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        dari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                jenis = 1;

                Calendar now = Calendar.getInstance();
                com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
                        InsentifConfigSalesActivity.this,
                        now.get(Calendar.YEAR), // Initial year selection
                        now.get(Calendar.MONTH), // Initial month selection
                        now.get(Calendar.DAY_OF_MONTH) // Inital day selection
                );
                dpd.show(getFragmentManager(), "Datepickerdialog");


            }
        });


        ke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                jenis = 2;

                Calendar now = Calendar.getInstance();
                com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
                        InsentifConfigSalesActivity.this,
                        now.get(Calendar.YEAR), // Initial year selection
                        now.get(Calendar.MONTH), // Initial month selection
                        now.get(Calendar.DAY_OF_MONTH) // Inital day selection
                );
                dpd.show(getFragmentManager(), "Datepickerdialog");


            }
        });



    }


    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        SimpleDateFormat df = new SimpleDateFormat("dd MMMM yyyy", new Locale("ID"));
        SimpleDateFormat sql = new SimpleDateFormat("yyyy-MM-dd", new Locale("ID"));
        String month;

        String tanggalDariNya;
        String tanggalKeNya;
        monthOfYear++;
        if (monthOfYear < 10)
            month = "0" + monthOfYear;
        else
            month = "" + monthOfYear;

        SimpleDateFormat sqlformat = new SimpleDateFormat("yyyyMMdd", new Locale("EN"));
        String tanggal = year + month + dayOfMonth;


        try {
            if (jenis == 1) {
                tanggalDariNya = df.format(sqlformat.parse(tanggal));
                dariTxt.setText(tanggalDariNya);
                tanggalDari = sql.format(df.parse(tanggalDariNya));

            } else {
                tanggalKeNya = df.format(sqlformat.parse(tanggal));
                keTxt.setText(tanggalKeNya);
                tanggalKe = sql.format(df.parse(tanggalKeNya));

            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    private void getKonfig() {
        dialog.show();
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<KonfigInsentif>> call = apiInterface.getKonfigInsentif();
        call.enqueue(new Callback<List<KonfigInsentif>>() {
            @Override
            public void onResponse(Call<List<KonfigInsentif>> call, Response<List<KonfigInsentif>> response) {
                dialog.dismiss();
                List<KonfigInsentif> konfigInsentifs = response.body();
                if (konfigInsentifs != null) {
                    for (KonfigInsentif konfigInsentif : konfigInsentifs) {

                        persentaseTxt.setText(konfigInsentif.getPersentase());
                        mobarTxt.setText(konfigInsentif.getInsentifMobar());
                        limaTxt.setText(konfigInsentif.getInsentifMokas15());
                        sepuluhTxt.setText(konfigInsentif.getInsentifMokas610());
                        limaBelasTxt.setText(konfigInsentif.getInsentifMokas1115());
                        duaPuluhTxt.setText(konfigInsentif.getInsentifMokas1620());
                        duaSatuTxt.setText(konfigInsentif.getInsentifMokas21());

                    }

                }
            }

            @Override
            public void onFailure(Call<List<KonfigInsentif>> call, Throwable t) {
                dialog.dismiss();
                t.printStackTrace();
                Toast.makeText(InsentifConfigSalesActivity.this, "Terjadi Kesalahan Tidak Terduga", Toast.LENGTH_SHORT).show();
            }


        });
    }



    @Override
    public void onBackPressed() {
        startActivity(new Intent(InsentifConfigSalesActivity.this, SalesMenuActivity.class));
    }

    private void initProgressDialog() {
        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading");
        dialog.setMessage("Sedang Memproses..");
        dialog.setCancelable(false);
    }
}
