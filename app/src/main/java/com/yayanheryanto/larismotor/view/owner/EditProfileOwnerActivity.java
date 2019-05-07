package com.yayanheryanto.larismotor.view.owner;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.redmadrobot.inputmask.MaskedTextChangedListener;
import com.yayanheryanto.larismotor.R;
import com.yayanheryanto.larismotor.model.User;
import com.yayanheryanto.larismotor.retrofit.ApiClient;
import com.yayanheryanto.larismotor.retrofit.ApiInterface;
import com.yayanheryanto.larismotor.view.LoginActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.yayanheryanto.larismotor.config.config.ACCESTOKEN;
import static com.yayanheryanto.larismotor.config.config.ID_USER;
import static com.yayanheryanto.larismotor.config.config.MY_PREFERENCES;
import static com.yayanheryanto.larismotor.config.config.NAMA_USER;
import static com.yayanheryanto.larismotor.helper.HelperClass.clearDash;

public class EditProfileOwnerActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText txtNama, txtAlamat, txtNoKTP, txtNoWa, txtUsername, txtPassword;
    private User user;
    private String id;
    private Button btnSave;

    private String ktp;
    private String wa;

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_owner);

        txtNama = findViewById(R.id.nama);
        txtAlamat = findViewById(R.id.alamat);
        txtNoKTP = findViewById(R.id.no_ktp);
        txtNoWa = findViewById(R.id.no_wa);
        txtUsername = findViewById(R.id.username);
        txtPassword = findViewById(R.id.password);
        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(this);


        initProgressDialog();
        getDataOwner();
        setInputMask();

    }

    private void initProgressDialog() {
        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading");
        dialog.setMessage("Sedang Memproses..");
        dialog.setCancelable(false);
    }


    private void getDataOwner() {
        dialog.show();
        id = getIntent().getExtras().getString("ID_USER", "");
        Log.d("ID_OWNER", id);
        SharedPreferences pref = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        String token = pref.getString(ACCESTOKEN, "");
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<User> call = apiInterface.getProfile(token, id);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                dialog.dismiss();
                user = response.body();

                if (response.body().getMessage().equals("success")) {
                    txtNama.setText(user.getNama());
                    txtAlamat.setText(user.getAlamat());
                    txtNoKTP.setText(user.getNoKtpSales());
                    txtNoWa.setText(user.getNoWa());
                    txtUsername.setText(user.getUsername());
                    txtPassword.setText(user.getPassword());
                }

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                dialog.dismiss();
                t.printStackTrace();
                Toast.makeText(EditProfileOwnerActivity.this, "Terjadi Kesalahan Tidak Terduga", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void updateProfile() {
        dialog.show();
        SharedPreferences pref = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = pref.edit();
        String id = pref.getString(ID_USER, "");
        String token = pref.getString(ACCESTOKEN, "");
        final String nama = txtNama.getText().toString();
        String alamat = txtAlamat.getText().toString();
        String username = txtUsername.getText().toString();
        String password = txtPassword.getText().toString();

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<User> call = apiInterface.updateProfileOwner(token, id, nama, alamat, clearDash(wa), clearDash(ktp), username, password, user.getNoKtpSales());
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                dialog.dismiss();
                if (response.body().getMessage().equals("success")) {
                    Toast.makeText(EditProfileOwnerActivity.this, "Profile Berhasil Diubah", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EditProfileOwnerActivity.this, OwnerMenuActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    editor.putString(NAMA_USER, nama);
                    editor.commit();
                } else {
                    Toast.makeText(EditProfileOwnerActivity.this, "Token Tidak Valid, Silahkan Login", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EditProfileOwnerActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                dialog.dismiss();
                t.printStackTrace();
                Toast.makeText(EditProfileOwnerActivity.this, "Terjadi Kesalahan Tidak Terduga", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onClick(View view) {
        updateProfile();
    }

    private void setInputMask() {

        final MaskedTextChangedListener ktpListener = new MaskedTextChangedListener(
                "[0000]-[0000]-[0000]-[0000]",
                txtNoKTP,
                new MaskedTextChangedListener.ValueListener() {
                    @Override
                    public void onTextChanged(boolean maskFilled, @NonNull final String extractedValue) {

                        ktp = extractedValue;

                    }
                }
        );

        txtNoKTP.addTextChangedListener(ktpListener);
        txtNoKTP.setOnFocusChangeListener(ktpListener);

        final MaskedTextChangedListener waListener = new MaskedTextChangedListener(
                "[000]-[000]-[000]-[0009]",
                txtNoWa,
                new MaskedTextChangedListener.ValueListener() {
                    @Override
                    public void onTextChanged(boolean maskFilled, @NonNull final String extractedValue) {

                        wa = extractedValue;

                    }
                }
        );

        txtNoWa.addTextChangedListener(waListener);
        txtNoWa.setOnFocusChangeListener(waListener);


    }
}
