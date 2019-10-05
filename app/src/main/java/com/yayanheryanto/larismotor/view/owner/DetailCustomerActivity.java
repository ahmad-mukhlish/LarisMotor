package com.yayanheryanto.larismotor.view.owner;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.yayanheryanto.larismotor.R;
import com.yayanheryanto.larismotor.model.Customer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import static com.yayanheryanto.larismotor.config.config.DATA_CUSTOMER;

public class DetailCustomerActivity extends AppCompatActivity {

    private TextView noKTP, nama, alamat, ttl, noHp, pekerjaan,
            agama, whatsapp, instagram, facebook, jumlahPembelian, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_customer);


        noKTP = findViewById(R.id.no_ktp_customer);
        nama = findViewById(R.id.nama);
        alamat = findViewById(R.id.alamat);
        ttl = findViewById(R.id.ttl);
        noHp = findViewById(R.id.no_hp);
        pekerjaan = findViewById(R.id.pekerjaan);
        agama = findViewById(R.id.agama);
        whatsapp = findViewById(R.id.whatsapp);
        instagram = findViewById(R.id.instagram);
        facebook = findViewById(R.id.facebook);
        jumlahPembelian = findViewById(R.id.jumlah_pembelian);
        email = findViewById(R.id.email);

        fillDetail();
    }

    private void fillDetail() {

        Bundle data = getIntent().getExtras();
        final Customer customer = data.getParcelable(DATA_CUSTOMER);


        noKTP.setText(customer.getNoKtp());
        nama.setText(customer.getNama());
        alamat.setText(customer.getAlamat());

        SimpleDateFormat df = new SimpleDateFormat("dd MMMM yyyy", new Locale("ID"));
        SimpleDateFormat sqlformat = new SimpleDateFormat("yyyy-MM-dd", new Locale("EN"));
        try {
            ttl.setText(df.format(sqlformat.parse(customer.getTtl())));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        noHp.setText(customer.getNoTelp());
        pekerjaan.setText(customer.getPekerjaan());
        agama.setText(customer.getAgama());

        whatsapp.setText(customer.getWhatsapp());
        whatsapp.setTextColor(Color.parseColor("#0645AD"));

        instagram.setText(customer.getInstagram());
        instagram.setTextColor(Color.parseColor("#0645AD"));

        facebook.setText(customer.getFacebook());
        facebook.setTextColor(Color.parseColor("#0645AD"));


        jumlahPembelian.setText(customer.getJumlahPembelian());

        final String fb = "http://facebook.com/" + facebook.getText().toString();
        final String ig = "http://instagram.com/" + instagram.getText().toString();
        final String wa = "http://whatsapp.com/" + whatsapp.getText().toString();


        whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, wa);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);

            }
        });

        noHp.setTextColor(Color.parseColor("#0645AD"));
        noHp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", customer.getNoTelp(), null));
                startActivity(intent);

            }
        });


        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(fb)));


            }
        });


        instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(ig));
                startActivity(i);


            }
        });

        email.setText(customer.getEmail());
        email.setTextColor(Color.parseColor("#0645AD"));
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] adresses = new String[1];
                adresses[0] = customer.getEmail();
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:")); // only email apps should handle this
                intent.putExtra(Intent.EXTRA_EMAIL, adresses);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }

            }
        });


    }


}
