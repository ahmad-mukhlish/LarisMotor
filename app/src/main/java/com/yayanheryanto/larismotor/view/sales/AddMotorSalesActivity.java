package com.yayanheryanto.larismotor.view.sales;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import com.yayanheryanto.larismotor.R;
import com.yayanheryanto.larismotor.model.MerkTipe;
import com.yayanheryanto.larismotor.view.LoginActivity;
import com.yayanheryanto.larismotor.view.owner.AddMotorActivity;
import com.yayanheryanto.larismotor.view.owner.EditMotorActivity;
import com.yayanheryanto.larismotor.view.owner.MotorActivity;
import com.yayanheryanto.larismotor.model.Merk;
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

import static android.view.View.GONE;
import static com.yayanheryanto.larismotor.config.config.ACCESTOKEN;
import static com.yayanheryanto.larismotor.config.config.DATA_MOTOR;
import static com.yayanheryanto.larismotor.config.config.DEBUG;
import static com.yayanheryanto.larismotor.config.config.ID_USER;
import static com.yayanheryanto.larismotor.config.config.MY_PREFERENCES;

public class AddMotorSalesActivity extends AppCompatActivity implements View.OnClickListener {


    private Button btnUpload, btnSave, btnCamera, check;
    private List<Merk> merk;
    private List<Tipe> tipe;
    private Spinner spinnerMerk, spinnerTipe;
    private ArrayAdapter<String> adapter, adapter2;
    private ImageView image1, image2, image3;
    private ArrayList<Image> images;
    private EditText no_mesin, no_polisi, no_rangka, tahun, harga, dp, cicilan, tenor;
    private int merkMotor, tipeMotor;
    private ProgressDialog dialog;
    private File file, file2 = null;
    private Uri tempUri;
    private TextView hint;

    private static int cam ;


