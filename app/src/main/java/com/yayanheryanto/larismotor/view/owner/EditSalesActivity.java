package com.yayanheryanto.larismotor.view.owner;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.redmadrobot.inputmask.MaskedTextChangedListener;
import com.yayanheryanto.larismotor.R;
import com.yayanheryanto.larismotor.view.LoginActivity;
import com.yayanheryanto.larismotor.model.Sales;
import com.yayanheryanto.larismotor.retrofit.ApiClient;
import com.yayanheryanto.larismotor.retrofit.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.yayanheryanto.larismotor.config.config.ACCESTOKEN;
import static com.yayanheryanto.larismotor.config.config.DATA_SALES;
import static com.yayanheryanto.larismotor.config.config.ID_USER;
import static com.yayanheryanto.larismotor.config.config.MY_PREFERENCES;
import static com.yayanheryanto.larismotor.helper.HelperClass.clearDash;

public class EditSalesActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etIdSales, etUsername, etPassword, etNama, etAlamat, etNoWa, etKtp;
    private Button btnSave;
    private ProgressDialog dialog;
    private Sales sales;


    private String ktp;
    private String wa;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_sales);


        etNama = findViewById(R.id.namaSales);
        etAlamat = findViewById(R.id.alamat);
        etKtp = findViewById(R.id.noKtp);
        etNoWa = findViewById(R.id.noWa);
        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);

        getFromIntent();
        initProgressDialog();
        setInputMask();
    }

    private void getFromIntent() {
        Bundle data = getIntent().getExtras();
        sales = data.getParcelable(DATA_SALES);

        etNama.setText(sales.getNama());
        etNoWa.setText(sales.getNoWa());
        etAlamat.setText(sales.getAlamat());
        etKtp.setText(sales.getNoKtpSales());
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnSave :
                updateSales();
                break;
        }
    }

    private void updateSales() {
        dialog.show();
        SharedPreferences pref = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = pref.edit();
        String id = pref.getString(ID_USER, "");
        String token = pref.getString(ACCESTOKEN, "");


        String namaSales = etNama.getText().toString();
        String alamatSales = etAlamat.getText().toString();
        String noKtpLama = sales.getNoKtpSales();

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Sales> call = apiInterface.updateSales(token,noKtpLama , namaSales, alamatSales,clearDash(wa),clearDash(ktp));
        call.enqueue(new Callback<Sales>() {
            @Override
            public void onResponse(Call<Sales> call, Response<Sales> response) {
                dialog.dismiss();
                if (response.body().getMessage().equals("success")){
                    Toast.makeText(EditSalesActivity.this, "Sales Berhasil Ditambah", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EditSalesActivity.this, SalesActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else{
                    Toast.makeText(EditSalesActivity.this, "Token Tidak Valid, Silahkan Login", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EditSalesActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<Sales> call, Throwable t) {
                dialog.dismiss();
                t.printStackTrace();
                Toast.makeText(EditSalesActivity.this, "Terjadi Kesalahan Tidak Terduga", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initProgressDialog() {
        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading");
        dialog.setMessage("Sedang Memproses..");
        dialog.setCancelable(false);
    }

    private void setInputMask(){

        final MaskedTextChangedListener ktpListener = new MaskedTextChangedListener(
                "[0000]-[0000]-[0000]-[0000]",
                etKtp,
                new MaskedTextChangedListener.ValueListener() {
                    @Override
                    public void onTextChanged(boolean maskFilled, @NonNull final String extractedValue) {

                        ktp = extractedValue;

                    }
                }
        );

        etKtp.addTextChangedListener(ktpListener);
        etKtp.setOnFocusChangeListener(ktpListener);

        final MaskedTextChangedListener waListener = new MaskedTextChangedListener(
                "[000]-[000]-[000]-[0009]",
                etNoWa,
                new MaskedTextChangedListener.ValueListener() {
                    @Override
                    public void onTextChanged(boolean maskFilled, @NonNull final String extractedValue) {

                        wa = extractedValue;

                    }
                }
        );

        etNoWa.addTextChangedListener(waListener);
        etNoWa.setOnFocusChangeListener(waListener);


    }

}
