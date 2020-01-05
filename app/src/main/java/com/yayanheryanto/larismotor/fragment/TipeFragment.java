package com.yayanheryanto.larismotor.fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.orhanobut.hawk.Hawk;
import com.yayanheryanto.larismotor.R;
import com.yayanheryanto.larismotor.adapter.TipeAdapter;
import com.yayanheryanto.larismotor.model.MerkTipe;
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
public class TipeFragment extends Fragment {

    private RecyclerView recyclerView;
    private TipeAdapter adapter;
    private LinearLayoutManager layoutManager;
    private ProgressDialog dialog;

    public TipeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_tipe, container, false);

        initProgressDialog();
        recyclerView = view.findViewById(R.id.rvTipe);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        MasterActivity.view_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {

                if (i == 1) {
                    Hawk.init(getContext()).build();
                    Integer merk = Hawk.get("merk");
                    getTipeMotor(merk);
                }

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        return view;
    }

    private void getTipeMotor(Integer merk) {
        dialog.show();
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<MerkTipe>> call = apiInterface.getTipeMotor(merk);
        call.enqueue(new Callback<List<MerkTipe>>() {
            @Override
            public void onResponse(Call<List<MerkTipe>> call, Response<List<MerkTipe>> response) {
                dialog.dismiss();
                List<MerkTipe> list = response.body();

                adapter = new TipeAdapter(getContext(), list, getFragmentManager());
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<MerkTipe>> call, Throwable t) {
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

}
