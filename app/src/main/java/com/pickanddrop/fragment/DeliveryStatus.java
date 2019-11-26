package com.pickanddrop.fragment;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pickanddrop.R;
import com.pickanddrop.activities.CameraActivity;
import com.pickanddrop.activities.DrawerContentSlideActivity;
import com.pickanddrop.api.APIClient;
import com.pickanddrop.api.APIInterface;
import com.pickanddrop.databinding.DeliveryStatusBinding;
import com.pickanddrop.dto.DeliveryDTO;
import com.pickanddrop.dto.LoginDTO;
import com.pickanddrop.utils.AppConstants;
import com.pickanddrop.utils.AppSession;
import com.pickanddrop.utils.OnDialogConfirmListener;
import com.pickanddrop.utils.PermissionUtil;
import com.pickanddrop.utils.Utilities;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeliveryStatus extends BaseFragment implements View.OnClickListener, AppConstants {

    private AppSession appSession;
    private Utilities utilities;
    private Context context;
    private DeliveryStatusBinding deliveryStatusBinding;
    private RequestOptions requestOptions;
    private String TAG = DeliveryStatus.class.getName();
    private DeliveryDTO.Data deliveryDTO;
    private final int PERMISSIONS_REQUEST_READ_CONTACTS = 4125;
    String[] PERMISSIONS = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private String imagePath = "", receiverName = "";
    private String[] SIGNATURE_PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private Button btn_get_sign, mClear, mGetSign, mCancel;
    private final int PERMISSIONS_REQUEST_READ_SIGNATURE = 188;
    private File file;
    private Dialog dialog;
    private LinearLayout mContent;
    private View view;
    private signature mSignature;
    private Bitmap bitmap;
    private MultipartBody.Part itemsImage = null, signatureImage = null;
    private boolean signatureTaken = false;

    // Creating Separate Directory for saving Generated Images
    String DIRECTORY = Environment.getExternalStorageDirectory().getPath() + "/DigitSign/";
    String pic_name = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
    String StoredPath = DIRECTORY + pic_name + ".png";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null && getArguments().containsKey("deliveryDTO")) {
            deliveryDTO = getArguments().getParcelable("deliveryDTO");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        deliveryStatusBinding = DataBindingUtil.inflate(inflater, R.layout.delivery_status, container, false);
        return deliveryStatusBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getActivity();
        appSession = new AppSession(context);
        utilities = Utilities.getInstance(context);

        initView();
    }

    private void initView() {
        deliveryStatusBinding.btnSubmit.setOnClickListener(this);
        deliveryStatusBinding.ivPhoto.setOnClickListener(this);
        deliveryStatusBinding.ivSign.setOnClickListener(this);
        deliveryStatusBinding.ivBack.setOnClickListener(this);

        requestOptions = new RequestOptions();
        requestOptions.centerCrop();
        requestOptions.override(70, 70);
        requestOptions.placeholder(R.drawable.user_ic);
        requestOptions.error(R.drawable.user_ic);

        Glide.with(context)
                .setDefaultRequestOptions(requestOptions)
                .load(appSession.getUser().getData().getProfileImage())
                .into(deliveryStatusBinding.ivProfile);

        if (deliveryDTO.getVehicleType() != null && deliveryDTO.getVehicleType().equalsIgnoreCase(context.getString(R.string.bike))) {
            deliveryStatusBinding.ivVehicle.setImageResource(R.drawable.bike_list);
        } else if (deliveryDTO.getVehicleType() != null && deliveryDTO.getVehicleType().equalsIgnoreCase(context.getString(R.string.car))) {
            deliveryStatusBinding.ivVehicle.setImageResource(R.drawable.car_list);
        } else if (deliveryDTO.getVehicleType() != null && deliveryDTO.getVehicleType().equalsIgnoreCase(context.getString(R.string.van))) {
            deliveryStatusBinding.ivVehicle.setImageResource(R.drawable.van_03);
        } else {
            deliveryStatusBinding.ivVehicle.setImageResource(R.drawable.truck_list);
        }

        deliveryStatusBinding.tvPickupLocation.setText(getString(R.string.pickup_loc_txt) +" - "+ deliveryDTO.getPickupaddress());
        deliveryStatusBinding.tvDropAddress.setText(getString(R.string.delivery_loc_txt) +" - "+ deliveryDTO.getDropoffaddress());
        deliveryStatusBinding.tvDeliveryDate.setText(getString(R.string.delivery_datein_txt) +" - "+ deliveryDTO.getPickupDate());
        deliveryStatusBinding.tvDeliveryTime.setText(getString(R.string.delivery_time) +" - "+ deliveryDTO.getDeliveryTime());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_submit:
                receiverName = deliveryStatusBinding.etReceiver.getText().toString();
                if (isValid()){
                    callDeliveryOrderApi();
                }
                break;
            case R.id.iv_photo:
                if (hasPermissions(context, PERMISSIONS)) {
                    Intent intent1 = new Intent(context, CameraActivity.class);
                    startActivityForResult(intent1, 123);
                } else {
                    ActivityCompat.requestPermissions(((DrawerContentSlideActivity) context), PERMISSIONS, PERMISSIONS_REQUEST_READ_CONTACTS);
                }
                break;
            case R.id.iv_sign:
                if (hasPermissions(context, SIGNATURE_PERMISSIONS)) {
                    // Method to create Directory, if the Directory doesn't exists
                    file = new File(DIRECTORY);
                    if (!file.exists()) {
                        file.mkdir();
                    }

                    // Dialog Function
                    dialog = new Dialog(context);
                    // Removing the features of Normal Dialogs
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.dialog_signature);
                    dialog.setCancelable(true);

                    dialog_action();
                } else {
                    ActivityCompat.requestPermissions(((DrawerContentSlideActivity) context), SIGNATURE_PERMISSIONS, PERMISSIONS_REQUEST_READ_SIGNATURE);
                }
                break;
            case R.id.iv_back:
                ((DrawerContentSlideActivity) context).popFragment();
                break;
        }
    }

    public void callDeliveryOrderApi() {
        if (!utilities.isNetworkAvailable())
            utilities.dialogOK(context, "", context.getResources().getString(R.string.network_error), context.getString(R.string.ok), false);
        else {
            final ProgressDialog mProgressDialog;
            mProgressDialog = ProgressDialog.show(context, null, null);
            mProgressDialog.setContentView(R.layout.progress_loader);
            mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            mProgressDialog.setCancelable(false);

            Map<String, RequestBody> partMap = new HashMap<>();

            if (!imagePath.equals("")) {
                try {
                    File profileImageFile = new File(imagePath);
                    RequestBody propertyImage = RequestBody.create(MediaType.parse("image/*"), profileImageFile);
                    itemsImage = MultipartBody.Part.createFormData("client_image", profileImageFile.getName(), propertyImage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (!StoredPath.equals("")) {
                try {
                    File profileImageFile = new File(StoredPath);
                    RequestBody propertyImage = RequestBody.create(MediaType.parse("image/*"), profileImageFile);
                    signatureImage = MultipartBody.Part.createFormData("signature_image", profileImageFile.getName(), propertyImage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            partMap.put("order_id", RequestBody.create(MediaType.parse("order_id"), deliveryDTO.getOrderId()));
            partMap.put("code", RequestBody.create(MediaType.parse("code"), APP_TOKEN));
            partMap.put("signature_name", RequestBody.create(MediaType.parse("signature_name"), receiverName));

            APIInterface apiInterface = APIClient.getClient();
            Call<LoginDTO> call = apiInterface.callDeliverOrderApi(partMap, signatureImage, itemsImage);

            call.enqueue(new Callback<LoginDTO>() {
                @Override
                public void onResponse(Call<LoginDTO> call, Response<LoginDTO> response) {
                    if (mProgressDialog != null && mProgressDialog.isShowing())
                        mProgressDialog.dismiss();
                    if (response.isSuccessful()) {
                        try {
                            if (response.body().getResult().equalsIgnoreCase("success")) {
                                utilities.dialogOKre(context, "", response.body().getMessage(), getString(R.string.ok), new OnDialogConfirmListener() {
                                    @Override
                                    public void onYes() {
                                        ((DrawerContentSlideActivity) context).popAllFragment();
                                        replaceFragmentWithoutBack(R.id.container_main, new CurrentList(), "CurrentList");
                                    }

                                    @Override
                                    public void onNo() {

                                    }
                                });
                            } else {
                                utilities.dialogOK(context, "", response.body().getMessage(), context.getString(R.string.ok), false);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<LoginDTO> call, Throwable t) {
                    if (mProgressDialog != null && mProgressDialog.isShowing())
                        mProgressDialog.dismiss();
                    utilities.dialogOK(context, "", context.getResources().getString(R.string.server_error), context.getResources().getString(R.string.ok), false);
                    Log.e(TAG, t.toString());
                }
            });
        }
    }

    public boolean isValid(){
        if (!signatureTaken) {
            utilities.dialogOK(context, "", getString(R.string.please_take_signature_client), getString(R.string.ok), false);
            return false;
        } else if (imagePath == null || imagePath.equals("")) {
            utilities.dialogOK(context, "", getString(R.string.please_take_image_client), getString(R.string.ok), false);
            return false;
        } else if (receiverName == null || receiverName.equals("")) {
            utilities.dialogOK(context, "", getString(R.string.please_enter_name_client), getString(R.string.ok), false);
            deliveryStatusBinding.etReceiver.requestFocus();
            return false;
        }
        return true;
    }

    public boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[],
                                           int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_READ_CONTACTS: {
                if (PermissionUtil.verifyPermissions(grantResults)) {
                    Intent intent1 = new Intent(context, CameraActivity.class);
                    startActivityForResult(intent1, 123);
                } else {
                    Toast.makeText(context, getString(R.string.permission_required), Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 2 && requestCode == 123) {
            if (data.hasExtra("image")) {
                if (!data.getStringExtra("image").equals("")) {
                    appSession.setImagePath("");
                    imagePath = data.getStringExtra("image");
                    appSession.setImagePath(imagePath);

                    Glide.with(context)
                            .setDefaultRequestOptions(requestOptions)
                            .load(imagePath)
                            .into(deliveryStatusBinding.ivPhoto);

                }
            }
        }
    }

    // Function for Digital Signature
    public void dialog_action() {

        mContent = (LinearLayout) dialog.findViewById(R.id.linearLayout);
        mSignature = new signature(context, null);
        mSignature.setBackgroundColor(Color.WHITE);
        // Dynamically generating Layout through java code
        mContent.addView(mSignature, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mClear = (Button) dialog.findViewById(R.id.clear);
        mGetSign = (Button) dialog.findViewById(R.id.getsign);
        mGetSign.setEnabled(false);
        mCancel = (Button) dialog.findViewById(R.id.cancel);
        view = mContent;
        signatureTaken = false;

        mClear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v("log_tag", "Panel Cleared");
                mSignature.clear();
                mGetSign.setEnabled(false);
                signatureTaken = false;
//                StoredPath = "";
            }
        });

        mGetSign.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                Log.v("log_tag", "Panel Saved");
                view.setDrawingCacheEnabled(true);
                mSignature.save(view, StoredPath);
                dialog.dismiss();
//                Toast.makeText(context, "Successfully Saved", Toast.LENGTH_SHORT).show();
                // Calling the same class
//                recreate();

            }
        });

        mCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.v("log_tag", "Panel Canceled");
                dialog.dismiss();
                signatureTaken = false;
                // Calling the same class
//                recreate();

//                StoredPath = "";
            }
        });
        dialog.show();
    }

    public class signature extends View {

        private static final float STROKE_WIDTH = 10f;
        private static final float HALF_STROKE_WIDTH = STROKE_WIDTH / 2;
        private Paint paint = new Paint();
        private Path path = new Path();

        private float lastTouchX;
        private float lastTouchY;
        private final RectF dirtyRect = new RectF();

        public signature(Context context, AttributeSet attrs) {
            super(context, attrs);
            paint.setAntiAlias(true);
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setStrokeWidth(STROKE_WIDTH);
        }

        public void save(View v, String StoredPath) {
            Log.v("log_tag", "Width: " + v.getWidth());
            Log.v("log_tag", "Height: " + v.getHeight());
            if (bitmap == null) {
                bitmap = Bitmap.createBitmap(mContent.getWidth(), mContent.getHeight(), Bitmap.Config.RGB_565);
            }
            Canvas canvas = new Canvas(bitmap);
            try {
                // Output the file
                FileOutputStream mFileOutStream = new FileOutputStream(StoredPath);
                v.draw(canvas);

                // Convert the output file to Image such as .png
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, mFileOutStream);
                Glide.with(context).setDefaultRequestOptions(requestOptions).load(StoredPath).into(deliveryStatusBinding.ivSign);

                mFileOutStream.flush();
                mFileOutStream.close();

            } catch (Exception e) {
                Log.v("log_tag", e.toString());
            }

        }

        public void clear() {
            path.reset();
            invalidate();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawPath(path, paint);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float eventX = event.getX();
            float eventY = event.getY();
            mGetSign.setEnabled(true);
            signatureTaken = true;

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    path.moveTo(eventX, eventY);
                    lastTouchX = eventX;
                    lastTouchY = eventY;
                    return true;

                case MotionEvent.ACTION_MOVE:

                case MotionEvent.ACTION_UP:

                    resetDirtyRect(eventX, eventY);
                    int historySize = event.getHistorySize();
                    for (int i = 0; i < historySize; i++) {
                        float historicalX = event.getHistoricalX(i);
                        float historicalY = event.getHistoricalY(i);
                        expandDirtyRect(historicalX, historicalY);
                        path.lineTo(historicalX, historicalY);
                    }
                    path.lineTo(eventX, eventY);
                    break;

                default:
                    debug("Ignored touch event: " + event.toString());
                    return false;
            }

            invalidate((int) (dirtyRect.left - HALF_STROKE_WIDTH),
                    (int) (dirtyRect.top - HALF_STROKE_WIDTH),
                    (int) (dirtyRect.right + HALF_STROKE_WIDTH),
                    (int) (dirtyRect.bottom + HALF_STROKE_WIDTH));

            lastTouchX = eventX;
            lastTouchY = eventY;

            return true;
        }

        private void debug(String string) {

            Log.v("log_tag", string);

        }

        private void expandDirtyRect(float historicalX, float historicalY) {
            if (historicalX < dirtyRect.left) {
                dirtyRect.left = historicalX;
            } else if (historicalX > dirtyRect.right) {
                dirtyRect.right = historicalX;
            }

            if (historicalY < dirtyRect.top) {
                dirtyRect.top = historicalY;
            } else if (historicalY > dirtyRect.bottom) {
                dirtyRect.bottom = historicalY;
            }
        }

        private void resetDirtyRect(float eventX, float eventY) {
            dirtyRect.left = Math.min(lastTouchX, eventX);
            dirtyRect.right = Math.max(lastTouchX, eventX);
            dirtyRect.top = Math.min(lastTouchY, eventY);
            dirtyRect.bottom = Math.max(lastTouchY, eventY);
        }
    }
}
