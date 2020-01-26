package com.yayanheryanto.larismotor.view.owner;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
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
import android.widget.Spinner;
import android.widget.Toast;

import com.orhanobut.hawk.Hawk;
import com.redmadrobot.inputmask.MaskedTextChangedListener;
import com.redmadrobot.inputmask.ReversedMaskTextChangedListener;
import com.yalantis.ucrop.UCrop;
import com.yayanheryanto.larismotor.R;
import com.yayanheryanto.larismotor.model.Merk;
import com.yayanheryanto.larismotor.model.Motor;
import com.yayanheryanto.larismotor.model.Tipe;
import com.yayanheryanto.larismotor.retrofit.ApiClient;
import com.yayanheryanto.larismotor.retrofit.ApiInterface;
import com.yayanheryanto.larismotor.view.LoginActivity;

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
import static com.yayanheryanto.larismotor.config.config.DEBUG;
import static com.yayanheryanto.larismotor.config.config.ID_USER;
import static com.yayanheryanto.larismotor.config.config.MY_PREFERENCES;
import static com.yayanheryanto.larismotor.helper.HelperClass.clearDot;
import static com.yayanheryanto.larismotor.helper.HelperClass.createDot;
import static com.yayanheryanto.larismotor.view.owner.AddMotorBekasActivity.checkImageResource;

public class AddMotorBaruActivity extends AppCompatActivity implements View.OnFocusChangeListener {

    private ProgressDialog dialog;

    private Button btnCamera;
    private Button btnUpload;
    private Button btnSave;

    private Spinner spinnerMerk;
    private Spinner spinnerTipe;

    private EditText harga;
    private EditText cicilan;
    private EditText tenor;
    private EditText dp;

    private ImageView image1;
    private ImageView image2;
    private ImageView image3;

    private String hargaMotor;
    private String dpMotor;
    private String cicilanMotor;

    private int merkMotor, tipeMotor;

    private ArrayList<Image> images;
    private File file, file2 = null;
    private ArrayAdapter<String> adapter, adapter2;

    private List<Merk> merk;
    private List<Tipe> tipe;

    private Uri uri1;
    private Uri uri2;
    private Uri uri3;

    private final int CAMERA_REQUEST = 110;
    private final int READ_EXTERNAL_STORAGE = 123;


