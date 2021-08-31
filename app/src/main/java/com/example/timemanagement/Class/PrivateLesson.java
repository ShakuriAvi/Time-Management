package com.example.timemanagement.Class;

import java.util.ArrayList;

public class PrivateLesson extends Lesson{
    private final double during = 0.75;
    public PrivateLesson(Company.Lesson_Type lesson_type, Coach coach,Student participant,  Company.Swimming_Style swimming_style) {
        super(lesson_type, coach,swimming_style);
        this.participant = participant;

    }
    public Student getParticipant() {
        return participant;
    }

    private Student participant;


    @Override
    public Number duringLesson() {
        return during;
    }
}
