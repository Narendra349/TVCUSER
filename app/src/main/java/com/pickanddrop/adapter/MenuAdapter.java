package com.pickanddrop.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pickanddrop.R;
import com.pickanddrop.utils.AppConstants;
import com.pickanddrop.utils.AppSession;
import com.pickanddrop.utils.OnItemClickListener;
import com.pickanddrop.utils.Utilities;

import java.util.ArrayList;
import java.util.HashMap;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> implements AppConstants {
    private Context context;
    private OnItemClickListener.OnItemClickCallback onItemClickCallback;
    private boolean claimStatus = false;
    private ArrayList<HashMap<String, String>> menuLIst;
    private AppSession appSession;
    private Utilities utilities;


    public MenuAdapter(Context context, OnItemClickListener.OnItemClickCallback onItemClickCallback, ArrayList<HashMap<String, String>> menuLIst, boolean claimStatus) {
        this.context = context;
        this.onItemClickCallback = onItemClickCallback;
        this.claimStatus = claimStatus;
        this.menuLIst = menuLIst;
        appSession = new AppSession(context);
        utilities = Utilities.getInstance(context);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_menu, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {
        viewHolder.tvName.setText(menuLIst.get(position).get(PN_NAME));

        Log.e(getClass().getName(),"Single Name >>>>>>>>>>"+menuLIst.get(position).get(PN_NAME));
        Log.e(getClass().getName(),"Single Image >>>>>>>>>>"+menuLIst.get(position).get(PN_VALUE));
        if (menuLIst.get(position).get(PN_VALUE) != null && !menuLIst.get(position).get(PN_VALUE).equals(""))
            viewHolder.ivMenu.setImageResource(Integer.parseInt(menuLIst.get(position).get(PN_VALUE)));


//        RequestOptions requestOptions = new RequestOptions();
//        Glide.with(context).setDefaultRequestOptions(requestOptions).load(menuLIst.get(position).get(PN_VALUE)).into(viewHolder.ivMenu);
        viewHolder.itemView.setOnClickListener(new OnItemClickListener(position, onItemClickCallback));
    }

    @Override
    public int getItemCount() {
        return menuLIst.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName;
        private ImageView ivMenu;

        public ViewHolder(View view) {
            super(view);
            tvName = (TextView) view.findViewById(R.id.textView);
            ivMenu = (ImageView) view.findViewById(R.id.imageView2);
        }
    }

}




