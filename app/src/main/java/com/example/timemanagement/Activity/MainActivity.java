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
    private static final String FILE_NAME_STUDENT = "Students.json";
    private static final String FILE_NAME_COACHES = "Coaches.json";
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
        initCoaches();
        company.setCoachesList(coaches);
        initStudentList();
        company.setStudentsList(studentList);
        company.setSchedule(new Schedule(coaches,studentList));
        company.getSchedule().makeSchedule();
    }
// read from json file all coaches
    private void initCoaches()throws IOException {
        String  json = getDataFromFile(FILE_NAME_COACHES);
        JSONObject myjson = null ;

        JSONArray the_json_array = null;
        coaches = new ArrayList<Coach>();
        try {
            myjson = new JSONObject(json);
            the_json_array = myjson.getJSONArray("Coaches");
            int the_json_arrays_size = the_json_array.length();

            for (int i = 0; i <the_json_arrays_size ; i++) {
                JSONObject another_json_object = the_json_array.getJSONObject(i);
                coaches.add(createCoach(another_json_object));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    //printCoaches();

    }

    private Coach createCoach(JSONObject another_json_object) {
        Coach coach =  null;
        try {
            String firstName = another_json_object.get("first name").toString();
            JSONArray the_json_array = another_json_object.getJSONArray("availability");
            int the_json_arrays_size = the_json_array.length();
            Hashtable<Integer, Boolean[]> availability = new Hashtable<>();
            double[] countHoursAvailability= {0,0,0,0,0};
            for (int i = 0; i <the_json_arrays_size ; i++) {
                JSONObject json = the_json_array.getJSONObject(i);
                Iterator<String> keys = json.keys();

                while (keys.hasNext()) {
                    String key = keys.next();
                    String val = json.get(key).toString();
                    String[] temp = val.split(",");
                    Boolean[] availableHours = new Boolean[12]; // The pool is open from 8 a.m until 8 p.m.

                    for (int j = 0; j < temp.length; j++) {
                       
                        int hour = Integer.parseInt(temp[j]);
                        availableHours[hour - 8] = true;
                        countHoursAvailability[Integer.parseInt(key)-1] += 1;
                    }

                    availability.put(Integer.parseInt(key),availableHours);
                }
            }
            String swimmingStyleString = another_json_object.get("swimmingStyle").toString();
            String[] temp = swimmingStyleString.split(",");
            ArrayList<Company.Swimming_Style> swimmingStyle = new ArrayList<>();
            for (int i = 0; i <temp.length ; i++) {


            if (temp[i].equals("BACK"))
                swimmingStyle.add( Company.Swimming_Style.BACK);
            else if (temp[i].equals("FREESTYLE"))
                swimmingStyle.add(Company.Swimming_Style.FREESTYLE);
            else if (temp[i].equals("BUTTERFLY"))
                swimmingStyle.add(Company.Swimming_Style.BUTTERFLY);
            else
                swimmingStyle.add(Company.Swimming_Style.BREASTSTROKE);
            }



            coach = new Coach(firstName,availability,swimmingStyle,countHoursAvailability);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return coach;
    }
// get data on json file
    private String getDataFromFile(String name)  throws IOException{
        InputStream is = this.getAssets().open(name);

        int size = is.available();

        byte[] buffer = new byte[size];

        is.read(buffer);

        is.close();

        String  json = new String(buffer, "UTF-8");
        return  json;
    }
//read from json file all Students
    private void initStudentList() throws IOException {

        String  json = getDataFromFile(FILE_NAME_STUDENT);
        JSONObject myjson = null ;

        JSONArray the_json_array = null;
        studentList = new ArrayList<Student>();
        try {
            myjson = new JSONObject(json);
            the_json_array = myjson.getJSONArray("Students");
            int the_json_arrays_size = the_json_array.length();

            for (int i = 0; i <the_json_arrays_size ; i++) {
                JSONObject another_json_object = the_json_array.getJSONObject(i);
                //Log.d("json","here :  " + the_json_array.get(i));
                studentList.add(createStudent(another_json_object));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        //printStudentList();

    }



    private Student createStudent(JSONObject another_json_object) {
        Student newStudent = null;
        try {
            String firstName = another_json_object.get("first name").toString();
            String lastName = another_json_object.get("last name").toString();
            String swimmingString = another_json_object.get("swimmingStyle").toString();
            Company.Swimming_Style Swimming_Style;
            if (swimmingString.equals("BACK"))
                Swimming_Style = Company.Swimming_Style.BACK;
            else if (swimmingString.equals("FREESTYLE"))
                Swimming_Style = Company.Swimming_Style.FREESTYLE;
            else if (swimmingString.equals("BUTTERFLY"))
                Swimming_Style = Company.Swimming_Style.BUTTERFLY;
            else {
                Swimming_Style = Company.Swimming_Style.BREASTSTROKE;
            }

            String lessonString = another_json_object.get("favoriteLesson").toString();
            Company.Lesson_Type lesson_type;
            if (lessonString.equals("PRIVATE"))
                lesson_type = Company.Lesson_Type.PRIVATE;
            else if (lessonString.equals("TEAM"))
                lesson_type = Company.Lesson_Type.TEAM;
            else
                lesson_type = Company.Lesson_Type.BOTH;

            String availableDaysString = another_json_object.get("availableDays").toString();
            String[] temp = availableDaysString.split(",");
            ArrayList<Integer> availableDays =new ArrayList<>();// there are 5 Business Days in week
            int count = 0;
            for (int i = 0; i < temp.length; i++) {
              //  Log.d("json","day " + i + " available " + temp[i]);
                int day = Integer.parseInt(temp[i]);
                availableDays.add(day);
                count++;
            }

            newStudent = new Student(firstName, lastName, Swimming_Style, lesson_type, availableDays);
            newStudent.setCountAvailabilityDays(count);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return newStudent;
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
