package com.example.timemanagement.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timemanagement.Class.Clock;
import com.example.timemanagement.Class.Company;
import com.example.timemanagement.Class.Lesson;
import com.example.timemanagement.Class.PrivateLesson;
import com.example.timemanagement.Class.TeamLesson;
import com.example.timemanagement.R;


import java.util.ArrayList;
import java.util.List;




    public class Adapter_ScheduleHours extends RecyclerView.Adapter<Adapter_ScheduleHours.ViewHolder> {

        private Context context;
        private ArrayList<Lesson> lessons;
       private ArrayList<Clock> times;
       private int day;
       private boolean isAllCoaches;
        public Adapter_ScheduleHours(Context context, ArrayList<Lesson> lessons,int day,boolean isAllCoaches){
            this.context= context;
            this.lessons = lessons;
            times = new ArrayList<>();
            this.day = day;
            this.isAllCoaches = isAllCoaches;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.recycleview_schedule, parent,false);
            return new Adapter_ScheduleHours.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
          Lesson lesson = lessons.get(position);
          addNewLessonToSchedule(lesson,position);
            int newStartHour = times.get(position).getStartHour();
            int newStartMinutes= (int)times.get(position).getStartMinutes();
            int newFinishHour = times.get(position).getFinishHour();
            int newFinishMinutes= (int)times.get(position).getFinishMinutes();
            holder.recycleTeams_TXT_time.setText(newStartHour + ":" + newStartMinutes + " - " + newFinishHour + ":" + newFinishMinutes);
            holder.recycleTeams_TXT_lesson.setText(lesson.getSwimming_style().toString());

            String nameParticipants = "";
//
            if ( lesson instanceof TeamLesson) {
                TeamLesson teamLesson = (TeamLesson) lesson;

                for (int i = 0; i < teamLesson.getParticipants().size() ; i++) {
                    nameParticipants +=" Team Lesson \n Participants:\n " + teamLesson.getParticipants().get(i).getFirstName() + " " + teamLesson.getParticipants().get(i).getLastName() + ",\n";
                }

            }else {
                PrivateLesson privateLesson = (PrivateLesson) lesson;
                nameParticipants += " Private Lesson \n Participant: " + privateLesson.getParticipant().getFirstName()+ " " + privateLesson.getParticipant().getLastName();
            }
            holder.recycleTeams_TXT_participants.setText(nameParticipants);
        }

        private void addNewLessonToSchedule(Lesson lesson,int position) {
            double during=lesson.getLesson_type()== Company.Lesson_Type.TEAM?1:0.75; //in houres
            if(position ==0) { //first lesson in day
                Boolean[] hoursInDay = lesson.getCoach().getAvailability().get(day);
                int startHour = 0;
                for (int i = 0; i <hoursInDay.length ; i++) {
                    if(hoursInDay[i] != null){
                        startHour = i+8; //because day start at 8 am
                        break;
                    }

                }
                times.add(new Clock(startHour, 0,during));
            }else{
                int newStartHour = times.get(position-1).getFinishHour();
                double newStartMinutes= times.get(position-1).getFinishMinutes();
                times.add(new Clock(newStartHour,newStartMinutes,during));
            }

        }


        @Override
        public int getItemCount() {
            return lessons.size();
        }
        Lesson getItem(int id) {
            return lessons.get(id);
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView recycleTeams_TXT_time;
        private TextView recycleTeams_TXT_lesson;
        private TextView recycleTeams_TXT_participants;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                recycleTeams_TXT_time =itemView.findViewById(R.id.recycleTeams_TXT_time);
                recycleTeams_TXT_lesson = itemView.findViewById(R.id.recycleTeams_TXT_lesson);
                recycleTeams_TXT_participants = itemView.findViewById(R.id.recycleTeams_TXT_participants);
            }
        }
    }


