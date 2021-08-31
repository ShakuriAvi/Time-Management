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

public class Schedule {
    private ArrayList<Coach> coachesList;
    private ArrayList<Student> studentsList;
    ArrayList<Conflict> conflicts;

    public Schedule(ArrayList<Coach> coachesList,ArrayList<Student> studentsList){
        this.coachesList=coachesList;
        this.studentsList=studentsList;
    }

    public HashMap<Integer, ArrayList<Lesson>> getScheduleAllCoaches() {
        return scheduleAllCoaches;
    }

    private HashMap<Integer, ArrayList< Lesson>> scheduleAllCoaches;


    public ArrayList<Conflict> getConflicts() {
        return conflicts;
    }

    public void makeSchedule() {
        Hashtable<Company.Swimming_Style, double[]> availabilityBySwimming_Style = new Hashtable<Company.Swimming_Style, double[]>(); //
        initAvailabilityByCoaches(availabilityBySwimming_Style);

        HashMap<Integer, ArrayList<Student>> appointmentByStudent = new HashMap<>();
        sortAppointmentsByStudent(appointmentByStudent);

        scheduleAllCoaches = new HashMap<>();
        conflicts = new ArrayList<>();

        manageScheduleAllCoaches(appointmentByStudent, conflicts);
        manageScheduleEachCoaches();
    }
    private void manageScheduleEachCoaches(){

        for (int i = 1; i <6 ; i++) {
            if (scheduleAllCoaches.get(i) != null) {
                for (int j = 0; j <scheduleAllCoaches.get(i).size() ; j++) {
                    Lesson myObject =  scheduleAllCoaches.get(i).get(j);
                    HashMap<Integer, ArrayList< Lesson>> scheduleCoach = myObject.getCoach().getScheduleCoach();
                    ArrayList<Lesson> lessons = scheduleCoach.get(i);
                    if(lessons == null)
                        lessons = new ArrayList<>();
                    if ( myObject instanceof TeamLesson) {
                        TeamLesson teamLesson = (TeamLesson) myObject;
                        lessons.add(teamLesson);
                        scheduleCoach.put(i,lessons);
                    }else{
                        PrivateLesson privateLesson = (PrivateLesson) myObject;
                        lessons.add(privateLesson);
                        scheduleCoach.put(i,lessons);
                    }
                    myObject.getCoach().setScheduleCoach(scheduleCoach);
                }
            }
        }
    }
    private void manageScheduleAllCoaches(  HashMap<Integer, ArrayList<Student>> appointmentByStudent, ArrayList<Conflict> conflicts) {

        for (int i = 1; i < 6; i++) {
            ArrayList<Student> tempStudentArr = appointmentByStudent.get(i);
            ArrayList<Lesson> lessonArrayList = new ArrayList<>();
            for (int j = 0; j < tempStudentArr.size(); j++) {
                Student tempStudent = tempStudentArr.get(j);
                if (scheduleAllCoaches.get(i) == null) {
                    Lesson lesson = createNewLesson(tempStudent, i);
                    if(lesson == null) {
                        conflicts.add(new Conflict( tempStudent,i));
                    }else{
                        lessonArrayList.add(lesson);
                    }
                } else {
                    if(tempStudent.getFavoriteLesson().equals(PRIVATE)) {
                        Lesson lesson = createNewLesson(tempStudent, i);
                        if(lesson == null)
                            conflicts.add(new Conflict( tempStudent,i));
                        else
                            lessonArrayList.add(lesson);
                    }else {
                        boolean exist = false;
                        TeamLesson teamLesson = null;
                        for (int k = 0; k <scheduleAllCoaches.get(i).size() ; k++) {
                            if ((scheduleAllCoaches.get(i).get(k).getLesson_type().equals(TEAM) || tempStudent.getFavoriteLesson().equals(BOTH)) && scheduleAllCoaches.get(i).get(k).getSwimming_style().equals(tempStudent.getSwimmingStyle()) ) { //check type of exist team lesson
                                exist = true;
                                Lesson myObject =  scheduleAllCoaches.get(i).get(k);
                                if ( myObject instanceof TeamLesson) {
                                    teamLesson = (TeamLesson) myObject;
                                }
                                break;
                            }
                        }
                        if(exist == true) {
                            addStudentToTeamLesson(teamLesson, tempStudent);
                        }
                        else {
                            Lesson lesson = createNewLesson(tempStudent, i);
                            if(lesson == null)
                                conflicts.add(new Conflict( tempStudent,i));
                            else
                                lessonArrayList.add(lesson);
                        }
                    }
                }
                scheduleAllCoaches.put(i,lessonArrayList);
            }
        }
    }

