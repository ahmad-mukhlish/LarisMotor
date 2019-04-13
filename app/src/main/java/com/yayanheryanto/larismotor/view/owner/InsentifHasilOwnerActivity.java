package com.yayanheryanto.larismotor.view.owner;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yayanheryanto.larismotor.R;

public class InsentifHasilOwnerActivity extends AppCompatActivity {

    EditText lain;
    ImageView edit, hapus;
    boolean klik;
    TextView sales, dari, hingga;
    View batas;
    RelativeLayout holder, holderMargin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insentif_hasil_owner);

        Bundle bundle = getIntent().getExtras();


        lain = findViewById(R.id.lain);
        edit = findViewById(R.id.edit);
        hapus = findViewById(R.id.hapus);
        sales = findViewById(R.id.sales);
        dari = findViewById(R.id.dari);
        hingga = findViewById(R.id.hingga);
        batas = findViewById(R.id.batas);
        holder = findViewById(R.id.holder);
        holderMargin = findViewById(R.id.holder_margin);

        sales.setText(bundle.getString("sales"));
        dari.setText(bundle.getString("dari"));
        hingga.setText(bundle.getString("hingga"));

        klik = false;

        setMargins(holderMargin, 0, 2, 2, 2);


        holderMargin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!klik) {
                    lain.setEnabled(true);
                    lain.requestFocus();
                    lain.setSelection(lain.getText().length());
                    edit.setImageResource(R.drawable.ic_save_256);
                    batas.setVisibility(View.VISIBLE);
                    holder.setVisibility(View.VISIBLE);
                    klik = true;
                    setMargins(holderMargin, 0, 2, 2, 0);

                } else {
                    setMargins(holderMargin, 0, 2, 2, 2);
                    edit.setImageResource(R.drawable.ic_pencil_edit_button);
                    batas.setVisibility(View.GONE);
                    holder.setVisibility(View.GONE);
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

            }
        });

    }

    private void simpan() {
        //save ke database
    }

    private void setMargins(View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();

            int sizeInDP = 1;

            int marginInDp = (int) TypedValue.applyDimension(
                    TypedValue.COMPLEX_UNIT_DIP, sizeInDP, getResources()
                            .getDisplayMetrics());

            p.setMargins(left * marginInDp, top * marginInDp,
                    right * marginInDp, bottom * marginInDp);

            view.requestLayout();
        }
    }
}
