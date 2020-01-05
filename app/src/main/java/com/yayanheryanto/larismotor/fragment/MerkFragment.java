package com.yayanheryanto.larismotor.fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.yayanheryanto.larismotor.R;
import com.yayanheryanto.larismotor.adapter.MerkAdapter;
import com.yayanheryanto.larismotor.interfaces.MerkToTipeInterface;
import com.yayanheryanto.larismotor.model.Merk;
import com.yayanheryanto.larismotor.retrofit.ApiClient;
import com.yayanheryanto.larismotor.retrofit.ApiInterface;
import com.yayanheryanto.larismotor.view.owner.MasterActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MerkFragment extends Fragment implements MerkToTipeInterface {


    private RecyclerView recyclerView;
    private MerkAdapter adapter;
    private LinearLayoutManager layoutManager;
    private ProgressDialog dialog;
    private MerkFragment merkFragment;

    public MerkFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_merk, container, false);

        initProgressDialog();
        recyclerView = view.findViewById(R.id.rvMerk);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        merkFragment = this;
        getMerk();


        return view;
    }

    private void getMerk() {
        dialog.show();
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<Merk>> call = apiInterface.getMerk();
        call.enqueue(new Callback<List<Merk>>() {
            @Override
            public void onResponse(Call<List<Merk>> call, Response<List<Merk>> response) {
                dialog.dismiss();
                List<Merk> list = response.body();

                adapter = new MerkAdapter(getContext(),list,getFragmentManager(),merkFragment);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Merk>> call, Throwable t) {
                dialog.dismiss();
                t.printStackTrace();
                Toast.makeText(getContext(), "Terjadi Kesalahan Tidak Terduga", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void initProgressDialog() {
        dialog = new ProgressDialog(getContext());
        dialog.setTitle("Loading");
        dialog.setMessage("Sedang Memproses..");
        dialog.setCancelable(false);
    }

    @Override
    public void moveToTipe(int halaman) {
        MasterActivity.view_pager.setCurrentItem(halaman);
    }
}