    private void addStudentToTeamLesson(TeamLesson lesson, Student tempStudent) {
        lesson.getParticipants().add(tempStudent);
    }



    private Lesson createNewLesson(Student student, int day) {
        for (int i = 0; i < coachesList.size(); i++) {
                double countHoursAvailabilityCoach = coachesList.get(i).getCountHoursAvailability()[day - 1];
                if ( countHoursAvailabilityCoach > 0.75) {
                    for (int k = 0; k < coachesList.get(i).getSwimmingStyle().size() ; k++) {
                        if(coachesList.get(i).getSwimmingStyle().get(k).equals(student.getSwimmingStyle())) {
                            if((student.getFavoriteLesson().equals(TEAM) || student.getFavoriteLesson().equals(BOTH)) && coachesList.get(i).getCountHoursAvailability()[day - 1] > 1) {
                                coachesList.get(i).getCountHoursAvailability()[day - 1] -= 1;
                                TeamLesson newTeam =  new TeamLesson(TEAM, coachesList.get(i), student.getSwimmingStyle());
                                ArrayList<Student> participants = new ArrayList<>();
                                participants.add(student);
                                newTeam.setParticipants(participants);
                                return newTeam;
                            }
                            else {
                                coachesList.get(i).getCountHoursAvailability()[day - 1] -= 0.75;
                                PrivateLesson newTeam  = new PrivateLesson(PRIVATE, coachesList.get(i), student, student.getSwimmingStyle());
                                return newTeam;
                            }
                        }
                    }

                }
            }
        return null;
    }




    private void sortAppointmentsByStudent(HashMap<Integer,ArrayList<Student>> appointment) {
        Collections.sort(studentsList, new SortByAvailable());
        for (int i = 0; i < studentsList.size(); i++) {
            ArrayList<Student> tempStudents;
            for (int j = 0; j < studentsList.get(i).getAvailableDays().size() ; j++) {
                int day = studentsList.get(i).getAvailableDays().get(j);
                if(appointment.get(day) == null)
                    tempStudents = new ArrayList<>();
                else
                    tempStudents = appointment.get(day);
                tempStudents.add(studentsList.get(i));
                appointment.put(day,tempStudents);
            }
        }
    }

    private void initAvailabilityByCoaches(Hashtable<Company.Swimming_Style, double[]> availabilityBySwimming_Style){
        for (int i = 0; i <coachesList.size() ; i++) {
            for (int j = 0; j < coachesList.get(i).getSwimmingStyle().size(); j++) {
                double[] computeWorkHours = new double[5];
                if(availabilityBySwimming_Style.get(coachesList.get(i).getSwimmingStyle().get(j)) == null){
                    Arrays.fill(computeWorkHours , 0);
                }else{
                    computeWorkHours = availabilityBySwimming_Style.get(coachesList.get(i).getSwimmingStyle().get(j));
                }
                computeHours(coachesList.get(i),computeWorkHours);
                availabilityBySwimming_Style.put(coachesList.get(i).getSwimmingStyle().get(j),computeWorkHours);
            }
        }
        // printAvailabilityBySwimming_Style(availabilityBySwimming_Style);
    }
    private void computeHours(Coach coach,double[] computeWorkHours) {

        for (int i = 0; i < coach.getCountHoursAvailability().length; i++) {

            computeWorkHours[i] += coach.getCountHoursAvailability()[i];
        }
    }
}

