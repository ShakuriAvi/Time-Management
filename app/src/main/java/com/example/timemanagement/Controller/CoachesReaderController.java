package com.example.timemanagement.Controller;

import android.app.Activity;

import com.example.timemanagement.Class.Coach;
import com.example.timemanagement.Class.Company;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

public class CoachesReaderController {
    private Activity activity;
    private ArrayList<Coach> coaches;
    private final String FILE_NAME_COACHES = "Coaches.json";
    public CoachesReaderController(Activity activity){
        this.activity=activity;

    }
    public ArrayList<Coach> initCoaches()throws IOException {
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

        return coaches;

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
        InputStream is = activity.getAssets().open(name);

        int size = is.available();

        byte[] buffer = new byte[size];

        is.read(buffer);

        is.close();

        String  json = new String(buffer, "UTF-8");
        return  json;
    }

}

