package com.yayanheryanto.larismotor.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yayanheryanto.larismotor.R;
import com.yayanheryanto.larismotor.view.owner.DetailSalesActivity;
import com.yayanheryanto.larismotor.view.owner.EditSalesActivity;
import com.yayanheryanto.larismotor.view.LoginActivity;
import com.yayanheryanto.larismotor.model.Sales;
import com.yayanheryanto.larismotor.retrofit.ApiClient;
import com.yayanheryanto.larismotor.retrofit.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.yayanheryanto.larismotor.config.config.ACCESTOKEN;
import static com.yayanheryanto.larismotor.config.config.DATA_SALES;
import static com.yayanheryanto.larismotor.config.config.ID_USER;
import static com.yayanheryanto.larismotor.config.config.MY_PREFERENCES;

public class SalesAdapter extends RecyclerView.Adapter<SalesAdapter.SalesViewHolder> {

    private Context mContext;
    private List<Sales> mList;
    private Activity parentActivity;
    private SalesAdapter adapter;
    private ProgressDialog progressDialog = null;

    public SalesAdapter(Context mContext, List<Sales> mList, Activity parentActivity) {
        this.mContext = mContext;
        this.mList = mList;
        this.parentActivity = parentActivity;
        this.adapter = this;
    }

    @Override
    public SalesAdapter.SalesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.sales_card, null, false);
        SalesAdapter.SalesViewHolder adapter = new SalesAdapter.SalesViewHolder(view);

        return adapter;
    }


    private void initProgressDialog() {
        progressDialog = new ProgressDialog(parentActivity);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Sedang Memproses..");
        progressDialog.setCancelable(false);
    }


    @Override
    public void onBindViewHolder(@NonNull SalesAdapter.SalesViewHolder holder, int position) {
        initProgressDialog();
        final Sales sales = mList.get(position);
        holder.textNama.setText(sales.getNama());
//        holder.textAlamat.setText(sales.getAlamat());
        holder.textTelp.setText(sales.getNoWa());
//        holder.textKtp.setText(sales.getNoKtpSales());

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, DetailSalesActivity.class);
                intent.putExtra(DATA_SALES, sales);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });

        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, EditSalesActivity.class);
                intent.putExtra(DATA_SALES, sales);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dialog = new AlertDialog.Builder(parentActivity)
                        .setTitle("Konfirmasi Hapus")
                        .setMessage("Hapus Data Motor?")
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                progressDialog.show();
                                final SharedPreferences pref = mContext.getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
                                final SharedPreferences.Editor editor = pref.edit();
                                String id = pref.getString(ID_USER, "");
                                String token = pref.getString(ACCESTOKEN, "");


                                ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                                Call<Sales> call = apiInterface.deleteSales(token, sales.getNoKtpSales(), sales.getIdUser()+"",id);
                                call.enqueue(new Callback<Sales>() {
                                    @Override
                                    public void onResponse(Call<Sales> call, Response<Sales> response) {
                                        progressDialog.dismiss();
                                        if (response.body().getMessage().equals("success")){
                                            mList.remove(sales);
                                            adapter.notifyDataSetChanged();
                                            Toast.makeText(parentActivity, "Sales Berhasil Dihapus", Toast.LENGTH_SHORT).show();
                                        }else {
                                            editor.putString(ID_USER,"");
                                            editor.putString(ACCESTOKEN, "");
                                            editor.commit();
                                            Toast.makeText(parentActivity, "Token Tidak Valid", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(parentActivity, LoginActivity.class);
                                            parentActivity.startActivity(intent);
                                            parentActivity.finish();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Sales> call, Throwable t) {
                                        progressDialog.dismiss();
                                        t.printStackTrace();
                                        Toast.makeText(parentActivity, "Terjadi Kesalahan Tidak Terduga", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        })

                        .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .create();
                dialog.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class SalesViewHolder extends RecyclerView.ViewHolder {

        private TextView textNama, textTelp, textAlamat, textKtp;
        private View view;
        private ImageView imgDelete, imgEdit;

        public SalesViewHolder(View itemView) {
            super(itemView);

            this.view = itemView;
            textNama = itemView.findViewById(R.id.txt_nama);
//            textAlamat = itemView.findViewById(R.id.txt_alamat);
            textTelp = itemView.findViewById(R.id.txt_telepon);
//            textKtp = itemView.findViewById(R.id.txt_ktp);
            imgDelete = itemView.findViewById(R.id.imgDelete);
            imgEdit = itemView.findViewById(R.id.imgEdit);

        }
    }
}
