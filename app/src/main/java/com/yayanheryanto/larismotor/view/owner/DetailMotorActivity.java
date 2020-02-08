package com.yayanheryanto.larismotor.view.owner;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yayanheryanto.larismotor.R;
import com.yayanheryanto.larismotor.adapter.MainSliderAdapter;
import com.yayanheryanto.larismotor.model.Customer;
import com.yayanheryanto.larismotor.model.Detail;
import com.yayanheryanto.larismotor.model.Motor;
import com.yayanheryanto.larismotor.retrofit.ApiClient;
import com.yayanheryanto.larismotor.retrofit.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ss.com.bannerslider.Slider;

import static android.view.View.GONE;
import static com.yayanheryanto.larismotor.config.config.DATA_MOTOR;
import static com.yayanheryanto.larismotor.helper.HelperClass.formatter;

public class DetailMotorActivity extends AppCompatActivity {

    private Slider slider;
    private TextView noMesin, noPolisi, noRangka, merk, tipe, tahun, hjm, status,
            kondisi, harga, dp, cicilan, tenor, nilaiSubsidi,
            hargaTerjual, pembeli, telpPembeli, labelSubsidi, namaSales;
    private ProgressDialog dialog;
    LinearLayout terjual, nopol, tersubsidi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_motor);

        slider = findViewById(R.id.banner_slider1);
        Slider.init(new com.yayanheryanto.larismotor.helper.PicassoImageLoadingService(this));

        noMesin = findViewById(R.id.no_mesin);
        noPolisi = findViewById(R.id.no_polisi);
        noRangka = findViewById(R.id.no_rangka);
        merk = findViewById(R.id.merk);
        tipe = findViewById(R.id.tipe);
        tahun = findViewById(R.id.tahun);
        hjm = findViewById(R.id.hjm);
        status = findViewById(R.id.status);
        kondisi = findViewById(R.id.kondisi);
        harga = findViewById(R.id.harga);
        dp = findViewById(R.id.dp);
        cicilan = findViewById(R.id.cicilan);
        tenor = findViewById(R.id.tenor);
        hargaTerjual = findViewById(R.id.harga_terjual);
        pembeli = findViewById(R.id.pembeli);
        telpPembeli = findViewById(R.id.no_telp_pembeli);
        terjual = findViewById(R.id.terjual);
        nopol = findViewById(R.id.nopol);
        nilaiSubsidi = findViewById(R.id.subsidi);
        tersubsidi = findViewById(R.id.tersubsidi);
        labelSubsidi = findViewById(R.id.label_subsidi);
        namaSales = findViewById(R.id.nama_sales);

        initProgressDialog();

        getDetail();
    }


    private void initProgressDialog() {
        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading");
        dialog.setMessage("Sedang Memproses..");
        dialog.setCancelable(false);
    }

    private void getDetail() {

        dialog.show();
        Bundle data = getIntent().getExtras();
        Motor motor = data.getParcelable(DATA_MOTOR);

        Log.v("cik",motor.getNoMesin());

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Detail> call = apiInterface.getDetail(motor.getNoMesin().toString());
        call.enqueue(new Callback<Detail>() {
            @Override
            public void onResponse(Call<Detail> call, Response<Detail> response) {
                dialog.dismiss();
                Log.v("cek",response.raw().body().toString());
                Detail motor = response.body();
                noMesin.setText(motor.getNoMesin());
                noPolisi.setText(motor.getNoPolisi());
                noRangka.setText(motor.getNoRangka());
                merk.setText(motor.getNamaMerk());
                tipe.setText(motor.getNamaTipe());
                tahun.setText("" + motor.getTahun());
                namaSales.setText(motor.getNamaSales());

                if (motor.getHjm() == null) {

                    hjm.setText("-");
                } else {
                    hjm.setText(formatter(motor.getHjm()));
                }

                harga.setText(formatter(motor.getHarga()));

                if (motor.getDp() == null || motor.getDp().equals("0")) {
                    dp.setText("-");
                } else {
                    dp.setText(formatter(motor.getDp()));
                }

                if (motor.getCicilan() == null || motor.getCicilan().equals("0")) {
                    cicilan.setText("-");
                } else {
                    cicilan.setText(formatter(motor.getCicilan()));
                }

                if (motor.getTenor() == null || motor.getTenor().equals("0")) {
                    tenor.setText("-");
                } else {
                    tenor.setText(motor.getTenor() + " Bulan");
                }


                if (motor.getStatus().equals("0")) {
                    status.setText("Tersedia");
                    terjual.setVisibility(GONE);
                    tersubsidi.setVisibility(GONE);
                } else {
                    status.setText("Sold Out");
                    terjual.setVisibility(View.VISIBLE);
                    hargaTerjual.setText(formatter(motor.getHargaTerjual()));

                    final ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                    Call<List<Customer>> calling = apiInterface.getNamaCustomerAndNoTelp(motor.getNoMesin());
                    calling.enqueue(new Callback<List<Customer>>() {
                        @Override
                        public void onResponse(Call<List<Customer>> call, Response<List<Customer>> response) {
                            if (response.body().isEmpty()) {
                                pembeli.setText("-");
                                telpPembeli.setText("-");
                            } else {
                                Customer customer = response.body().get(0);

                                pembeli.setText(customer.getNama());
                                telpPembeli.setText(customer.getNoTelp());
                            }

                        }

                        @Override
                        public void onFailure(Call<List<Customer>> call, Throwable t) {

                        }
                    });


                }

                if (motor.getKondisi().equals("1")) {
                    kondisi.setText("Baru");
                    slider.setVisibility(GONE);
                    nopol.setVisibility(GONE);

                    if (motor.getSubsidi() == null) {
                        nilaiSubsidi.setText("-");
                    } else {
                        nilaiSubsidi.setText(formatter(motor.getSubsidi()));
                    }
                    tahun.setText("Baru");

                } else {
                    kondisi.setText("Bekas");

                    labelSubsidi.setText("Pencairan Leasing");

                    if (motor.getPencairanLeasing() == null) {
                        nilaiSubsidi.setText("-");
                    } else {
                        nilaiSubsidi.setText(formatter(motor.getPencairanLeasing()));
                    }
                    slider.setAdapter(new MainSliderAdapter(motor.getGambar(), motor.getGambar1(), motor.getGambar2()));
                }


            }

            @Override
            public void onFailure(Call<Detail> call, Throwable t) {
                dialog.dismiss();
                t.printStackTrace();
                Toast.makeText(DetailMotorActivity.this, "Terjadi Kesalahan Tidak Terduga", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
