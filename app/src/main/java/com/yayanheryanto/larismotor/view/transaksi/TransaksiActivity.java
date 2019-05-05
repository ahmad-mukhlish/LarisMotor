package com.yayanheryanto.larismotor.view.transaksi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.redmadrobot.inputmask.MaskedTextChangedListener;
import com.redmadrobot.inputmask.ReversedMaskTextChangedListener;
import com.yayanheryanto.larismotor.R;
import com.yayanheryanto.larismotor.model.Merk;
import com.yayanheryanto.larismotor.model.MerkTipe;
import com.yayanheryanto.larismotor.model.Motor;
import com.yayanheryanto.larismotor.model.Tipe;
import com.yayanheryanto.larismotor.retrofit.ApiClient;
import com.yayanheryanto.larismotor.retrofit.ApiInterface;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;
import static com.yayanheryanto.larismotor.config.config.DATA_MOTOR;
import static com.yayanheryanto.larismotor.helper.HelperClass.clearDot;
import static com.yayanheryanto.larismotor.helper.HelperClass.createDot;
import static com.yayanheryanto.larismotor.helper.HelperClass.formatter;

public class TransaksiActivity extends AppCompatActivity implements View.OnFocusChangeListener {


    private Spinner spinnerCaraBayar;
    private EditText cicilan;
    private EditText tenor;
    private EditText dp;
    private EditText harga;
    private EditText nomorMesin;
    private EditText nomorRangka;
    private EditText nomorPolisi;
    private EditText subsidi;
    private EditText pencairanLeasing;
    private Spinner spinnerMerk;
    private Spinner spinnerTipe;
    private EditText tahun;
    private EditText hargaJualMinimum;
    private EditText mediator;
    private Button checklist;
    private TextView tanggal, merk, tipe, pembayaran;
    private ArrayAdapter<String> merkAdapter;
    private ArrayAdapter<String> tipeAdapter;
    private int kondisi;
    private ProgressDialog dialog;
    private String statusMotor;
    private int idTipe, idMerk;
    private List<Tipe> tipes;
    private boolean flagTenor;
    private TextInputLayout wHarga, wDp, wTenor, wCicilan, wSubsidi, wPencairanLeasing, wNomorMesin,
            wNomorRangka, wNomorPolisi, wTahun, wHargaJualMinimum, wMediator;

