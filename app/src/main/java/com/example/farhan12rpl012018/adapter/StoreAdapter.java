package com.example.farhan12rpl012018.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.farhan12rpl012018.R;
import com.example.farhan12rpl012018.model.StoreModel;

import java.util.List;

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.StoreViewHolder> {

    private Context mContext;
    private List<StoreModel> mData;
    Resources resources;
    String URL;

    public StoreAdapter(Context mContext, List<StoreModel> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }
    @NonNull
    @Override
    public StoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.store_recycler, parent, false);
        StoreViewHolder holder = new StoreViewHolder(v);
        mContext = parent.getContext();
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull StoreViewHolder holder, int position) {

        resources = this.mContext.getResources();
        URL = resources.getString(R.string.main_url);
        String imageUrl = URL  + mData.get(position).getFoto();
        Glide
                .with(this.mContext)
                .load(imageUrl)
                .into(holder.foto);
        holder.merk.setText(mData.get(position).getMerk());
        holder.hargasewa.setText(mData.get(position).getHargasewa());

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class StoreViewHolder extends RecyclerView.ViewHolder {

        private ImageView foto;
        private TextView merk, hargasewa;

        public StoreViewHolder( View view) {
            super(view);
            foto = (ImageView) view.findViewById(R.id.img_sepeda);
            merk = (TextView)view.findViewById(R.id.txt_merk);
            hargasewa = (TextView)view.findViewById(R.id.txt_hargasewa);
        }
    }
}