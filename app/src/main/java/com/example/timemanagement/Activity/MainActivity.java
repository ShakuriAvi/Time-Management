package com.example.timemanagement.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.timemanagement.Class.Coach;
import com.example.timemanagement.Class.Company;
import com.example.timemanagement.Class.Conflict;
import com.example.timemanagement.Class.Schedule;
import com.example.timemanagement.Class.Student;
import com.example.timemanagement.Controller.CoachesReaderController;
import com.example.timemanagement.Controller.StudentsReaderController;
import com.example.timemanagement.Fragment.ScheduleFragment;
import com.example.timemanagement.R;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textview.MaterialTextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String SP_FILE = "MY_SP";


    private ArrayList<Student> studentList;
    private ArrayList<Coach> coaches;
    private Company company;
    private Toolbar activity_toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private Dialog mDialog;
    private MaterialTextView popup_TXT_conflicts;
    private Button popup_BTN_confirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_main);
        try {
            initCompany();
            initView();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    private void initView() {
        prefs = getSharedPreferences(SP_FILE, MODE_PRIVATE);
         editor = prefs.edit();
        editor.clear().commit();
        editor.putInt("Coach", 0);
        editor.apply();
        activity_toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(activity_toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, activity_toolbar, R.string.Navigation_drawer_open, R.string.Navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ScheduleFragment()).commit();

        popUp();
    }
    private void popUp(){
        mDialog = new Dialog(this);
        mDialog.setContentView(R.layout.popup_conflicts);
        popup_TXT_conflicts = mDialog.findViewById(R.id.popup_TXT_conflicts);
        popup_BTN_confirm = mDialog.findViewById(R.id.popup_BTN_confirm);
        String[] days = {"","Sunday","Monday","Tuesday","Wednesday","Thursday"};
        String conflictsShow="";
        for (int i = 0; i <company.getSchedule().getConflicts().size() ; i++) {
            Conflict conflict = company.getSchedule().getConflicts().get(i);
            conflictsShow +=  " " + popup_TXT_conflicts.getText() + " " +conflict.getStudent().getFirstName() + " "+conflict.getStudent().getLastName() +   ": " + "There is no coach available " + " at " + days[conflict.getDay()] + "\n";
        }
        mDialog.getWindow();
        mDialog.show();
        popup_TXT_conflicts.setText(conflictsShow);
        popup_BTN_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.cancel();
            }
        });

    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        //editor.clear();
        switch (item.getItemId()) {

            case R.id.nav_ITEM_firstCoach:
                activity_toolbar.setTitle("Yotam");
                editor.putInt("Coach", 0);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ScheduleFragment()).commit();
                editor.apply();
                break;
            case R.id.nav_ITEM_secondCoach:
                activity_toolbar.setTitle("Yoni");
                editor.putInt("Coach", 1);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ScheduleFragment()).commit();
                editor.apply();
                break;
            case R.id.nav_ITEM_thirdCoach:
                activity_toolbar.setTitle("Jonny");
                editor.putInt("Coach", 2);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ScheduleFragment()).commit();
                editor.apply();
                break;


        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initCompany() throws IOException {
        company = Company.getInstance();
        CoachesReaderController coachesReaderController = new CoachesReaderController(this);
        coaches = coachesReaderController.initCoaches();

        company.setCoachesList(coaches);
        StudentsReaderController studentsReaderController = new StudentsReaderController(this);
        studentList = studentsReaderController.initStudentList();

        company.setStudentsList(studentList);
        company.setSchedule(new Schedule(coaches,studentList));
        company.getSchedule().makeSchedule();
    }


    private void printStudentList() {
        for (int i = 0; i < studentList.size(); i++) {
            Log.d("json", "\nstudent : firstName " + studentList.get(i).getFirstName() + " , last Name " + studentList.get(i).getLastName() + " , Swimming_Style " + studentList.get(i).getSwimmingStyle() + ", lesson_type " + studentList.get(i).getFavoriteLesson() + "\n");
            for (int j = 0; j < studentList.get(i).getAvailableDays().size(); j++) {
                Log.d("json", "day " + (j + 1) + " available " + studentList.get(i).getAvailableDays().get(j));
            }
        }
    }
        private void printCoaches(){
            for (int i = 0; i <coaches.size() ; i++) {
                Log.d("json","\nstudent : firstName " + coaches.get(i).getFirstName() + "\n");
                    Log.d("json","student : firstName " + coaches.get(i).getAvailability() + "\n");
                Log.d("json","student : firstName " + coaches.get(i).getSwimmingStyle() + "\n");

            }
        }


}
