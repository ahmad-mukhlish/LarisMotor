package com.yayanheryanto.larismotor.view.transaksi;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.yayanheryanto.larismotor.R;
import com.yayanheryanto.larismotor.model.Customer;
import com.yayanheryanto.larismotor.model.Motor;
import com.yayanheryanto.larismotor.retrofit.ApiClient;
import com.yayanheryanto.larismotor.retrofit.ApiInterface;
import com.yayanheryanto.larismotor.view.owner.OwnerMenuActivity;
import com.yayanheryanto.larismotor.view.sales.SalesMenuActivity;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.yayanheryanto.larismotor.config.config.ACCESTOKEN;
import static com.yayanheryanto.larismotor.config.config.DATA_MOTOR;
import static com.yayanheryanto.larismotor.config.config.ID_USER;
import static com.yayanheryanto.larismotor.config.config.KTP_SALES;
import static com.yayanheryanto.larismotor.config.config.MY_PREFERENCES;

public class IsiDataActivity extends AppCompatActivity implements com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener {


    Spinner spinnerTxtAgama;
    EditText nomorKtp;
    EditText nama;
    EditText alamat;
    EditText nomorTelp;
    EditText ttl;
    EditText pekerjaan;
    EditText whatsapp;
    EditText instagram;
    EditText facebook;
    Button checklistIsiData;
    ImageView date;
    int statusCustomer;

