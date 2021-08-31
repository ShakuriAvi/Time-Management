package com.example.timemanagement.Class;

import java.util.ArrayList;
import java.util.Comparator;

public class Student {
    private String firstName;
    private String lastName;
    private Company.Swimming_Style swimmingStyle;
    private Company.Lesson_Type favoriteLesson;
    private ArrayList<Integer> availableDays;
    private int countAvailabilityDays;
    public Student(String firstName, String lastName, Company.Swimming_Style swimmingStyle, Company.Lesson_Type favoriteLesson, ArrayList<Integer> availableDays){
        this.firstName=firstName;
        this.lastName = lastName;
        this.swimmingStyle=swimmingStyle;
        this.favoriteLesson=favoriteLesson;
        this.availableDays=availableDays;
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Company.Swimming_Style getSwimmingStyle() {
        return swimmingStyle;
    }

    public void setSwimmingStyle(Company.Swimming_Style swimmingStyle) {
        this.swimmingStyle = swimmingStyle;
    }

    public Company.Lesson_Type getFavoriteLesson() {
        return favoriteLesson;
    }

    public void setFavoriteLesson(Company.Lesson_Type favoriteLesson) {
        this.favoriteLesson = favoriteLesson;
    }

    public ArrayList<Integer> getAvailableDays() {
        return availableDays;
    }

    public void setAvailableDays(ArrayList<Integer> availableDays) {
        this.availableDays = availableDays;
    }

    public void setCountAvailabilityDays(int count) {
        this.countAvailabilityDays = count;
    }
    public int getCountAvailabilityDays() {
        return countAvailabilityDays;
    }

}
    class SortByAvailable implements Comparator<Student>{

        @Override
        public int compare(Student o1, Student o2) {
            return o1.getCountAvailabilityDays() - o2.getCountAvailabilityDays();
        }
}