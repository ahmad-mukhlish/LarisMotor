package com.yayanheryanto.larismotor.view.pending;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
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
import com.yayanheryanto.larismotor.model.Merk;
import com.yayanheryanto.larismotor.model.MerkTipe;
import com.yayanheryanto.larismotor.model.PendingBeli;
import com.yayanheryanto.larismotor.model.Tipe;
import com.yayanheryanto.larismotor.retrofit.ApiClient;
import com.yayanheryanto.larismotor.retrofit.ApiInterface;
import com.yayanheryanto.larismotor.view.LoginActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.yayanheryanto.larismotor.config.config.ACCESTOKEN;
import static com.yayanheryanto.larismotor.config.config.DATA_PENDING;
import static com.yayanheryanto.larismotor.config.config.ID_USER;
import static com.yayanheryanto.larismotor.config.config.MY_PREFERENCES;
import static com.yayanheryanto.larismotor.helper.HelperClass.clearDash;
import static com.yayanheryanto.larismotor.helper.HelperClass.clearDot;
import static com.yayanheryanto.larismotor.helper.HelperClass.convertToTitleCaseIteratingChars;
import static com.yayanheryanto.larismotor.helper.HelperClass.createDot;

public class EditPendingBeliActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, View.OnFocusChangeListener {


