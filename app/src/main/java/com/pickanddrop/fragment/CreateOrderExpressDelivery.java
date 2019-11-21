package com.pickanddrop.fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TimePicker;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.pickanddrop.R;
import com.pickanddrop.activities.DrawerContentSlideActivity;
import com.pickanddrop.databinding.CreateOrderExpressDeliveryBinding;
import com.pickanddrop.dto.DeliveryDTO;
import com.pickanddrop.utils.AppConstants;
import com.pickanddrop.utils.AppSession;
import com.pickanddrop.utils.CustomSpinnerForAll;
import com.pickanddrop.utils.Utilities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

public class CreateOrderExpressDelivery extends BaseFragment implements AppConstants, View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private Context context;
    private AppSession appSession;
    private Utilities utilities;
    private CreateOrderExpressDeliveryBinding createOrderExpressDeliveryBinding;
    private String countryCode = "", deliveryType = "", pickupLat = "", pickupLong = "", companyName = "", firstName = "", lastName = "", mobile = "", pickUpAddress = "", itemDescription = "", itemQuantity = "", deliDate = "", deliTime = "", specialInstruction = ""
    ,pickDate = "", pickTime = "", pickupLiftGate = "", ClassGood = "", TypeGood = "", NoofPallets = "", productWidth = "", productHeight = "", productLength = "", productMeasure = "", productWeight;
    private PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
    private static final int REQUEST_PICK_PLACE = 2345;
    private int mYear, mMonth, mDay;
    private Calendar c;
    private DatePickerDialog datePickerDialog;
    private String TAG = CreateOrderOne.class.getName();
    private boolean rescheduleStatus = false;
    private DeliveryDTO.Data data;
    private ArrayList<HashMap<String, String>> measureTypeList;
    private CustomSpinnerForAll customSpinnerAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null && getArguments().containsKey("delivery_type")) {
            deliveryType = getArguments().getString("delivery_type");
            Log.e(TAG, deliveryType);
        }

        if (getArguments() != null && getArguments().containsKey("deliveryDTO")) {
            data = getArguments().getParcelable("deliveryDTO");
            rescheduleStatus = true;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        createOrderExpressDeliveryBinding = DataBindingUtil.inflate(inflater, R.layout.create_order_express_delivery, container, false);
        return createOrderExpressDeliveryBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getActivity();
        appSession = new AppSession(context);
        utilities = Utilities.getInstance(context);

        initView();
        initToolBar();

        if (rescheduleStatus) {
            setValues();
        }
    }

    private void setValues() {
        createOrderExpressDeliveryBinding.etFirstName.setText(data.getPickupFirstName());
        createOrderExpressDeliveryBinding.etLastName.setText(data.getPickupLastName());
        createOrderExpressDeliveryBinding.etMobile.setText(data.getPickupMobNumber());
        createOrderExpressDeliveryBinding.etPickupAddress.setText(data.getPickupaddress());
        createOrderExpressDeliveryBinding.etPickSpecialInst.setText(data.getPickupSpecialInst());

//                pickupLiftGate = createOrderExpressDeliveryBinding.etPickSpecialInst.getText().toString();
        createOrderExpressDeliveryBinding.etPickupDate.setText(data.getPickupDate());
        createOrderExpressDeliveryBinding.etPickupTime.setText(data.getPickupTime());
        createOrderExpressDeliveryBinding.etGoodClass.setText(data.getClassGoods());
        createOrderExpressDeliveryBinding.etGoodType.setText(data.getTypeGoods());
        createOrderExpressDeliveryBinding.etPalletsCount.setText(data.getNoOfPallets());
        createOrderExpressDeliveryBinding.etGoodWidth.setText(data.getProductWidth());
        createOrderExpressDeliveryBinding.etGoodHight.setText(data.getProductHeight());
        createOrderExpressDeliveryBinding.etGoodLength.setText(data.getProductLength());
        createOrderExpressDeliveryBinding.etPalletsTotalWeight.setText(data.getProductWeight());
        if(data.getPickupLiftGate().equals("insidePickup")){
            createOrderExpressDeliveryBinding.rbInsidePickup.setChecked(true);
        }else if(data.getPickupLiftGate().equals("liftGate")){
            createOrderExpressDeliveryBinding.rbLiftGate.setChecked(true);
        }


        createOrderExpressDeliveryBinding.ccp.setCountryForPhoneCode(Integer.parseInt(data.getPickupCountryCode()));

        deliveryType = data.getDeliveryType();
        pickupLat = data.getPickupLat();
        pickupLong = data.getPickupLong();

        createOrderExpressDeliveryBinding.etPickupAddress.setEnabled(false);
    }

    private void initToolBar() {

    }

    private void initView() {
//        createOrderExpressDeliveryBinding.ccp.registerPhoneNumberTextView(createOrderExpressDeliveryBinding.etMobile);
        c = Calendar.getInstance();
        measureTypeList = new ArrayList<>();
        createOrderExpressDeliveryBinding.ivBack.setOnClickListener(this);
        createOrderExpressDeliveryBinding.etPickupAddress.setOnClickListener(this);
        createOrderExpressDeliveryBinding.btnNext.setOnClickListener(this);
        createOrderExpressDeliveryBinding.etPickupDate.setOnClickListener(this);
        createOrderExpressDeliveryBinding.etPickupTime.setOnClickListener(this);


        HashMap<String, String> hashMap1 = new HashMap<>();
        hashMap1.put(PN_NAME, "");
        hashMap1.put(PN_VALUE, getResources().getString(R.string.feet));
        measureTypeList.add(hashMap1);
        hashMap1 = new HashMap<>();
        hashMap1.put(PN_NAME, "");
        hashMap1.put(PN_VALUE, getResources().getString(R.string.cm));
        measureTypeList.add(hashMap1);

        customSpinnerAdapter = new CustomSpinnerForAll(context, R.layout.spinner_textview, measureTypeList, getResources().getColor(R.color.black_color), getResources().getColor(R.color.light_black), getResources().getColor(R.color.transparent));
        createOrderExpressDeliveryBinding.spType.setAdapter(customSpinnerAdapter);

        createOrderExpressDeliveryBinding.spType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                if (i != 0) {
                    productMeasure = measureTypeList.get(i).get(PN_VALUE);
//                } else {
//                    productMeasure = "";
//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                if(productMeasure != null & !productMeasure.equals(""))
                  productMeasure = measureTypeList.get(0).get(PN_VALUE);
            }
        });
        createOrderExpressDeliveryBinding.rgLiftGate.setOnCheckedChangeListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_next:
                Utilities.hideKeyboard(createOrderExpressDeliveryBinding.btnNext);
                countryCode = createOrderExpressDeliveryBinding.ccp.getSelectedCountryCode();
                Log.e(TAG, ">>>>>>>>>>>>>>"+countryCode);

                firstName = createOrderExpressDeliveryBinding.etFirstName.getText().toString();
                lastName = createOrderExpressDeliveryBinding.etLastName.getText().toString();
                mobile = createOrderExpressDeliveryBinding.etMobile.getText().toString();
                pickUpAddress = createOrderExpressDeliveryBinding.etPickupAddress.getText().toString();
                specialInstruction = createOrderExpressDeliveryBinding.etPickSpecialInst.getText().toString();
