package com.nickteck.schoolapp.fragment;


import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidadvance.topsnackbar.TSnackbar;
import com.nickteck.schoolapp.AdditionalClass.HelperClass;
import com.nickteck.schoolapp.R;
import com.nickteck.schoolapp.activity.CommonFragmentActivity;
import com.nickteck.schoolapp.activity.DashboardActivity;
import com.nickteck.schoolapp.activity.LoginActivity;
import com.nickteck.schoolapp.adapter.StudentCustomListAdapter;
import com.nickteck.schoolapp.api.ApiClient;
import com.nickteck.schoolapp.api.ApiInterface;
import com.nickteck.schoolapp.database.DataBaseHandler;
import com.nickteck.schoolapp.interfaces.OnBackPressedListener;
import com.nickteck.schoolapp.model.ParentDetails;
import com.nickteck.schoolapp.service.MyApplication;
import com.nickteck.schoolapp.service.NetworkChangeReceiver;
import com.nickteck.schoolapp.utilclass.Constants;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.stfalcon.multiimageview.MultiImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import hu.aut.utillib.circular.animation.CircularAnimationUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DashboardFragment extends Fragment  implements OnBackPressedListener, NetworkChangeReceiver.ConnectivityReceiverListener,View.OnClickListener {

    View mainView;
   // CircleImageView profile_image;
    private MultiImageView profile_image1;

    public int screenWidth,screenHeight;
    public View myView;
    RelativeLayout frameMainLayout;
    String TAG = DashboardFragment.class.getName();
    TextView txtChildName,txtMobileNumber;
    Animation animSlideDown,txtNumberAnimation,profileImgAnimation;
    LinearLayout ldtChildName,ldtMobileNumber,ldtImage;
    boolean isNetworkConnected= false;
    TSnackbar tSnackbar;
    ApiInterface apiInterface;
    DataBaseHandler dataBaseHandler;
    public LinearLayout about_child,announcement;
    CardView ldtChoiceChildren;

    ArrayList<Bitmap>bitmapArrayList = new ArrayList<>();
    ArrayList<String>bitmapStrArrayList = new ArrayList<>();
    private LinearLayout all_children_dialoge;
    private ArrayList<String> getStudentNameArrayList  = new ArrayList<>();
    private String[] getStudentNameStringArray;
    private StudentCustomListAdapter studentCustomListAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mainView = inflater.inflate(R.layout.fragment_dashboard, container, false);

        init();
        setOnclickListener();

        return mainView;
    }




    private void init() {
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

        about_child = (LinearLayout) mainView.findViewById(R.id.about_child);
        about_child.setOnClickListener(this);

        announcement = (LinearLayout) mainView.findViewById(R.id.announcement);
        announcement.setOnClickListener(this);

        ldtChoiceChildren = (CardView) mainView.findViewById(R.id.ldtChoiceChildren);
        ldtChoiceChildren.setVisibility(View.INVISIBLE);

        frameMainLayout = mainView.findViewById(R.id.frameMainLayout);
        MyApplication.getInstance().setConnectivityListener(this);

        profile_image1 = (MultiImageView) mainView.findViewById(R.id.profile_image_dashBoard);
        all_children_dialoge = (LinearLayout) mainView.findViewById(R.id.all_children_dialoge);



        if ((DashboardActivity)getActivity() != null)
            ((DashboardActivity) getActivity()).setOnBackPressedListener(this);
    }

    private void setOnclickListener() {
        all_children_dialoge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openDialoge();
            }
        });

    }

    private void openDialoge() {

        ListView select_stu_list;
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.student_select_dialoge);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        layoutParams.copyFrom(window.getAttributes());

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthLcl = (int) (displayMetrics.widthPixels*0.9f);
        int heightLcl =  WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.width = widthLcl;
        layoutParams.height = heightLcl;
        layoutParams.gravity = Gravity.CENTER;

        window.setAttributes(layoutParams);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        select_stu_list = (ListView) dialog.findViewById(R.id.select_stu_list);


        studentCustomListAdapter = new StudentCustomListAdapter(getActivity(),getStudentNameArrayList);
        select_stu_list.setAdapter(studentCustomListAdapter);
        dialog.show();


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
     //   profile_image = view.findViewById(R.id.profile_image);
        profile_image1 = view.findViewById(R.id.profile_image_dashBoard);

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
            JSONArray getStudentArray = getParentObject.getJSONArray("student_details");
            bitmapArrayList = new ArrayList<>();
            bitmapStrArrayList = new ArrayList<>();
            for (int i = 0 ;i < getStudentArray.length() ; i ++){
                JSONObject studObject = getStudentArray.getJSONObject(i);
                if (studObject.has("student_photo")) {
                    String stuPhoto = studObject.getString("student_photo");
                    bitmapStrArrayList.add(stuPhoto);
                }
            }
            if (getStudentArray.length() != 1) {
                try{
                    for(int i=0; i<=getStudentArray.length();i++){
                        JSONObject jsonObject = getStudentArray.getJSONObject(i);
                        if(jsonObject.has("student_name")){
                            String studentName = jsonObject.getString("student_name");
                            getStudentNameArrayList.add(studentName);
                         //  getStudentNameStringArray = getStudentNameArrayList.toArray(new String[getStudentNameArrayList.size()]);

                        }
                    }
                }catch (Exception e){
                    Toast.makeText(getActivity(), "Student Name is Empty", Toast.LENGTH_SHORT).show();
                }

                ldtChoiceChildren.setVisibility(View.VISIBLE);

            }
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    new loadImg().execute();
                }
            });

            profile_image1.setShape(MultiImageView.Shape.CIRCLE);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
            Intent intent  = null;
            switch (v.getId()) {

                case R.id.about_child:
                     intent = new Intent(getActivity(), CommonFragmentActivity.class);
                    intent.putExtra("from",Constants.ABOUT_CHILD_FRAGMENT);
                    startActivity(intent);
                    break;

                case R.id.announcement:
                    intent = new Intent(getActivity(), CommonFragmentActivity.class);
                    intent.putExtra("from",Constants.ANNOUNEMENT_FRAGMENT);
                    startActivity(intent);
                    break;

            }
    }

    Target target = new Target() {
        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            bitmapArrayList.add(bitmap);
            profile_image1.setTag(target);
            profile_image1.addImage(bitmap);
            Log.e(TAG, "onBitmapLoaded: " );
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
            Log.e(TAG, "onBitmapFailed " );
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
            Log.e(TAG, "onPrepareLoad: " );


        }
    };



    private class loadImg extends AsyncTask<String,Void,Bitmap> {

        @Override
        protected Bitmap doInBackground(String... strings) {
            try {
                for (int i = 0; i < bitmapStrArrayList.size(); i++) {
                    String link = bitmapStrArrayList.get(i);
                    if (link != null && !link.isEmpty()) {
                        Bitmap bitmap = null;
                        if (isNetworkConnected) {
                             bitmap = Picasso.with(getActivity()).load(link).get();
                        }else {
                            bitmap = Picasso.with(getActivity())
                                    .load(link)
                                    .networkPolicy(NetworkPolicy.OFFLINE)
                                    .get();
                        }
                        bitmapArrayList.add(bitmap);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap aVoid) {
            super.onPostExecute(aVoid);
            Log.e(TAG, "onPostExecute: "+bitmapArrayList.size() );
            for (int i = 0 ; i <bitmapArrayList.size() ;i ++){
                profile_image1.addImage(bitmapArrayList.get(i));
            }
        }
    }
}
