package com.yayanheryanto.larismotor.view.laporan;

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
import com.yayanheryanto.larismotor.model.Sales;
import com.yayanheryanto.larismotor.retrofit.ApiClient;
import com.yayanheryanto.larismotor.retrofit.ApiInterface;
import com.yayanheryanto.larismotor.view.owner.InsentifHasilOwnerActivity;
import com.yayanheryanto.larismotor.view.owner.OwnerMenuActivity;

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

public class FilterActivity extends AppCompatActivity implements com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener {

    private ImageView dari, ke;
    private EditText dariTxt, keTxt;
    public static
    String tanggalDari = "kosong", tanggalKe = "kosong", sales = "-1", kondisi = "-1", caraBayar = "-1";

    private int jenis;
    ArrayAdapter<String> salesAdapter;

    private Spinner spinnerSales;
    private Spinner spinnerKondisi;
    private Spinner spinnerCaraBayar;


    private final List<Sales> saleses = new ArrayList<>();


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        SimpleDateFormat df = new SimpleDateFormat("dd MMMM yyyy", new Locale("ID"));
        final SimpleDateFormat sql = new SimpleDateFormat("yyyy-MM-dd", new Locale("ID"));

        dari = findViewById(R.id.dari_picker);
        ke = findViewById(R.id.ke_picker);

        dariTxt = findViewById(R.id.dari);
        keTxt = findViewById(R.id.ke);

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


                if (spinnerSales.getSelectedItemPosition() != 0) {
                    sales = saleses.get(spinnerSales.getSelectedItemPosition() - 1).getNoKtpSales();
                } else {
                    sales = "-1";
                }


                if (spinnerKondisi.getSelectedItemPosition() == 0) {
                    kondisi = "-1";
                } else if (spinnerKondisi.getSelectedItemPosition() == 1) {
                    kondisi = "1";
                } else {
                    kondisi = "0";
                }

                if (spinnerCaraBayar.getSelectedItemPosition() == 0) {
                    caraBayar = "-1";
                } else if (spinnerCaraBayar.getSelectedItemPosition() == 1) {
                    caraBayar = "0";
                } else {
                    caraBayar = "1";
                }


                try {
                    if (tanggalDari.equals("kosong") || tanggalKe.equals("kosong") || (sql.parse(tanggalDari).after(sql.parse(tanggalKe)))) {
                        Toast.makeText(FilterActivity.this, "Data tanggal belum valid...", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(FilterActivity.this, LaporanActivity.class);
                        intent.putExtra("insentif", false);
                        intent.putExtra("posSales", spinnerSales.getSelectedItemPosition());
                        intent.putExtra("posKondisi", spinnerKondisi.getSelectedItemPosition());
                        intent.putExtra("posCaraBayar", spinnerCaraBayar.getSelectedItemPosition());
                        startActivity(intent);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        Button insentif = findViewById(R.id.insentif);
        insentif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (spinnerSales.getSelectedItemPosition() != 0) {
                    sales = saleses.get(spinnerSales.getSelectedItemPosition() - 1).getNoKtpSales();
                } else {
                    sales = "-1";
                }


                if (spinnerKondisi.getSelectedItemPosition() == 0) {
                    kondisi = "-1";
                } else if (spinnerKondisi.getSelectedItemPosition() == 1) {
                    kondisi = "1";
                } else {
                    kondisi = "0";
                }

                if (spinnerCaraBayar.getSelectedItemPosition() == 0) {
                    caraBayar = "-1";
                } else if (spinnerCaraBayar.getSelectedItemPosition() == 1) {
                    caraBayar = "0";
                } else {
                    caraBayar = "1";
                }


                try {
                    if (tanggalDari.equals("kosong") || tanggalKe.equals("kosong") || (sql.parse(tanggalDari).after(sql.parse(tanggalKe)))) {
                        Toast.makeText(FilterActivity.this, "Data tanggal belum valid...", Toast.LENGTH_SHORT).show();
                    } else if (sales.equalsIgnoreCase("-1")) {
                        Toast.makeText(FilterActivity.this, "Data sales belum valid...", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(FilterActivity.this, InsentifHasilOwnerActivity.class);
                        intent.putExtra("sales", saleses.get(spinnerSales.getSelectedItemPosition() - 1));
                        intent.putExtra("dariSqL", FilterActivity.tanggalDari);
                        intent.putExtra("hinggaSql", FilterActivity.tanggalKe);
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
                        FilterActivity.this,
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
                        FilterActivity.this,
                        now.get(Calendar.YEAR), // Initial year selection
                        now.get(Calendar.MONTH), // Initial month selection
                        now.get(Calendar.DAY_OF_MONTH) // Inital day selection
                );
                dpd.show(getFragmentManager(), "Datepickerdialog");


            }
        });

        spinnerSales = findViewById(R.id.spinner_sales);
        getSales();

        final String[] kondisi = {"Pilih Kondisi", "Mobar", "Mokas"};
        spinnerKondisi = findViewById(R.id.spinner_kondisi);
        ArrayAdapter<String> kondisiAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, kondisi);
        spinnerKondisi.setAdapter(kondisiAdapter);

        final String[] caraBayar = {"Pilih Cara Bayar", "Cash", "Kredit"};
        spinnerCaraBayar = findViewById(R.id.spinner_metode_bayar);
        ArrayAdapter<String> caraBayarAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, caraBayar);
        spinnerCaraBayar.setAdapter(caraBayarAdapter);


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

    private void getSales() {


        SharedPreferences pref = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = pref.edit();
        String token = pref.getString(ACCESTOKEN, "");

        final ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<Sales>> call = apiInterface.getFilterSales(token);
        call.enqueue(new Callback<List<Sales>>() {
            @Override
            public void onResponse(Call<List<Sales>> call, Response<List<Sales>> response) {

                for (Sales salesNow : response.body()) {

                    saleses.add(salesNow);

                }
                String[] merkArray = new String[saleses.size() + 1];

                int i = 1;

                merkArray[0] = "Pilih Sales";

                for (Sales salesNow : saleses) {

                    merkArray[i] = salesNow.getNama();
                    i++;
                }


                salesAdapter = new ArrayAdapter<>(FilterActivity.this, android.R.layout.simple_spinner_item, merkArray);
                spinnerSales.setAdapter(salesAdapter);

                Bundle bundle = getIntent().getExtras();

                if (bundle.getBoolean("back")) {
                    spinnerSales.setSelection(bundle.getInt("posSales"));
                    spinnerKondisi.setSelection(bundle.getInt("posKondisi"));
                    spinnerCaraBayar.setSelection(bundle.getInt("posCaraBayar"));
                }


            }

            @Override
            public void onFailure(Call<List<Sales>> call, Throwable t) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(FilterActivity.this, OwnerMenuActivity.class));
    }
}
