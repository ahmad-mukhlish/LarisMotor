package com.yayanheryanto.larismotor.view.pending;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.yayanheryanto.larismotor.R;
import com.yayanheryanto.larismotor.model.PendingBeli;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import static com.yayanheryanto.larismotor.config.config.DATA_PENDING;
import static com.yayanheryanto.larismotor.helper.HelperClass.convertToTitleCaseIteratingChars;

public class DetailPendingBeliActivity extends AppCompatActivity {

    private TextView txtNama, txtAlamat, txtNoTelepon, txtMerk, txtTipe, txtTahun,
            txtHarga, txtTanggalBeli;
    private PendingBeli pendingBeli;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pending_beli);


        txtNama = findViewById(R.id.nama_detail_beli);
        txtAlamat = findViewById(R.id.alamat_detail_beli);
        txtNoTelepon = findViewById(R.id.telepon_detail_beli);
        txtMerk = findViewById(R.id.nama_merk_detail_beli);
        txtTipe = findViewById(R.id.nama_tipe_detail_beli);
        txtTahun = findViewById(R.id.tahun_detail_beli);
        txtHarga = findViewById(R.id.harga_detail_beli);
        txtTanggalBeli = findViewById(R.id.tanggal_detail_beli);
        getFromIntent();

    }

    private void getFromIntent() {

        Bundle data = getIntent().getExtras();
        pendingBeli = data.getParcelable(DATA_PENDING);

        txtNama.setText(convertToTitleCaseIteratingChars(pendingBeli.getNama()));
        txtAlamat.setText(pendingBeli.getAlamat());
        txtNoTelepon.setText(pendingBeli.getNoTelp());
        txtMerk.setText(pendingBeli.getNamaMerk());
        txtTipe.setText(pendingBeli.getNamaTipe());
        txtTahun.setText(""+ pendingBeli.getTahun());
        txtHarga.setText("Rp. "+ pendingBeli.getHarga());

        if (pendingBeli.getNoTelp() == null) {
            txtNoTelepon.setText("-");
        } else {
            txtNoTelepon.setText(pendingBeli.getNoTelp());
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
    }



}
