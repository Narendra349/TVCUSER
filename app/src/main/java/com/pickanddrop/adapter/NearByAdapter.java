package com.pickanddrop.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pickanddrop.R;
import com.pickanddrop.dto.DeliveryDTO;
import com.pickanddrop.utils.AppConstants;
import com.pickanddrop.utils.AppSession;
import com.pickanddrop.utils.ImageViewCircular;
import com.pickanddrop.utils.OnItemClickListener;
import com.pickanddrop.utils.Utilities;


import java.util.ArrayList;

public class NearByAdapter extends RecyclerView.Adapter<NearByAdapter.ViewHolder> implements AppConstants {
    private Context context;
    private OnItemClickListener.OnItemClickCallback onItemClickCallback, onItemClickCallbackDetails;
    private AppSession appSession;
    private Utilities utilities;
    private RequestOptions requestOptions, requestOptions1;
    private String status = "";
    private ArrayList<DeliveryDTO.Data> deliveryDTOArrayList;


    public NearByAdapter(Context context, ArrayList<DeliveryDTO.Data> deliveryDTOArrayList, OnItemClickListener.OnItemClickCallback onItemClickCallback, OnItemClickListener.OnItemClickCallback onItemClickCallbackDetails, String status) {
        this.context = context;
        this.onItemClickCallback = onItemClickCallback;
        appSession = new AppSession(context);
        utilities = Utilities.getInstance(context);
        this.status = status;
        this.deliveryDTOArrayList = deliveryDTOArrayList;
        this.onItemClickCallbackDetails = onItemClickCallbackDetails;

        requestOptions = new RequestOptions();
        requestOptions.centerCrop();
        requestOptions.override(150, 150);
        requestOptions.placeholder(R.drawable.user_praba);
        requestOptions.error(R.drawable.user_praba);

        requestOptions1 = new RequestOptions();
        requestOptions1.override(100, 100);
        requestOptions1.placeholder(R.drawable.truck_list);
        requestOptions1.error(R.drawable.truck_list);
    }


    @Override
    public NearByAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_near_deliveries, parent, false);
        return new NearByAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final NearByAdapter.ViewHolder viewHolder, int position) {
        try {
            Glide.with(context)
                    .setDefaultRequestOptions(requestOptions)
                    .load(IMAGE_URL + deliveryDTOArrayList.get(position).getSignatureImage())
                    .into(viewHolder.ivProfile);

            viewHolder.tvPickLoc.setVisibility(View.VISIBLE);
            viewHolder.tvDropLoc.setVisibility(View.VISIBLE);
            viewHolder.tvPrice.setVisibility(View.VISIBLE);
            viewHolder.ivProfile.setVisibility(View.GONE);

            viewHolder.tvDeliveryId.setText(context.getString(R.string.delivery_id_txt) + " - " + deliveryDTOArrayList.get(position).getOrderId());
            viewHolder.tvDeliveryDate.setText(context.getString(R.string.delivery_datein_txt) + " - " + deliveryDTOArrayList.get(position).getPickupDate());
            viewHolder.tvPickLoc.setText(context.getString(R.string.pickup_loc_txt) + " - " + deliveryDTOArrayList.get(position).getPickupaddress());
            viewHolder.tvDropLoc.setText(context.getString(R.string.delivery_loc_txt) + " - " + deliveryDTOArrayList.get(position).getDropoffaddress());

            try {
                viewHolder.tvPrice.setText(context.getString(R.string.us_dollar) + " " + String.format("%.2f", Double.parseDouble(deliveryDTOArrayList.get(position).getDriverDeliveryCost())));
            } catch (Exception e) {
                viewHolder.tvPrice.setText(context.getString(R.string.us_dollar));
                e.printStackTrace();
            }

            if (deliveryDTOArrayList.get(position).getVehicleType() != null && deliveryDTOArrayList.get(position).getVehicleType().equalsIgnoreCase(context.getString(R.string.bike))) {
                Glide.with(context)
                        .setDefaultRequestOptions(requestOptions1)
                        .load(R.drawable.bike_list)
                        .into(viewHolder.ivVehicle);
            } else if (deliveryDTOArrayList.get(position).getVehicleType() != null && deliveryDTOArrayList.get(position).getVehicleType().equalsIgnoreCase(context.getString(R.string.car))) {
                Glide.with(context)
                        .setDefaultRequestOptions(requestOptions1)
                        .load(R.drawable.car_list)
                        .into(viewHolder.ivVehicle);
            } else if (deliveryDTOArrayList.get(position).getVehicleType() != null && deliveryDTOArrayList.get(position).getVehicleType().equalsIgnoreCase(context.getString(R.string.van))) {
                Glide.with(context)
                        .setDefaultRequestOptions(requestOptions1)
                        .load(R.drawable.van_03)
                        .into(viewHolder.ivVehicle);
            } else {
                Glide.with(context)
                        .setDefaultRequestOptions(requestOptions1)
                        .load(R.drawable.truck_list)
                        .into(viewHolder.ivVehicle);
            }
            viewHolder.tvAccept.setOnClickListener(new OnItemClickListener(position, onItemClickCallback));
            viewHolder.itemView.setOnClickListener(new OnItemClickListener(position, onItemClickCallbackDetails));


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return deliveryDTOArrayList.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvPriceText, tvPrice, tvDeliveryId, tvDeliveryDate, tvPickLoc, tvDropLoc, tvAccept;
        private ImageViewCircular ivProfile;
        private ImageView ivVehicle;

        public ViewHolder(View view) {
            super(view);
            tvDeliveryId = (TextView) view.findViewById(R.id.tv_delivery_id);
            tvDeliveryDate = (TextView) view.findViewById(R.id.tv_delivery_date);
            tvPickLoc = (TextView) view.findViewById(R.id.tv_pickup_location);
            tvDropLoc = (TextView) view.findViewById(R.id.tv_delivery_location);
            ivProfile = (ImageViewCircular) view.findViewById(R.id.iv_profile);
            ivVehicle = (ImageView) view.findViewById(R.id.iv_vehicle);
            tvPriceText = (TextView) view.findViewById(R.id.tv_price_text);
            tvPrice = (TextView) view.findViewById(R.id.tv_price);
            tvAccept = (TextView) view.findViewById(R.id.tv_accept);
        }
    }

}






