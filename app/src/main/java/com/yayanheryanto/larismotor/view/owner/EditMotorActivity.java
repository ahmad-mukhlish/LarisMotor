package com.yayanheryanto.larismotor.view.owner;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.hawk.Hawk;
import com.redmadrobot.inputmask.MaskedTextChangedListener;
import com.redmadrobot.inputmask.ReversedMaskTextChangedListener;
import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;
import com.yayanheryanto.larismotor.R;
import com.yayanheryanto.larismotor.model.Merk;
import com.yayanheryanto.larismotor.model.MerkTipe;
import com.yayanheryanto.larismotor.model.Motor;
import com.yayanheryanto.larismotor.model.Tipe;
import com.yayanheryanto.larismotor.retrofit.ApiClient;
import com.yayanheryanto.larismotor.retrofit.ApiInterface;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import id.zelory.compressor.Compressor;
import in.myinnos.awesomeimagepicker.activities.AlbumSelectActivity;
import in.myinnos.awesomeimagepicker.helpers.ConstantsCustomGallery;
import in.myinnos.awesomeimagepicker.models.Image;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.yayanheryanto.larismotor.config.config.ACCESTOKEN;
import static com.yayanheryanto.larismotor.config.config.BASE_URL;
import static com.yayanheryanto.larismotor.config.config.DATA_MOTOR;
import static com.yayanheryanto.larismotor.config.config.DEBUG;
import static com.yayanheryanto.larismotor.config.config.ID_USER;
import static com.yayanheryanto.larismotor.config.config.MY_PREFERENCES;
import static com.yayanheryanto.larismotor.helper.HelperClass.clearDot;
import static com.yayanheryanto.larismotor.helper.HelperClass.createDot;
import static com.yayanheryanto.larismotor.view.sales.AddMotorSalesActivity.checkImageResource;


public class EditMotorActivity extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {

    private Button btnUpload, btnSave, btnCamera;
    private List<Merk> merk;
    private List<Tipe> tipe;
    private Spinner spinnerMerk, spinnerTipe;
    private ArrayAdapter<String> adapter, adapter2;
    private ImageView image1, image2, image3;
    private ArrayList<Image> images;
    private EditText no_mesin, no_polisi, no_rangka, tahun, hjm, harga, harga_terjual, dp, cicilan, tenor;
    private RadioGroup status;
    private int merkMotor, tipeMotor;
    private ProgressDialog dialog;
    private Motor motor;
    private String s1, s2;
    private TextInputLayout terjual;
    private File file, file2 = null;
    private RadioButton sold;

    private final int CAMERA_REQUEST = 110;
    private final int READ_EXTERNAL_STORAGE = 123;

    private Uri tempUri;
    boolean buttonCamera, buttonGallery = false;

    private String hjmMotor;
    private String hargaMotor;
    private String hargaTerjual;
    private String dpMotor;
    private String cicilanMotor;

    private TextView labelDepan;
    private TextView labelSamping;
    private TextView labelBelakang;
    private TextView labelAmbil;

