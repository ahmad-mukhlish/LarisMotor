package com.yayanheryanto.larismotor.view.owner;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
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

import static com.yayanheryanto.larismotor.config.config.ACCESTOKEN;
import static com.yayanheryanto.larismotor.config.config.MY_PREFERENCES;
import static com.yayanheryanto.larismotor.helper.HelperClass.formatter;

public class InsentifHasilOwnerActivity extends AppCompatActivity {

    private EditText lainTxt, persentaseMobarTxt;
    private ImageView edit, hapus, editPm, hapusPm;
    private boolean klik, klikPm;
    private TextView salesTxt, dariTxt, hinggaTxt,
            jumlahMokasTxt, jumlahMobarTxt, nominalMobarTxt, nominalMokasTxt, persentaseMokasTxt, totalTxt;

    private View batas, batasPm;
    private RelativeLayout holder, holderMargin, holderPm, holderMarginPm;
    private int jumlahMobar, jumlahMokas, lain, persentaseMobar, persentaseMokas, nominalMobar, nominalMokas;
    private String dariSql, hinggaSql;
    private Sales salesNow;
    private KonfigInsentif konfigInsentif;

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insentif_hasil_owner);

        initProgressDialog();

        Bundle bundle = getIntent().getExtras();
        salesNow = bundle.getParcelable("sales");
        dariSql = bundle.getString("dariSql");
        hinggaSql = bundle.getString("hinggaSql");

        persentaseMobarTxt = findViewById(R.id.persentase_mobar);
        lainTxt = findViewById(R.id.lain);

        edit = findViewById(R.id.edit);
        hapus = findViewById(R.id.hapus);

        editPm = findViewById(R.id.edit_pm);
        hapusPm = findViewById(R.id.hapus_pm);

        salesTxt = findViewById(R.id.sales);
        dariTxt = findViewById(R.id.dari);
        hinggaTxt = findViewById(R.id.hingga);

        batas = findViewById(R.id.batas);
        holder = findViewById(R.id.holder);
        holderMargin = findViewById(R.id.holder_margin);

        batasPm = findViewById(R.id.batas_pm);
        holderPm = findViewById(R.id.holder_pm);
        holderMarginPm = findViewById(R.id.holder_margin_pm);

        jumlahMobarTxt = findViewById(R.id.jumlah_mobar);
        jumlahMokasTxt = findViewById(R.id.jumlah_mokas);
        nominalMobarTxt = findViewById(R.id.nominal_mobar);
        nominalMokasTxt = findViewById(R.id.nominal_mokas);

        persentaseMokasTxt = findViewById(R.id.persentase_mokas);
        totalTxt = findViewById(R.id.total);


        salesTxt.setText(salesNow.getNama());
        dariTxt.setText(bundle.getString("dari"));
        hinggaTxt.setText(bundle.getString("hingga"));

        klik = false;
        klikPm = false;

        setMargins(holderMargin, 0, 2, 2, 2);
        setMargins(holderMarginPm, 0, 2, 2, 2);


        holderMargin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!klik) {
                    lainTxt.setEnabled(true);
                    lainTxt.requestFocus();
                    lainTxt.setSelection(lainTxt.getText().length());
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
                    lainTxt.setEnabled(false);
                    lainTxt.clearFocus();
                    simpanLain();
                    klik = false;

                }

            }
        });

        hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                lainTxt.setText("0");

            }
        });

        holderMarginPm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!klikPm) {
                    persentaseMobarTxt.setEnabled(true);
                    persentaseMobarTxt.requestFocus();
                    persentaseMobarTxt.setSelection(persentaseMobarTxt.getText().length());
                    editPm.setImageResource(R.drawable.ic_save_256);
                    batasPm.setVisibility(View.VISIBLE);
                    holderPm.setVisibility(View.VISIBLE);
                    klikPm = true;
                    setMargins(holderMarginPm, 0, 2, 2, 0);

                } else {
                    setMargins(holderMarginPm, 0, 2, 2, 2);
                    editPm.setImageResource(R.drawable.ic_pencil_edit_button);
                    batasPm.setVisibility(View.GONE);
                    holderPm.setVisibility(View.GONE);
                    persentaseMobarTxt.setEnabled(false);
                    persentaseMobarTxt.clearFocus();
                    simpanPersentaseMobar();
                    klikPm = false;

                }

            }
        });

        hapusPm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                persentaseMobarTxt.setText("0");

            }
        });

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

    private void simpanLain() {

        dialog.show();
        SharedPreferences pref = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        String token = pref.getString(ACCESTOKEN, "");

        String lainParam = lainTxt.getText().toString();
        lain = Integer.parseInt(lainParam);

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<KonfigInsentif> call = apiInterface.updateLain(token, salesNow.getNoKtpSales(), lainParam);

        call.enqueue(new Callback<KonfigInsentif>() {
            @Override
            public void onResponse(Call<KonfigInsentif> call, Response<KonfigInsentif> response) {
                dialog.dismiss();
                if (response.body().getMessage().equals("success")) {
                    getTotal();
                    Toast.makeText(InsentifHasilOwnerActivity.this, "Data Telah Disubmit", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(InsentifHasilOwnerActivity.this, "Token Tidak Valid, Silahkan Login", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<KonfigInsentif> call, Throwable t) {
                dialog.dismiss();
                t.printStackTrace();
                Toast.makeText(InsentifHasilOwnerActivity.this, "Terjadi Kesalahan Tidak Terduga", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void simpanPersentaseMobar() {
        dialog.show();
        SharedPreferences pref = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        String token = pref.getString(ACCESTOKEN, "");

        String persentaseMobarParam = persentaseMobarTxt.getText().toString();
        persentaseMobar = Integer.parseInt(persentaseMobarParam);

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<KonfigInsentif> call = apiInterface.updatePersentaseMobar(token, salesNow.getNoKtpSales(), persentaseMobarParam);

        call.enqueue(new Callback<KonfigInsentif>() {
            @Override
            public void onResponse(Call<KonfigInsentif> call, Response<KonfigInsentif> response) {
                dialog.dismiss();
                if (response.body().getMessage().equals("success")) {
                    getTotal();
                    Toast.makeText(InsentifHasilOwnerActivity.this, "Data Telah Disubmit", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(InsentifHasilOwnerActivity.this, "Token Tidak Valid, Silahkan Login", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<KonfigInsentif> call, Throwable t) {
                dialog.dismiss();
                t.printStackTrace();
                Toast.makeText(InsentifHasilOwnerActivity.this, "Terjadi Kesalahan Tidak Terduga", Toast.LENGTH_SHORT).show();
            }
        });
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
                getPersentaseMokas();
                getTotal();
                dialog.dismiss();


            }

            @Override
            public void onFailure(Call<List<KonfigInsentif>> call, Throwable t) {
                dialog.dismiss();
                t.printStackTrace();
                Toast.makeText(InsentifHasilOwnerActivity.this, "Terjadi Kesalahan Tidak Terduga", Toast.LENGTH_SHORT).show();
            }


        });
    }

    private void getNominalMobar() {

        nominalMobar = jumlahMobar * Integer.parseInt(konfigInsentif.getInsentifMobar());
        nominalMobarTxt.setText(formatter(nominalMobar + ""));
        getTotal();

    }

    private void getNominalMokas() {


        if (jumlahMokas < 6) {
            nominalMokas = jumlahMokas * Integer.parseInt(konfigInsentif.getInsentifMokas15());
        } else if (jumlahMokas > 5 && jumlahMokas < 11) {
            nominalMokas = jumlahMokas * Integer.parseInt(konfigInsentif.getInsentifMokas610());
        } else if (jumlahMokas > 10 && jumlahMokas < 16) {
            nominalMokas = jumlahMokas * Integer.parseInt(konfigInsentif.getInsentifMokas1115());
        } else if (jumlahMokas > 15 && jumlahMokas < 21) {
            nominalMokas = jumlahMokas * Integer.parseInt(konfigInsentif.getInsentifMokas1620());
        } else if (jumlahMokas > 20) {
            nominalMokas = jumlahMokas * Integer.parseInt(konfigInsentif.getInsentifMokas21());
        }

        nominalMokasTxt.setText(formatter(nominalMokas + ""));
        getTotal();


    }

    public void getLain() {


        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Integer> call = apiInterface.getLain(salesNow.getIdUser() + "");
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                lain = response.body();
                lainTxt.setText(lain + "");
                getTotal();
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        });

    }

    public void getPersentaseMobar() {


        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Integer> call = apiInterface.getPersentaseMobar(salesNow.getIdUser() + "");
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                persentaseMobar = response.body();
                persentaseMobarTxt.setText(persentaseMobar + "");
                getTotal();
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        });

    }

    public void getPersentaseMokas() {


        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Integer> call = apiInterface.getPersentaseMokas(salesNow.getIdUser() + "", dariSql, hinggaSql);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                persentaseMokas = response.body();
                persentaseMokas = persentaseMokas * Integer.parseInt(konfigInsentif.getPersentase()) / 100;
                persentaseMokasTxt.setText(formatter(persentaseMokas + ""));
                getTotal();
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {

            }
        });

    }

    public void getTotal() {


        int total = 0;
        total = nominalMobar + nominalMokas + persentaseMokas + persentaseMobar + lain;

        Log.v("cek1", nominalMobar + "");
        Log.v("cek2", nominalMokas + "");
        Log.v("cek3", persentaseMokas + "");
        Log.v("cek4", persentaseMobar + "");
        Log.v("cek5", lain + "");


        totalTxt.setText(formatter(total+""));

    }


}
