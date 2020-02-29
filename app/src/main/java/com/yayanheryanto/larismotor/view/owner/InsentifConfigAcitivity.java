package com.yayanheryanto.larismotor.view.owner;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.redmadrobot.inputmask.MaskedTextChangedListener;
import com.redmadrobot.inputmask.ReversedMaskTextChangedListener;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.yayanheryanto.larismotor.R;
import com.yayanheryanto.larismotor.model.KonfigInsentif;
import com.yayanheryanto.larismotor.model.Sales;
import com.yayanheryanto.larismotor.retrofit.ApiClient;
import com.yayanheryanto.larismotor.retrofit.ApiInterface;
import com.yayanheryanto.larismotor.view.laporan.LaporanActivity;

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
import static com.yayanheryanto.larismotor.helper.HelperClass.clearDot;
import static com.yayanheryanto.larismotor.helper.HelperClass.createDot;

public class InsentifConfigAcitivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, View.OnFocusChangeListener {

    private ProgressDialog dialog;
    private ImageView dari, ke, persentase, mobar, lima, sepuluh, limaBelas, duaPuluh, duaSatu;
    private EditText dariTxt, keTxt, persentaseTxt, mobarTxt, limaTxt, sepuluhTxt, limaBelasTxt,
            duaPuluhTxt, duaSatuTxt;
    public static
    String tanggalDari = "kosong", tanggalKe = "kosong", sales = "-1", kondisi = "-1", caraBayar = "-1";

    private int jenis;
    private ArrayAdapter<String> salesAdapter;

    private Spinner spinnerSales;
    private final List<Sales> saleses = new ArrayList<>();

