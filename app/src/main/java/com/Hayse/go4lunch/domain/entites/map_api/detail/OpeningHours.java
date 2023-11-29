package com.Hayse.go4lunch.domain.entites.map_api.detail;

import java.util.List;

public class OpeningHours {
    private boolean openNow;
    private List<PeriodsItem> periods;
    private List<String> weekdayText;

    public void setOpenNow(boolean openNow){
        this.openNow = openNow;
    }

    public boolean isOpenNow(){
        return openNow;
    }

    public void setPeriods(List<PeriodsItem> periods){
        this.periods = periods;
    }

    public List<PeriodsItem> getPeriods(){
        return periods;
    }

    public void setWeekdayText(List<String> weekdayText){
        this.weekdayText = weekdayText;
    }

    public List<String> getWeekdayText(){
        return weekdayText;
    }

    @Override
    public String toString(){
        return
                "OpeningHours{" +
                        "open_now = '" + openNow + '\'' +
                        ",periods = '" + periods + '\'' +
                        ",weekday_text = '" + weekdayText + '\'' +
                        "}";
    }
}