    private final int CAMERA_REQUEST = 110;
    private final int READ_EXTERNAL_STORAGE = 123;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_motor_sales);

        initProgressDialog();

        btnCamera = findViewById(R.id.btnCamera);
        btnUpload = findViewById(R.id.btnImage);
        btnSave = findViewById(R.id.btnSave);
        spinnerMerk = findViewById(R.id.spinner1);
        spinnerTipe = findViewById(R.id.spinner2);
        no_mesin = findViewById(R.id.no_mesin);
        no_polisi = findViewById(R.id.no_polisi);
        no_rangka = findViewById(R.id.no_rangka);
        tahun = findViewById(R.id.tahun);
        harga = findViewById(R.id.harga);
        cicilan = findViewById(R.id.cicilan);
        tenor = findViewById(R.id.tenor);
        dp = findViewById(R.id.dp);
        check = findViewById(R.id.check);
        hint = findViewById(R.id.hint);

        image1 = findViewById(R.id.image1);
        image2 = findViewById(R.id.image2);
        image3 = findViewById(R.id.image3);

        btnUpload.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnCamera.setOnClickListener(this);

        if (cam == 1) {
            reveal();
            check.setVisibility(GONE);
            hint.setVisibility(GONE);
            notFound();
        } else if (cam == 1) {
            reveal();
            check.setVisibility(GONE);
            hint.setVisibility(GONE);

        } else {
            hide();
        }


        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences pref = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
                String id = pref.getString(ID_USER, "");
                ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                Call<Motor> call = apiInterface.validateMotor(id, no_mesin.getText().toString());

                call.enqueue(new Callback<Motor>() {
                    @Override
                    public void onResponse(Call<Motor> call, Response<Motor> response) {
                        if (response.body().getNoMesin().equals("1")) {
                            reveal();
                            check.setVisibility(GONE);
                            hint.setVisibility(GONE);
                            notFound();
                        } else if (response.body().getNoMesin().equals("0")) {
                            Toast.makeText(getBaseContext(), "Motor sudah tersedia", Toast.LENGTH_SHORT).show();
                        } else {

                            Intent intent = new Intent(AddMotorSalesActivity.this,EditMotorSalesActivity.class) ;
                            intent.putExtra(DATA_MOTOR,response.body()) ;
                            intent.putExtra("ada",true) ;

                            startActivity(intent);

                        }


                    }

                    @Override
                    public void onFailure(Call<Motor> call, Throwable t) {

                    }
                });
            }
        });

    }


    private void hide() {
        no_polisi.setVisibility(View.GONE);
        no_rangka.setVisibility(View.GONE);
        tahun.setVisibility(View.GONE);
        harga.setVisibility(View.GONE);
        dp.setVisibility(View.GONE);
        cicilan.setVisibility(View.GONE);
        tenor.setVisibility(View.GONE);
        spinnerMerk.setVisibility(View.GONE);
        spinnerTipe.setVisibility(View.GONE);
        image1.setVisibility(View.GONE);
        image2.setVisibility(View.GONE);
        image3.setVisibility(View.GONE);
        btnUpload.setVisibility(View.GONE);
        btnSave.setVisibility(View.GONE);
        btnCamera.setVisibility(View.GONE);

    }

    private void reveal() {
        no_polisi.setVisibility(View.VISIBLE);
        no_rangka.setVisibility(View.VISIBLE);
        tahun.setVisibility(View.VISIBLE);
        harga.setVisibility(View.VISIBLE);
        dp.setVisibility(View.VISIBLE);
        cicilan.setVisibility(View.VISIBLE);
        tenor.setVisibility(View.VISIBLE);
        spinnerMerk.setVisibility(View.VISIBLE);
        spinnerTipe.setVisibility(View.VISIBLE);
        image1.setVisibility(View.VISIBLE);
        image2.setVisibility(View.VISIBLE);
        image3.setVisibility(View.VISIBLE);
        btnUpload.setVisibility(View.VISIBLE);
        btnSave.setVisibility(View.VISIBLE);
        btnCamera.setVisibility(View.VISIBLE);

    }

    private void notFound() {
        getMerk();

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line);
        adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line);
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

    private void initProgressDialog() {
        dialog = new ProgressDialog(this);
        dialog.setTitle("Loading");
        dialog.setMessage("Sedang Memproses..");
        dialog.setCancelable(false);
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
                    for (Merk merkMotor : merk){
                        Log.d(DEBUG, merkMotor.getNamaMerk());
                        adapter.add(merkMotor.getNamaMerk()) ;
                    }

                    spinnerMerk.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Merk>> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(AddMotorSalesActivity.this, "Terjadi Kesalahan Tidak Terduga", Toast.LENGTH_SHORT).show();
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
                    for (Tipe tipeMotor : tipe){
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
                Toast.makeText(AddMotorSalesActivity.this, "Terjadi Kesalahan Tidak Terduga", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnImage:
                Intent intent = new Intent(this, AlbumSelectActivity.class);
                intent.putExtra(ConstantsCustomGallery.INTENT_EXTRA_LIMIT, 3); // set limit for image selection
                startActivityForResult(intent, ConstantsCustomGallery.REQUEST_CODE);
                break;

            case R.id.btnSave:
                uploadImage();
                break;

            case R.id.btnCamera: {
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

    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    public static boolean checkImageResource(Context ctx, ImageView imageView,
                                             int imageResource) {
        boolean result = false;

        if (ctx != null && imageView != null && imageView.getDrawable() != null) {
            Drawable.ConstantState constantState;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                constantState = ctx.getResources()
                        .getDrawable(imageResource, ctx.getTheme())
                        .getConstantState();
            } else {
                constantState = ctx.getResources().getDrawable(imageResource)
                        .getConstantState();
            }

            if (imageView.getDrawable().getConstantState() == constantState) {
                result = true;
            }
        }

        return result;
    }

    private void uploadImage() {

        if (checkImageResource(this,image1,R.drawable.motorbike)){
            Toast.makeText(this, "Gambar Motor Belum Dimasukan", Toast.LENGTH_SHORT).show();
        }else {
            dialog.show();


            SharedPreferences pref = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
            final SharedPreferences.Editor editor = pref.edit();
            String id = pref.getString(ID_USER, "");
            String token = pref.getString(ACCESTOKEN, "");


            String mesin = no_mesin.getText().toString();
            String polisi = no_polisi.getText().toString();
            String rangka = no_rangka.getText().toString();
            String tahunMotor = tahun.getText().toString();
            String hargaMotor = harga.getText().toString();
            String dpMotor = dp.getText().toString();
            String cicilanMotor = cicilan.getText().toString();
            String tenorMotor = tenor.getText().toString();


            MultipartBody.Builder builder = new MultipartBody.Builder();
            builder.setType(MultipartBody.FORM);
            builder.addFormDataPart("no_polisi", polisi);
            builder.addFormDataPart("no_mesin", mesin);
            builder.addFormDataPart("no_rangka", rangka);
            builder.addFormDataPart("tahun", tahunMotor);
            builder.addFormDataPart("status", "0");
            builder.addFormDataPart("tipe", String.valueOf(tipeMotor));
            builder.addFormDataPart("merk", String.valueOf(merkMotor));
            builder.addFormDataPart("id_user", id);
            builder.addFormDataPart("harga", hargaMotor);
            builder.addFormDataPart("dp", dpMotor);
            builder.addFormDataPart("cicilan", cicilanMotor);
            builder.addFormDataPart("tenor", tenorMotor);

            if (images == null) {
                builder.addFormDataPart("file[]", file.getName(), RequestBody.create(MediaType.parse("multipart/form-data"), file));
            } else {
                for (int i = 0; i < images.size(); i++) {
                    file2 = new File(images.get(i).path);
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
            Call<Motor> call = apiInterface.addMotor(token, requestBody);
            call.enqueue(new Callback<Motor>() {
                @Override
                public void onResponse(Call<Motor> call, Response<Motor> response) {
                    dialog.dismiss();
                    if (response.body().getMessage().equals("success")) {
                        Toast.makeText(AddMotorSalesActivity.this, "Motor Berhasil Ditambah", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AddMotorSalesActivity.this, MotorSalesActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(AddMotorSalesActivity.this, "Token Tidak Valid, Silahkan Login", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(AddMotorSalesActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<Motor> call, Throwable t) {
                    dialog.dismiss();
                    t.printStackTrace();
                    Toast.makeText(AddMotorSalesActivity.this, "Terjadi kesalahan Tidak Terduga", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ConstantsCustomGallery.REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            images = data.getParcelableArrayListExtra(ConstantsCustomGallery.INTENT_EXTRA_IMAGES);


            for (int i = 0; i < images.size(); i++) {
                Uri uri = Uri.fromFile(new File(images.get(i).path));
                if (i == 0) {
                    image1.setImageURI(uri);
                }
                if (i == 1) {
                    image2.setImageURI(uri);
                }
                if (i == 2) {
                    image3.setImageURI(uri);
                }
                Log.d(DEBUG, String.valueOf(uri));

            }
        } else if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {

            Bitmap photo = (Bitmap) data.getExtras().get("data");
            image1.setImageBitmap(photo);
            tempUri = getImageUri(getApplicationContext(), photo);
            file = new File(getRealPathFromURI(tempUri));

        }
    }

    private void goToCamera() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_REQUEST);
        cam = 1;

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

    @Override
    public void onBackPressed() {
        startActivity(new Intent(AddMotorSalesActivity.this,MotorSalesActivity.class));
    }
}
