package com.pickanddrop.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.pickanddrop.R;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by deepak on 6/10/16.
 */
public class CustomSpinnerForAll extends ArrayAdapter<HashMap<String, String>> {

    Context mContext;
    List<HashMap<String, String>> list;
    int layoutResourceId;
    View row = null;
    int textColor, hintColor, backgroundColor;
    boolean status = false;
    List<String> fuelType;

//    public CustomSpinnerForAll(Context mContext, int layoutResourceId, List<HashMap<String, String>> list, int textColor) {
//        super(mContext, layoutResourceId, list);
//        this.mContext = mContext;
//        this.list = list;
//        this.layoutResourceId = layoutResourceId;
//        this.textColor = textColor;
//        status = false;
//        // TODO Auto-generated constructor stub
//    }

    public CustomSpinnerForAll(Context mContext, int layoutResourceId, List<HashMap<String, String>> list, int textColor, int hintColor, int backgroundColor) {
        super(mContext, layoutResourceId, list);
        this.mContext = mContext;
        this.list = list;
        this.layoutResourceId = layoutResourceId;
        this.textColor = textColor;
        this.hintColor = hintColor;
        this.backgroundColor = backgroundColor;
        // TODO Auto-generated constructor stub
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomDropDownView(position, convertView, parent);
    }

    @Override
    public int getCount() {

        try {
            return (list.size());
        } catch (Exception e1) {
            e1.printStackTrace();

            return 0;
        } catch (Error e1) {
            e1.printStackTrace();

            return 0;
        }

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {
        try {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            TextView label = (TextView) row.findViewById(R.id.text);


            Log.i(getClass().getName(), Locale.getDefault().getDisplayLanguage());
            if (position == 0) {
                label.setTextColor(hintColor);
//                if (status){
////                    label.setGravity(Gravity.CENTER);
//                    label.setTextColor(mContext.getResources().getColor(R.color.black_text_color));
//                }else {
//                label.setTextColor(mContext.getResources().getColor(R.color.light_black));
//                }
            }
            else {
                label.setTextColor(textColor);
            }
            label.setText(list.get(position).get("value"));
            return row;
        } catch (Exception e) {
            e.printStackTrace();
            return row;
        }
    }

    public View getCustomDropDownView(int position, View convertView, ViewGroup parent) {
        try {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            TextView label = (TextView) row.findViewById(R.id.text);
            label.setSingleLine(false);
            label.setMaxLines(2);
            label.setText("  " + list.get(position).get("value") + "  ");
            if (position == 0) {
//                label.setBackgroundColor(mContext.getResources().getColor(
//                        R.color.white_color));
//                if (status){
////                    label.setGravity(Gravity.CENTER);
//                    label.setTextColor(mContext.getResources().getColor(
//                            R.color.black_text_color));
//                } else {
//                    label.setTextColor(mContext.getResources().getColor(
//                            R.color.light_black));
//                }

                label.setBackgroundColor(backgroundColor);
                label.setTextColor(hintColor);
            } else {
//                label.setBackgroundColor(mContext.getResources().getColor(
//                        R.color.white_color));
//                label.setTextColor(mContext.getResources().getColor(
//                        R.color.black_color));

                label.setBackgroundColor(backgroundColor);
                label.setTextColor(textColor);

            }
            return row;
        } catch (Exception e) {
            e.printStackTrace();
            return row;
        }
    }


}