    private ProgressDialog dialog;
    private Motor motor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_isi_data);

        nomorKtp = findViewById(R.id.nomor_ktp);
        nama = findViewById(R.id.nama);
        alamat = findViewById(R.id.alamat);
        nomorTelp = findViewById(R.id.no_telp);
        ttl = findViewById(R.id.ttl);
        pekerjaan = findViewById(R.id.pekerjaan);
        whatsapp = findViewById(R.id.whatsapp);
        instagram = findViewById(R.id.instagram);
        facebook = findViewById(R.id.facebook);
        checklistIsiData = findViewById(R.id.cheklist_isi_data);
        date = findViewById(R.id.date);

        nomorKtp.setText("");

        initProgressDialog();

        getFromIntent();

        spinnerTxtAgama = findViewById(R.id.spinner_txt_agama);
        ArrayAdapter<CharSequence> spinnerAgamaAdapter = ArrayAdapter.createFromResource(this,
                R.array.txtAgama, android.R.layout.simple_spinner_item);


        spinnerTxtAgama.setAdapter(spinnerAgamaAdapter);

        checklistIsiData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                String stringNomorKtp = nomorKtp.getText().toString();
                final ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                Call<List<Customer>> call = apiInterface.getCustomerById(stringNomorKtp);
                call.enqueue(new Callback<List<Customer>>() {
                    @Override
                    public void onResponse(Call<List<Customer>> call, Response<List<Customer>> response) {

                        if (response.body().isEmpty() || response.body() == null) {
                            dialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Data Customer tidak ditemukan", Toast.LENGTH_SHORT).show();
                            clearAll();
                            enable();
                            statusCustomer = 0;


                        } else {
                            dialog.dismiss();
                            statusCustomer = 1;
                            Customer customer = response.body().get(0);
                            nama.setText(customer.getNama());
                            nama.setEnabled(false);
                            nama.setTextColor(Color.BLACK);

                            alamat.setText(customer.getAlamat());
                            alamat.setEnabled(false);
                            alamat.setTextColor(Color.BLACK);

                            nomorTelp.setText(customer.getNoTelp());
                            nomorTelp.setEnabled(false);
                            nomorTelp.setTextColor(Color.BLACK);


                            SimpleDateFormat df = new SimpleDateFormat("dd MMMM yyyy", new Locale("ID"));
                            SimpleDateFormat sqlformat = new SimpleDateFormat("yyyy-MM-dd", new Locale("EN"));
                            try {
                                ttl.setText(df.format(sqlformat.parse(customer.getTtl())));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            ttl.setEnabled(false);
                            ttl.setTextColor(Color.BLACK);

                            pekerjaan.setText(customer.getPekerjaan());
                            pekerjaan.setEnabled(false);
                            pekerjaan.setTextColor(Color.BLACK);

                            whatsapp.setText(customer.getWhatsapp());
                            whatsapp.setEnabled(false);
                            whatsapp.setTextColor(Color.BLACK);

                            instagram.setText(customer.getInstagram());
                            instagram.setEnabled(false);
                            instagram.setTextColor(Color.BLACK);

                            facebook.setText(customer.getFacebook());
                            facebook.setEnabled(false);
                            facebook.setTextColor(Color.BLACK);

                        }


                    }

                    @Override
                    public void onFailure(Call<List<Customer>> call, Throwable t) {
                        dialog.dismiss();
                        Log.v("cik", t.getMessage());
                        Toast.makeText(IsiDataActivity.this, "Terjadi Kesalahan Tidak Terduga", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });

        disable();

        TextView next = findViewById(R.id.next_isi_data);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                writeToDatabase();


            }
        });

        TextView prev = findViewById(R.id.prev_isi_data);
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(IsiDataActivity.this, TransaksiActivity.class));


            }
        });

    }

    private void initProgressDialog() {
        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading");
        dialog.setMessage("Sedang Memproses..");
        dialog.setCancelable(false);
    }

    private void disable() {
        nama.setEnabled(false);
        alamat.setEnabled(false);
        ttl.setEnabled(false);
        spinnerTxtAgama.setEnabled(false);
        nomorTelp.setEnabled(false);
        pekerjaan.setEnabled(false);
        whatsapp.setEnabled(false);
        instagram.setEnabled(false);
        facebook.setEnabled(false);

    }

    private void enable() {
        nama.setEnabled(true);
        alamat.setEnabled(true);
        ttl.setEnabled(true);
        spinnerTxtAgama.setEnabled(true);
        pekerjaan.setEnabled(true);
        nomorTelp.setEnabled(true);
        whatsapp.setEnabled(true);
        instagram.setEnabled(true);
        facebook.setEnabled(true);

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Calendar now = Calendar.getInstance();
                com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
                        IsiDataActivity.this,
                        now.get(Calendar.YEAR), // Initial year selection
                        now.get(Calendar.MONTH), // Initial month selection
                        now.get(Calendar.DAY_OF_MONTH) // Inital day selection
                );
                dpd.show(getFragmentManager(), "Datepickerdialog");


            }
        });

    }

    private void getFromIntent() {
        Bundle bundle = getIntent().getExtras();
        motor = bundle.getParcelable(DATA_MOTOR);
        Log.v("cik2",motor.getMediator()+"") ;


    }

    private void clearAll() {
        nama.setText("");
        alamat.setText("");
        ttl.setText("");
        spinnerTxtAgama.setSelection(0);
        pekerjaan.setText("");
        whatsapp.setText("");
        instagram.setText("");
        facebook.setText("");
        nomorTelp.setText("");

    }


    private void writeToDatabase() {

        dialog.show();
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        SharedPreferences pref = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        String id = pref.getString(ID_USER, "");
        String token = pref.getString(ACCESTOKEN, "");
        String no_ktp_sales = pref.getString(KTP_SALES, "");
        Call<Motor> call = null;

        String nomorKtpTxt = nomorKtp.getText().toString();
        String namaTxt = nama.getText().toString();
        String alamatTxt = alamat.getText().toString();
        String nomorTelpTxt = nomorTelp.getText().toString();
        String Agama = spinnerTxtAgama.getSelectedItem().toString();


        String ttlTxt = ttl.getText().toString();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(ttlTxt);
        } catch (ParseException e) {

            e.printStackTrace();
        }
        String tanggal = dateFormat.format(convertedDate);

        String pekerjaanTxt = pekerjaan.getText().toString();
        String whatsappTxt = whatsapp.getText().toString();
        String instagramTxt = instagram.getText().toString();
        String facebookTxt = facebook.getText().toString();


        //bekas = 0;
        //customer ada = 1;
        if (motor.getKondisi() == 0 && statusCustomer == 1) {
            //ubah status motor dan simpan ke transaksi, customer++
            Log.v("apa", no_ktp_sales);
            call = apiInterface.mokasWithNoCust(token, motor.getNoMesin(), nomorKtpTxt, no_ktp_sales, String.valueOf(motor.getHargaTerjual()),
                    motor.getDp() + "", motor.getCicilan() + "", motor.getTenor() + "",
                    motor.getPencairanLeasing() + "", motor.getMediator() + "");
        } else if (motor.getKondisi() == 0 && statusCustomer == 0) {
            //ubah status motor, simpan customer dan transaksi
            call = apiInterface.mokasWithCust(token, motor.getNoMesin(), nomorKtpTxt, namaTxt, alamatTxt, nomorTelpTxt, tanggal, Agama, pekerjaanTxt, whatsappTxt, instagramTxt, facebookTxt,
                    String.valueOf(motor.getHargaTerjual()), motor.getDp() + "", motor.getCicilan() + "",
                    motor.getTenor() + "", motor.getPencairanLeasing() + "", no_ktp_sales, motor.getMediator() + "");
        } else if (motor.getKondisi() == 1 && statusCustomer == 1) {
            //simpan, simpan ke transaksi, customer++


            call = apiInterface.mobarWithNoCust(token, nomorKtpTxt, no_ktp_sales, motor.getNoMesin(), motor.getNoRangka(), String.valueOf(motor.getIdMerk()), String.valueOf(motor.getIdTipe()),
                    String.valueOf(motor.getTahun()), String.valueOf(motor.getHjm()), id, motor.getHargaTerjual() + "",
                    String.valueOf(motor.getDp()), String.valueOf(motor.getCicilan()), String.valueOf(motor.getTenor()),
                    motor.getSubsidi() + "", motor.getMediator() + "");

        } else if (motor.getKondisi() == 1 && statusCustomer == 0) {
            call = apiInterface.mobarWithCust(token, motor.getNoMesin(), motor.getNoRangka(), String.valueOf(motor.getTahun()), String.valueOf(motor.getHjm()),
                    String.valueOf(motor.getIdTipe()), String.valueOf(motor.getIdMerk()), id, String.valueOf(motor.getHargaTerjual()),
                    String.valueOf(motor.getDp()), String.valueOf(motor.getCicilan()), String.valueOf(motor.getTenor()), motor.getSubsidi() + "",
                    nomorKtpTxt, namaTxt, alamatTxt, nomorTelpTxt, Agama, pekerjaanTxt, whatsappTxt,
                    instagramTxt, facebookTxt, no_ktp_sales, tanggal, motor.getMediator() + "");
        }
        call.enqueue(new Callback<Motor>() {
            @Override
            public void onResponse(Call<Motor> call, Response<Motor> response) {
                dialog.dismiss();

                if (response.body().getMessage().equalsIgnoreCase("success")) {
                    Toast.makeText(IsiDataActivity.this, "Transasksi Berhasil Disimpan", Toast.LENGTH_SHORT).show();

                    SharedPreferences pref = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
                    String id = pref.getString(ID_USER, "");

                    if (id.equals("1")) {
                        startActivity(new Intent(IsiDataActivity.this, OwnerMenuActivity.class));
                    } else {
                        startActivity(new Intent(IsiDataActivity.this, SalesMenuActivity.class));
                    }
                }
            }

            @Override
            public void onFailure(Call<Motor> call, Throwable t) {
                Log.d("cobaa", t.getMessage());
                dialog.dismiss();
                Toast.makeText(IsiDataActivity.this, "Terjadi Kesalahan Tidak Terduga", Toast.LENGTH_SHORT).show();
            }


        });

    }

    @Override
    public void onDateSet(com.wdullaer.materialdatetimepicker.date.DatePickerDialog view,
                          int year, int monthOfYear, int dayOfMonth) {
        SimpleDateFormat df = new SimpleDateFormat("dd MMMM yyyy", new Locale("ID"));
        Log.v("cik", monthOfYear + "");
        String month;
        monthOfYear++;
        if (monthOfYear < 10)
            month = "0" + monthOfYear;
        else
            month = "" + monthOfYear;

        SimpleDateFormat sqlformat = new SimpleDateFormat("yyyyMMdd", new Locale("EN"));
        String tanggal = year + month + dayOfMonth;


        try {
            ttl.setText(df.format(sqlformat.parse(tanggal)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


}