    private String hjmMotor;
    private String hargaMotor;
    private String mediatorMotor;
    private String dpMotor;
    private String cicilanMotor;
    private String subsidiMotor;
    private String pencairanLeasingMotor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaksi);

        harga = findViewById(R.id.harga);
        wHarga = findViewById(R.id.w_harga);
        dp = findViewById(R.id.dp);
        wDp = findViewById(R.id.w_dp);
        tenor = findViewById(R.id.tenor);
        wTenor = findViewById(R.id.w_tenor);
        cicilan = findViewById(R.id.cicilan);
        wCicilan = findViewById(R.id.w_cicilan);
        subsidi = findViewById(R.id.subsidi);
        wSubsidi = findViewById(R.id.w_subsidi);
        pencairanLeasing = findViewById(R.id.pencairan_leasing);
        wPencairanLeasing = findViewById(R.id.w_pencairan_leasing);
        nomorMesin = findViewById(R.id.nomor_mesin_trans);
        wNomorMesin = findViewById(R.id.w_nomor_mesin);
        nomorRangka = findViewById(R.id.nomor_rangka_trans);
        wNomorRangka = findViewById(R.id.w_nomor_rangka);
        nomorPolisi = findViewById(R.id.nomor_polisi_trans);
        wNomorPolisi = findViewById(R.id.w_nomor_polisi);
        tahun = findViewById(R.id.tahun_trans);
        wTahun = findViewById(R.id.w_tahun);
        hargaJualMinimum = findViewById(R.id.harga_jual_minimum_trans);
        wHargaJualMinimum = findViewById(R.id.w_harga_jual_minimum);
        checklist = findViewById(R.id.cheklist);
        tanggal = findViewById(R.id.tanggal);
        spinnerMerk = findViewById(R.id.spinnerMerk);
        spinnerTipe = findViewById(R.id.spinnerTipe);
        merk = findViewById(R.id.merk);
        tipe = findViewById(R.id.tipe);
        pembayaran = findViewById(R.id.pembayaran);
        mediator = findViewById(R.id.mediator);
        wMediator = findViewById(R.id.w_mediator);

        initProgressDialog();


        SimpleDateFormat df = new SimpleDateFormat("EEEE, dd MMMM yyyy", new Locale("ID"));
        tanggal.setText(df.format(Calendar.getInstance().getTime()));

        final String[] mobarArray = {"Pilih Kondisi", "Mobar", "Mokas"};
        final Spinner spinnerMobar = findViewById(R.id.mobar);
        ArrayAdapter<String> mobar = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mobarArray);
        spinnerMobar.setAdapter(mobar);

        spinnerCaraBayar = findViewById(R.id.cara_bayar);
        ArrayAdapter<CharSequence> caraBayar = ArrayAdapter.createFromResource(this,
                R.array.caraBayar, android.R.layout.simple_spinner_item);
        spinnerCaraBayar.setAdapter(caraBayar);

        Bundle bundle = getIntent().getExtras();

        if (!bundle.getBoolean("deal")) {

            spinnerMobar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    statusMotor = mobarArray[position];
                    clearAll();
                    spinnerCaraBayar.setSelection(0);

                    if (position == 0) {

                        nomorMesin.setVisibility(GONE);
                        wNomorMesin.setVisibility(GONE);
                        nomorRangka.setVisibility(GONE);
                        wNomorRangka.setVisibility(GONE);
                        nomorPolisi.setVisibility(GONE);
                        wNomorPolisi.setVisibility(GONE);
                        spinnerMerk.setVisibility(GONE);
                        spinnerTipe.setVisibility(GONE);
                        tahun.setVisibility(GONE);
                        wTahun.setVisibility(GONE);
                        hargaJualMinimum.setVisibility(GONE);
                        wHargaJualMinimum.setVisibility(GONE);
                        spinnerCaraBayar.setVisibility(GONE);
                        harga.setVisibility(GONE);
                        wHarga.setVisibility(GONE);
                        checklist.setVisibility(GONE);
                        merk.setVisibility(GONE);
                        tipe.setVisibility(GONE);
                        pembayaran.setVisibility(GONE);
                        dp.setVisibility(GONE);
                        wDp.setVisibility(GONE);
                        cicilan.setVisibility(GONE);
                        wCicilan.setVisibility(GONE);
                        tenor.setVisibility(GONE);
                        wTenor.setVisibility(GONE);
                        pencairanLeasing.setVisibility(GONE);
                        wPencairanLeasing.setVisibility(GONE);
                        subsidi.setVisibility(GONE);
                        wSubsidi.setVisibility(GONE);

                    } else if (position == 1) {
                        nomorMesin.setVisibility(View.VISIBLE);
                        wNomorMesin.setVisibility(View.VISIBLE);
                        nomorRangka.setVisibility(View.VISIBLE);
                        wNomorRangka.setVisibility(View.VISIBLE);
                        nomorRangka.setEnabled(true);
                        nomorPolisi.setVisibility(GONE);
                        wNomorPolisi.setVisibility(GONE);
                        merk.setVisibility(View.VISIBLE);
                        spinnerMerk.setVisibility(View.VISIBLE);
                        spinnerMerk.setEnabled(true);
                        tipe.setVisibility(View.VISIBLE);
                        spinnerTipe.setVisibility(View.VISIBLE);
                        spinnerTipe.setEnabled(true);
                        tahun.setVisibility(View.VISIBLE);
                        tahun.setEnabled(true);
                        wTahun.setVisibility(View.VISIBLE);
                        hargaJualMinimum.setVisibility(View.VISIBLE);
                        wHargaJualMinimum.setVisibility(View.VISIBLE);
                        pembayaran.setVisibility(View.VISIBLE);
                        spinnerCaraBayar.setVisibility(View.VISIBLE);
                        hargaJualMinimum.setEnabled(true);
                        checklist.setVisibility(GONE);
                        kondisi = 1;
                        getMerk();
                    } else {
                        nomorMesin.setVisibility(View.VISIBLE);
                        wNomorMesin.setVisibility(View.VISIBLE);
                        nomorRangka.setVisibility(View.VISIBLE);
                        wNomorRangka.setVisibility(View.VISIBLE);
                        nomorPolisi.setVisibility(View.VISIBLE);
                        wNomorPolisi.setVisibility(View.VISIBLE);
                        merk.setVisibility(View.VISIBLE);
                        spinnerMerk.setVisibility(View.VISIBLE);
                        spinnerMerk.setEnabled(false);
                        tipe.setVisibility(View.VISIBLE);
                        spinnerTipe.setVisibility(View.VISIBLE);
                        spinnerTipe.setEnabled(false);
                        nomorRangka.setEnabled(false);
                        tahun.setVisibility(View.VISIBLE);
                        wTahun.setVisibility(View.VISIBLE);
                        tahun.setEnabled(false);
                        hargaJualMinimum.setVisibility(View.VISIBLE);
                        wHargaJualMinimum.setVisibility(View.VISIBLE);
                        hargaJualMinimum.setEnabled(false);
                        checklist.setVisibility(View.VISIBLE);
                        pembayaran.setVisibility(View.VISIBLE);
                        spinnerCaraBayar.setVisibility(View.VISIBLE);
                        kondisi = 0;
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            checklist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fillData(nomorMesin.getText().toString());
                }
            });

        } else {

            nomorMesin.setVisibility(View.VISIBLE);
            wNomorMesin.setVisibility(View.VISIBLE);
            nomorRangka.setVisibility(View.VISIBLE);
            wNomorRangka.setVisibility(View.VISIBLE);
            nomorPolisi.setVisibility(View.VISIBLE);
            wNomorPolisi.setVisibility(View.VISIBLE);
            merk.setVisibility(View.VISIBLE);
            spinnerMerk.setVisibility(View.VISIBLE);
            spinnerMerk.setEnabled(false);
            tipe.setVisibility(View.VISIBLE);
            spinnerTipe.setVisibility(View.VISIBLE);
            spinnerTipe.setEnabled(false);
            nomorRangka.setEnabled(false);
            tahun.setVisibility(View.VISIBLE);
            tahun.setEnabled(false);
            wTahun.setVisibility(View.VISIBLE);
            hargaJualMinimum.setVisibility(View.VISIBLE);
            hargaJualMinimum.setEnabled(false);
            wHargaJualMinimum.setVisibility(View.VISIBLE);
            pembayaran.setVisibility(View.VISIBLE);
            spinnerCaraBayar.setVisibility(View.VISIBLE);
            kondisi = 0;

            spinnerMobar.setSelection(2);
            spinnerMobar.setEnabled(false);


            checklist.setVisibility(View.GONE);

            nomorMesin.setText(bundle.getString("data"));
            nomorMesin.setEnabled(false);
            nomorMesin.setTextColor(Color.parseColor("#000000"));
            fillData(bundle.getString("data"));

        }

        spinnerCaraBayar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {
                    harga.setVisibility(View.GONE);
                    wHarga.setVisibility(GONE);
                    dp.setVisibility(GONE);
                    wDp.setVisibility(GONE);
                    tenor.setVisibility(GONE);
                    wTenor.setVisibility(GONE);
                    cicilan.setVisibility(GONE);
                    wCicilan.setVisibility(GONE);
                    subsidi.setVisibility(View.GONE);
                    wSubsidi.setVisibility(GONE);
                    pencairanLeasing.setVisibility(View.GONE);
                    wPencairanLeasing.setVisibility(GONE);
                    mediator.setVisibility(GONE);
                    wMediator.setVisibility(GONE);
                } else if (position == 1) {
                    harga.setVisibility(View.VISIBLE);
                    wHarga.setVisibility(View.VISIBLE);
                    mediator.setVisibility(View.VISIBLE);
                    wMediator.setVisibility(View.VISIBLE);
                    dp.setVisibility(GONE);
                    wDp.setVisibility(GONE);
                    tenor.setVisibility(GONE);
                    wTenor.setVisibility(GONE);
                    cicilan.setVisibility(GONE);
                    wCicilan.setVisibility(GONE);
                    subsidi.setVisibility(GONE);
                    wSubsidi.setVisibility(GONE);
                    pencairanLeasing.setVisibility(GONE);
                    wPencairanLeasing.setVisibility(GONE);
                } else {

                    harga.setVisibility(GONE);
                    wHarga.setVisibility(GONE);
                    mediator.setVisibility(View.VISIBLE);
                    wMediator.setVisibility(View.VISIBLE);
                    dp.setVisibility(View.VISIBLE);
                    wDp.setVisibility(View.VISIBLE);
                    tenor.setVisibility(View.VISIBLE);
                    wTenor.setVisibility(View.VISIBLE);
                    cicilan.setVisibility(View.VISIBLE);
                    wCicilan.setVisibility(View.VISIBLE);

                    dp.setEnabled(true);
                    tenor.setEnabled(true);
                    cicilan.setEnabled(true);

                    if (spinnerMobar.getSelectedItemPosition() == 1) {
                        subsidi.setVisibility(View.VISIBLE);
                        wSubsidi.setVisibility(View.VISIBLE);
                    } else {
                        pencairanLeasing.setVisibility(View.VISIBLE);
                        wPencairanLeasing.setVisibility(View.VISIBLE);
                    }

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        TextView textView = findViewById(R.id.next_trans);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tahun.getText().toString().isEmpty() || nomorMesin.getText().toString().isEmpty()
                        || nomorRangka.getText().toString().isEmpty() || hargaJualMinimum.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Data masih ada yang kosong", Toast.LENGTH_SHORT).show();
                } else {
                    if (spinnerCaraBayar.getSelectedItemPosition() == 0) {

                        Toast.makeText(getApplicationContext(), "Silakan pilih cara bayar", Toast.LENGTH_SHORT).show();


                    } else if (spinnerCaraBayar.getSelectedItemPosition() == 1 &&
                            (harga.getText().toString().isEmpty() || mediator.getText().toString().isEmpty())) {

                        Toast.makeText(getApplicationContext(), "Silakan isi semua data cash (bila tidak ada isi dengan 0)", Toast.LENGTH_SHORT).show();


                    } else if (spinnerCaraBayar.getSelectedItemPosition() == 2
                            && spinnerMobar.getSelectedItemPosition() == 2 &&
                            (dp.getText().toString().isEmpty() || cicilan.getText().toString().isEmpty()
                                    || tenor.getText().toString().isEmpty()
                                    || mediator.getText().toString().isEmpty() || pencairanLeasing.getText().toString().isEmpty())) {
                        Toast.makeText(getApplicationContext(), "Silakan isi semua data mokas kredit (bila tidak ada isi dengan 0)", Toast.LENGTH_SHORT).show();

                    } else if (spinnerCaraBayar.getSelectedItemPosition() == 2
                            && spinnerMobar.getSelectedItemPosition() == 1 &&
                            (dp.getText().toString().isEmpty() || cicilan.getText().toString().isEmpty()
                                    || tenor.getText().toString().isEmpty()
                                    || mediator.getText().toString().isEmpty() || subsidi.getText().toString().isEmpty())) {
                        Toast.makeText(getApplicationContext(), "Silakan isi semua data mobar kredit (bila tidak ada isi dengan 0)", Toast.LENGTH_SHORT).show();
                    } else {
                        getData();


                    }

                }
            }
        });

        setInputMask();

    }

    private void getData() {

        Motor motor = new Motor();
        String noMesin, noRangka, tahunMotor,
                tenorMotor;

        if (kondisi == 0) {
            noMesin = nomorMesin.getText().toString();

            if (spinnerCaraBayar.getSelectedItemPosition() == 1) {
                motor.setHargaTerjual(Integer.valueOf(clearDot(hargaMotor)));
            } else {
                tenorMotor = tenor.getText().toString();


                motor.setDp(Integer.valueOf(clearDot(dpMotor)));
                motor.setCicilan(Integer.valueOf(clearDot(cicilanMotor)));

                if (flagTenor) {
                    motor.setTenor(Integer.valueOf(tenorMotor.substring(16)));
                } else {
                    motor.setTenor(Integer.valueOf(tenorMotor));
                }

                motor.setPencairanLeasing(Integer.valueOf(clearDot(pencairanLeasingMotor)));
            }

            motor.setNoMesin(noMesin);
            motor.setKondisi(kondisi);
            if (mediatorMotor.isEmpty() || mediatorMotor.equals("")) {
                motor.setMediator(null);
            } else {
                motor.setMediator(Integer.valueOf(mediatorMotor));
            }


            Intent intent = new Intent(TransaksiActivity.this, IsiDataActivity.class);
            intent.putExtra(DATA_MOTOR, motor);
            startActivity(intent);

        } else {
            noMesin = nomorMesin.getText().toString();
            noRangka = nomorRangka.getText().toString();
            tahunMotor = tahun.getText().toString();


            motor.setNoMesin(noMesin);
            motor.setNoRangka(noRangka);
            motor.setTahun(Integer.valueOf(tahunMotor));
            motor.setHjm(Integer.valueOf(clearDot(hjmMotor)));
            motor.setKondisi(kondisi);
            motor.setIdTipe(idTipe);
            motor.setIdMerk(idMerk);

            if (spinnerCaraBayar.getSelectedItemPosition() == 1) {
                motor.setHargaTerjual(Integer.valueOf(clearDot(hargaMotor)));
                motor.setMediator(Integer.valueOf(mediatorMotor));
            } else {

                tenorMotor = tenor.getText().toString();


                motor.setDp(Integer.valueOf(clearDot(dpMotor)));
                motor.setCicilan(Integer.valueOf(clearDot(cicilanMotor)));
                motor.setTenor(Integer.valueOf(tenorMotor));
                motor.setSubsidi(Integer.valueOf(clearDot(subsidiMotor)));
                motor.setMediator(Integer.valueOf(mediatorMotor));


            }

            Intent intent = new Intent(TransaksiActivity.this, IsiDataActivity.class);
            intent.putExtra(DATA_MOTOR, motor);
            startActivity(intent);


        }
    }

    private void clearAll() {
        nomorMesin.setText("");
        nomorRangka.setText("");
        spinnerMerk.setSelection(0);
        spinnerTipe.setSelection(0);
        tahun.setText("");
        hargaJualMinimum.setText("");
        dp.setText("");
        tenor.setText("");
        cicilan.setText("");
        subsidi.setText("");
        pencairanLeasing.setText("");
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
                String[] merkArray = new String[merks.size()];

                int i = 0;

                for (Merk merkNow : merks) {

                    merkArray[i] = merkNow.getNamaMerk();
                    i++;
                }

                merkAdapter = new ArrayAdapter<>(TransaksiActivity.this, android.R.layout.simple_spinner_item, merkArray);
                spinnerMerk.setAdapter(merkAdapter);


            }

            @Override
            public void onFailure(Call<List<Merk>> call, Throwable t) {

            }
        });

        spinnerMerk.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {

                Call<List<Tipe>> call2 = apiInterface.getTipe(String.valueOf(merks.get(position).getIdMerk()));
                call2.enqueue(new Callback<List<Tipe>>() {
                    @Override
                    public void onResponse(Call<List<Tipe>> call, Response<List<Tipe>> response) {

                        tipes = new ArrayList<>();
                        for (Tipe tipeNow : response.body()) {

                            tipes.add(tipeNow);

                        }

                        int i = 0;

                        String[] tipeArray;

                        if (tipes.isEmpty()) {
                            tipeArray = new String[1];
                            tipeArray[i] = "Tipe tidak tersedia";

                        } else {
                            tipeArray = new String[tipes.size()];
                            for (Tipe tipeNow : tipes) {

                                tipeArray[i] = tipeNow.getNamaTipe();
                                i++;
                            }
                        }
                        tipeAdapter = new ArrayAdapter<>(TransaksiActivity.this, android.R.layout.simple_spinner_item, tipeArray);
                        spinnerTipe.setAdapter(tipeAdapter);
                        idMerk = merks.get(position).getIdMerk();

                    }

                    @Override
                    public void onFailure(Call<List<Tipe>> call, Throwable t) {

                    }
                });


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spinnerTipe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (tipes.size() != 0) {
                    idTipe = tipes.get(i).getIdTipe();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }


    private void initProgressDialog() {
        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading");
        dialog.setMessage("Sedang Memproses..");
        dialog.setCancelable(false);
    }

    private void fillData(String stringNomorMesin) {
        dialog.show();
        final ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<Motor>> call = apiInterface.getMotorById(stringNomorMesin);
        call.enqueue(new Callback<List<Motor>>() {
            @Override
            public void onResponse(Call<List<Motor>> call, Response<List<Motor>> response) {

                if (response.body().isEmpty()) {

                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Data motor tidak tersedia", Toast.LENGTH_SHORT).show();
                    clearAll();


                } else {
                    dialog.dismiss();
                    Motor motor = response.body().get(0);

                    nomorRangka.setText("No. Rangka : " + motor.getNoRangka());
                    nomorRangka.setEnabled(false);
                    nomorRangka.setTextColor(Color.BLACK);

                    nomorPolisi.setText("No. Polisi     : " + motor.getNoPolisi());
                    nomorPolisi.setEnabled(false);
                    nomorPolisi.setTextColor(Color.BLACK);


                    if (motor.getDp() == null) {
                        dp.setText("");
                    } else {
                        dp.setText(createDot(motor.getDp() + ""));
                        dp.setEnabled(false);
                        dp.setTextColor(Color.BLACK);
                    }


                    if (motor.getTenor() == null) {
                        tenor.setText("");
                        flagTenor = false;
                    } else {
                        tenor.setText("Tenor (Bulan) : " + motor.getTenor());
                        tenor.setEnabled(false);
                        tenor.setTextColor(Color.BLACK);
                        flagTenor = true;
                    }


                    if (motor.getCicilan() == null) {
                        cicilan.setText("");
                    } else {
                        cicilan.setText(createDot(motor.getCicilan() + ""));
                        cicilan.setEnabled(false);
                        cicilan.setTextColor(Color.BLACK);
                    }

                    Call<List<MerkTipe>> call2 = apiInterface.getMerkById(String.valueOf(motor.getIdMerk()), String.valueOf(motor.getIdTipe()));
                    call2.enqueue(new Callback<List<MerkTipe>>() {
                        @Override
                        public void onResponse(Call<List<MerkTipe>> call, Response<List<MerkTipe>> response) {

                            MerkTipe merkTipe = response.body().get(0);

                            String[] merkArray = new String[1];
                            merkArray[0] = merkTipe.getNamaMerk();

                            merkAdapter = new ArrayAdapter<String>(TransaksiActivity.this,
                                    android.R.layout.simple_spinner_item, merkArray);

                            spinnerMerk.setAdapter(merkAdapter);
                            spinnerMerk.setSelection(0);

                            String[] tipeArray = new String[1];
                            tipeArray[0] = merkTipe.getNamaTipe();

                            tipeAdapter = new ArrayAdapter<String>(TransaksiActivity.this,
                                    android.R.layout.simple_spinner_item, tipeArray);

                            spinnerTipe.setAdapter(tipeAdapter);
                            spinnerTipe.setSelection(0);

                        }

                        @Override
                        public void onFailure(Call<List<MerkTipe>> call, Throwable t) {
                            dialog.dismiss();
                        }
                    });

                    tahun.setText("Tahun : " + motor.getTahun());
                    tahun.setEnabled(false);
                    tahun.setTextColor(Color.BLACK);

                    if (motor.getHjm() == null) {
                        hargaJualMinimum.setText("HJM Belum Terisi ");
                        hargaJualMinimum.setEnabled(false);
                        hargaJualMinimum.setTextColor(Color.BLACK);
                    } else {
                        hargaJualMinimum.setText(formatter(motor.getHjm() + ""));
                        hargaJualMinimum.setEnabled(false);
                        hargaJualMinimum.setTextColor(Color.BLACK);
                    }
                }


            }

            @Override
            public void onFailure(Call<List<Motor>> call, Throwable t) {
                dialog.dismiss();
                Log.v("cik", t.getMessage());

            }
        });

    }

    private void setInputMask() {

        final MaskedTextChangedListener reversedListener = new ReversedMaskTextChangedListener(
                "[000].[000].[000].[000].[000]",
                hargaJualMinimum,
                new MaskedTextChangedListener.ValueListener() {
                    @Override
                    public void onTextChanged(boolean maskFilled, @NonNull final String extractedValue) {

                        hjmMotor = extractedValue;

                    }
                }
        );

        hargaJualMinimum.addTextChangedListener(reversedListener);
        hargaJualMinimum.setOnFocusChangeListener(this);

        final MaskedTextChangedListener hargaListener = new ReversedMaskTextChangedListener(
                "[000].[000].[000].[000].[000]",
                harga,
                new MaskedTextChangedListener.ValueListener() {
                    @Override
                    public void onTextChanged(boolean maskFilled, @NonNull final String extractedValue) {

                        hargaMotor = extractedValue;

                    }
                }
        );

        harga.addTextChangedListener(hargaListener);
        harga.setOnFocusChangeListener(this);

        final MaskedTextChangedListener mediatorListener = new ReversedMaskTextChangedListener(
                "[000].[000].[000].[000].[000]",
                mediator,
                new MaskedTextChangedListener.ValueListener() {
                    @Override
                    public void onTextChanged(boolean maskFilled, @NonNull final String extractedValue) {

                        mediatorMotor = extractedValue;

                    }
                }
        );

        mediator.addTextChangedListener(mediatorListener);
        mediator.setOnFocusChangeListener(this);

        final MaskedTextChangedListener dpListener = new ReversedMaskTextChangedListener(
                "[000].[000].[000].[000].[000]",
                dp,
                new MaskedTextChangedListener.ValueListener() {
                    @Override
                    public void onTextChanged(boolean maskFilled, @NonNull final String extractedValue) {

                        dpMotor = extractedValue;

                    }
                }
        );

        dp.addTextChangedListener(dpListener);
        dp.setOnFocusChangeListener(this);

        final MaskedTextChangedListener cicilanListener = new ReversedMaskTextChangedListener(
                "[000].[000].[000].[000].[000]",
                cicilan,
                new MaskedTextChangedListener.ValueListener() {
                    @Override
                    public void onTextChanged(boolean maskFilled, @NonNull final String extractedValue) {

                        cicilanMotor = extractedValue;

                    }
                }
        );

        cicilan.addTextChangedListener(cicilanListener);
        cicilan.setOnFocusChangeListener(this);

        final MaskedTextChangedListener subsidiListener = new ReversedMaskTextChangedListener(
                "[000].[000].[000].[000].[000]",
                subsidi,
                new MaskedTextChangedListener.ValueListener() {
                    @Override
                    public void onTextChanged(boolean maskFilled, @NonNull final String extractedValue) {

                        subsidiMotor = extractedValue;

                    }
                }
        );

        subsidi.addTextChangedListener(subsidiListener);
        subsidi.setOnFocusChangeListener(this);

        final MaskedTextChangedListener pencairanLeasingListener = new ReversedMaskTextChangedListener(
                "[000].[000].[000].[000].[000]",
                pencairanLeasing,
                new MaskedTextChangedListener.ValueListener() {
                    @Override
                    public void onTextChanged(boolean maskFilled, @NonNull final String extractedValue) {

                        pencairanLeasingMotor = extractedValue;

                    }
                }
        );

        pencairanLeasing.addTextChangedListener(pencairanLeasingListener);
        pencairanLeasing.setOnFocusChangeListener(this);


    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {

        switch (view.getId()) {
            case R.id.harga_jual_minimum_trans:
                if (hasFocus && !hargaJualMinimum.getText().toString().isEmpty()) {
                    hargaJualMinimum.setText(createDot(hjmMotor) + "");
                }
                break;

            case R.id.harga:
                if (hasFocus && !harga.getText().toString().isEmpty()) {
                    harga.setText(createDot(hargaMotor) + "");
                }
                break;

            case R.id.mediator:
                if (hasFocus && !mediator.getText().toString().isEmpty()) {
                    mediator.setText(createDot(mediatorMotor) + "");
                }
                break;

            case R.id.dp:
                if (hasFocus && !dp.getText().toString().isEmpty()) {
                    dp.setText(createDot(dpMotor) + "");
                }
                break;

            case R.id.cicilan:
                if (hasFocus && !cicilan.getText().toString().isEmpty()) {
                    cicilan.setText(createDot(cicilanMotor) + "");
                }
                break;

            case R.id.subsidi:
                if (hasFocus && !subsidi.getText().toString().isEmpty()) {
                    subsidi.setText(createDot(subsidiMotor) + "");
                }
                break;

            case R.id.pencairan_leasing:
                if (hasFocus && !pencairanLeasing.getText().toString().isEmpty()) {
                    pencairanLeasing.setText(createDot(pencairanLeasingMotor) + "");
                }
                break;
        }

    }
}
