package by.kuchinsky.alexandr.mangofit.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import by.kuchinsky.alexandr.mangofit.Interface.ItemClickListener;
import by.kuchinsky.alexandr.mangofit.R;

public class ServiceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView service_name, service_price;
    public ImageView service_image, fav_image;
    private ItemClickListener itemClickListener;

    public ServiceViewHolder(View itemView) {
        super(itemView);


        service_name = (TextView)itemView.findViewById(R.id.service_name);
        service_price = (TextView)itemView.findViewById(R.id.service_price);
        service_image=(ImageView)itemView.findViewById(R.id.service_image);
        fav_image=(ImageView)itemView.findViewById(R.id.fav);
        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);
    }
}