//                pickupLiftGate = createOrderExpressDeliveryBinding.etPickSpecialInst.getText().toString();
                pickDate = createOrderExpressDeliveryBinding.etPickupDate.getText().toString();
                pickTime= createOrderExpressDeliveryBinding.etPickupTime.getText().toString();
                ClassGood= createOrderExpressDeliveryBinding.etGoodClass.getText().toString();
                TypeGood= createOrderExpressDeliveryBinding.etGoodType.getText().toString();
                NoofPallets= createOrderExpressDeliveryBinding.etPalletsCount.getText().toString();
                productWidth= createOrderExpressDeliveryBinding.etGoodWidth.getText().toString();
                productHeight= createOrderExpressDeliveryBinding.etGoodHight.getText().toString();
                productLength= createOrderExpressDeliveryBinding.etGoodLength.getText().toString();
//                productMeasure= createOrderExpressDeliveryBinding.et.getText().toString();
                productWeight= createOrderExpressDeliveryBinding.etPalletsTotalWeight.getText().toString();

                if (isValid()) {
                    CreateOrderExpressDeliveryDrop createOrderExpressDeliveryDrop = new CreateOrderExpressDeliveryDrop();
                    DeliveryDTO.Data deliveryDTO = null;
                    Bundle bundle = new Bundle();
                    if (rescheduleStatus) {
                        deliveryDTO = data;
                        bundle.putString("rescheduleStatus", "rescheduleStatus");
                    } else {
                        deliveryDTO = new DeliveryDTO().new Data();
                    }

                    deliveryDTO.setPickupFirstName(firstName);
                    deliveryDTO.setPickupLastName(lastName);
                    deliveryDTO.setPickupMobNumber(mobile);
                    deliveryDTO.setPickupaddress(pickUpAddress);
                    deliveryDTO.setPickupLiftGate(pickupLiftGate);
                    deliveryDTO.setPickupDate(pickDate);
                    deliveryDTO.setPickupTime(pickTime);
                    deliveryDTO.setClassGoods(ClassGood);
                    deliveryDTO.setTypeGoods(TypeGood);
                    deliveryDTO.setNoOfPallets(NoofPallets);
                    deliveryDTO.setProductWidth(productWidth);
                    deliveryDTO.setProductHeight(productHeight);
                    deliveryDTO.setProductLength(productLength);
                    deliveryDTO.setProductLength(productMeasure);
                    deliveryDTO.setProductLength(productWeight);
                    deliveryDTO.setPickupSpecialInst(specialInstruction);
                    deliveryDTO.setPickupLat(pickupLat);
                    deliveryDTO.setPickupLong(pickupLong);
                    deliveryDTO.setDeliveryType(deliveryType);
                    deliveryDTO.setPickupCountryCode(countryCode);

                    bundle.putParcelable("deliveryDTO", deliveryDTO);
                    createOrderExpressDeliveryDrop.setArguments(bundle);
                    addFragmentWithoutRemove(R.id.container_main, createOrderExpressDeliveryDrop, "CreateOrderExpressDeliveryDrop");
                }
                break;
            case R.id.iv_back:
                ((DrawerContentSlideActivity) context).popFragment();
                break;
            case R.id.et_pickup_address:
                try {
                    startActivityForResult(builder.build(getActivity()), REQUEST_PICK_PLACE);
                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.et_pickup_date:
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                createOrderExpressDeliveryBinding.etPickupDate.setText(Utilities.formatDateShow(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth));
                            }
                        }, mYear, mMonth, mDay);
