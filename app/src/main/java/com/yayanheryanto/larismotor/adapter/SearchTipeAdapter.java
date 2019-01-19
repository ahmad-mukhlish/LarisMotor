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
import com.yayanheryanto.larismotor.model.MerkTipe;
import com.yayanheryanto.larismotor.retrofit.ApiClient;
import com.yayanheryanto.larismotor.retrofit.ApiInterface;
import com.yayanheryanto.larismotor.view.LoginActivity;
import com.yayanheryanto.larismotor.view.owner.EditTipeActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.yayanheryanto.larismotor.config.config.ACCESTOKEN;
import static com.yayanheryanto.larismotor.config.config.DATA_TIPE;
import static com.yayanheryanto.larismotor.config.config.ID_USER;
import static com.yayanheryanto.larismotor.config.config.MY_PREFERENCES;

public class SearchTipeAdapter extends RecyclerView.Adapter<SearchTipeAdapter.TipeViewHolder> {


    private Context mContext;
    private List<MerkTipe> mList;
    private SearchTipeAdapter adapter;
    private ProgressDialog progressDialog = null;


    public SearchTipeAdapter(Context mContext, List<MerkTipe> mList) {
        this.mContext = mContext;
        this.mList = mList;
        this.adapter = this;
    }

    @NonNull
    @Override
    public SearchTipeAdapter.TipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.tipe_card, null, false);
        SearchTipeAdapter.TipeViewHolder adapter = new SearchTipeAdapter.TipeViewHolder(view);

        return adapter;
    }

    @Override
    public void onBindViewHolder(@NonNull TipeViewHolder holder, int position) {
        initProgressDialog();
        final MerkTipe tipe = mList.get(position);
        holder.txtNamaTipe.setText(tipe.getNamaTipe());
        holder.txtNamaMerk.setText(tipe.getNamaMerk());

        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, EditTipeActivity.class);
                intent.putExtra(DATA_TIPE, tipe);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog dialog = new AlertDialog.Builder(mContext)
                        .setTitle("Konfirmasi Hapus")
                        .setMessage("Hapus Data Tipe?")
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                progressDialog.show();
                                final SharedPreferences pref = mContext.getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
                                final SharedPreferences.Editor editor = pref.edit();
                                String id = pref.getString(ID_USER, "");
                                String token = pref.getString(ACCESTOKEN, "");

                                ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                                Call<MerkTipe> call = apiInterface.deleteTipe(token, tipe.getIdTipe());
                                call.enqueue(new Callback<MerkTipe>() {
                                    @Override
                                    public void onResponse(Call<MerkTipe> call, Response<MerkTipe> response) {
                                        progressDialog.dismiss();
                                        if (response.body().getMessage().equals("success")) {
                                            mList.remove(tipe);
                                            adapter.notifyDataSetChanged();
                                            Toast.makeText(mContext, "Tipe Berhasil Dihapus", Toast.LENGTH_SHORT).show();
                                        } else {
                                            editor.putString(ID_USER, "");
                                            editor.putString(ACCESTOKEN, "");
                                            editor.commit();
                                            Toast.makeText(mContext, "Token Tidak Valid", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(mContext, LoginActivity.class);
                                            mContext.startActivity(intent);
                                            ((Activity) mContext).finish();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<MerkTipe> call, Throwable t) {
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


    private void initProgressDialog() {
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Sedang Memproses..");
        progressDialog.setCancelable(false);
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class TipeViewHolder extends RecyclerView.ViewHolder {

        private TextView txtIdTipe, txtNamaTipe, txtNamaMerk;
        private ImageView imgDelete, imgEdit;

        public TipeViewHolder(View itemView) {
            super(itemView);

            txtNamaMerk = itemView.findViewById(R.id.txt_nama_merk);
            txtNamaTipe = itemView.findViewById(R.id.txt_nama_tipe);
            imgDelete = itemView.findViewById(R.id.imgDelete);
            imgEdit = itemView.findViewById(R.id.imgEdit);
        }
    }
}