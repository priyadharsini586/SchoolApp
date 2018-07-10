package com.nickteck.schoolapp.fragment;


import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
import com.nickteck.schoolapp.model.AboutMyChildDetails;
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
import java.util.HashMap;

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
    TextView txtChildName,txtMobileNumber,txtChooseChild,txtBrodgeIcon,txtAnnouncementBrodgeIcon;
    Animation animSlideDown,txtNumberAnimation,profileImgAnimation;
    LinearLayout ldtChildName,ldtMobileNumber,ldtImage,ldtCalendar;
    boolean isNetworkConnected= false;
    TSnackbar tSnackbar;
    ApiInterface apiInterface;
    DataBaseHandler dataBaseHandler;
    public LinearLayout about_child,announcement,ldtBusTrack;
    CardView ldtChoiceChildren;

    ArrayList<Bitmap>bitmapArrayList = new ArrayList<>();
    ArrayList<String>bitmapStrArrayList = new ArrayList<>();
    private LinearLayout all_children_dialoge;
    private ArrayList<ParentDetails.student_details> getStudentNameArrayList  = new ArrayList<>();
    public static ArrayList<String> getStudentIdArrayList = new ArrayList<>();
    private String[] getStudentNameStringArray;
    private StudentCustomListAdapter studentCustomListAdapter;
    String childId = "-1";
    private LinearLayout about_events;
    public static final String MyPREFERENCES = "MyStudentId";

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

        about_events = (LinearLayout) mainView.findViewById(R.id.about_event);
        about_events.setOnClickListener(this);

        ldtChoiceChildren = (CardView) mainView.findViewById(R.id.ldtChoiceChildren);
        ldtChoiceChildren.setVisibility(View.INVISIBLE);

        txtBrodgeIcon = (TextView) mainView.findViewById(R.id.txtBrodgeIcon);
        txtBrodgeIcon.setVisibility(View.GONE);

        txtAnnouncementBrodgeIcon = (TextView) mainView.findViewById(R.id.txtAnnouncementBrodgeIcon);
        txtAnnouncementBrodgeIcon.setVisibility(View.GONE);

        ldtBusTrack = (LinearLayout) mainView.findViewById(R.id.ldtBusTrack);
        ldtBusTrack.setOnClickListener(this);

        frameMainLayout = mainView.findViewById(R.id.frameMainLayout);
        MyApplication.getInstance().setConnectivityListener(this);

        profile_image1 = (MultiImageView) mainView.findViewById(R.id.profile_image_dashBoard);
        all_children_dialoge = (LinearLayout) mainView.findViewById(R.id.all_children_dialoge);

        txtChooseChild= mainView.findViewById(R.id.txtChooseChild);

        ldtCalendar= (LinearLayout) mainView.findViewById(R.id.ldtCalendar);
        ldtCalendar.setOnClickListener(this);

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

        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.student_select_dialoge);
        final WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
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

        ImageView imgCloseDialog = dialog.findViewById(R.id.imgCloseDialog);
        imgCloseDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        studentCustomListAdapter = new StudentCustomListAdapter(getActivity(),getStudentNameArrayList);
        select_stu_list.setAdapter(studentCustomListAdapter);
        select_stu_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                txtChooseChild.setText(getStudentNameArrayList.get(i).getStudent_name());
                Log.e(TAG, "onItemClick: "+getStudentNameArrayList.get(i).getStudent_name() +" student id " +getStudentNameArrayList.get(i).getStudent_id());
                if (!getStudentNameArrayList.get(i).getStudent_id().equals("-1")) {
                    getSelectedChildern(getStudentNameArrayList.get(i).getStudent_id());
                    childId = getStudentNameArrayList.get(i).getStudent_id();


                }else {
                    childId = "-1";
                    setIntoView();
                }
                setBadgeIcon(childId);
                setAnnouncementIcon(childId);
                dialog.cancel();
            }
        });
        dialog.show();


    }

    private void setAnnouncementIcon(String childId) {

        int count = 0;
        AboutMyChildDetails aboutMyChildDetails = AboutMyChildDetails.getInstance();

        if (!childId.equals("-1")) {
            if (aboutMyChildDetails.getAnnouncementCount().size() != 0){
                Object value = aboutMyChildDetails.getAnnouncementCount().get(childId);


                if (value != null) {
                    count =(int) value;
                    txtAnnouncementBrodgeIcon.setVisibility(View.VISIBLE);
                    count = count + aboutMyChildDetails.getCommounAnnouncementCount();
                    txtAnnouncementBrodgeIcon.setText(String.valueOf(count));
                } else {
                    txtAnnouncementBrodgeIcon.setVisibility(View.GONE);
                }
            }

        }else {
            if (aboutMyChildDetails.getAnnouncementCount().size() != 0){
                ArrayList<Object> getAllCount = HelperClass.getAllvaluesFromHashMap(aboutMyChildDetails.getAnnouncementCount());
                int totalCount = 0;
                for (int  i= 0 ; i < getAllCount.size() ; i++){
                    int tocount =(int) getAllCount.get(i);
                    totalCount = totalCount + tocount;
                }
                txtAnnouncementBrodgeIcon.setVisibility(View.VISIBLE);
                totalCount = totalCount + aboutMyChildDetails.getCommounAnnouncementCount();
                txtAnnouncementBrodgeIcon.setText(String.valueOf(totalCount));
            }else {
                txtAnnouncementBrodgeIcon.setVisibility(View.GONE);
            }

        }
    }

    private void setBadgeIcon(String childId){
        String count = null;
        AboutMyChildDetails aboutMyChildDetails = AboutMyChildDetails.getInstance();

        if (!childId.equals("-1")) {
            if (aboutMyChildDetails.getStudentCount().size() != 0){
                Object value = aboutMyChildDetails.getStudentCount().get(childId);
                count = String.valueOf(value);

                if (!count.equals("0") && value != null) {
                    txtBrodgeIcon.setVisibility(View.VISIBLE);
                    txtBrodgeIcon.setText(count);
                } else {
                    txtBrodgeIcon.setVisibility(View.GONE);
                }
            }

        }else {
            if (aboutMyChildDetails.getStudentCount().size() != 0){
                ArrayList<Object> getAllCount = HelperClass.getAllvaluesFromHashMap(aboutMyChildDetails.getStudentCount());
                int totalCount = 0;
                for (int  i= 0 ; i < getAllCount.size() ; i++){
                    int tocount =(int) getAllCount.get(i);
                    totalCount = totalCount + tocount;
                }
                txtBrodgeIcon.setVisibility(View.VISIBLE);
                txtBrodgeIcon.setText(String.valueOf(totalCount));
            }

        }
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

        if (android.os.Build.VERSION.SDK_INT >= 17) {
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
        if (isNetworkConnected) {
            getDataFromServer();

        }
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
                        Log.e(TAG, "onResponse:"+response.isSuccessful() );
                        ParentDetails parentDetails = response.body();
                        if (parentDetails.getStatus_code() != null){
                            if (parentDetails.getStatus_code().equals(Constants.SUCESS)){
                                if (parentDetails.getDevice_id().equals(dataBaseHandler.getDeviceId())){

                                    JSONObject parentObject = parentDetails.toJSON();
                                    dataBaseHandler.dropParentDetails();
                                    dataBaseHandler.insertParentDetails(parentDetails.getParent_id(),parentObject.toString());
                                    setIntoView();
                                    getChildunReadCount();
                                }else {
                                    Toast.makeText(getActivity(),"Session Expired",Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                                    startActivity(intent);
                                    getActivity().finish();
                                }

                            }
                        }
                    }else{
                        setIntoView();
                        Toast.makeText(getActivity(),response.message(),Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ParentDetails> call, Throwable t) {
                    setIntoView();
                    Toast.makeText(getActivity(),"Server Error",Toast.LENGTH_LONG).show();
                }
            });

        }else {
            tSnackbar.show();
            setIntoView();
        }

    }

    private void setIntoView() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (dataBaseHandler.ifParentChildisExists()) {
                    String getParentDetails = dataBaseHandler.getParentChildDetails();
                    try {
                        JSONObject getParentObject = new JSONObject(getParentDetails);
                        txtChildName.setText(getParentObject.getString("parent_name"));
                        txtMobileNumber.setText(dataBaseHandler.getMobileNumber());
                        JSONArray getStudentArray = getParentObject.getJSONArray("student_details");
                        bitmapArrayList = new ArrayList<>();
                        bitmapStrArrayList = new ArrayList<>();
                        getStudentNameArrayList = new ArrayList<>();
                        getStudentIdArrayList = new ArrayList<>();
                        ParentDetails.student_details student_details = new ParentDetails.student_details();
                        student_details.setStudent_id("-1");
                        student_details.setStudent_name("All Children");
                        getStudentNameArrayList.add(student_details);
                        for (int i = 0; i < getStudentArray.length(); i++) {
                            JSONObject studObject = getStudentArray.getJSONObject(i);
                            if (studObject.has("student_photo")) {
                                String stuPhoto = studObject.getString("student_photo");
                                String studId = studObject.getString("student_id");
                                String studentClass = studObject.getString("student_std");
                                String studentSec = studObject.getString("student_section");

                              //  setDataInSharedPreferences(studId);
                                bitmapStrArrayList.add(stuPhoto);
                                if (studObject.has("student_name")) {
                                    String studentName = studObject.getString("student_name");
                                    ParentDetails.student_details student_detail = new ParentDetails.student_details();
                                    student_detail.setStudent_id(studId);
                                    student_detail.setStudent_name(studentName);
                                    student_detail.setStudent_std(studentClass);
                                    student_detail.setStudent_section(studentSec);

                                    getStudentNameArrayList.add(student_detail);
                                    getStudentIdArrayList.add(studId);
                                    Toast.makeText(getActivity(), ""+getStudentIdArrayList, Toast.LENGTH_SHORT).show();

                                }
                            }
                        }

                        if (getStudentArray.length() != 1) {
                            ldtChoiceChildren.setVisibility(View.VISIBLE);
                            ldtChoiceChildren.setVisibility(View.VISIBLE);
                        } else {
                            ldtChoiceChildren.setVisibility(View.GONE);
                        }

                        new LoadImage().execute();
                        profile_image1.setShape(MultiImageView.Shape.CIRCLE);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void setDataInSharedPreferences(String studId) {
        SharedPreferences sharedpreferences = getActivity().getSharedPreferences(MyPREFERENCES, getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("studentId", studId);
        editor.commit();


    }

    @Override
    public void onClick(View v) {
            Intent intent  = null;
            switch (v.getId()) {

                case R.id.about_child:
                    intent = new Intent(getActivity(), CommonFragmentActivity.class);
                    intent.putExtra("from",Constants.ABOUT_CHILD_FRAGMENT);
                    intent.putExtra("childId",childId);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);

                    break;

                case R.id.announcement:
                    intent = new Intent(getActivity(), CommonFragmentActivity.class);
                    intent.putExtra("from",Constants.ANNOUNEMENT_FRAGMENT);
                    intent.putExtra("childId",childId);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                    break;

                case R.id.ldtCalendar:
                    intent = new Intent(getActivity(), CommonFragmentActivity.class);
                    intent.putExtra("from",Constants.CALENDAR_FRAGMENT);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                    break;

                case R.id.about_event:
                    intent = new Intent(getActivity(), CommonFragmentActivity.class);
                    intent.putExtra("from",Constants.EVENTS_FRAGMENT);
                    startActivity(intent);
                    break;

                case R.id.ldtBusTrack:
                    intent = new Intent(getActivity(),CommonFragmentActivity.class);
                    intent.putExtra("from",Constants.BUS_TRACK_FRAGMENT);
                    startActivity(intent );
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
        public void onBitmapFailed(Exception e, Drawable errorDrawable) {
            Log.e(TAG, "onBitmapFailed " );
        }

       /* @Override
        public void onBitmapFailed(Drawable errorDrawable) {
            Log.e(TAG, "onBitmapFailed " );
        }*/

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
            Log.e(TAG, "onPrepareLoad: " );


        }
    };



    private class LoadImage extends AsyncTask<String,Void,Bitmap> {

        @Override
        protected Bitmap doInBackground(String... strings) {
            try {
                bitmapArrayList = new ArrayList<>();
                for (int i = 0; i < bitmapStrArrayList.size(); i++) {
                    String link = bitmapStrArrayList.get(i);
                    if (link != null && !link.isEmpty()) {
                        Bitmap bitmap = null;
                        if (isNetworkConnected) {
                             bitmap = Picasso.get().load(link).get();
                        }else {
                            bitmap = Picasso.get()
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
            profile_image1.clear();
            for (int i = 0 ; i <bitmapArrayList.size() ;i ++){
                profile_image1.addImage(bitmapArrayList.get(i));
            }
        }
    }



    public void getSelectedChildern(final String studentId){

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String getParentDetails = dataBaseHandler.getParentChildDetails();
                if (getParentDetails != null && !getParentDetails.isEmpty()){
                    try {
                        JSONObject getParentObject = new JSONObject(getParentDetails);
                        JSONArray getStudentArray = getParentObject.getJSONArray("student_details");
                        bitmapStrArrayList = new ArrayList<>();
                        for (int i = 0 ;i < getStudentArray.length() ; i ++) {
                            JSONObject studObject = getStudentArray.getJSONObject(i);
                            if (studObject.has("student_photo")) {
                                String stuPhoto = studObject.getString("student_photo");
                                String studId = studObject.getString("student_id");
                                if (studentId.equals(studId)) {
                                    bitmapStrArrayList.add(stuPhoto);
                                    if (studObject.has("student_name")) {
                                        String studentName = studObject.getString("student_name");
                                    }
                                }
                            }
                        }
                        new LoadImage().execute();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    public void getChildunReadCount(){
        if (isNetworkConnected){
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("parent_id", dataBaseHandler.getParentId());
            }catch (JSONException e){
                e.printStackTrace();
            }
            Call<AboutMyChildDetails>getMsgRead = apiInterface.getMsgCount(jsonObject);
            getMsgRead.enqueue(new Callback<AboutMyChildDetails>() {
                @Override
                public void onResponse(Call<AboutMyChildDetails> call, Response<AboutMyChildDetails> response) {
                    if (response.isSuccessful()){
                        AboutMyChildDetails aboutMyChildDetails = response.body();
                        if (aboutMyChildDetails.getStatus_code() != null){
                            if (aboutMyChildDetails.getStatus_code().equals(Constants.SUCESS)){
                                ArrayList<AboutMyChildDetails.students_details> myChildDetails = aboutMyChildDetails.getStudents_details();
                                HashMap<Object,Object>getChildCount = new HashMap<>();
                                int totalCount = 0 ;
                                for (int j = 0 ; j < myChildDetails.size() ; j++){
                                    AboutMyChildDetails.students_details  students_details = myChildDetails.get(j);
                                    totalCount =totalCount + Integer.valueOf(students_details.getMessage_count());
                                    if (Integer.valueOf(students_details.getMessage_count()) != 0)
                                        getChildCount.put(students_details.getStudent_id(), Integer.valueOf(students_details.getMessage_count()));
                                }
                                AboutMyChildDetails aboutMyChildDetailsCount = AboutMyChildDetails.getInstance();
                                aboutMyChildDetailsCount.setStudentCount(getChildCount);
                                if (totalCount != 0){
                                    txtBrodgeIcon.setVisibility(View.VISIBLE);
                                    txtBrodgeIcon.setText(String.valueOf(totalCount));
                                }

                                ArrayList<AboutMyChildDetails.special_announcement> special_announcementArrayList = aboutMyChildDetails.getSpecial_announcement();
                                HashMap<Object,Object>getAnnouncementList = new HashMap<>();
                                int announcementTotalCount = 0 ;
                                for (int j = 0 ; j < special_announcementArrayList.size() ; j++){
                                    AboutMyChildDetails.special_announcement  specialAnnouncement = special_announcementArrayList.get(j);
                                    announcementTotalCount =announcementTotalCount + specialAnnouncement.getCount();
                                    if (specialAnnouncement.getCount() != 0)
                                        getAnnouncementList.put(specialAnnouncement.getStudent_id(),specialAnnouncement.getCount());
                                }

                                aboutMyChildDetailsCount.setAnnouncementCount(getAnnouncementList);
                                aboutMyChildDetailsCount.setCommounAnnouncementCount(Integer.parseInt(aboutMyChildDetails.getCommon_announcement()));
                                if (announcementTotalCount != 0){
                                    txtAnnouncementBrodgeIcon.setVisibility(View.VISIBLE);
                                    announcementTotalCount = announcementTotalCount + aboutMyChildDetailsCount.getCommounAnnouncementCount();
                                    txtAnnouncementBrodgeIcon.setText(String.valueOf(announcementTotalCount));
                                }

                            }
                        }
                    }

                }

                @Override
                public void onFailure(Call<AboutMyChildDetails> call, Throwable t) {

                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
       /* AboutMyChildDetails aboutMyChildDetails = AboutMyChildDetails.getInstance();
        if (aboutMyChildDetails.getStudentCount().size() != 0){
            ArrayList<Object> getAllCount = HelperClass.getAllvaluesFromHashMAp(aboutMyChildDetails.getStudentCount());
            int totalCount = 0;
            for (int  i= 0 ; i < getAllCount.size() ; i++){
                int count =(int) getAllCount.get(i);
                totalCount = totalCount + count;
            }
            txtBrodgeIcon.setVisibility(View.VISIBLE);
            txtBrodgeIcon.setText(String.valueOf(totalCount));
        }else {
            txtBrodgeIcon.setVisibility(View.GONE);

        }*/
        setBadgeIcon(childId);
        setAnnouncementIcon(childId);
       /* if (aboutMyChildDetails.getAnnouncementCount().size() != 0){
            ArrayList<Object> getAllCount = HelperClass.getAllvaluesFromHashMAp(aboutMyChildDetails.getAnnouncementCount());
            int totalCount = 0;
            for (int  i= 0 ; i < getAllCount.size() ; i++){
                int count =(int) getAllCount.get(i);
                totalCount = totalCount + count;
            }
            txtAnnouncementBrodgeIcon.setVisibility(View.VISIBLE);
            txtAnnouncementBrodgeIcon.setText(String.valueOf(totalCount));
        }else {
            txtAnnouncementBrodgeIcon.setVisibility(View.GONE);

        }*/
    }



}
