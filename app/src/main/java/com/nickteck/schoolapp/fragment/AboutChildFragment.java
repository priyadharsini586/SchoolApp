package com.nickteck.schoolapp.fragment;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidadvance.topsnackbar.TSnackbar;
import com.nickteck.schoolapp.AdditionalClass.HelperClass;
import com.nickteck.schoolapp.R;
import com.nickteck.schoolapp.activity.CommonFragmentActivity;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AboutChildFragment extends Fragment implements NetworkChangeReceiver.ConnectivityReceiverListener,OnBackPressedListener {
    View mainView;
    LinearLayout ldtMainAboutView,ldtImageView,ldtMessageView;
    View view = null;
    DataBaseHandler dataBaseHandler;
    TextView txtStudentName, txtTeacherName;
    boolean isNetworkConnected = false;
    TSnackbar tSnackbar;
    ApiInterface apiInterface;
    String childId;
    ArrayList<String> bitmapArrayList = new ArrayList<>();
    ProgressBar progressLoadAboutChilg;
    Toolbar toolBarTitle;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mainView = inflater.inflate(R.layout.fragment_about_child, container, false);
        dataBaseHandler = new DataBaseHandler(getActivity());
        MyApplication.getInstance().setConnectivityListener(this);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        initView();


        return mainView;
    }

    private void initView() {

        ldtMainAboutView = (LinearLayout) mainView.findViewById(R.id.ldtMainAboutView);
        progressLoadAboutChilg = (ProgressBar) mainView.findViewById(R.id.progressLoadAboutChilg);
        progressLoadAboutChilg.setVisibility(View.VISIBLE);
        toolBarTitle = (Toolbar) getActivity().findViewById(R.id.toolBarTitle);
        TextView toolBarTextView = (TextView) toolBarTitle.findViewById(R.id.toolBarTextView);
        toolBarTextView.setText("About Child");

    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        isNetworkConnected = isConnected;
        if (!isConnected) {
            tSnackbar.show();
        }else {
            if (tSnackbar.isShown())
                tSnackbar.dismiss();
        }
    }
    public void getDataFromServer(){
        if (isNetworkConnected){
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("student_id", childId);
            }catch (JSONException e){
                e.printStackTrace();
            }
            Call<AboutMyChildDetails> aboutMyChildDetailsCall = apiInterface.getChildAboutDetails(jsonObject);
            aboutMyChildDetailsCall.enqueue(new Callback<AboutMyChildDetails>() {
                @Override
                public void onResponse(Call<AboutMyChildDetails> call, Response<AboutMyChildDetails> response) {
                    if (response.isSuccessful()) {
                        AboutMyChildDetails aboutMyChildDetails = response.body();
                        if (aboutMyChildDetails.getStatus_code() != null) {
                            if (aboutMyChildDetails.getStatus_code().equals(Constants.SUCESS)) {
                                String studentId = aboutMyChildDetails.getStudent_id();
                                JSONObject parentObject = aboutMyChildDetails.toJSON();
                                if (dataBaseHandler.ifStudentIdisExists(studentId)) {
                                    dataBaseHandler.dropChildAboutDetails(studentId);
                                    dataBaseHandler.insertChildAboutDetails(studentId,parentObject.toString());
                                }else {
                                    dataBaseHandler.insertChildAboutDetails(studentId,parentObject.toString());
                                }

                                setViewFromDb();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<AboutMyChildDetails> call, Throwable t) {
                    Toast.makeText(getActivity(),"Server Error",Toast.LENGTH_LONG).show();
                    setViewFromDb();
                }
            });


        }else {
            tSnackbar.show();
            setViewFromDb();
        }

    }

    public void setViewFromDb(){
        if (dataBaseHandler.ifChildAboutDetailsisExists()){
            String getChildDetails = dataBaseHandler.getChildAboutDetails(childId);
            try {
                JSONObject getChildAboutObject = new JSONObject(getChildDetails);
                JSONArray childDetailsArray = getChildAboutObject.getJSONArray("student_notes");
                LayoutInflater vi = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View v = vi.inflate(R.layout.about_child_row, null);
                TextView txtStudentName = v.findViewById(R.id.txtStudentName);
                txtStudentName.setText(getChildAboutObject.getString("student_name"));
                String studentimageUrl = getImageDeatils(getChildAboutObject.getString("student_id"));
                CircleImageView circleImageView = v.findViewById(R.id.studentImage);
                new LoadImage(circleImageView).execute(studentimageUrl);
                LinearLayout ldtTeacherList = v.findViewById(R.id.ldtTeacherList);
                ldtMainAboutView.addView(v);
                for (int i= 0 ; i < childDetailsArray.length() ; i ++){
                    JSONObject jsonObject = childDetailsArray.getJSONObject(i);
                    LayoutInflater teacherList = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View childView = teacherList.inflate(R.layout.teacher_msg_row, null);
                    TextView txtTeacherName = childView.findViewById(R.id.txtTeacherName);
                    TextView txtMessage=  childView.findViewById(R.id.txtMessage);
                    txtMessage.setText(jsonObject.getString("message"));
                    txtTeacherName.setText(jsonObject.getString("teacher_name"));
                    ldtTeacherList.addView(childView);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            progressLoadAboutChilg.setVisibility(View.GONE);
        }

    }

    public void setAllAboutViewFromDb(){
        if (dataBaseHandler.ifChildAboutDetailsisExists()){
            String getChildDetails = dataBaseHandler.getChildAboutDetails("P"+dataBaseHandler.getParentId());
            try {
                JSONObject getChildAboutObject = new JSONObject(getChildDetails);
                JSONArray childDetailsArray = getChildAboutObject.getJSONArray("students_details");

                for (int i = 0 ; i < childDetailsArray.length() ; i++){
                    JSONObject jsonObject = childDetailsArray.getJSONObject(i);
                    JSONArray notesArray = jsonObject.getJSONArray("student_notes");

                    LayoutInflater vi = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View v = vi.inflate(R.layout.about_child_row, null);
                    TextView txtStudentName = v.findViewById(R.id.txtStudentName);
                    CircleImageView  circleImageView = v.findViewById(R.id.studentImage);
                    txtStudentName.setText(jsonObject.getString("student_name"));
                    String studentimageUrl = getImageDeatils(jsonObject.getString("student_id"));
                    new LoadImage(circleImageView).execute(studentimageUrl);
                    ldtMainAboutView.addView(v);
                    LinearLayout ldtTeacherList = v.findViewById(R.id.ldtTeacherList);
                    for (int j= 0 ; j < notesArray.length() ; j ++){
                        JSONObject notesArrayJSONObject = notesArray.getJSONObject(j);
                        LayoutInflater teacherList = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View childView = teacherList.inflate(R.layout.teacher_msg_row, null);
                        TextView txtTeacherName = childView.findViewById(R.id.txtTeacherName);
                        TextView txtMessage=  childView.findViewById(R.id.txtMessage);
                        txtMessage.setText(notesArrayJSONObject.getString("message"));
                        txtTeacherName.setText(notesArrayJSONObject.getString("teacher_name"));
                        ldtTeacherList.addView(childView);

                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            progressLoadAboutChilg.setVisibility(View.GONE);
        }

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tSnackbar = HelperClass.showTopSnackBar(view, "Network not connected");
        if ((CommonFragmentActivity)getActivity() != null) {
            ((CommonFragmentActivity) getActivity()).setOnBackPressedListener(this);
        }
        if (HelperClass.isNetworkAvailable(getActivity())) {
            isNetworkConnected = true;
            if (tSnackbar.isShown())
                tSnackbar.dismiss();
        } else {
            isNetworkConnected = false;
            tSnackbar.show();
        }

        if (isNetworkConnected) {
            if (!childId.equals("-1"))
                getDataFromServer();
            else {
                getAllChildDetails();
            }
            readStatus();
        }
        else {
            if (!childId.equals("-1")) {
                setViewFromDb();
            }else {
                getAllChildDetails();
            }
        }
    }

    public void childId (String childId){
        this.childId = childId;
    }

    public void getAllChildDetails(){
        if (isNetworkConnected){
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("parent_id", dataBaseHandler.getParentId());
            }catch (JSONException e){
                e.printStackTrace();
            }
            Call<AboutMyChildDetails> aboutMyChildDetailsCall = apiInterface.getAllChildAboutDetails(jsonObject);
            aboutMyChildDetailsCall.enqueue(new Callback<AboutMyChildDetails>() {
                @Override
                public void onResponse(Call<AboutMyChildDetails> call, Response<AboutMyChildDetails> response) {
                    if (response.isSuccessful())
                    {
                        AboutMyChildDetails aboutMyChildDetails = response.body();
                        if (aboutMyChildDetails.getStatus_code() != null) {
                            if (aboutMyChildDetails.getStatus_code().equals(Constants.SUCESS)) {
                                String studentId ="P"+dataBaseHandler.getParentId();
                                JSONObject parentObject = aboutMyChildDetails.toAllChildJSON();
                                if (dataBaseHandler.ifStudentIdisExists(studentId)) {
                                    dataBaseHandler.dropChildAboutDetails(studentId);
                                    dataBaseHandler.insertChildAboutDetails(studentId,parentObject.toString());
                                }else {
                                    dataBaseHandler.insertChildAboutDetails(studentId,parentObject.toString());
                                }

                                setAllAboutViewFromDb();

                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<AboutMyChildDetails> call, Throwable t) {

                    setAllAboutViewFromDb();
                }
            });


        }else {
            tSnackbar.show();
            setAllAboutViewFromDb();
        }
    }

    public void readStatus(){
        if (isNetworkConnected){
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("student_id", childId);
                jsonObject.put("parent_id",dataBaseHandler.getParentId());
            }catch (JSONException e){
                e.printStackTrace();
            }
            Call<AboutMyChildDetails> aboutMyChildDetailsCall = apiInterface.readStatus(jsonObject);
            aboutMyChildDetailsCall.enqueue(new Callback<AboutMyChildDetails>() {
                @Override
                public void onResponse(Call<AboutMyChildDetails> call, Response<AboutMyChildDetails> response) {
                    if (response.isSuccessful()){
                        AboutMyChildDetails aboutMyChildDetails = AboutMyChildDetails.getInstance();
                        HashMap<Object,Object> getChildCount = new HashMap<>();
                            if (childId.equals("-1"))
                            {
                                aboutMyChildDetails.setStudentCount(getChildCount);
                            }else {
                                getChildCount.putAll(aboutMyChildDetails.getStudentCount());
                                getChildCount.remove(childId);
                                aboutMyChildDetails.setStudentCount(getChildCount);
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
    public void onBackPressed() {
        ((CommonFragmentActivity)getActivity()).finish();
    }

    public String getImageDeatils(String studentId){
        String getParentDetails = dataBaseHandler.getParentChildDetails();
        String studentPhoto = "";
        try {
            JSONObject getParentObject = new JSONObject(getParentDetails);
            JSONArray getStudentArray = getParentObject.getJSONArray("student_details");
            bitmapArrayList = new ArrayList<>();
            for (int i = 0 ;i < getStudentArray.length() ; i ++) {
                JSONObject studObject = getStudentArray.getJSONObject(i);
                if (studObject.has("student_photo")) {
                    String stuPhoto = studObject.getString("student_photo");
                    String studId = studObject.getString("student_id");
                    if (studentId.equals(studId)) {
                     studentPhoto = stuPhoto;
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return studentPhoto;
    }


    private class LoadImage extends AsyncTask<String,Void,Bitmap> {

        CircleImageView circleImageView ;
        public LoadImage(CircleImageView circleImageView){
            this.circleImageView = circleImageView;
        }
        @Override
        protected Bitmap doInBackground(String... strings) {
            String url = strings[0];
            Bitmap bitmap = null;
            try {
            if (isNetworkConnected) {
                bitmap = Picasso.with(getActivity()).load(url).get();
            }else {
                bitmap = Picasso.with(getActivity())
                            .load(url)
                            .networkPolicy(NetworkPolicy.OFFLINE)
                            .get();
            }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap aVoid) {
            super.onPostExecute(aVoid);

            circleImageView.setImageBitmap(aVoid);
        }
    }
}
