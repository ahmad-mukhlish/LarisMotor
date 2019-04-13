package com.yayanheryanto.larismotor.view.owner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.yayanheryanto.larismotor.R;

public class InsentifHasilOwnerActivity extends AppCompatActivity {

    EditText lain;
    ImageView edit, hapus;
    boolean klik;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insentif_hasil_owner);

        lain = findViewById(R.id.lain);
        edit = findViewById(R.id.edit);
        hapus = findViewById(R.id.hapus);

        klik = false;

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!klik) {
                    lain.setEnabled(true);
                    lain.requestFocus();
                    lain.setSelection(lain.getText().length());
                    klik = true;

                } else {
                    lain.setEnabled(false);
                    lain.clearFocus();
                    simpan();
                    klik = false;

                }

            }
        });

        hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                lain.setText("0");
                simpan();

            }
        });

    }

    private void simpan() {
        //save ke database
    }
}
