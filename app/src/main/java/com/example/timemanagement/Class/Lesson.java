package com.example.timemanagement.Class;

public abstract class Lesson {
    private Company.Lesson_Type lesson_type;
    private Company.Swimming_Style swimming_style;
    private Coach coach;
    public Lesson(Company.Lesson_Type lesson_type,Coach coach,Company.Swimming_Style swimming_style){
        this.lesson_type = lesson_type;
        this.coach = coach;
        this.swimming_style =swimming_style;
    }
    public Company.Swimming_Style getSwimming_style() {
        return swimming_style;
    }
    public Company.Lesson_Type getLesson_type() {
        return lesson_type;
    }

    public void setLesson_type(Company.Lesson_Type lesson_type) {
        this.lesson_type = lesson_type;
    }

    public Coach getCoach() {
        return coach;
    }

    public void setCoach(Coach coach) {
        this.coach = coach;
    }

    public abstract Number duringLesson();
}