package com.nickteck.schoolapp.fragment;


import android.annotation.SuppressLint;
import android.content.Context;
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
import android.widget.TextView;
import android.widget.Toast;

import com.androidadvance.topsnackbar.TSnackbar;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.exceptions.OutOfDateRangeException;
import com.applandeo.materialcalendarview.listeners.OnCalendarPageChangeListener;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.nickteck.schoolapp.AdditionalClass.HelperClass;
import com.nickteck.schoolapp.R;
import com.nickteck.schoolapp.activity.CommonFragmentActivity;
import com.nickteck.schoolapp.api.ApiClient;
import com.nickteck.schoolapp.api.ApiInterface;
import com.nickteck.schoolapp.database.DataBaseHandler;
import com.nickteck.schoolapp.interfaces.OnBackPressedListener;
import com.nickteck.schoolapp.model.AboutMyChildDetails;
import com.nickteck.schoolapp.model.ShowEvent;
import com.nickteck.schoolapp.service.MyApplication;
import com.nickteck.schoolapp.service.NetworkChangeReceiver;
import com.nickteck.schoolapp.utilclass.Constants;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class CalendarFragment extends Fragment implements OnBackPressedListener, NetworkChangeReceiver.ConnectivityReceiverListener {


    View mainView;
    TextView txtNoEvent;
    String TAG = CalendarFragment.class.getName();
    CalendarView eventCalendar;
    Calendar calendar;
    SimpleDateFormat df,dbFormat;
    boolean isNetworkConnected = false;
    TSnackbar tSnackbar;
    int month,year;
    ApiInterface apiInterface;
    LinearLayout ldteventCalendar;
    View lastView;
    HashMap<String,ArrayList<ShowEvent.EventDetails>> getEventDetail;
    DataBaseHandler dataBaseHandler;
    String dataMonth = "";
    private Toolbar toolBarTitle;
    @SuppressLint("SimpleDateFormat")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mainView = inflater.inflate(R.layout.fragment_calendar, container, false);

        if (getActivity() != null) {
            ((CommonFragmentActivity) getActivity()).setOnBackPressedListener(this);
        }
        toolBarTitle = (Toolbar) getActivity().findViewById(R.id.toolBarTitle);
        TextView toolBarTextView = (TextView) toolBarTitle.findViewById(R.id.toolBarTextView);
        toolBarTextView.setText("Calendar");

        MyApplication.getInstance().setConnectivityListener(this);

        dataBaseHandler = new DataBaseHandler(getActivity());

        eventCalendar = mainView.findViewById(R.id.eventCalendar);

        ldteventCalendar = mainView.findViewById(R.id.ldteventCalendar);
        txtNoEvent = mainView.findViewById(R.id.txtNoEvent);
        txtNoEvent.setVisibility(View.GONE);

        lastView = mainView.findViewById(R.id.lastView);
        lastView.setVisibility(View.GONE);

        calendar = Calendar.getInstance();
        dbFormat = new SimpleDateFormat("dd/MMM/yyyy");

        df = new SimpleDateFormat("dd MMM yyyy");
        Date currentTime = Calendar.getInstance().getTime();
        try {
            eventCalendar.setDate(currentTime);
        } catch (OutOfDateRangeException e) {
            e.printStackTrace();
        }

        eventCalendar.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {
                Calendar calendar = eventDay.getCalendar();
                String formattedDate = df.format(calendar.getTime());
                Log.e(TAG, "onDayClick: "+formattedDate);
                showEvent(formattedDate);
            }
        });
        eventCalendar.setOnForwardPageChangeListener(new OnCalendarPageChangeListener() {
            @Override
            public void onChange() {
                calendar.add(Calendar.MONTH,1);
                String formattedDate = df.format(calendar.getTime());

                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH) + 1;
                dataMonth =  dbFormat.format(calendar.getTime());
                getEventList();

            }
        });

        eventCalendar.setOnPreviousPageChangeListener(new OnCalendarPageChangeListener() {
            @Override
            public void onChange() {
                calendar.add(Calendar.MONTH,-1);
                String formattedDate = df.format(calendar.getTime());

                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH) + 1;
                dataMonth =  dbFormat.format(calendar.getTime());
                getEventList();
            }
        });


        return mainView;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tSnackbar = HelperClass.showTopSnackBar(view, "Network not connected");


        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH) + 1;

        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        if (HelperClass.isNetworkAvailable(getActivity())) {
            isNetworkConnected = true;
            if (tSnackbar.isShown())
                tSnackbar.dismiss();
        } else {
            isNetworkConnected = false;
            tSnackbar.show();
        }
        if (isNetworkConnected) {
            dataMonth =  dbFormat.format(c.getTime());
            getEventList();
        }else {
            dataMonth =  dbFormat.format(c.getTime());
            setIntoViewFromLocal();
        }


    }

    @Override
    public void onBackPressed() {
        ((CommonFragmentActivity)getActivity()).finish();
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        isNetworkConnected = isConnected;
        if (!isConnected) {
            tSnackbar.show();
        }else {
            getEventList();
            if (tSnackbar.isShown())
                tSnackbar.dismiss();
        }
    }

    public void getEventList(){
        if (isNetworkConnected){
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("month", month);
                jsonObject.put("year",year);
            }catch (JSONException e){
                e.printStackTrace();
            }
            Call<ShowEvent> showEventCall = apiInterface.getEvents(jsonObject);
            showEventCall.enqueue(new Callback<ShowEvent>() {
                @Override
                public void onResponse(Call<ShowEvent> call, Response<ShowEvent> response) {
                    if (response.isSuccessful()){
                        ShowEvent showEvent = response.body();
                        if (showEvent.getStatus_code() != null) {
                            Log.e(TAG, "onResponse: " );
                            if (showEvent.getStatus_code().equals(Constants.SUCESS)) {
                                if (dataBaseHandler.ifMonthisExists(dataMonth)){
                                    dataBaseHandler.dropCalendarEvent(dataMonth);
                                    dataBaseHandler.insertCalenddarEvents(dataMonth,showEvent.getEventJSON().toString());
                                }else {
                                    dataBaseHandler.insertCalenddarEvents(dataMonth,showEvent.getEventJSON().toString());
                                }
                                setIntoViewFromLocal();
                            }else {
                                ldteventCalendar.setVisibility(View.GONE);
                                ldteventCalendar.removeAllViews();
                                txtNoEvent.setVisibility(View.VISIBLE);
                                lastView.setVisibility(View.GONE);
                            }
                        }
                    }else {
                        Toast.makeText(getActivity(),"Server Error not success",Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ShowEvent> call, Throwable t) {
                    Toast.makeText(getActivity(),"Server Error",Toast.LENGTH_LONG).show();
                }
            });


        }else {
            setIntoViewFromLocal();
        }
    }


    public void showEvent(String date){
        if (getEventDetail.size() != 0){
            ArrayList<ShowEvent.EventDetails>eventDetails = getEventDetail.get(date);
            if (eventDetails != null){
                txtNoEvent.setVisibility(View.GONE);
                ldteventCalendar.setVisibility(View.VISIBLE);
                lastView.setVisibility(View.VISIBLE);
                SimpleDateFormat getDateFormat = new SimpleDateFormat("dd MMM yyyy KK:mm");
                SimpleDateFormat outputFormat = new SimpleDateFormat("EE MMM dd");
                SimpleDateFormat hourFormat = new SimpleDateFormat("hh:mm a");
                ldteventCalendar.removeAllViews();
                for (int i = 0 ; i < eventDetails.size() ; i ++) {
                    ShowEvent.EventDetails details = eventDetails.get(i);
                    LayoutInflater teacherList = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View childView = teacherList.inflate(R.layout.add_event_layout, null);
                    TextView txtDateFormat = childView.findViewById(R.id.txtDateFormat);
                    TextView txtHeader = childView.findViewById(R.id.txtHeader);
                    TextView txtContent = childView.findViewById(R.id.txtContent);

                    txtHeader.setText(details.getTitle());
                    txtContent.setText(details.getContent());
                    String eventDate = details.getHeld_on();
                    Calendar cal = Calendar.getInstance();
                    try {
                        cal.setTime(getDateFormat.parse(eventDate));
                        Date selectedDate = getDateFormat.parse(eventDate);
                        String dayOfTheWeek = outputFormat.format(selectedDate);
                        String hour = hourFormat.format(selectedDate);
//                        txtTime.setText(hour);
                        txtDateFormat.setText(dayOfTheWeek);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    ldteventCalendar.addView(childView);
                }
            }else {
                ldteventCalendar.setVisibility(View.GONE);
                ldteventCalendar.removeAllViews();
                txtNoEvent.setVisibility(View.VISIBLE);
                lastView.setVisibility(View.GONE);

            }
        }
    }

    public void setIntoViewFromLocal(){
        if (dataBaseHandler.ifMonthisExists(dataMonth)) {
            String getEventDetails = dataBaseHandler.getCalendarEvents();
            try {
                JSONObject jsonObject = new JSONObject(getEventDetails);
                JSONArray eventDetailsArray = jsonObject.getJSONArray("event_details");
                List<EventDay> events = new ArrayList<>();
                List<String> dateList = new ArrayList<>();
                getEventDetail = new HashMap<>();
                SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm", Locale.ENGLISH);
                for (int i = 0 ; i < eventDetailsArray.length() ; i ++){
                    JSONObject eventObject =  eventDetailsArray.getJSONObject(i);
                    ShowEvent.EventDetails eventDetails = new ShowEvent.EventDetails();

                    String getHeldDate = eventObject.getString("held_on");
                    eventDetails.setHeld_on(getHeldDate);
                    eventDetails.setContent(eventObject.getString("content"));
                    eventDetails.setTitle(eventObject.getString("title"));
                    ArrayList<ShowEvent.EventDetails>getEvent = new ArrayList<>();

                    Calendar cal = Calendar.getInstance();
                    try {
                        cal.setTime(sdf.parse(getHeldDate));
                        events.add(new EventDay(cal, R.drawable.calendar_event_dot));
                        Date date = sdf.parse(getHeldDate);
                        String outputDate = df.format(date);
                        if (!dateList.contains(outputDate)){
                            dateList.add(outputDate);
                            getEvent.add(eventDetails);
                            getEventDetail.put(outputDate,getEvent);
                        }else {
                            getEvent = getEventDetail.get(outputDate);
                            getEvent.add(eventDetails);
                            getEventDetail.put(outputDate,getEvent);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                }
                eventCalendar.setEvents(events);
                Date c = Calendar.getInstance().getTime();
                String formattedDate = df.format(c);
                showEvent(formattedDate);

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }else {
            Toast.makeText(getActivity(),"No Data Available",Toast.LENGTH_SHORT).show();
        }

    }
}
