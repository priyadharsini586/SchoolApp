package com.nickteck.schoolapp.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.nickteck.schoolapp.AdditionalClass.HelperClass;
import com.nickteck.schoolapp.R;
import com.nickteck.schoolapp.adapter.ViewPagerAdapter;
import com.nickteck.schoolapp.api.ApiClient;
import com.nickteck.schoolapp.api.ApiInterface;
import com.nickteck.schoolapp.model.LoginDetails;
import com.nickteck.schoolapp.service.MyApplication;
import com.nickteck.schoolapp.service.NetworkChangeReceiver;
import com.nickteck.schoolapp.utilclass.Constants;
import com.stfalcon.smsverifycatcher.OnSmsCatchListener;
import com.stfalcon.smsverifycatcher.SmsVerifyCatcher;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity implements NetworkChangeReceiver.ConnectivityReceiverListener {



    ArrayList<Integer> sliderImages = new ArrayList<>();
    int page = 0;
    private ArrayList<LoginDetails> imageModelArrayList;
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    CirclePageIndicator indicator;
    private int [] sliderList = {R.drawable.slide_1,R.drawable.silde_2,R.drawable.slide_3,R.drawable.ic_splash_screen};
    private EditText mMobileNo;
    private boolean isPhone;
    private EditText meditActivationCode;
    private CircularProgressButton mactivationSumbit;
    CircularProgressButton mbtnSubmit;
    private SmsVerifyCatcher smsVerifyCatcher;
    ArrayList<String> permissions = new ArrayList<>();
    ArrayList<String> permissionsToRequest;
    ArrayList<String> permissionsRejected = new ArrayList<>();
    private String getMobileNo;
    ApiInterface apiInterface;
    boolean isNetworkConnected;
    RelativeLayout mainView;
    boolean netWorkConnection;
    private String deviceId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        mainView = (RelativeLayout) findViewById(R.id.sclMainView);



        // getting permission for app
        getPermission();

        init();
        setOnClickListener();
        imageSlider();
    }

    private void getPermission() {
        //  permissions.add(Manifest.permission.GET_ACCOUNTS);
         permissions.add(Manifest.permission.READ_SMS);
         permissions.add(Manifest.permission.RECEIVE_SMS);
         permissions.add(Manifest.permission.SEND_SMS);
       /* permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        permissions.add(Manifest.permission.CAMERA);
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);*/
        permissionsToRequest = findUnAskedPermissions(permissions);

        MyApplication.getInstance().setConnectivityListener(this);
        if (HelperClass.isNetworkAvailable(getApplicationContext()))
            netWorkConnection = true;
        else
            netWorkConnection = false;
    }

    private void init() {
        imageModelArrayList =  populateList();
        sliderImages.add(R.drawable.slide_1);
        sliderImages.add(R.drawable.silde_2);
        sliderImages.add(R.drawable.slide_3);
        sliderImages.add(R.drawable.ic_splash_screen);
        indicator = (CirclePageIndicator) findViewById(R.id.indicator);
        mPager = (ViewPager) findViewById(R.id.viewPager);
        mMobileNo = (EditText) findViewById(R.id.editPhoneNo);
        mbtnSubmit = (CircularProgressButton) findViewById(R.id.btnSubmit);
        meditActivationCode = (EditText)findViewById(R.id.editActivationCode);
        mactivationSumbit = (CircularProgressButton) findViewById(R.id.activationSumbit);


        //init SmsVerifyCatcher
        smsVerifyCatcher = new SmsVerifyCatcher(this, new OnSmsCatchListener<String>() {
            @Override
            public void onSmsCatch(String message) {
                String code = parseCode(message);//Parse verification code
                meditActivationCode.setText(code);//set code in edit text
                //then you can send verification code to server
            }
        });


    }

    private boolean hasPermission(String permission) {
        if (canAskPermission()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canAskPermission() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    private ArrayList findUnAskedPermissions(ArrayList<String> wanted) {
        ArrayList result = new ArrayList();

        for (String perm : wanted) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }

        return result;
    }

    private ArrayList<LoginDetails> populateList(){

        ArrayList<LoginDetails> list = new ArrayList<>();

        for(int i = 0; i <sliderList.length; i++){
            LoginDetails imageModel = new LoginDetails();
            imageModel.setImage_drawable(sliderList[i]);
            list.add(imageModel);
        }
        return list;
    }



    private void setOnClickListener() {
        mbtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isPhone = HelperClass.isValidMobile(mMobileNo.getText().toString());
                //check for mobile no is valid
                if (!isPhone){
                    mbtnSubmit.startAnimation();
                    checkLogin();
                }else {
                    validation();
                }
            }
        });
        mactivationSumbit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                mactivationSumbit.startAnimation();
                Intent intent = new Intent(getApplicationContext(),DashboardActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void checkLogin() {
        if (netWorkConnection){
            getDeviceId();
            getMobileNo = mMobileNo.getText().toString();
            // api call for the add  mobile no validation
            apiInterface = ApiClient.getClient().create(ApiInterface.class);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("phone", getMobileNo);
                jsonObject.put("device_id", deviceId);
            }catch (JSONException e){
                e.printStackTrace();
            }
            Call<LoginDetails> checkMobileNo = apiInterface.checkMobileNo(jsonObject);
            checkMobileNo.enqueue(new Callback<LoginDetails>() {

                @Override
                public void onResponse(Call<LoginDetails> call, Response<LoginDetails> response) {
                    if (response.isSuccessful()){
                        LoginDetails loginDetails = response.body();
                        mbtnSubmit.stopAnimation();
                        if (loginDetails.getStatus_code() != null) {
                            if (loginDetails.getStatus_code().equals(Constants.SUCESS)) {
                                Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                                startActivity(intent);
                                finish();
                             //   getOptApi();
                            } else if (loginDetails.getStatus_code().equals(Constants.NOT_VERIFIED)) {
                                Toast.makeText(LoginActivity.this, loginDetails.getStatus_message(), Toast.LENGTH_SHORT).show();
                                getOptApi();


                            } else if (loginDetails.getStatus_code().equals(Constants.FAILURE)) {
                                Toast.makeText(LoginActivity.this, loginDetails.getStatus_message(), Toast.LENGTH_SHORT).show();

                            }
                        }

                    }

                }
                @Override
                public void onFailure(Call<LoginDetails> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, "Server Error", Toast.LENGTH_SHORT).show();

                }
            });

        }else {
            HelperClass.showTopSnackBar(mainView,"Network not connected");
        }

        // checking for opt
        // checkForOtp();
    }

    private void getDeviceId() {
         deviceId = Settings.Secure.getString(LoginActivity.this.getContentResolver(), Settings.Secure.ANDROID_ID);


    }

    private void getOptApi() {
        // get opt if status code is -1
        if (netWorkConnection) {
            getMobileNo = mMobileNo.getText().toString();
            apiInterface = ApiClient.getClient().create(ApiInterface.class);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("phone", getMobileNo);
            }catch (JSONException e){
                e.printStackTrace();
            }
            Call<LoginDetails> checkMobileNo = apiInterface.checkOpt(jsonObject);
            checkMobileNo.enqueue(new Callback<LoginDetails>() {

                @Override
                public void onResponse(Call<LoginDetails> call, Response<LoginDetails> response) {
                    if (response.isSuccessful()) {
                        LoginDetails loginDetails = response.body();
                        if (loginDetails.getStatus_code() != null) {
                            if (loginDetails.getStatus_code().equals(Constants.SUCESS)) {
                                Toast.makeText(LoginActivity.this, "otp generated successfully", Toast.LENGTH_SHORT).show();
                                enableActivationBox();
                            }else {
                                Toast.makeText(LoginActivity.this, "Unregistered user", Toast.LENGTH_SHORT).show();
                            }


                        }
                    }

                }

                @Override
                public void onFailure(Call<LoginDetails> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                }
            });


        }else {
            HelperClass.showTopSnackBar(mainView,"Network not connected");
        }
    }

    private void enableActivationBox(){
        // after login api sucess then only it should start
        mMobileNo.setVisibility(View.GONE);
        mbtnSubmit.setVisibility(View.GONE);
        meditActivationCode.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this,R.anim.edit_text_animation));
        mactivationSumbit.startAnimation(AnimationUtils.loadAnimation(LoginActivity.this,R.anim.edit_text_animation));
        meditActivationCode.setVisibility(View.VISIBLE);
        mactivationSumbit.setVisibility(View.VISIBLE);
        checkForOtp();

    }

    private void checkForOtp() {
        //init SmsVerifyCatcher
        smsVerifyCatcher = new SmsVerifyCatcher(this, new OnSmsCatchListener<String>() {
            @Override
            public void onSmsCatch(String message) {
                String code = parseCode(message);//Parse verification code
                meditActivationCode.setText(code);//set code in edit text
                //then you can send verification code to server
            }
        });

        //set phone number filter if needed
        smsVerifyCatcher.setPhoneNumberFilter("777");


    }

    /**
     * Parse verification code
     *
     * @param message sms message
     * @return only four numbers from massage string
     */
    private String parseCode(String message) {
        Pattern p = Pattern.compile("\\b\\d{4}\\b");
        Matcher m = p.matcher(message);
        String code = "";
        while (m.find()) {
            code = m.group(0);
        }
        return code;
    }

    public void validation() {
        if (!isPhone) {
            mMobileNo.setError("Invalid Phone number");
        }

    }

    private void imageSlider() {
        mPager.setAdapter(new ViewPagerAdapter(LoginActivity.this,imageModelArrayList));
        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;
        indicator.setRadius(3 * density);
        NUM_PAGES =imageModelArrayList.size();

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };

        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 1000, 1000);


        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        smsVerifyCatcher.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        smsVerifyCatcher.onStop();
    }

    /**
     * need for Android 6 real time permissions
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        smsVerifyCatcher.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {

        netWorkConnection = isConnected;
        if (mainView != null) {
            if (!isConnected)
                HelperClass.showTopSnackBar(mainView,"Network not connected");
        }

    }
}
