package com.pickanddrop.activities;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pickanddrop.R;
import com.pickanddrop.fragment.Review;

public class NotificationDialog extends AppCompatActivity implements OnClickListener {
    private Context context = this;
    private TextView tvMessage, tvTitleDailogBox;
    private TextView tv_no, tv_yes;
    String order_id = "", title = "", message = "", type = "", id = "", image = "", orderId = "", orderStatus = "", status = "",
            storeStatus = "", notification_id = "", inviteUnique = "", fullName = "", profileImage = "", rating = "";
    String yes_no = "";
    Bundle bundle;
    ImageView iv_banner;
    private ProgressDialog progressDialog;
    private MediaPlayer mp;
    private RequestOptions requestOptions;
    private LinearLayout llPopup;
    private FrameLayout frameLayout;

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_notification);
            context = this;


            Window window = getWindow();
            window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT);
            window.setBackgroundDrawable(new ColorDrawable(0));
            Dialog dialog = new Dialog(context);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            // dialog.setContentView(R.layout.dialog_box_ok);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            tvMessage = (TextView) findViewById(R.id.tv_message);
            iv_banner = (ImageView) findViewById(R.id.iv_banner);
            tvTitleDailogBox = (TextView) findViewById(R.id.title_dailog_box);
            tvMessage.setMovementMethod(new ScrollingMovementMethod());
            tv_yes = (TextView) findViewById(R.id.tv_yes);
            tv_no = (TextView) findViewById(R.id.tv_no);
            tv_yes.setOnClickListener(this);
            tv_no.setOnClickListener(this);
            llPopup = (LinearLayout) findViewById(R.id.ll_main);
            frameLayout = (FrameLayout) findViewById(R.id.container_notification);

            bundle = getIntent().getExtras();
            mp = MediaPlayer.create(this, R.raw.notification);
            mp.setLooping(false);
            mp.start();

            requestOptions = new RequestOptions();
            requestOptions.centerCrop();
            requestOptions.override(150, 150);
            requestOptions.placeholder(R.drawable.user_praba);
            requestOptions.error(R.drawable.user_praba);

            if (bundle != null) {
                type = bundle.getString("type");
                message = bundle.getString("message");
                title = bundle.getString("title");
                id = bundle.getString("user_type");
                image = bundle.getString("filename");
                notification_id = bundle.getString("notification_id");
                inviteUnique = bundle.getString("inviteunique");

                fullName = bundle.getString("full_name");
                profileImage = bundle.getString("profile_image");
                rating = bundle.getString("rating");
                order_id = bundle.getString("order_id");

            }

            if (!profileImage.equals("") || !rating.equals("")) {
                tv_no.setVisibility(View.VISIBLE);
                tv_no.setText("Rate");
            }

            tvMessage.setText(Html.fromHtml(message));
            tvTitleDailogBox.setText(Html.fromHtml(title));
            if (!image.equals("")) {
                iv_banner.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .setDefaultRequestOptions(requestOptions)
                        .load(image)
                        .into(iv_banner);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_no:
                if (mp.isPlaying()) {
                    mp.stop();
                }

                if (!profileImage.equals("") || !rating.equals("")){
                    llPopup.setVisibility(View.GONE);
                    frameLayout.setVisibility(View.VISIBLE);

                    Bundle bundle = new Bundle();
                    Review review = new Review();
                    bundle.putString("profileImage", profileImage);
                    bundle.putString("rating", rating);
                    bundle.putString("notification_id", notification_id);
                    bundle.putString("fullName", fullName);
                    bundle.putString("order_id", order_id);
                    review.setArguments(bundle);

                    getSupportFragmentManager().beginTransaction().replace(R.id.container_notification, review).commit();
//                    Intent intent = new Intent(NotificationDialog.this, DrawerContentSlideActivity.class);
//                    intent.putExtra("profileImage", profileImage);
//                    intent.putExtra("rating", rating);
//                    intent.putExtra("notification_id", notification_id);
//                    intent.putExtra("fullName", fullName);
//                    intent.putExtra("order_id", order_id);
//                    startActivity(intent);
//                    finish();

//                    Toast.makeText(context, "Review popup show", Toast.LENGTH_SHORT).show();
                } else {
                    finish();
                }
                break;
            case R.id.tv_yes:
                if (mp.isPlaying()) {
                    mp.stop();
                }

                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mp.stop();
    }
}
