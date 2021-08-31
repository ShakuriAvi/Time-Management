package com.example.timemanagement.Class;

public class Conflict {


    private Student student;
    private int day;
    public Conflict( Student student, int day){
        this.student = student;
        this.day=day;
    }

    public Student getStudent() {
        return student;
    }

    public int getDay() {
        return day;
    }


}
