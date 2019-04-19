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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yayanheryanto.larismotor.R;
import com.yayanheryanto.larismotor.model.PendingBeli;
import com.yayanheryanto.larismotor.retrofit.ApiClient;
import com.yayanheryanto.larismotor.retrofit.ApiInterface;
import com.yayanheryanto.larismotor.view.LoginActivity;
import com.yayanheryanto.larismotor.view.pending.DetailPendingBeliActivity;
import com.yayanheryanto.larismotor.view.pending.EditPendingBeliActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.yayanheryanto.larismotor.config.config.ACCESTOKEN;
import static com.yayanheryanto.larismotor.config.config.DATA_PENDING;
import static com.yayanheryanto.larismotor.config.config.ID_USER;
import static com.yayanheryanto.larismotor.config.config.MY_PREFERENCES;

public class SearchPendingBeliAdapter extends RecyclerView.Adapter<SearchPendingBeliAdapter.PendingViewHolder>{


    private Context mContext;
    private List<PendingBeli> mList;
    private SearchPendingBeliAdapter adapter;
    private ProgressDialog progressDialog = null;

    public SearchPendingBeliAdapter(Context mContext, List<PendingBeli> mList) {
        this.mContext = mContext;
        this.mList = mList;
        this.adapter = this;
    }

    @Override
    public PendingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.pending_beli, null, false);
        SearchPendingBeliAdapter.PendingViewHolder adapter = new SearchPendingBeliAdapter.PendingViewHolder(view);

        return adapter;
    }


    private void initProgressDialog() {
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Sedang Memproses..");
        progressDialog.setCancelable(false);
    }


    @Override
    public void onBindViewHolder(@NonNull PendingViewHolder holder, int position) {
        initProgressDialog();
        final PendingBeli pendingBeli = mList.get(position);
        holder.txtNama.setText(convertToTitleCaseIteratingChars(pendingBeli.getNama()));
        holder.txtNamaMotor.setText(pendingBeli.getNamaMerk() + " " + pendingBeli.getNamaTipe());
        Log.d("Hhhh", pendingBeli.getNamaMerk() + " " + pendingBeli.getNamaTipe());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mContext, DetailPendingBeliActivity.class);
                intent.putExtra(DATA_PENDING, pendingBeli);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });

        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, EditPendingBeliActivity.class);
                intent.putExtra(DATA_PENDING, pendingBeli);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dialog = new AlertDialog.Builder(mContext)
                        .setTitle("Konfirmasi Hapus")
                        .setMessage("Hapus Data PendingBeli?")
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                progressDialog.show();
                                SharedPreferences pref = mContext.getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
                                final SharedPreferences.Editor editor = pref.edit();
                                String id = pref.getString(ID_USER, "");
                                String token = pref.getString(ACCESTOKEN, "");

                                ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                                Call<PendingBeli> call = apiInterface.deletePendingBeli(token, pendingBeli.getIdPending());
                                call.enqueue(new Callback<PendingBeli>() {
                                    @Override
                                    public void onResponse(Call<PendingBeli> call, Response<PendingBeli> response) {
                                        progressDialog.dismiss();
                                        if (response.body().getMessage().equals("success")){
                                            mList.remove(pendingBeli);
                                            adapter.notifyDataSetChanged();
                                            Toast.makeText(mContext, "PendingBeli Berhasil Dihapus", Toast.LENGTH_SHORT).show();
                                        }else {
                                            editor.putString(ID_USER,"");
                                            editor.putString(ACCESTOKEN, "");
                                            editor.commit();
                                            Toast.makeText(mContext, "Token Tidak Valid", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(mContext, LoginActivity.class);
                                            mContext.startActivity(intent);
                                            ((Activity)mContext).finish();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<PendingBeli> call, Throwable t) {
                                        progressDialog.dismiss();
                                        t.printStackTrace();
                                        Toast.makeText(mContext, "Terjadi Kesalahan Tidak Terduga", Toast.LENGTH_SHORT).show();
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

    public class PendingViewHolder extends RecyclerView.ViewHolder {

        private TextView txtNama, txtNamaMotor;
        private ImageView imgDelete, imgEdit;
        private View mView;

        public PendingViewHolder(View itemView) {
            super(itemView);

            this.mView = itemView;
            txtNama = itemView.findViewById(R.id.txt_nama);
            txtNamaMotor = itemView.findViewById(R.id.txt_nama_motor);
            imgDelete = itemView.findViewById(R.id.imgDelete);
            imgEdit = itemView.findViewById(R.id.imgEdit);
        }
    }

    private String convertToTitleCaseIteratingChars(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        StringBuilder converted = new StringBuilder();

        boolean convertNext = true;
        for (char ch : text.toCharArray()) {
            if (Character.isSpaceChar(ch)) {
                convertNext = true;
            } else if (convertNext) {
                ch = Character.toTitleCase(ch);
                convertNext = false;
            } else {
                ch = Character.toLowerCase(ch);
            }
            converted.append(ch);
        }

        return converted.toString();
    }
}