    private Uri uri1;
    private Uri uri2;
    private Uri uri3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_motor);

        btnCamera = findViewById(R.id.btnCamera);
        btnUpload = findViewById(R.id.btnImage);
        btnSave = findViewById(R.id.btnSave);
        spinnerMerk = findViewById(R.id.spinner1);
        spinnerTipe = findViewById(R.id.spinner2);
        no_mesin = findViewById(R.id.no_mesin);
        no_polisi = findViewById(R.id.no_polisi);
        no_rangka = findViewById(R.id.no_rangka);
        tahun = findViewById(R.id.tahun);
        hjm = findViewById(R.id.hjm);
        status = findViewById(R.id.status);
        harga = findViewById(R.id.harga);
        harga_terjual = findViewById(R.id.harga_terjual);
        cicilan = findViewById(R.id.cicilan);
        tenor = findViewById(R.id.tenor);
        dp = findViewById(R.id.dp);
        terjual = findViewById(R.id.terjual);
        sold = findViewById(R.id.radio_sold_out);

        labelAmbil = findViewById(R.id.label_ambil);
        labelDepan = findViewById(R.id.label_depan);
        labelSamping = findViewById(R.id.label_samping);
        labelBelakang = findViewById(R.id.label_belakang);

        image1 = findViewById(R.id.image1);
        image2 = findViewById(R.id.image2);
        image3 = findViewById(R.id.image3);

        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditMotorActivity.this, AlbumSelectActivity.class);
                intent.putExtra(ConstantsCustomGallery.INTENT_EXTRA_LIMIT, 1); // set limit for image selection
                startActivityForResult(intent, ConstantsCustomGallery.REQUEST_CODE);
                Hawk.put("codeImage", 1);
            }
        });

        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditMotorActivity.this, AlbumSelectActivity.class);
                intent.putExtra(ConstantsCustomGallery.INTENT_EXTRA_LIMIT, 1); // set limit for image selection
                startActivityForResult(intent, ConstantsCustomGallery.REQUEST_CODE);
                Hawk.put("codeImage", 2);
            }
        });

        image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditMotorActivity.this, AlbumSelectActivity.class);
                intent.putExtra(ConstantsCustomGallery.INTENT_EXTRA_LIMIT, 1); // set limit for image selection
                startActivityForResult(intent, ConstantsCustomGallery.REQUEST_CODE);
                Hawk.put("codeImage", 3);
            }
        });

        initProgressDialog();

        getDataFromIntent();

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line);
        adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line);


        getMerkById(String.valueOf(motor.getIdMerk()), String.valueOf(motor.getIdTipe()));
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

        btnUpload.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnCamera.setOnClickListener(this);

        setInputMask();
        setTitle("Edit Motor");
        Hawk.init(this).build();

    }

    private void getDataFromIntent() {

        Bundle data = getIntent().getExtras();
        motor = (Motor) data.getParcelable(DATA_MOTOR);
        no_mesin.setText(motor.getNoMesin());

        if (motor.getNoPolisi() == null) {
            no_polisi.setText("");
        } else {
            no_polisi.setText("" + motor.getNoPolisi());
        }


        no_rangka.setText(motor.getNoRangka());

        hjmMotor = motor.getHjm() + "";
        hargaMotor = motor.getHarga() + "";
        hargaTerjual = motor.getHargaTerjual() + "";
        dpMotor = motor.getDp() + "";
        cicilanMotor = motor.getCicilan() + "";

        if (motor.getHjm() == null || motor.getHjm() == 0) {
            hjm.setText("HJM belum terisi");
        } else {
            hjm.setText(createDot(hjmMotor));
        }

        tahun.setText("" + motor.getTahun());
        harga.setText(createDot(hargaMotor));

        if (motor.getHargaTerjual() == null || motor.getHargaTerjual() == 0) {
            harga_terjual.setText("");
        } else {
            harga_terjual.setText(createDot(hargaTerjual));
        }

        if (motor.getDp() == null || motor.getDp() == 0) {
            dp.setText("");
        } else {
            dp.setText(createDot(dpMotor));
        }

        if (motor.getCicilan() == null || motor.getCicilan() == 0) {
            cicilan.setText("");
        } else {
            cicilan.setText(createDot(cicilanMotor));
        }

        if (motor.getTenor() == null || motor.getTenor() == 0) {
            tenor.setText("");
        } else {
            tenor.setText("" + motor.getTenor());
        }


        if (motor.getStatus().equals(0)) {
            status.check(R.id.radio_available);
            terjual.setVisibility(View.GONE);

        } else {
            status.check(R.id.radio_sold_out);
            terjual.setVisibility(View.VISIBLE);
        }

        status.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (status.getCheckedRadioButtonId() == R.id.radio_available) {
                    terjual.setVisibility(View.GONE);
                } else {
                    terjual.setVisibility(View.VISIBLE);
                }

            }
        });


        if (motor.getGambar() != null) {
            Picasso.get().load(BASE_URL + "storage/motor/" + motor.getGambar()).into(image1);
        }
        if (motor.getGambar1() != null) {
            Picasso.get().load(BASE_URL + "storage/motor/" + motor.getGambar1()).into(image2);
        }
        if (motor.getGambar2() != null) {
            Picasso.get().load(BASE_URL + "storage/motor/" + motor.getGambar2()).into(image3);
        }

        if (data.getBoolean("ada")) {
            freeze();
        }

    }

    private void initProgressDialog() {
        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading");
        dialog.setMessage("Sedang Memproses..");
        dialog.setCancelable(false);
    }

    private void freeze() {

        no_mesin.setEnabled(false);
        no_mesin.setTextColor(Color.BLACK);

        no_polisi.setEnabled(false);
        no_polisi.setTextColor(Color.BLACK);

        no_rangka.setEnabled(false);
        no_rangka.setTextColor(Color.BLACK);

        tahun.setEnabled(false);
        tahun.setTextColor(Color.BLACK);

        harga.setText("");
        hjm.setText("");
        dp.setText("");
        cicilan.setText("");
        tenor.setText("");

        status.check(R.id.radio_available);
        terjual.setVisibility(View.GONE);
        status.setEnabled(false);
        sold.setEnabled(false);


    }

    private void getMerk() {
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<Merk>> call = apiInterface.getMerk();
        call.enqueue(new Callback<List<Merk>>() {
            @Override
            public void onResponse(Call<List<Merk>> call, Response<List<Merk>> response) {
                Log.d(DEBUG, String.valueOf(response.body().size()));
                merk = response.body();
                if (merk != null) {
                    for (Merk merkMotor : merk) {
                        adapter.add(merkMotor.getNamaMerk());

                    }
                    Log.d(DEBUG, s1 + " " + s2);
                    spinnerMerk.setAdapter(adapter);
                    spinnerMerk.setSelection(adapter.getPosition(s1));
                }
            }

            @Override
            public void onFailure(Call<List<Merk>> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(EditMotorActivity.this, "Terjadi Kesalahan Tidak Terduga", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(EditMotorActivity.this, "Terjadi Kesalahan Tidak Terduga", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(EditMotorActivity.this, "Terjadi Kesalahan Tidak Terduga", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnImage:
                buttonGallery = true;
                Intent intent = new Intent(this, AlbumSelectActivity.class);
                intent.putExtra(ConstantsCustomGallery.INTENT_EXTRA_LIMIT, 3); // set limit for image selection
                startActivityForResult(intent, ConstantsCustomGallery.REQUEST_CODE);
                break;

            case R.id.btnSave:
                uploadImage();
                break;


            case R.id.btnCamera: {
                buttonCamera = true;
                if (Build.VERSION.SDK_INT >= 23) {
                    if (!Settings.System.canWrite(this)) {
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE);
                    } else {
                        request();
                    }
                } else {
                    goToCamera();
                }
            }

            break;

        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ConstantsCustomGallery.REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            images = data.getParcelableArrayListExtra(ConstantsCustomGallery.INTENT_EXTRA_IMAGES);
            Uri uri = Uri.fromFile(new File(images.get(0).path));

            int codeImage = Hawk.get("codeImage");
            switch (codeImage) {

                case 1: {
                    UCrop.of(uri, uri)
                            .withAspectRatio(16, 9)
                            .withMaxResultSize(1024, 1024)
                            .start(this, 202);
                    break;
                }

                case 2: {
                    UCrop.of(uri, uri)
                            .withAspectRatio(16, 9)
                            .withMaxResultSize(1024, 1024)
                            .start(this, 203);
                    break;
                }

                case 3: {
                    UCrop.of(uri, uri)
                            .withAspectRatio(16, 9)
                            .withMaxResultSize(1024, 1024)
                            .start(this, 204);
                    break;
                }

            }


        } else if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {

            Bitmap photo = (Bitmap) data.getExtras().get("data");
            image1.setImageBitmap(photo);
            tempUri = getImageUri(getApplicationContext(), photo);
            file = new File(getRealPathFromURI(tempUri));

        } else if (resultCode == RESULT_OK && requestCode == 202) {

            Hawk.put("uri1", UCrop.getOutput(data));
            Log.v("cikan", UCrop.getOutput(data).toString());
            uri1 = Hawk.get("uri1");
            Log.v("cikan", uri1.toString());
            image1.setImageURI(uri1);

        } else if (resultCode == RESULT_OK && requestCode == 203) {

            Hawk.put("uri2", UCrop.getOutput(data));
            uri2 = Hawk.get("uri2");
            image2.setImageURI(uri2);

        } else if (resultCode == RESULT_OK && requestCode == 204) {

            Hawk.put("uri3", UCrop.getOutput(data));
            uri3 = Hawk.get("uri3");
            image3.setImageURI(uri3);

        }
    }

    private void goToCamera() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_REQUEST);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (tempUri != null) {
            outState.putString("cameraImageUri", tempUri.toString());
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.containsKey("cameraImageUri")) {
            tempUri = Uri.parse(savedInstanceState.getString("cameraImageUri"));
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, timeStamp, null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        String path = "";
        if (getContentResolver() != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }

    private void uploadImage() {
        if (checkImageResource(this, image1, R.drawable.motorbike) || checkImageResource(this, image2, R.drawable.motorbike)
                || checkImageResource(this, image3, R.drawable.motorbike)
        ) {
            Toast.makeText(this, "Gambar Motor Belum Dimasukan", Toast.LENGTH_SHORT).show();
        } else {
            dialog.show();
            SharedPreferences pref = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
            String token = pref.getString(ACCESTOKEN, "");
            String id = pref.getString(ID_USER, "");

            int selectedId = status.getCheckedRadioButtonId();
            RadioButton radioButton = (RadioButton) findViewById(selectedId);
            String tersedia = radioButton.getText().toString();
            String statusMotor = "1";
            if (tersedia.equalsIgnoreCase("tersedia")) {
                statusMotor = "0";
            }

            String mesin = no_mesin.getText().toString();
            String polisi = no_polisi.getText().toString();
            String rangka = no_rangka.getText().toString();
            String tahunMotor = tahun.getText().toString();
            String tenorMotor = tenor.getText().toString();


            MultipartBody.Builder builder = new MultipartBody.Builder();
            builder.setType(MultipartBody.FORM);
            builder.addFormDataPart("no_mesin_awal", motor.getNoMesin());
            builder.addFormDataPart("no_polisi", polisi);
            builder.addFormDataPart("no_mesin", mesin);
            builder.addFormDataPart("no_rangka", rangka);
            builder.addFormDataPart("hjm", clearDot(hjmMotor));
            builder.addFormDataPart("tahun", tahunMotor);
            builder.addFormDataPart("status", statusMotor);
            builder.addFormDataPart("tipe", String.valueOf(tipeMotor));
            builder.addFormDataPart("merk", String.valueOf(merkMotor));
            builder.addFormDataPart("id_user", id);
            builder.addFormDataPart("harga", clearDot(hargaMotor));
            builder.addFormDataPart("harga_terjual", clearDot(hargaTerjual));
            builder.addFormDataPart("dp", clearDot(dpMotor));
            builder.addFormDataPart("cicilan", clearDot(cicilanMotor));
            builder.addFormDataPart("tenor", tenorMotor);


            if (motor.getGambar() != null) {
                builder.addFormDataPart("gambar", motor.getGambar());
            }
            if (motor.getGambar1() != null) {
                builder.addFormDataPart("gambar1", motor.getGambar1());
            }
            if (motor.getGambar2() != null) {
                builder.addFormDataPart("gambar2", motor.getGambar2());
            }

//

            for (int i = 0; i < 3; i++) {

                if (i == 0) {
                    if (uri1 != null) {
                        file2 = new File(uri1.getPath());
                    }
                } else if (i == 1) {
                    if (uri2 != null) {
                        file2 = new File(uri2.getPath());
                    }
                } else {
                    if (uri3 != null) {
                        file2 = new File(uri3.getPath());
                    }
                }

                if (file2 != null) {
                    try {
                        file = new Compressor(this).compressToFile(file2);

                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("Error", e.getMessage());
                    }
                    builder.addFormDataPart("file[]", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file));
                    Log.d(DEBUG, file.getName());
                }
            }

//            }
            final MultipartBody requestBody = builder.build();
            ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
            Call<Motor> call = apiInterface.updateMotor(token, requestBody);
            call.enqueue(new Callback<Motor>() {
                @Override
                public void onResponse(Call<Motor> call, Response<Motor> response) {
                    dialog.dismiss();

                    try {
                        Log.v("coba", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

//                    if (response.body().getMessage().equals("success")) {
//                        Toast.makeText(EditMotorActivity.this, "Data Motor Berhasil Diubah", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(EditMotorActivity.this, MotorActivity.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent);
//                    } else {
//                        Toast.makeText(EditMotorActivity.this, "Token Tidak Valid, Silahkan Login", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(EditMotorActivity.this, LoginActivity.class);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent);
//                    }
                }

                @Override
                public void onFailure(Call<Motor> call, Throwable t) {
                    dialog.dismiss();
                    t.printStackTrace();
                    Toast.makeText(EditMotorActivity.this, "Terjadi kesalahan Tidak Terduga", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void request() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST);
        } else {
            goToCamera();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case READ_EXTERNAL_STORAGE: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    request();
                } else {
                    Toast.makeText(this, "Izin akses pada eksternal memori ditolak", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            case CAMERA_REQUEST: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    goToCamera();
                } else {
                    Toast.makeText(this, "Izin akses pada kamera ditolak", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    private void setInputMask() {

        final MaskedTextChangedListener reversedListener = new ReversedMaskTextChangedListener(
                "[000].[000].[000].[000].[000]",
                harga,
                new MaskedTextChangedListener.ValueListener() {
                    @Override
                    public void onTextChanged(boolean maskFilled, @NonNull final String extractedValue) {

                        hargaMotor = extractedValue;

                    }
                }
        );

        harga.addTextChangedListener(reversedListener);
        harga.setOnFocusChangeListener(this);

        final MaskedTextChangedListener terjualListener = new ReversedMaskTextChangedListener(
                "[000].[000].[000].[000].[000]",
                harga_terjual,
                new MaskedTextChangedListener.ValueListener() {
                    @Override
                    public void onTextChanged(boolean maskFilled, @NonNull final String extractedValue) {

                        hargaTerjual = extractedValue;

                    }
                }
        );

        harga_terjual.addTextChangedListener(terjualListener);
        harga_terjual.setOnFocusChangeListener(this);

        final MaskedTextChangedListener hjmListener = new ReversedMaskTextChangedListener(
                "[000].[000].[000].[000].[000]",
                hjm,
                new MaskedTextChangedListener.ValueListener() {
                    @Override
                    public void onTextChanged(boolean maskFilled, @NonNull final String extractedValue) {

                        hjmMotor = extractedValue;

                    }
                }
        );

        hjm.addTextChangedListener(hjmListener);
        hjm.setOnFocusChangeListener(this);


        final MaskedTextChangedListener dpListener = new ReversedMaskTextChangedListener(
                "[000].[000].[000].[000].[000]",
                dp,
                new MaskedTextChangedListener.ValueListener() {
                    @Override
                    public void onTextChanged(boolean maskFilled, @NonNull final String extractedValue) {

                        dpMotor = extractedValue;
//                        Log.v("cik2",clearDot(dpMotor));

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
    }


    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        switch (view.getId()) {
            case R.id.harga:
                if (hasFocus) {
                    harga.setText(createDot(hargaMotor) + "");
                }
                break;

            case R.id.hjm:
                if (hasFocus && !(motor.getHjm() == null)) {
                    hjm.setText(createDot(hjmMotor) + "");
                }
                break;

            case R.id.harga_terjual:
                if (hasFocus && !(motor.getHargaTerjual() == null)) {
                    harga_terjual.setText(createDot(hargaTerjual));
                }
                break;

            case R.id.dp:
                if (hasFocus && !(motor.getDp() == null)) {
                    dp.setText(createDot(dpMotor));
                }
                break;

            case R.id.cicilan:
                if (hasFocus && !(motor.getCicilan() == null)) {
                    cicilan.setText(createDot(cicilanMotor));
                }
                break;

        }
    }
}
