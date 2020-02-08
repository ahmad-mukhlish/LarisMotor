package com.yayanheryanto.larismotor.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
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
import com.yayanheryanto.larismotor.view.owner.MasterActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.yayanheryanto.larismotor.config.config.ACCESTOKEN;
import static com.yayanheryanto.larismotor.config.config.DATA_TIPE;
import static com.yayanheryanto.larismotor.config.config.ID_USER;
import static com.yayanheryanto.larismotor.config.config.MY_PREFERENCES;

public class TipeAdapter extends RecyclerView.Adapter<TipeAdapter.TipeViewHolder> {


    private Context mContext;
    private List<MerkTipe> mList;
    private FragmentManager parentActivity;
    private TipeAdapter adapter;
    private ProgressDialog progressDialog = null;


    public TipeAdapter(Context mContext, List<MerkTipe> mList, FragmentManager parentActivity) {
        this.mContext = mContext;
        this.mList = mList;
        this.parentActivity = parentActivity;
        this.adapter = this;
    }

    @NonNull
    @Override
    public TipeAdapter.TipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.tipe_card, null, false);
        TipeAdapter.TipeViewHolder adapter = new TipeAdapter.TipeViewHolder(view);

        return adapter;
    }


    private void initProgressDialog() {
        progressDialog = new ProgressDialog((MasterActivity) mContext);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Sedang Memproses..");
        progressDialog.setCancelable(false);
    }

    @Override
    public void onBindViewHolder(@NonNull TipeAdapter.TipeViewHolder holder, int position) {
        initProgressDialog();
        final MerkTipe tipe = mList.get(position);
        holder.txtNamaMerk.setText(tipe.getNamaMerk() + " - " + tipe.getNamaTipe());

        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tipe.getNamaTipe().equalsIgnoreCase("Default")) {

                    Toast.makeText(mContext, "default tidak boleh diedit", Toast.LENGTH_SHORT).show();

                } else {
                    Intent intent = new Intent(mContext, EditTipeActivity.class);
                    intent.putExtra(DATA_TIPE, tipe);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                }
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
                                Call<String> call = apiInterface.deleteTipe(token, tipe.getIdTipe());
                                call.enqueue(new Callback<String>() {
                                    @Override
                                    public void onResponse(Call<String> call, Response<String> response) {
                                        progressDialog.dismiss();

                                        if (response.body().equals("success")) {
                                            mList.remove(tipe);
                                            adapter.notifyDataSetChanged();
                                            Toast.makeText(mContext, "Tipe Berhasil Dihapus", Toast.LENGTH_SHORT).show();
                                        } else if (response.body().equalsIgnoreCase("token tidak valid")) {
                                            editor.putString(ID_USER, "");
                                            editor.putString(ACCESTOKEN, "");
                                            editor.commit();
                                            Toast.makeText(mContext, response.body(), Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(mContext, LoginActivity.class);
                                            mContext.startActivity(intent);
                                            ((Activity) mContext).finish();
                                        } else {
                                            Toast.makeText(mContext, response.body(), Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<String> call, Throwable t) {
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

    public class TipeViewHolder extends RecyclerView.ViewHolder {

        private TextView txtIdTipe, txtNamaTipe, txtNamaMerk;
        private ImageView imgDelete, imgEdit;

        public TipeViewHolder(View itemView) {
            super(itemView);

            txtNamaMerk = itemView.findViewById(R.id.txt_nama_merk);
            imgDelete = itemView.findViewById(R.id.imgDelete);
            imgEdit = itemView.findViewById(R.id.imgEdit);
        }
    }
}
