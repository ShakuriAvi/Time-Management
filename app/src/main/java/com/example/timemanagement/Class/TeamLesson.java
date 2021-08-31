package com.example.timemanagement.Class;

import java.util.ArrayList;

public class TeamLesson extends Lesson {
    private ArrayList<Student> participants;
    private final int during = 1;

    public TeamLesson(Company.Lesson_Type lesson_type, Coach coach,Company.Swimming_Style swimming_style) {
        super(lesson_type, coach,swimming_style);
    }
    public ArrayList<Student> getParticipants() {
        return participants;
    }

    public void setParticipants(ArrayList<Student> participants) {
        this.participants = participants;
    }


    @Override
    public Number duringLesson() {
        return during;
    }
}
