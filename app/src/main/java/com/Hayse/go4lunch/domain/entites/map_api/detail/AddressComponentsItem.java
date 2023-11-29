package com.Hayse.go4lunch.domain.entites.map_api.detail;

import java.util.List;

public class AddressComponentsItem {
    private List<String> types;
    private String shortName;
    private String longName;

    public void setTypes(List<String> types){
        this.types = types;
    }

    public List<String> getTypes(){
        return types;
    }

    public void setShortName(String shortName){
        this.shortName = shortName;
    }

    public String getShortName(){
        return shortName;
    }

    public void setLongName(String longName){
        this.longName = longName;
    }

    public String getLongName(){
        return longName;
    }

    @Override
    public String toString(){
        return
                "AddressComponentsItem{" +
                        "types = '" + types + '\'' +
                        ",short_name = '" + shortName + '\'' +
                        ",long_name = '" + longName + '\'' +
                        "}";
    }
}