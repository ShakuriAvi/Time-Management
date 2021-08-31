package com.example.timemanagement.Class;

public class Clock {
    int startHour;
    double startMinutes;
    int finishHour;
    double finishMinutes;

    public void setFinishHour(int finishHour) {
        this.finishHour = finishHour;
    }

    public void setFinishMinutes(double finishMinutes) {
        this.finishMinutes = finishMinutes;
    }

    public Clock(int startHour, double startMinutes, double during){
        this.startHour = startHour;
        this.startMinutes = startMinutes;
        computeNewFinish(during);
    }
    public void computeNewFinish(double during){
        double temp = during*60;

            if(startMinutes + temp > 60) {
                setFinishHour(startHour +1);
                setFinishMinutes((startMinutes + temp) % 60);
            }else if(startMinutes + temp == 60){
                setFinishHour(startHour +1);
                setFinishMinutes( 0);
            }else {
                setFinishMinutes((startMinutes + temp) % 60);
                setFinishHour(startHour);
            }
    }

    public int getStartHour() {
        return startHour;
    }

    public double getStartMinutes() {
        return startMinutes;
    }

    public int getFinishHour() {
        return finishHour;
    }

    public double getFinishMinutes() {
        return finishMinutes;
    }
}
