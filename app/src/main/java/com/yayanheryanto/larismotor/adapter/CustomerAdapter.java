package com.yayanheryanto.larismotor.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yayanheryanto.larismotor.R;
import com.yayanheryanto.larismotor.model.Customer;
import com.yayanheryanto.larismotor.view.owner.DetailCustomerActivity;
import com.yayanheryanto.larismotor.view.owner.EditCustomerActivity;

import java.util.List;

import static com.yayanheryanto.larismotor.config.config.DATA_CUSTOMER;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.CustomerViewHolder> {



    private Context mContext;
    private List<Customer> mList;

    public CustomerAdapter(Context mContext, List<Customer> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public CustomerAdapter.CustomerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.customer_card, null, false);
        CustomerAdapter.CustomerViewHolder adapter = new CustomerAdapter.CustomerViewHolder(view);
        return adapter;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerAdapter.CustomerViewHolder customerViewHolder, int i) {
        final Customer customer = mList.get(i);
        customerViewHolder.textNama.setText(customer.getNama());
        customerViewHolder.textTelp.setText(customer.getNoTelp());

        customerViewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailCustomerActivity.class);
                intent.putExtra("coba", customer.getNoKtp());
                intent.putExtra(DATA_CUSTOMER, customer);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK) ;
                mContext.startActivity(intent);
            }
        });

        customerViewHolder.imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, EditCustomerActivity.class);
                intent.putExtra(DATA_CUSTOMER, customer);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class CustomerViewHolder extends RecyclerView.ViewHolder {

        private TextView textNama, textTelp;
        private View view;
        private ImageView imgEdit;

        public CustomerViewHolder(@NonNull View itemView) {
            super(itemView);

            this.view = itemView;
            textNama = itemView.findViewById(R.id.txt_nama);
            textTelp = itemView.findViewById(R.id.txt_telepon);
            imgEdit = itemView.findViewById(R.id.imgEdit);
        }
    }
}
