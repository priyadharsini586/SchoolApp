package com.nickteck.schoolapp.fragment;


import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidadvance.topsnackbar.TSnackbar;
import com.nickteck.schoolapp.AdditionalClass.HelperClass;
import com.nickteck.schoolapp.R;
import com.nickteck.schoolapp.activity.DashboardActivity;
import com.nickteck.schoolapp.activity.LoginActivity;
import com.nickteck.schoolapp.api.ApiClient;
import com.nickteck.schoolapp.api.ApiInterface;
import com.nickteck.schoolapp.database.DataBaseHandler;
import com.nickteck.schoolapp.interfaces.OnBackPressedListener;
import com.nickteck.schoolapp.model.LoginDetails;
import com.nickteck.schoolapp.model.ParentDetails;
import com.nickteck.schoolapp.service.MyApplication;
import com.nickteck.schoolapp.service.NetworkChangeReceiver;
import com.nickteck.schoolapp.utilclass.Constants;
import com.nickteck.schoolapp.utilclass.UtilClasses;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;
import hu.aut.utillib.circular.animation.CircularAnimationUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DashboardFragment extends Fragment  implements OnBackPressedListener, NetworkChangeReceiver.ConnectivityReceiverListener {

    View mainView;
    CircleImageView profile_image;
    private int screenWidth;
    private int screenHeight;
    private View myView;
    RelativeLayout frameMainLayout;
    String TAG = DashboardFragment.class.getName();
    TextView txtChildName,txtMobileNumber;
    Animation animSlideDown,txtNumberAnimation,profileImgAnimation;
    LinearLayout ldtChildName,ldtMobileNumber,ldtImage;
    boolean isNetworkConnected= false;
    TSnackbar tSnackbar;
    ApiInterface apiInterface;
    DataBaseHandler dataBaseHandler;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mainView = inflater.inflate(R.layout.fragment_dashboard, container, false);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        dataBaseHandler = new DataBaseHandler(getActivity());

        animSlideDown = AnimationUtils.loadAnimation(getActivity(),R.anim.textview_top_to_down);
        txtNumberAnimation = AnimationUtils.loadAnimation(getActivity(),R.anim.textview_top_to_down);
        profileImgAnimation = AnimationUtils.loadAnimation(getActivity(),R.anim.textview_top_to_down);

        txtChildName = (TextView) mainView.findViewById(R.id.txtChildName);
        txtChildName.setVisibility(View.VISIBLE);
        txtMobileNumber = (TextView)mainView.findViewById(R.id.txtMobileNumber);
        txtMobileNumber.setVisibility(View.VISIBLE);

        ldtChildName = (LinearLayout)mainView.findViewById(R.id.ldtChildName);
        ldtChildName.setVisibility(View.GONE);
        ldtMobileNumber = (LinearLayout)mainView.findViewById(R.id.ldtMobileNumber);
        ldtMobileNumber.setVisibility(View.GONE);
        ldtImage = (LinearLayout)mainView.findViewById(R.id.ldtImage);
        ldtImage.setVisibility(View.GONE);

        frameMainLayout = mainView.findViewById(R.id.frameMainLayout);
        MyApplication.getInstance().setConnectivityListener(this);

        if ((DashboardActivity)getActivity() != null)
            ((DashboardActivity) getActivity()).setOnBackPressedListener(this);

        init();
        onclickListener();

        return mainView;
    }



    private void init() {
    }
    private void onclickListener() {


    }

    private void openCustomDialoge(View v) {
        // common custom alert dialoge
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.custom_alert_dialoge);
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Display display = getActivity().getWindowManager().getDefaultDisplay();

        if (android.os.Build.VERSION.SDK_INT >= 13) {
            Point size = new Point();
            display.getSize(size);
            screenWidth = size.x;
            screenHeight = size.y;
        } else {
            screenWidth = display.getWidth();
            screenHeight = display.getHeight();
        }


        frameMainLayout = view.findViewById(R.id.frameMainLayout);
        myView = view.findViewById(R.id.linear);
        profile_image = view.findViewById(R.id.profile_image);

        //background imageview animation
        int[] myViewLocation = new int[2];
        myView.getLocationInWindow(myViewLocation);
        float finalRadius = CircularAnimationUtils.hypo(screenWidth - myViewLocation[0], screenHeight - myViewLocation[1]);
        int[] center = CircularAnimationUtils.getCenter(frameMainLayout, (View) myView.getParent());
        ObjectAnimator animator =
                CircularAnimationUtils.createCircularReveal(myView, center[0], center[1], 0, finalRadius);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(1500);
        animator.start();



        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                Log.e(TAG, "onAnimationEnd: " );
                ldtImage.setVisibility(View.VISIBLE);
                ldtImage.setAnimation(profileImgAnimation);


            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        profileImgAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ldtChildName.setVisibility(View.VISIBLE);
                ldtChildName.setAnimation(animSlideDown);
        }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        animSlideDown.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                ldtMobileNumber.setVisibility(View.VISIBLE);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ldtMobileNumber.setAnimation(txtNumberAnimation);
                    }
                });

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        tSnackbar = HelperClass.showTopSnackBar(mainView, "Network not connected");

        if (HelperClass.isNetworkAvailable(getActivity())) {
            isNetworkConnected = true;
            if (tSnackbar.isShown())
                tSnackbar.dismiss();
        }
        else {
            isNetworkConnected = false;
            tSnackbar.show();
        }
        if (isNetworkConnected)
            getDataFromServer();
        else
            setIntoView();


    }

    @Override
    public void onBackPressed() {
        ((DashboardActivity)getActivity()).finish();
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        isNetworkConnected = isConnected;
        if (!isConnected) {
            tSnackbar.show();
        }else
        {
            if (tSnackbar.isShown())
                tSnackbar.dismiss();
        }
    }


    public void getDataFromServer(){
        if (isNetworkConnected){
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("phone", dataBaseHandler.getMobileNumber());
            }catch (JSONException e){
                e.printStackTrace();
            }
            Call<ParentDetails> checkMobileNo = apiInterface.getParentDetails(jsonObject);
            checkMobileNo.enqueue(new Callback<ParentDetails>() {
                @Override
                public void onResponse(Call<ParentDetails> call, Response<ParentDetails> response) {
                    if (response.isSuccessful()){
                        ParentDetails parentDetails = response.body();
                        if (parentDetails.getStatus_code() != null){
                            if (parentDetails.getStatus_code().equals(Constants.SUCESS)){
                                if (parentDetails.getDevice_id().equals(dataBaseHandler.getDeviceId())){

                                    JSONObject parentObject = parentDetails.toJSON();
                                    dataBaseHandler.dropParentDetails();
                                    dataBaseHandler.insertParentDetails(parentDetails.getParent_id(),parentObject.toString());
                                    setIntoView();

                                }else {
                                    Toast.makeText(getActivity(),"Session Expired",Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                                    startActivity(intent);
                                    getActivity().finish();
                                }

                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<ParentDetails> call, Throwable t) {

                }
            });

        }else {
            tSnackbar.show();
            setIntoView();
        }

    }

    private void setIntoView() {
        String getParentDetails = dataBaseHandler.getParentChildDetails();
        try {
            JSONObject getParentObject = new JSONObject(getParentDetails);
            txtChildName.setText(getParentObject.getString("parent_name"));
            txtMobileNumber.setText(dataBaseHandler.getMobileNumber());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
