package com.yayanheryanto.larismotor.view.transaksi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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

public class TransaksiActivity extends AppCompatActivity {


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
    private boolean flagDp, flagCicilan, flagTenor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaksi);

        harga = findViewById(R.id.harga);
        dp = findViewById(R.id.dp);
        tenor = findViewById(R.id.tenor);
        cicilan = findViewById(R.id.cicilan);
        subsidi = findViewById(R.id.subsidi);
        pencairanLeasing = findViewById(R.id.pencairan_leasing);
        nomorMesin = findViewById(R.id.nomor_mesin_trans);
        nomorRangka = findViewById(R.id.nomor_rangka_trans);
        nomorPolisi = findViewById(R.id.nomor_polisi_trans);
        tahun = findViewById(R.id.tahun_trans);
        hargaJualMinimum = findViewById(R.id.harga_jual_minimum_trans);
        checklist = findViewById(R.id.cheklist);
        tanggal = findViewById(R.id.tanggal);
        spinnerMerk = findViewById(R.id.spinnerMerk);
        spinnerTipe = findViewById(R.id.spinnerTipe);
        merk = findViewById(R.id.merk);
        tipe = findViewById(R.id.tipe);
        pembayaran = findViewById(R.id.pembayaran);
        mediator = findViewById(R.id.mediator);

        initProgressDialog();


        SimpleDateFormat df = new SimpleDateFormat("EEEE, dd MMMM yyyy", new Locale("ID"));

        tanggal.setText(df.format(Calendar.getInstance().getTime()));

        final String[] mobarArray = {"Pilih Kondisi", "Mobar", "Mokas"};
        final Spinner spinnerMobar = findViewById(R.id.mobar);
        ArrayAdapter<String> mobar = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, mobarArray);
        spinnerMobar.setAdapter(mobar);
        spinnerMobar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                statusMotor = mobarArray[position];
                clearAll();
                spinnerCaraBayar.setSelection(0);

                if (position == 0) {

                    nomorMesin.setVisibility(GONE);
                    nomorRangka.setVisibility(GONE);
                    nomorPolisi.setVisibility(GONE);
                    spinnerMerk.setVisibility(GONE);
                    spinnerTipe.setVisibility(GONE);
                    tahun.setVisibility(GONE);
                    hargaJualMinimum.setVisibility(GONE);
                    spinnerCaraBayar.setVisibility(GONE);
                    harga.setVisibility(GONE);
                    checklist.setVisibility(GONE);
                    merk.setVisibility(GONE);
                    tipe.setVisibility(GONE);
                    pembayaran.setVisibility(GONE);
                    dp.setVisibility(GONE);
                    cicilan.setVisibility(GONE);
                    tenor.setVisibility(GONE);
                    pencairanLeasing.setVisibility(GONE);
                    subsidi.setVisibility(GONE);

                } else if (position == 1) {
                    nomorMesin.setVisibility(View.VISIBLE);
                    nomorRangka.setVisibility(View.VISIBLE);
                    nomorRangka.setEnabled(true);
                    nomorPolisi.setVisibility(GONE);
                    merk.setVisibility(View.VISIBLE);
                    spinnerMerk.setVisibility(View.VISIBLE);
                    spinnerMerk.setEnabled(true);
                    tipe.setVisibility(View.VISIBLE);
                    spinnerTipe.setVisibility(View.VISIBLE);
                    spinnerTipe.setEnabled(true);
                    tahun.setVisibility(View.VISIBLE);
                    tahun.setEnabled(true);
                    hargaJualMinimum.setVisibility(View.VISIBLE);
                    pembayaran.setVisibility(View.VISIBLE);
                    spinnerCaraBayar.setVisibility(View.VISIBLE);
                    hargaJualMinimum.setEnabled(true);
                    checklist.setVisibility(GONE);
                    kondisi = 1;
                    getMerk();


                } else {
                    nomorMesin.setVisibility(View.VISIBLE);
                    nomorRangka.setVisibility(View.VISIBLE);
                    nomorPolisi.setVisibility(View.VISIBLE);
                    merk.setVisibility(View.VISIBLE);
                    spinnerMerk.setVisibility(View.VISIBLE);
                    spinnerMerk.setEnabled(false);
                    tipe.setVisibility(View.VISIBLE);
                    spinnerTipe.setVisibility(View.VISIBLE);
                    spinnerTipe.setEnabled(false);
                    nomorRangka.setEnabled(false);
                    tahun.setVisibility(View.VISIBLE);
                    tahun.setEnabled(false);
                    hargaJualMinimum.setVisibility(View.VISIBLE);
                    hargaJualMinimum.setHint(R.string.lbl_hjm);
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

        spinnerCaraBayar = findViewById(R.id.cara_bayar);
        ArrayAdapter<CharSequence> caraBayar = ArrayAdapter.createFromResource(this,
                R.array.caraBayar, android.R.layout.simple_spinner_item);


        spinnerCaraBayar.setAdapter(caraBayar);
        spinnerCaraBayar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {
                    harga.setVisibility(View.GONE);
                    dp.setVisibility(GONE);
                    tenor.setVisibility(GONE);
                    cicilan.setVisibility(GONE);
                    subsidi.setVisibility(View.GONE);
                    pencairanLeasing.setVisibility(View.GONE);
                    mediator.setVisibility(GONE);
                } else if (position == 1) {
                    harga.setVisibility(View.VISIBLE);
                    mediator.setVisibility(View.VISIBLE);
                    dp.setVisibility(GONE);
                    tenor.setVisibility(GONE);
                    cicilan.setVisibility(GONE);
                    subsidi.setVisibility(GONE);
                    pencairanLeasing.setVisibility(GONE);
                } else {

                    harga.setVisibility(GONE);
                    mediator.setVisibility(View.VISIBLE);
                    dp.setVisibility(View.VISIBLE);
                    tenor.setVisibility(View.VISIBLE);
                    cicilan.setVisibility(View.VISIBLE);

                    dp.setEnabled(true);
                    tenor.setEnabled(true);
                    cicilan.setEnabled(true);

                    if (spinnerMobar.getSelectedItemPosition() == 1) {
                        subsidi.setVisibility(View.VISIBLE);
                    } else {
                        pencairanLeasing.setVisibility(View.VISIBLE);
                    }

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        checklist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.show();

                String stringNomorMesin = nomorMesin.getText().toString();
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
                                flagDp = false;
                            } else {
                                dp.setText("DP       : Rp. " + motor.getDp());
                                dp.setEnabled(false);
                                dp.setTextColor(Color.BLACK);
                                flagDp = true;
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
                                flagCicilan = false;
                            } else {
                                cicilan.setText("Cicilan : Rp. " + motor.getCicilan());
                                cicilan.setEnabled(false);
                                cicilan.setTextColor(Color.BLACK);
                                flagCicilan = true;
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
                                hargaJualMinimum.setText("HJM    : Rp." + motor.getHjm());
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
        });

        TextView textView = findViewById(R.id.next_trans);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });

    }

    private void getData() {

        Motor motor = new Motor();
        String noMesin, noRangka, tahunMotor, hjm, dpMotor, cicilanMotor,
                tenorMotor, hargaTerjual, subsidiMotor, pencairanLeasingMotor, mediatorMotor;

        if (kondisi == 0) {
            noMesin = nomorMesin.getText().toString();

            if (spinnerCaraBayar.getSelectedItemPosition() == 1) {
                hargaTerjual = harga.getText().toString();
                motor.setHargaTerjual(Integer.valueOf(hargaTerjual));
            } else {
                dpMotor = dp.getText().toString();
                cicilanMotor = cicilan.getText().toString();
                tenorMotor = tenor.getText().toString();
                pencairanLeasingMotor = pencairanLeasing.getText().toString();


                if (flagDp) {
                    motor.setDp(Integer.valueOf(dpMotor.substring(15)));
                } else {
                    motor.setDp(Integer.valueOf(dpMotor));

                }

                if (flagCicilan) {
                    motor.setCicilan(Integer.valueOf(cicilanMotor.substring(15)));
                } else {
                    motor.setCicilan(Integer.valueOf(cicilanMotor));
                }

                if (flagTenor) {
                    motor.setTenor(Integer.valueOf(tenorMotor.substring(16)));
                } else {
                    motor.setTenor(Integer.valueOf(tenorMotor));
                }

                motor.setPencairanLeasing(Integer.valueOf(pencairanLeasingMotor));
            }

            motor.setNoMesin(noMesin);
            motor.setKondisi(kondisi);
            mediatorMotor = mediator.getText().toString();
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
            hjm = hargaJualMinimum.getText().toString();

            motor.setNoMesin(noMesin);
            motor.setNoRangka(noRangka);
            motor.setTahun(Integer.valueOf(tahunMotor));
            motor.setHjm(Integer.valueOf(hjm));
            motor.setKondisi(kondisi);
            motor.setIdTipe(idTipe);
            motor.setIdMerk(idMerk);

            if (spinnerCaraBayar.getSelectedItemPosition() == 1) {
                hargaTerjual = harga.getText().toString();
                motor.setHargaTerjual(Integer.valueOf(hargaTerjual));
                mediatorMotor = mediator.getText().toString();
                motor.setMediator(Integer.valueOf(mediatorMotor));
            } else {

                dpMotor = dp.getText().toString();
                cicilanMotor = cicilan.getText().toString();
                tenorMotor = tenor.getText().toString();
                subsidiMotor = subsidi.getText().toString();
                mediatorMotor = mediator.getText().toString();


                motor.setDp(Integer.valueOf(dpMotor));
                motor.setCicilan(Integer.valueOf(cicilanMotor));
                motor.setTenor(Integer.valueOf(tenorMotor));
                motor.setSubsidi(Integer.valueOf(subsidiMotor));
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


}
