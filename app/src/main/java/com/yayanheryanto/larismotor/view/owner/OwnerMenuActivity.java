package com.yayanheryanto.larismotor.view.owner;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.yayanheryanto.larismotor.R;
import com.yayanheryanto.larismotor.view.LoginActivity;
import com.yayanheryanto.larismotor.view.laporan.FilterActivity;
import com.yayanheryanto.larismotor.view.laporan.LaporanActivity;
import com.yayanheryanto.larismotor.view.pending.PendingTransaksiActivity;
import com.yayanheryanto.larismotor.adapter.MenuAdapter;
import com.yayanheryanto.larismotor.model.Menu;
import com.yayanheryanto.larismotor.view.transaksi.TransaksiActivity;

import java.util.ArrayList;
import java.util.List;

import static com.yayanheryanto.larismotor.config.config.ACCESTOKEN;
import static com.yayanheryanto.larismotor.config.config.ID_USER;
import static com.yayanheryanto.larismotor.config.config.MY_PREFERENCES;
import static com.yayanheryanto.larismotor.config.config.NAMA_USER;

public class OwnerMenuActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private List<Menu> list;
    private MenuAdapter adapter;
    private LinearLayoutManager layoutManager;
    private String id, token;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private TextView txName;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_menu);


        pref = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
        id = pref.getString(ID_USER, "");
        token = pref.getString(ACCESTOKEN, "");
        String namaUser = pref.getString(NAMA_USER, "");
        //Toast.makeText(this, token, Toast.LENGTH_SHORT).show();

        txName = findViewById(R.id.txName);
        image = findViewById(R.id.image);
        image.setOnClickListener(this);

        txName.setText(namaUser);

        ScrollView scrollView = findViewById(R.id.scrollView);
        scrollView.setFocusableInTouchMode(true);
        scrollView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);

        recyclerView = findViewById(R.id.recyclerview);
        list = new ArrayList<Menu>();
        list.add(new Menu(R.drawable.user, "Customer",CustomerActivity.class));
        list.add(new Menu(R.drawable.motorbike, "Motor", MotorActivity.class));
        list.add(new Menu(R.drawable.transaction, "Transaksi",TransaksiActivity.class));
        list.add(new Menu(R.drawable.time, "Pending Transaksi",PendingTransaksiActivity.class));
        list.add(new Menu(R.drawable.money, "Atur Insentif",InsentifConfigAcitivity.class));
        list.add(new Menu(R.drawable.report, "Laporan Penjualan",FilterActivity.class));
        list.add(new Menu(R.drawable.master, "Master",MasterActivity.class));
        list.add(new Menu(R.drawable.sales, "Sales",SalesActivity.class));

        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(this,2);
        adapter = new MenuAdapter(this,list);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.menu_owner, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                pref = getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
                editor = pref.edit();
                editor.putString(ACCESTOKEN,"");
                editor.putString(ID_USER, "");
                editor.putString(NAMA_USER, "");
                editor.commit();
                Intent intent = new Intent(OwnerMenuActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View view) {
        if (view==image){
            Intent intent = new Intent(OwnerMenuActivity.this, EditProfileOwnerActivity.class);
            intent.putExtra("ID_USER", id);
            startActivity(intent);
        }
    }
}
