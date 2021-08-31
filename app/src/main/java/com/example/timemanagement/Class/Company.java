package com.example.timemanagement.Class;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import static com.example.timemanagement.Class.Company.Lesson_Type.BOTH;
import static com.example.timemanagement.Class.Company.Lesson_Type.PRIVATE;
import static com.example.timemanagement.Class.Company.Lesson_Type.TEAM;

public class Company {
    public enum Lesson_Type {
        PRIVATE,
        TEAM,
        BOTH

    }

    public enum Swimming_Style {
        BUTTERFLY,
        BACK,
        FREESTYLE,
        BREASTSTROKE,
    }

    private static Company single_instance = null;

    // variable of type String
    private ArrayList<Coach> coachesList;
    private ArrayList<Student> studentsList;

    public static Company getSingle_instance() {
        return single_instance;
    }

    public static void setSingle_instance(Company single_instance) {
        Company.single_instance = single_instance;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    private Schedule schedule;



    public ArrayList<Coach> getCoachesList() {
        return coachesList;
    }

    public void setCoachesList(ArrayList<Coach> coachesList) {
        this.coachesList = coachesList;
    }

    public ArrayList<Student> getStudentsList() {
        return studentsList;
    }

    public void setStudentsList(ArrayList<Student> studentsList) {
        this.studentsList = studentsList;
    }


    // private constructor restricted to this class itself
    private Company()
    {

    }

    // static method to create instance of Singleton class
    public static Company getInstance()
    {
        if (single_instance == null)
            single_instance = new Company();

        return single_instance;
    }

}
