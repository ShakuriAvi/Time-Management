package com.example.timemanagement.Class;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;

public class Coach {
   private String firstName;
   private Hashtable<Integer, Boolean[]> availability;
   private double[] countHoursAvailability;
   private ArrayList<Company.Swimming_Style> swimmingStyle;
   private HashMap<Integer, ArrayList< Lesson>> scheduleCoach = new HashMap<>();
   public Coach(String firstName, Hashtable<Integer, Boolean[]> availability, ArrayList<Company.Swimming_Style> swimmingStyle, double[] countHoursAvailability){
      this.firstName=firstName;
      this.availability = availability;
      this.swimmingStyle = swimmingStyle;
      this.countHoursAvailability =  countHoursAvailability;
   }
   public HashMap<Integer, ArrayList<Lesson>> getScheduleCoach() {
      return scheduleCoach;
   }

   public void setScheduleCoach(HashMap<Integer, ArrayList<Lesson>> scheduleCoache) {
      this.scheduleCoach = scheduleCoache;
   }


   public String getFirstName() {
      return firstName;
   }

   public Hashtable<Integer, Boolean[]> getAvailability() {
      return availability;
   }

   public ArrayList<Company.Swimming_Style> getSwimmingStyle() {
      return swimmingStyle;
   }


   public double[] getCountHoursAvailability() {
      return countHoursAvailability;
   }

   public void setCountHoursAvailability(double[] countHoursAvailability) {
      this.countHoursAvailability = countHoursAvailability;
   }


}