//                calendar.add(Calendar.YEAR, -18);
                datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
                datePickerDialog.show();
                break;
            case R.id.et_pickup_time:
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
//                        etTime.setText(selectedHour + ":" + selectedMinute);
                        createOrderExpressDeliveryBinding.etPickupTime.setText(updateTime(selectedHour, selectedMinute));
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
                break;
        }
    }

    // Used to convert 24hr format to 12hr format with AM/PM values
    private String updateTime(int hours, int mins) {

        String timeSet = "";
        if (hours > 12) {
            hours -= 12;
            timeSet = "PM";
        } else if (hours == 0) {
            hours += 12;
            timeSet = "AM";
        } else if (hours == 12)
            timeSet = "PM";
        else
            timeSet = "AM";


        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

        // Append in a StringBuilder
        String aTime = new StringBuilder().append(hours).append(':')
                .append(minutes).append(" ").append(timeSet).toString();

        return aTime;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PICK_PLACE && resultCode == RESULT_OK) {
            Place place = PlacePicker.getPlace(context, data);

            Log.i(getClass().getName(), "Class is >>>>>" + place.getName() + " " + place.getAddress() + "   " + place.getLatLng());
            pickupLat = place.getLatLng().latitude + "";
            pickupLong = place.getLatLng().longitude + "";
            createOrderExpressDeliveryBinding.etPickupAddress.setText(getAddressFromLatLong(place.getLatLng().latitude, place.getLatLng().longitude, false));
        }
    }

    public String getAddressFromLatLong(double latitude, double longitude, boolean status) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(context, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();

            return address +" "+city;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public boolean isValid() {
        if (firstName == null || firstName.equals("")) {
            utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_enter_first_name), getString(R.string.ok), false);
            createOrderExpressDeliveryBinding.etFirstName.requestFocus();
            return false;
        } else if (lastName == null || lastName.equals("")) {
            utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_enter_last_name), getString(R.string.ok), false);
            createOrderExpressDeliveryBinding.etLastName.requestFocus();
            return false;
        } else if (mobile.trim().length() == 0) {
            utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_enter_mobile_number), getString(R.string.ok), false);
            createOrderExpressDeliveryBinding.etMobile.requestFocus();
            return false;
        } else if (!utilities.checkMobile(mobile)) {
            utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_enter_valid_mobile_number), getString(R.string.ok), false);
            createOrderExpressDeliveryBinding.etMobile.requestFocus();
            return false;
        } else if (pickUpAddress == null || pickUpAddress.equals("")) {
            utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_select_pick_address), getString(R.string.ok), false);
            createOrderExpressDeliveryBinding.etPickupAddress.requestFocus();
            return false;
        } else if (pickDate == null || pickTime.equals("")) {
            utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_select_deli_date), getString(R.string.ok), false);
            createOrderExpressDeliveryBinding.etPickupDate.requestFocus();
            return false;
        } else if (ClassGood == null || ClassGood.equals("")) {
            utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_enter_class_good), getString(R.string.ok), false);
            createOrderExpressDeliveryBinding.etGoodClass.requestFocus();
            return false;
        } else if (TypeGood == null || TypeGood.equals("")) {
            utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_enter_type_good), getString(R.string.ok), false);
            createOrderExpressDeliveryBinding.etGoodType.requestFocus();
            return false;
        }else if (NoofPallets == null || NoofPallets.equals("")) {
            utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_enter_no_of_pallets), getString(R.string.ok), false);
            createOrderExpressDeliveryBinding.etPalletsCount.requestFocus();
            return false;
        }else if (productWidth == null || productWidth.equals("")) {
            utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_enter_parcel_width), getString(R.string.ok), false);
            createOrderExpressDeliveryBinding.etGoodWidth.requestFocus();
            return false;
        }else if (productHeight == null || productHeight.equals("")) {
            utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_enter_parcel_height), getString(R.string.ok), false);
            createOrderExpressDeliveryBinding.etGoodHight.requestFocus();
            return false;
        }else if (productLength == null || productLength.equals("")) {
            utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_enter_parcel_length), getString(R.string.ok), false);
            createOrderExpressDeliveryBinding.etGoodLength.requestFocus();
            return false;
        }else if (productWeight == null || productWeight.equals("")) {
            utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_enter_parcel_weight), getString(R.string.ok), false);
            createOrderExpressDeliveryBinding.etPalletsTotalWeight.requestFocus();
            return false;
        }else if (pickupLiftGate == null || pickupLiftGate.equals("")) {
            utilities.dialogOK(context, getString(R.string.validation_title), getString(R.string.please_select_pickup_lifegate_date), getString(R.string.ok), false);
            createOrderExpressDeliveryBinding.rgLiftGate.requestFocus();
            return false;
        }
        return true;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
      switch (checkedId){
          case R.id.rb_inside_pickup:
              pickupLiftGate = "insidePickup";
              break;
          case R.id.rb_lift_gate:
              pickupLiftGate = "liftGate";
              break;
      }
    }
}