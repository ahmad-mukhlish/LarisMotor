package com.yayanheryanto.larismotor.view.pending;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.yayanheryanto.larismotor.R;
import com.yayanheryanto.larismotor.model.PendingJual;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import static com.yayanheryanto.larismotor.config.config.DATA_PENDING;
import static com.yayanheryanto.larismotor.helper.HelperClass.convertToTitleCaseIteratingChars;
import static com.yayanheryanto.larismotor.helper.HelperClass.formatter;

public class DetailPendingJualActivity extends AppCompatActivity {

    private TextView txtNama, txtAlamat, txtNoTelepon, txtMerk, txtTipe, txtNoPolisi, txtNoMesin,
            txtTahun, txtHarga, txtTanggalJual;
    private PendingJual pending;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pending_jual);


        txtNama = findViewById(R.id.nama_detail_jual);
        txtAlamat = findViewById(R.id.alamat_detail_jual);
        txtNoTelepon = findViewById(R.id.telepon_detail_jual);
        txtMerk = findViewById(R.id.nama_merk_detail_jual);
        txtTipe = findViewById(R.id.nama_tipe_detail_jual);
        txtTahun = findViewById(R.id.tahun_detail_jual);
        txtHarga = findViewById(R.id.harga_detail_jual);
        txtNoMesin = findViewById(R.id.nomor_mesin_detail_jual);
        txtNoPolisi = findViewById(R.id.nomor_polisi_detail_jual);
        txtTanggalJual = findViewById(R.id.tanggal_detail_jual);


        getFromIntent();
    }

    private void getFromIntent() {

        Bundle data = getIntent().getExtras();
        pending = data.getParcelable(DATA_PENDING);

        txtNama.setText(convertToTitleCaseIteratingChars(pending.getNama()));
        txtAlamat.setText(pending.getAlamat());

        if (pending.getNoTelp() == null) {
            txtNoTelepon.setText("-");
        } else {
            txtNoTelepon.setText(pending.getNoTelp());
        }
        txtMerk.setText(pending.getNamaMerk());
        txtTipe.setText(pending.getNamaTipe());

        if (pending.getNoPolisi() == null) {
            txtNoPolisi.setText("-");
        } else {
            txtNoPolisi.setText(pending.getNoPolisi());
        }

        if (pending.getNoMesin() == null) {
            txtNoMesin.setText("-");
        } else {
            txtNoMesin.setText(pending.getNoMesin());
        }

        txtTahun.setText("" + pending.getTahun());
        txtHarga.setText(formatter(pending.getHarga()+""));

        if (pending.getTanggalJual() == null) {

            txtTanggalJual.setText("-");

        } else {
            SimpleDateFormat df = new SimpleDateFormat("dd MMMM yyyy", new Locale("ID"));
            SimpleDateFormat sql = new SimpleDateFormat("yyyy-MM-dd", new Locale("ID"));

            try {
                txtTanggalJual.setText(df.format(sql.parse(pending.getTanggalJual())));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }





}
