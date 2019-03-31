package com.yayanheryanto.larismotor.view.laporan;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.yayanheryanto.larismotor.R;
import com.yayanheryanto.larismotor.adapter.LaporanAdapter;
import com.yayanheryanto.larismotor.model.Transaksi;
import com.yayanheryanto.larismotor.retrofit.ApiClient;
import com.yayanheryanto.larismotor.retrofit.ApiInterface;

import java.util.List;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.model.TableColumnDpWidthModel;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LaporanActivity extends AppCompatActivity {

    private static final String[] TABLE_HEADERS = {"No.", "Tanggal", "Nopol/Nosin", "Pembeli", "Sales",
            "Kondisi", "Bayar", "Harga Terjual", "Subsidi", "Mediator", "Netto"
    };
    private TableView tableView;
    private LaporanAdapter adapter;
    private ProgressDialog dialog;

    private int posSales, posKondisi, posCaraBayar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan);


        initProgressDialog();


        tableView = findViewById(R.id.tableView);
        tableView.setHeaderAdapter(new SimpleTableHeaderAdapter(this, TABLE_HEADERS));
        TableColumnDpWidthModel columnModel = new TableColumnDpWidthModel(this, 11, 100);
        columnModel.setColumnWidth(0, 60);
        columnModel.setColumnWidth(1, 120);
        columnModel.setColumnWidth(2, 150);
        columnModel.setColumnWidth(3, 250);
        columnModel.setColumnWidth(7, 200);
        columnModel.setColumnWidth(8, 200);
        columnModel.setColumnWidth(9, 200);
        columnModel.setColumnWidth(10, 200);
        tableView.setColumnModel(columnModel);

        getTransaksi();

        Bundle bundle = getIntent().getExtras();

        posSales = bundle.getInt("posSales");
        posKondisi = bundle.getInt("posKondisi");
        posCaraBayar = bundle.getInt("posCaraBayar");

        Log.v("cak",posSales+"") ;
        Log.v("cak",posKondisi+"") ;
        Log.v("cak",posCaraBayar+"") ;


    }

    private void initProgressDialog() {
        dialog = new ProgressDialog(this);
        dialog.setTitle("Sedang Memeriksa");
        dialog.setMessage("Loading..");
        dialog.setCancelable(false);
    }


    private void getTransaksi() {

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);


        Log.v("cik",FilterActivity.sales) ;
        Log.v("cik",FilterActivity.kondisi) ;
        Log.v("cik",FilterActivity.caraBayar) ;

        Call<List<Transaksi>> call = apiInterface.getTransaksi(
                FilterActivity.tanggalDari, FilterActivity.tanggalKe,
                FilterActivity.sales, FilterActivity.kondisi, FilterActivity.caraBayar);



        call.enqueue(new Callback<List<Transaksi>>() {
            @Override
            public void onResponse(Call<List<Transaksi>> call, Response<List<Transaksi>> response) {
                dialog.dismiss();
                if (response.body().isEmpty()) {
                    Toast.makeText(LaporanActivity.this, "Data Penjualan Tidak Tersedia", Toast.LENGTH_SHORT).show();
                } else {
                    List<Transaksi> transaksi;
                    transaksi = response.body();

                    int nomor = 1;
                    String pembanding = transaksi.get(0).getTanggal();
                    for (Transaksi transaksiNow : transaksi) {

                        if (nomor != 1) {

                            transaksiNow.setSama(transaksiNow.getTanggal().equals(pembanding));

                        }

                        if (transaksiNow.isSama()) {

                            transaksiNow.setTanggal("");

                        } else {

                            pembanding = transaksiNow.getTanggal();
                        }

                        transaksiNow.setNomor(nomor);
                        nomor++;


                    }

                    adapter = new LaporanAdapter(getApplicationContext(), transaksi);
                    tableView.setDataAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Transaksi>> call, Throwable t) {
                dialog.dismiss();
                Log.e("Error", t.getLocalizedMessage());
                Toast.makeText(LaporanActivity.this, "Terjadi Kesalahan Tidak Terduga", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(LaporanActivity.this, FilterActivity.class);



        intent.putExtra("posSales", posSales);
        intent.putExtra("posKondisi", posKondisi);
        intent.putExtra("posCaraBayar", posCaraBayar);
        intent.putExtra("back", true);
        startActivity(intent);
    }
}