    private Uri tempUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_motor_baru);
        inits();
        bind();

    }

    private void bind() {

        harga = findViewById(R.id.harga_mobar);
        cicilan = findViewById(R.id.cicilan_mobar);
        tenor = findViewById(R.id.tenor_mobar);
        dp = findViewById(R.id.dp_mobar);

        spinnerMerk = findViewById(R.id.spinner1_mobar);
        spinnerTipe = findViewById(R.id.spinner2_mobar);

        image1 = findViewById(R.id.image1_mobar);
        image2 = findViewById(R.id.image2_mobar);
        image3 = findViewById(R.id.image3_mobar);


        btnCamera = findViewById(R.id.btnCamera_mobar);
        btnUpload = findViewById(R.id.btnImage_mobar);
        btnSave = findViewById(R.id.btnSave_mobar);

        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddMotorBaruActivity.this, AlbumSelectActivity.class);
                intent.putExtra(ConstantsCustomGallery.INTENT_EXTRA_LIMIT, 1); // set limit for image selection
                startActivityForResult(intent, ConstantsCustomGallery.REQUEST_CODE);
                Hawk.put("codeImageMobar", 1);
            }
        });

        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddMotorBaruActivity.this, AlbumSelectActivity.class);
                intent.putExtra(ConstantsCustomGallery.INTENT_EXTRA_LIMIT, 1); // set limit for image selection
                startActivityForResult(intent, ConstantsCustomGallery.REQUEST_CODE);
                Hawk.put("codeImageMobar", 2);
            }
        });

        image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddMotorBaruActivity.this, AlbumSelectActivity.class);
                intent.putExtra(ConstantsCustomGallery.INTENT_EXTRA_LIMIT, 1); // set limit for image selection
                startActivityForResult(intent, ConstantsCustomGallery.REQUEST_CODE);
                Hawk.put("codeImageMobar", 3);
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postMotorBaru();
            }
        });

        setTitle("Tambah Motor Baru");
        setInputMask();
        setupSpinner();
    }

    private void setupSpinner() {
        getMerk();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line);
        adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line);
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
//                        if (merkMotor.getNamaMerk().equalsIgnoreCase("Yamaha") ||
//                                merkMotor.getNamaMerk().equalsIgnoreCase("Honda") ||
//                                merkMotor.getNamaMerk().equalsIgnoreCase("Mobil")
//                        )
                        adapter.add(merkMotor.getNamaMerk());
                    }

                    spinnerMerk.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Merk>> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(AddMotorBaruActivity.this,
                        "Terjadi Kesalahan Tidak Terduga", Toast.LENGTH_SHORT).show();
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
                        Log.d(DEBUG, tipeMotor.getNamaTipe());
                        adapter2.add(tipeMotor.getNamaTipe());
                    }

                    spinnerTipe.setAdapter(adapter2);
                }
            }

            @Override
            public void onFailure(Call<List<Tipe>> call, Throwable t) {
                dialog.dismiss();
                t.printStackTrace();
                Toast.makeText(AddMotorBaruActivity.this, "Terjadi Kesalahan Tidak Terduga", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void postMotorBaru() {
        if (checkImageResource(this, image1, R.drawable.motorbike) ||
                checkImageResource(this, image2, R.drawable.motorbike)
                || checkImageResource(this, image3, R.drawable.motorbike)) {
            Toast.makeText(this, "Gambar Motor Belum Dimasukan", Toast.LENGTH_SHORT).show();
        } else if (hargaMotor == null) {
            Toast.makeText(this, "Harga Motor Belum Dimasukan", Toast.LENGTH_SHORT).show();
        } else {
            dialog.show();
            SharedPreferences pref = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
            final SharedPreferences.Editor editor = pref.edit();
            String id = pref.getString(ID_USER, "");
            String token = pref.getString(ACCESTOKEN, "");


            hargaMotor = harga.getText().toString();
            dpMotor = dp.getText().toString();
            cicilanMotor = cicilan.getText().toString();
            String tenorMotor = tenor.getText().toString();


            MultipartBody.Builder builder = new MultipartBody.Builder();
            builder.setType(MultipartBody.FORM);
            builder.addFormDataPart("tipe", String.valueOf(tipeMotor));
            builder.addFormDataPart("merk", String.valueOf(merkMotor));
            builder.addFormDataPart("id_user", id);
            builder.addFormDataPart("harga", clearDot(hargaMotor));
            builder.addFormDataPart("dp", clearDot(dpMotor));
            builder.addFormDataPart("cicilan", clearDot(cicilanMotor));
            builder.addFormDataPart("tenor", tenorMotor);


            if (images == null) {
                builder.addFormDataPart("file[]", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file));
            } else {
                for (int i = 0; i < 3; i++) {

                    if (i == 0) {
                        file2 = new File(uri1.getPath());
                    } else if (i == 1) {
                        file2 = new File(uri2.getPath());
                    } else {
                        file2 = new File(uri3.getPath());
                    }

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


            MultipartBody requestBody = builder.build();
            ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
            Call<Motor> call = apiInterface.addMotorBaru(token, requestBody);


            call.enqueue(new Callback<Motor>() {
                @Override
                public void onResponse(Call<Motor> call, Response<Motor> response) {
                    dialog.dismiss();
                    if (response.body().getMessage().equals("success")) {
                        Toast.makeText(AddMotorBaruActivity.this, "Motor Berhasil Ditambah", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AddMotorBaruActivity.this, MotorActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(AddMotorBaruActivity.this, "Token Tidak Valid, Silahkan Login", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AddMotorBaruActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<Motor> call, Throwable t) {
                    dialog.dismiss();
                    t.printStackTrace();
                    Toast.makeText(AddMotorBaruActivity.this, "Terjadi kesalahan Tidak Terduga", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void inits() {
        initProgressDialog();
        Hawk.init(this).build();

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
    }

    @Override
    public void onFocusChange(View view, boolean hasFocus) {
        switch (view.getId()) {
            case R.id.harga_mobar:
                if (hasFocus && !harga.getText().toString().isEmpty()) {
                    harga.setText(createDot(hargaMotor) + "");
                }
                break;

            case R.id.dp_mobar:
                if (hasFocus && !dp.getText().toString().isEmpty()) {
                    dp.setText(createDot(dpMotor));
                }
                break;

            case R.id.cicilan_mobar:
                if (hasFocus && !cicilan.getText().toString().isEmpty()) {
                    cicilan.setText(createDot(cicilanMotor));
                }
                break;

        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(AddMotorBaruActivity.this, MotorActivity.class));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ConstantsCustomGallery.REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            //The array list has the image paths of the selected images

            images = data.getParcelableArrayListExtra(ConstantsCustomGallery.INTENT_EXTRA_IMAGES);
            Uri uri = Uri.fromFile(new File(images.get(0).path));

            int codeImage = Hawk.get("codeImageMobar");

            UCrop.Options options = new UCrop.Options();
            options.setFreeStyleCropEnabled(true);

            switch (codeImage) {

                case 1: {


                    UCrop.of(uri, uri)
                            .withOptions(options)
                            .start(this, 202);
                    break;
                }

                case 2: {
                    UCrop.of(uri, uri)
                            .withOptions(options)
                            .start(this, 203);
                    break;
                }

                case 3: {
                    UCrop.of(uri, uri)
                            .withOptions(options)
                            .start(this, 204);
                    break;
                }

            }


        } else if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            image1.setImageBitmap(photo);
            tempUri = getImageUri(getApplicationContext(), photo);
            UCrop.of(tempUri, tempUri)
                    .withAspectRatio(16, 9)
                    .withMaxResultSize(1023, 1024)
                    .start(this, 205);

        } else if (resultCode == RESULT_OK && requestCode == 202) {

            Hawk.put("uri1", UCrop.getOutput(data));
            uri1 = Hawk.get("uri1");
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

}
