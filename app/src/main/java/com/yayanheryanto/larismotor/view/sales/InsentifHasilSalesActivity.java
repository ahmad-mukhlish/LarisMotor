package com.yayanheryanto.larismotor.view.sales;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.yayanheryanto.larismotor.R;

import static com.yayanheryanto.larismotor.config.config.MY_PREFERENCES;
import static com.yayanheryanto.larismotor.config.config.NAMA_USER;

public class InsentifHasilSalesActivity extends AppCompatActivity {

    TextView sales, dari, hingga;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insentif_hasil_sales);

        sales = findViewById(R.id.sales);
        dari = findViewById(R.id.dari);
        hingga = findViewById(R.id.hingga);

        Bundle bundle = getIntent().getExtras();

        pref = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);

        sales.setText(pref.getString(NAMA_USER, ""));
        dari.setText(bundle.getString("dari"));
        hingga.setText(bundle.getString("hingga"));



    }
}