    private EditText txtNama, txtAlamat, txtNoTelepon, txtNamaMotor, txtTahun, txtHarga, txtTanggalBeli;
    private Button btnSave;
    private ProgressDialog dialog;
    private PendingBeli pendingBeli;
    private Spinner spinnerMerk, spinnerTipe;
    private ArrayAdapter<String> adapter, adapter2;
    private String s1, s2;
    private int merkMotor, tipeMotor;
    private List<Merk> merk;
    private List<Tipe> tipe;
    private ImageView tanggalBeliImg;
    private String tanggalBeli;
    private String harga;
    private String telepon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pending_beli);


        txtNama = findViewById(R.id.nama);
        txtAlamat = findViewById(R.id.alamat);
        txtNoTelepon = findViewById(R.id.telepon);
        txtTahun = findViewById(R.id.tahun);
        txtHarga = findViewById(R.id.harga_edit_pending_beli);
        btnSave = findViewById(R.id.btnSave);
        tanggalBeliImg = findViewById(R.id.tanggal_beli_picker);
        txtTanggalBeli = findViewById(R.id.tanggal_beli);


        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line);
        adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line);
        spinnerMerk = findViewById(R.id.spinner1);
        spinnerTipe = findViewById(R.id.spinner2);

        tanggalBeliImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Calendar now = Calendar.getInstance();
                com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
                        EditPendingBeliActivity.this,
                        now.get(Calendar.YEAR), // Initial year selection
                        now.get(Calendar.MONTH), // Initial month selection
                        now.get(Calendar.DAY_OF_MONTH) // Inital day selection
                );
                dpd.show(getFragmentManager(), "Datepickerdialog");


            }
        });

        btnSave.setOnClickListener(this);
        initProgressDialog();

        getFromIntent();

        getMerkById(String.valueOf(pendingBeli.getIdMerk()), String.valueOf(pendingBeli.getIdTipe()));
        getMerk();

        spinnerMerk.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                getTipe(String.valueOf(merk.get(i).getIdMerk()));
                merkMotor = merk.get(i).getIdMerk();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinnerTipe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tipeMotor = tipe.get(i).getIdTipe();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        setInputMask();

    }

    private void getFromIntent() {

        Bundle data = getIntent().getExtras();
        pendingBeli = data.getParcelable(DATA_PENDING);

        txtNama.setText(convertToTitleCaseIteratingChars(pendingBeli.getNama()));
        txtAlamat.setText(pendingBeli.getAlamat());
        txtTahun.setText("" + pendingBeli.getTahun());

        harga = pendingBeli.getHarga() + "";
        telepon = pendingBeli.getNoTelp() + "";

        txtHarga.setText(createDot(harga));

        if (pendingBeli.getNoTelp() == null) {
            txtNoTelepon.setText("-");
        } else {
            txtNoTelepon.setText(telepon);
        }

        if (pendingBeli.getTanggalBeli() == null) {

            txtTanggalBeli.setText("-");

        } else {
            SimpleDateFormat df = new SimpleDateFormat("dd MMMM yyyy", new Locale("ID"));
            SimpleDateFormat sql = new SimpleDateFormat("yyyy-MM-dd", new Locale("ID"));

            try {
                txtTanggalBeli.setText(df.format(sql.parse(pendingBeli.getTanggalBeli())));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        tanggalBeli = pendingBeli.getTanggalBeli();
    }


    private void initProgressDialog() {
        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading");
        dialog.setMessage("Sedang Memproses..");
        dialog.setCancelable(false);
    }

    private void getMerk() {
        dialog.show();
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<Merk>> call = apiInterface.getMerk();
        call.enqueue(new Callback<List<Merk>>() {
            @Override
            public void onResponse(Call<List<Merk>> call, Response<List<Merk>> response) {
                dialog.dismiss();
                merk = response.body();
                if (merk != null) {
                    for (Merk merkMotor : merk) {
                        adapter.add(merkMotor.getNamaMerk());

                    }
                    spinnerMerk.setAdapter(adapter);
                    spinnerMerk.setSelection(adapter.getPosition(s1));
                }
            }

            @Override
            public void onFailure(Call<List<Merk>> call, Throwable t) {
                dialog.dismiss();
                t.printStackTrace();
                Toast.makeText(EditPendingBeliActivity.this, "Terjadi Kesalahan Tidak Terduga", Toast.LENGTH_SHORT).show();
            }


        });
    }

    private void getMerkById(String id_merk, String id_tipe) {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<MerkTipe>> call = apiInterface.getMerkById(id_merk, id_tipe);
        call.enqueue(new Callback<List<MerkTipe>>() {
            @Override
            public void onResponse(Call<List<MerkTipe>> call, Response<List<MerkTipe>> response) {
                s2 = response.body().get(0).getNamaTipe();
                s1 = response.body().get(0).getNamaMerk();
            }

            @Override
            public void onFailure(Call<List<MerkTipe>> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(EditPendingBeliActivity.this, "Terjadi Kesalahan Tidak Terduga", Toast.LENGTH_SHORT).show();
            }


        });
    }


    private void getTipe(String idMerk) {
        dialog.show();
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<Tipe>> call = apiInterface.getTipe(idMerk);
        call.enqueue(new Callback<List<Tipe>>() {
            @Override
            public void onResponse(Call<List<Tipe>> call, Response<List<Tipe>> response) {
                dialog.dismiss();
                tipe = response.body();
                if (tipe != null) {
                    adapter2.clear();
                    for (Tipe tipeMotor : tipe) {
                        adapter2.add(tipeMotor.getNamaTipe());
                    }

                    spinnerTipe.setAdapter(adapter2);
                    spinnerTipe.setSelection(adapter2.getPosition(s2));
                }
            }

            @Override
            public void onFailure(Call<List<Tipe>> call, Throwable t) {
                dialog.dismiss();
                t.printStackTrace();
                Toast.makeText(EditPendingBeliActivity.this, "Terjadi Kesalahan Tidak Terduga", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSave:
                updatePending();
                break;
        }
    }

    private void updatePending() {
        dialog.show();
        SharedPreferences pref = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        String id = pref.getString(ID_USER, "");
        String token = pref.getString(ACCESTOKEN, "");

        String nama = txtNama.getText().toString();
        String alamat = txtAlamat.getText().toString();
        String tahun = txtTahun.getText().toString();
        int id_pending = pendingBeli.getIdPending();

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<PendingBeli> call = apiInterface.updatePendingBeli(token, id_pending, nama, alamat,
                clearDash(telepon), merkMotor, tipeMotor, tahun, clearDot(harga), tanggalBeli);

        Log.v("cik",tanggalBeli);

        call.enqueue(new Callback<PendingBeli>() {
            @Override
            public void onResponse(Call<PendingBeli> call, Response<PendingBeli> response) {
                dialog.dismiss();
                if (response.body().getMessage().equals("success")) {
                    Toast.makeText(EditPendingBeliActivity.this, "Pending Beli Berhasil Diubah", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EditPendingBeliActivity.this, PendingTransaksiActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("editJual", false);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(EditPendingBeliActivity.this, "Token Tidak Valid, Silahkan Login", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EditPendingBeliActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<PendingBeli> call, Throwable t) {
                dialog.dismiss();
                t.printStackTrace();
                Toast.makeText(EditPendingBeliActivity.this, "Terjadi Kesalahan Tidak Terduga", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        SimpleDateFormat df = new SimpleDateFormat("dd MMMM yyyy", new Locale("ID"));
        SimpleDateFormat sql = new SimpleDateFormat("yyyy-MM-dd", new Locale("ID"));
        String month;

        String tanggalBeliNya;
        monthOfYear++;
        if (monthOfYear < 10)
            month = "0" + monthOfYear;
        else
            month = "" + monthOfYear;

        SimpleDateFormat sqlformat = new SimpleDateFormat("yyyyMMdd", new Locale("EN"));
        String tanggal = year + month + dayOfMonth;


        try {
            tanggalBeliNya = df.format(sqlformat.parse(tanggal));
            txtTanggalBeli.setText(tanggalBeliNya);
            tanggalBeli = sql.format(df.parse(tanggalBeliNya));

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void setInputMask() {

        final MaskedTextChangedListener reversedListener = new ReversedMaskTextChangedListener(
                "[000].[000].[000].[000].[000]",
                txtHarga,
                new MaskedTextChangedListener.ValueListener() {
                    @Override
                    public void onTextChanged(boolean maskFilled, @NonNull final String extractedValue) {

                        harga = extractedValue;

                    }
                }
        );

        txtHarga.addTextChangedListener(reversedListener);
        txtHarga.setOnFocusChangeListener(this);

        final MaskedTextChangedListener telpListener = new MaskedTextChangedListener(
                "[000]-[000]-[000]-[0009]",
                txtNoTelepon,
                new MaskedTextChangedListener.ValueListener() {
                    @Override
                    public void onTextChanged(boolean maskFilled, @NonNull final String extractedValue) {

                        telepon = extractedValue;

                    }
                }
        );

        txtNoTelepon.addTextChangedListener(telpListener);
        txtNoTelepon.setOnFocusChangeListener(telpListener);


    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        switch (view.getId()) {
            case R.id.harga_edit_pending_beli:
                if (hasFocus && !(pendingBeli.getHarga() == null)) {
                    txtHarga.setText(createDot(harga));
                }

                break;
        }
    }
}
