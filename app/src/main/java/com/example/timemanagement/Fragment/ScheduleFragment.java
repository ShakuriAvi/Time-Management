package com.example.timemanagement.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timemanagement.Adapter.Adapter_ScheduleHours;
import com.example.timemanagement.Class.Coach;
import com.example.timemanagement.Class.Company;
import com.example.timemanagement.Class.Lesson;
import com.example.timemanagement.R;

import java.util.ArrayList;

public class ScheduleFragment extends Fragment {
    private View view;
    private static final String SP_FILE = "MY_SP";
    private SharedPreferences preferences;
    private Company company;
    private RecyclerView schedule_LST_details1;
    private RecyclerView schedule_LST_details2;
    private RecyclerView schedule_LST_details3;
    private RecyclerView schedule_LST_details4;
    private RecyclerView schedule_LST_details5;
    private ArrayList<RecyclerView> recyclerViews;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.schedule_fragment, container, false);
        initSchedule();
        return view;
    }
    private void initSchedule(){ company = Company.getInstance();
       preferences  = getActivity().getSharedPreferences(SP_FILE, getActivity().MODE_PRIVATE);
        int numberCoach = preferences.getInt("Coach", -1);

        recyclerViews = new ArrayList<>();
        schedule_LST_details1 = view.findViewById(R.id.schedule_LST_details1);
        recyclerViews.add(schedule_LST_details1);
        schedule_LST_details2 = view.findViewById(R.id.schedule_LST_details2);
        recyclerViews.add(schedule_LST_details2);
        schedule_LST_details3 = view.findViewById(R.id.schedule_LST_details3);
        recyclerViews.add(schedule_LST_details3);
        schedule_LST_details4 = view.findViewById(R.id.schedule_LST_details4);
        recyclerViews.add(schedule_LST_details4);
        schedule_LST_details5 = view.findViewById(R.id.schedule_LST_details5);
        recyclerViews.add(schedule_LST_details5);
        for (int i = 1; i < 6 ; i++) {
            recyclerViews.get(i-1).setLayoutManager(new LinearLayoutManager(getActivity()));
        }
        if(numberCoach!=-1) {
            Coach coach = company.getCoachesList().get(numberCoach);
            for (int i = 1; i <6 ; i++) {
                ArrayList<Lesson> lessonsDay = coach.getScheduleCoach().get(i);
                actionAdapter(lessonsDay,i,false);
            }
        }

    }
    private void actionAdapter(ArrayList<Lesson> lessonsDay,int day,boolean isAllCoaches){
        if(lessonsDay!=null) {
            Adapter_ScheduleHours adapter = new Adapter_ScheduleHours(getContext(), lessonsDay,day,isAllCoaches);
            recyclerViews.get(day - 1).setAdapter(adapter);
        }
    }
}