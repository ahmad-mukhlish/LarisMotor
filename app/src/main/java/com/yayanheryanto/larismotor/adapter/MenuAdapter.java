package com.yayanheryanto.larismotor.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yayanheryanto.larismotor.R;
import com.yayanheryanto.larismotor.model.Menu;

import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {


    private Context mContext;
    private List<Menu> mList;

    public MenuAdapter(Context mContext, List<Menu> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }


    @Override
    public MenuAdapter.MenuViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.menu_card, null, false);
        MenuViewHolder adapter = new MenuViewHolder(view);

        return adapter;
    }

    @Override
    public void onBindViewHolder(@NonNull MenuAdapter.MenuViewHolder menuViewHolder, int i) {
        final Menu menu = mList.get(i);
        menuViewHolder.imageMenu.setImageResource(menu.getImage());
        menuViewHolder.textMenu.setText(menu.getTitle());
        menuViewHolder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, menu.getmContext());
                intent.putExtra("back",false) ;
                intent.putExtra("editJual",false) ;
                intent.putExtra("deal",false);
                mContext.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MenuViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageMenu;
        private TextView textMenu;
        private View view;

        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);

            this.view = itemView;
            imageMenu = itemView.findViewById(R.id.imageMenu);
            textMenu = itemView.findViewById(R.id.textMenu);
        }
    }
}
