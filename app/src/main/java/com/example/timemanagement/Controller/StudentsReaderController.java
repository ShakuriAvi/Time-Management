package com.example.timemanagement.Controller;

import android.app.Activity;

import com.example.timemanagement.Class.Company;
import com.example.timemanagement.Class.Student;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class StudentsReaderController {
    private final String FILE_NAME_STUDENT = "Students.json";
    private ArrayList<Student> studentList;
    private Activity activity;
    public StudentsReaderController(Activity activity){
        this.activity=activity;
    }

    public ArrayList<Student> initStudentList() throws IOException {

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


       return studentList;

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
    // get data on json file
    private String getDataFromFile(String name)  throws IOException{
        InputStream is = activity.getAssets().open(name);

        int size = is.available();

        byte[] buffer = new byte[size];

        is.read(buffer);

        is.close();

        String  json = new String(buffer, "UTF-8");
        return  json;
    }

}
