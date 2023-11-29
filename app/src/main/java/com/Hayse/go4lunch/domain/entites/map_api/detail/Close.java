package com.Hayse.go4lunch.domain.entites.map_api.detail;

public class Close {
    private String time;
    private int day;

    public void setTime(String time){
        this.time = time;
    }

    public String getTime(){
        return time;
    }

    public void setDay(int day){
        this.day = day;
    }

    public int getDay(){
        return day;
    }

    @Override
    public String toString(){
        return
                "Close{" +
                        "time = '" + time + '\'' +
                        ",day = '" + day + '\'' +
                        "}";
    }
}
