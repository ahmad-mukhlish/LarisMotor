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

import com.squareup.picasso.Picasso;
import com.yayanheryanto.larismotor.R;
import com.yayanheryanto.larismotor.retrofit.ApiClient;
import com.yayanheryanto.larismotor.retrofit.ApiInterface;
import com.yayanheryanto.larismotor.view.LoginActivity;
import com.yayanheryanto.larismotor.view.owner.DetailMotorActivity;
import com.yayanheryanto.larismotor.view.sales.EditMotorSalesActivity;
import com.yayanheryanto.larismotor.model.Motor;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.yayanheryanto.larismotor.config.config.ACCESTOKEN;
import static com.yayanheryanto.larismotor.config.config.BASE_URL;
import static com.yayanheryanto.larismotor.config.config.DATA_MOTOR;
import static com.yayanheryanto.larismotor.config.config.ID_USER;
import static com.yayanheryanto.larismotor.config.config.MY_PREFERENCES;

public class MotorSalesAdapter extends RecyclerView.Adapter<MotorSalesAdapter.MotorViewHolder>{


    private Context mContext;
    private List<Motor> mList;
    private Activity parentActivity;
    private MotorSalesAdapter adapter;
    private ProgressDialog progressDialog = null;

    public MotorSalesAdapter(Context mContext, List<Motor> mList, Activity parentActivity) {
        this.mContext = mContext;
        this.mList = mList;
        this.parentActivity = parentActivity;
        this.adapter = this;
    }


    @NonNull
    @Override
    public MotorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.motor_sales_card, null, false);
        MotorSalesAdapter.MotorViewHolder adapter = new MotorSalesAdapter.MotorViewHolder(view);

        return adapter;
    }


    private void initProgressDialog() {
        progressDialog = new ProgressDialog(parentActivity);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Sedang Memproses..");
        progressDialog.setCancelable(false);
    }



    @Override
    public void onBindViewHolder(@NonNull MotorViewHolder holder, int position) {
        initProgressDialog();
        final Motor motor = mList.get(position);
        Picasso.get()
                .load(BASE_URL+"storage/motor/"+motor.getGambar())
                .into(holder.imageMotor);
        holder.textHjm.setText(""+motor.getHarga());
        holder.textNopol.setText(motor.getNoPolisi());


        holder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, EditMotorSalesActivity.class);
                intent.putExtra(DATA_MOTOR, motor);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });

        holder.imgDeelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog dialog = new AlertDialog.Builder(parentActivity)
                        .setTitle("Konfirmasi Hapus")
                        .setMessage("Hapus Data Motor?")
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                progressDialog.show();
                                SharedPreferences pref = mContext.getSharedPreferences(MY_PREFERENCES, Context.MODE_PRIVATE);
                                final SharedPreferences.Editor editor = pref.edit();
                                String id = pref.getString(ID_USER, "");
                                String token = pref.getString(ACCESTOKEN, "");

                                ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                                Call<Motor> call = apiInterface.delete(token, motor.getNoMesin(), motor.getGambar(), motor.getGambar1(), motor.getGambar2());
                                call.enqueue(new Callback<Motor>() {
                                    @Override
                                    public void onResponse(Call<Motor> call, Response<Motor> response) {
                                        progressDialog.dismiss();
                                        if (response.body().getMessage().equals("success")){
                                            mList.remove(motor);
                                            adapter.notifyDataSetChanged();
                                            Toast.makeText(parentActivity, "Data Motor Berhasil Dihapus", Toast.LENGTH_SHORT).show();
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
                                    public void onFailure(Call<Motor> call, Throwable t) {
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

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, DetailMotorActivity.class);
                intent.putExtra(DATA_MOTOR, motor);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MotorViewHolder extends RecyclerView.ViewHolder {


        private ImageView imageMotor, imgDeelete, imgEdit;
        private TextView textNopol, textHjm;
        private View view;

        public MotorViewHolder(View itemView) {
            super(itemView);


            this.view = itemView;
            imageMotor = itemView.findViewById(R.id.image);
            imgDeelete = itemView.findViewById(R.id.imgDelete);
            imgEdit = itemView.findViewById(R.id.imgEdit);
            textNopol = itemView.findViewById(R.id.txt_plat);
            textHjm = itemView.findViewById(R.id.txt_hjm);

        }
    }
}