    private String insentifMobar;
    private String limaStr;
    private String sepuluhStr;
    private String limaBelasStr;
    private String duaPuluhStr;
    private String duaSatuStr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insentif_config);

        SimpleDateFormat df = new SimpleDateFormat("dd MMMM yyyy", new Locale("ID"));
        final SimpleDateFormat sql = new SimpleDateFormat("yyyy-MM-dd", new Locale("ID"));

        dari = findViewById(R.id.dari_picker);
        ke = findViewById(R.id.ke_picker);
        persentase = findViewById(R.id.img_persen);
        mobar = findViewById(R.id.img_insentif);
        lima = findViewById(R.id.img_mokas15);
        sepuluh = findViewById(R.id.img_mokas610);
        limaBelas = findViewById(R.id.img_mokas1115);
        duaPuluh = findViewById(R.id.img_mokas1620);
        duaSatu = findViewById(R.id.img_mokas2125);


        dariTxt = findViewById(R.id.dari);
        keTxt = findViewById(R.id.ke);
        persentaseTxt = findViewById(R.id.persentase);
        mobarTxt = findViewById(R.id.insentif_mobar);
        limaTxt = findViewById(R.id.insentif_mokas15);
        sepuluhTxt = findViewById(R.id.insentif_mokas610);
        limaBelasTxt = findViewById(R.id.insentif_mokas1115);
        duaPuluhTxt = findViewById(R.id.insentif_mokas1620);
        duaSatuTxt = findViewById(R.id.insentif_mokas2125);

        setInputMask();

        persentase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!persentaseTxt.isFocused()) {
                    persentaseTxt.setEnabled(true);
                    persentaseTxt.requestFocus();
                    persentase.setImageResource(R.drawable.ic_save);
                } else {
                    persentaseTxt.setEnabled(false);
                    persentaseTxt.clearFocus();
                    updateKonfigInsentif();
                    persentase.setImageResource(R.drawable.ic_write_256);
                }

            }
        });

        mobar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!mobarTxt.isFocused()) {
                    mobarTxt.setEnabled(true);
                    mobarTxt.requestFocus();
                    mobar.setImageResource(R.drawable.ic_save);
                } else {
                    mobarTxt.setEnabled(false);
                    mobarTxt.clearFocus();
                    updateKonfigInsentif();
                    mobar.setImageResource(R.drawable.ic_write_256);
                }

            }
        });

        lima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!limaTxt.isFocused()) {
                    limaTxt.setEnabled(true);
                    limaTxt.requestFocus();
                    lima.setImageResource(R.drawable.ic_save);

                } else {
                    limaTxt.setEnabled(false);
                    limaTxt.clearFocus();
                    updateKonfigInsentif();
                    lima.setImageResource(R.drawable.ic_write_256);

                }

            }
        });

        sepuluh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!sepuluhTxt.isFocused()) {
                    sepuluhTxt.setEnabled(true);
                    sepuluhTxt.requestFocus();
                    sepuluh.setImageResource(R.drawable.ic_save);

                } else {
                    sepuluhTxt.setEnabled(false);
                    sepuluhTxt.clearFocus();
                    updateKonfigInsentif();
                    sepuluh.setImageResource(R.drawable.ic_write_256);
                }

            }
        });

        limaBelas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!limaBelasTxt.isFocused()) {
                    limaBelasTxt.setEnabled(true);
                    limaBelasTxt.requestFocus();
                    limaBelas.setImageResource(R.drawable.ic_save);

                } else {
                    limaBelasTxt.setEnabled(false);
                    limaBelasTxt.clearFocus();
                    updateKonfigInsentif();
                    limaBelas.setImageResource(R.drawable.ic_write_256);
                }

            }
        });

        duaPuluh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!duaPuluhTxt.isFocused()) {
                    duaPuluhTxt.setEnabled(true);
                    duaPuluhTxt.requestFocus();
                    duaPuluh.setImageResource(R.drawable.ic_save);

                } else {
                    duaPuluhTxt.setEnabled(false);
                    duaPuluhTxt.clearFocus();
                    updateKonfigInsentif();
                    duaPuluh.setImageResource(R.drawable.ic_write_256);
                }

            }
        });

        duaSatu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!duaSatuTxt.isFocused()) {
                    duaSatuTxt.setEnabled(true);
                    duaSatuTxt.requestFocus();
                    duaSatu.setImageResource(R.drawable.ic_save);

                } else {
                    duaSatuTxt.setEnabled(false);
                    duaSatuTxt.clearFocus();
                    updateKonfigInsentif();
                    duaSatu.setImageResource(R.drawable.ic_write_256);

                }

            }
        });

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


                if (spinnerSales.getSelectedItemPosition() != 0) {
                    sales = saleses.get(spinnerSales.getSelectedItemPosition() - 1).getNoKtpSales();
                } else {
                    sales = "-1";
                }


                try {
                    if (tanggalDari.equals("kosong") || tanggalKe.equals("kosong") || (sql.parse(tanggalDari).after(sql.parse(tanggalKe))) || sales.equals("-1")) {
                        Toast.makeText(InsentifConfigAcitivity.this, "Data sales atau tanggal ada yang belum valid...", Toast.LENGTH_SHORT).show();
                    } else {


                        Intent intent = new Intent(InsentifConfigAcitivity.this, InsentifHasilOwnerActivity.class);
                        intent.putExtra("sales", saleses.get(spinnerSales.getSelectedItemPosition() - 1));
                        intent.putExtra("dari", dariTxt.getText().toString());
                        intent.putExtra("hingga", keTxt.getText().toString());
                        intent.putExtra("dariSql", tanggalDari);
                        intent.putExtra("hinggaSql", tanggalKe);
                        startActivity(intent);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        Button laporan = findViewById(R.id.laporan);
        laporan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (spinnerSales.getSelectedItemPosition() != 0) {
                    sales = saleses.get(spinnerSales.getSelectedItemPosition() - 1).getNoKtpSales();
                } else {
                    sales = "-1";
                }


                try {
                    if (tanggalDari.equals("kosong") || tanggalKe.equals("kosong") || (sql.parse(tanggalDari).after(sql.parse(tanggalKe))) || sales.equals("-1")) {
                        Toast.makeText(InsentifConfigAcitivity.this, "Data sales atau tanggal ada yang belum valid...", Toast.LENGTH_SHORT).show();
                    } else {


                        Intent intent = new Intent(InsentifConfigAcitivity.this, LaporanActivity.class);
                        intent.putExtra("insentif",true);
                        intent.putExtra("sales", saleses.get(spinnerSales.getSelectedItemPosition() - 1));
                        intent.putExtra("dari", dariTxt.getText().toString());
                        intent.putExtra("hingga", keTxt.getText().toString());
                        intent.putExtra("dariSql", tanggalDari);
                        intent.putExtra("hinggaSql", tanggalKe);
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
                        InsentifConfigAcitivity.this,
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
                        InsentifConfigAcitivity.this,
                        now.get(Calendar.YEAR), // Initial year selection
                        now.get(Calendar.MONTH), // Initial month selection
                        now.get(Calendar.DAY_OF_MONTH) // Inital day selection
                );
                dpd.show(getFragmentManager(), "Datepickerdialog");


            }
        });

        spinnerSales = findViewById(R.id.spinner_sales);
        getSales();


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


                salesAdapter = new ArrayAdapter<>(InsentifConfigAcitivity.this, android.R.layout.simple_spinner_item, merkArray);
                spinnerSales.setAdapter(salesAdapter);

                Bundle bundle = getIntent().getExtras();

                if (bundle.getBoolean("back")) {
                    spinnerSales.setSelection(bundle.getInt("posSales"));
                }


            }

            @Override
            public void onFailure(Call<List<Sales>> call, Throwable t) {

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
                dialog.dismiss();
                List<KonfigInsentif> konfigInsentifs = response.body();
                if (konfigInsentifs != null) {
                    for (KonfigInsentif konfigInsentif : konfigInsentifs) {


                        persentaseTxt.setText(konfigInsentif.getPersentase());

                        insentifMobar = konfigInsentif.getInsentifMobar();
                        limaStr = konfigInsentif.getInsentifMokas15();
                        sepuluhStr = konfigInsentif.getInsentifMokas610();
                        limaBelasStr = konfigInsentif.getInsentifMokas1115();
                        duaPuluhStr = konfigInsentif.getInsentifMokas1620();
                        duaSatuStr = konfigInsentif.getInsentifMokas21();

                        mobarTxt.setText(createDot(insentifMobar));
                        limaTxt.setText(createDot(limaStr));
                        sepuluhTxt.setText(createDot(sepuluhStr));
                        limaBelasTxt.setText(createDot(limaBelasStr));
                        duaPuluhTxt.setText(createDot(duaPuluhStr));
                        duaSatuTxt.setText(createDot(duaSatuStr));

                    }

                }
            }

            @Override
            public void onFailure(Call<List<KonfigInsentif>> call, Throwable t) {
                dialog.dismiss();
                t.printStackTrace();
                Toast.makeText(InsentifConfigAcitivity.this, "Terjadi Kesalahan Tidak Terduga", Toast.LENGTH_SHORT).show();
            }


        });
    }

    private void updateKonfigInsentif() {

        dialog.show();
        SharedPreferences pref = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        String token = pref.getString(ACCESTOKEN, "");

        String persen = persentaseTxt.getText().toString();

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<KonfigInsentif> call = apiInterface.updateKonfigInsentif(token, persen, clearDot(insentifMobar), clearDot(limaStr),
                clearDot(sepuluhStr), clearDot(limaBelasStr), clearDot(duaPuluhStr), clearDot(duaSatuStr));

        call.enqueue(new Callback<KonfigInsentif>() {
            @Override
            public void onResponse(Call<KonfigInsentif> call, Response<KonfigInsentif> response) {
                dialog.dismiss();
                if (response.body().getMessage().equals("success")) {
                    Toast.makeText(InsentifConfigAcitivity.this, "Data Konfig Telah Disubmit", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(InsentifConfigAcitivity.this, "Token Tidak Valid, Silahkan Login", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<KonfigInsentif> call, Throwable t) {
                dialog.dismiss();
                t.printStackTrace();
                Toast.makeText(InsentifConfigAcitivity.this, "Terjadi Kesalahan Tidak Terduga", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(InsentifConfigAcitivity.this, OwnerMenuActivity.class));
    }

    private void initProgressDialog() {
        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading");
        dialog.setMessage("Sedang Memproses..");
        dialog.setCancelable(false);
    }


    private void setInputMask() {

        final MaskedTextChangedListener reversedListener = new ReversedMaskTextChangedListener(
                "[000].[000].[000].[000].[000]",
                mobarTxt,
                new MaskedTextChangedListener.ValueListener() {
                    @Override
                    public void onTextChanged(boolean maskFilled, @NonNull final String extractedValue) {

                        insentifMobar = extractedValue;

                    }
                }
        );

        mobarTxt.addTextChangedListener(reversedListener);
        mobarTxt.setOnFocusChangeListener(this);

        final MaskedTextChangedListener limaListener = new ReversedMaskTextChangedListener(
                "[000].[000].[000].[000].[000]",
                limaTxt,
                new MaskedTextChangedListener.ValueListener() {
                    @Override
                    public void onTextChanged(boolean maskFilled, @NonNull final String extractedValue) {

                        limaStr = extractedValue;

                    }
                }
        );

        limaTxt.addTextChangedListener(limaListener);
        limaTxt.setOnFocusChangeListener(this);

        final MaskedTextChangedListener sepuluhListener = new ReversedMaskTextChangedListener(
                "[000].[000].[000].[000].[000]",
                sepuluhTxt,
                new MaskedTextChangedListener.ValueListener() {
                    @Override
                    public void onTextChanged(boolean maskFilled, @NonNull final String extractedValue) {

                        sepuluhStr = extractedValue;

                    }
                }
        );

        sepuluhTxt.addTextChangedListener(sepuluhListener);
        sepuluhTxt.setOnFocusChangeListener(this);

        final MaskedTextChangedListener limaBelasListener = new ReversedMaskTextChangedListener(
                "[000].[000].[000].[000].[000]",
                limaBelasTxt,
                new MaskedTextChangedListener.ValueListener() {
                    @Override
                    public void onTextChanged(boolean maskFilled, @NonNull final String extractedValue) {

                        limaBelasStr = extractedValue;

                    }
                }
        );

        limaBelasTxt.addTextChangedListener(limaBelasListener);
        limaBelasTxt.setOnFocusChangeListener(this);

        final MaskedTextChangedListener duaPuluhListener = new ReversedMaskTextChangedListener(
                "[000].[000].[000].[000].[000]",
                duaPuluhTxt,
                new MaskedTextChangedListener.ValueListener() {
                    @Override
                    public void onTextChanged(boolean maskFilled, @NonNull final String extractedValue) {

                        duaPuluhStr = extractedValue;

                    }
                }
        );

        duaPuluhTxt.addTextChangedListener(duaPuluhListener);
        duaPuluhTxt.setOnFocusChangeListener(this);

        final MaskedTextChangedListener duaSatuListener = new ReversedMaskTextChangedListener(
                "[000].[000].[000].[000].[000]",
                duaSatuTxt,
                new MaskedTextChangedListener.ValueListener() {
                    @Override
                    public void onTextChanged(boolean maskFilled, @NonNull final String extractedValue) {

                        duaSatuStr = extractedValue;

                    }
                }
        );

        duaSatuTxt.addTextChangedListener(duaSatuListener);
        duaSatuTxt.setOnFocusChangeListener(this);

    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {

        switch (view.getId()) {
            case R.id.insentif_mobar:
                if (hasFocus) {
                    mobarTxt.setText(createDot(insentifMobar) + "");
                }
                break;
            case R.id.insentif_mokas15:
                if (hasFocus) {
                    limaTxt.setText(createDot(limaStr) + "");
                }
                break;
            case R.id.insentif_mokas610:
                if (hasFocus) {
                    sepuluhTxt.setText(createDot(sepuluhStr) + "");
                }
                break;
            case R.id.insentif_mokas1115:
                if (hasFocus) {
                    limaBelasTxt.setText(createDot(limaBelasStr) + "");
                }
                break;
            case R.id.insentif_mokas1620:
                if (hasFocus) {
                    duaPuluhTxt.setText(createDot(duaPuluhStr) + "");
                }
                break;
            case R.id.insentif_mokas2125:
                if (hasFocus) {
                    duaSatuTxt.setText(createDot(duaSatuStr) + "");
                }
                break;



        }

    }
}
