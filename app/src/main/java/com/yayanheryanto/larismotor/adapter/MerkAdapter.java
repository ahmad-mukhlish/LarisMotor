package com.yayanheryanto.larismotor.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.hawk.Hawk;
import com.yayanheryanto.larismotor.R;
import com.yayanheryanto.larismotor.interfaces.MerkToTipeInterface;
import com.yayanheryanto.larismotor.view.owner.EditMerkActivity;
import com.yayanheryanto.larismotor.view.LoginActivity;
import com.yayanheryanto.larismotor.view.owner.MasterActivity;
import com.yayanheryanto.larismotor.model.Merk;
import com.yayanheryanto.larismotor.retrofit.ApiClient;
import com.yayanheryanto.larismotor.retrofit.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.yayanheryanto.larismotor.config.config.ACCESTOKEN;
import static com.yayanheryanto.larismotor.config.config.DATA_MERK;
import static com.yayanheryanto.larismotor.config.config.ID_USER;
import static com.yayanheryanto.larismotor.config.config.MY_PREFERENCES;

public class MerkAdapter extends RecyclerView.Adapter<MerkAdapter.MerkViewHolder>{


    private Context mContext;
    private List<Merk> mList;
    private FragmentManager parentActivity;
    private MerkAdapter adapter;
    private ProgressDialog progressDialog = null;
    private MerkToTipeInterface merkToTipeInterface;


    public MerkAdapter(Context mContext, List<Merk> mList, FragmentManager parentActivity, MerkToTipeInterface merkToTipeInterface) {
        this.mContext = mContext;
        this.mList = mList;
        this.parentActivity = parentActivity;
        this.adapter = this;
        this.merkToTipeInterface = merkToTipeInterface;
    }

    @Override
    public MerkAdapter.MerkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.merk_card, null, false);
        MerkAdapter.MerkViewHolder adapter = new MerkAdapter.MerkViewHolder(view);

        return adapter;
    }

    private void initProgressDialog() {
        progressDialog = new ProgressDialog(mContext);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Sedang Memproses..");
        progressDialog.setCancelable(false);
    }

    @Override
    public void onBindViewHolder(@NonNull MerkAdapter.MerkViewHolder holder, final int position) {
        initProgressDialog();
        final Merk merk = mList.get(position);
        holder.txtNamaMerk.setText(merk.getNamaMerk());

        holder.imgLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Hawk.init(mContext).build();
                Hawk.put("merk",mList.get(position).getIdMerk());
                merkToTipeInterface.moveToTipe(1);

            }
        });

        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, EditMerkActivity.class);
                intent.putExtra(DATA_MERK, merk);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dialog = new AlertDialog.Builder(mContext)
                        .setTitle("Konfirmasi Hapus")
                        .setMessage("Hapus Data Merk?")
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                progressDialog.show();
                                SharedPreferences pref = mContext.getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
                                final SharedPreferences.Editor editor = pref.edit();
                                String id = pref.getString(ID_USER, "");
                                String token = pref.getString(ACCESTOKEN, "");

                                ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                                Call<Merk> call = apiInterface.deleteMerk(token, merk.getIdMerk());
                                call.enqueue(new Callback<Merk>() {
                                    @Override
                                    public void onResponse(Call<Merk> call, Response<Merk> response) {
                                        progressDialog.dismiss();
                                        if (response.body().getMessage().equals("success")){
                                            mList.remove(merk);
                                            adapter.notifyDataSetChanged();
                                            Toast.makeText(mContext, "Merk Berhasil Dihapus", Toast.LENGTH_SHORT).show();
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
                                    public void onFailure(Call<Merk> call, Throwable t) {
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


    public class MerkViewHolder extends RecyclerView.ViewHolder {

        private TextView txtIdMerk, txtNamaMerk;
        private ImageView imgDelete, imgEdit, imgLink;

        public MerkViewHolder(View itemView) {
            super(itemView);

            txtNamaMerk = itemView.findViewById(R.id.txt_nama_merk);
            imgDelete = itemView.findViewById(R.id.imgDelete);
            imgEdit = itemView.findViewById(R.id.imgEdit);
            imgLink = itemView.findViewById(R.id.imgLink);
        }
    }
}
