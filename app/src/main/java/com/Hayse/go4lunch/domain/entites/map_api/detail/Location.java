package com.Hayse.go4lunch.domain.entites.map_api.detail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

// This class has been generated with https://www.jsonschema2pojo.org/
public class Location
{

    private Object lng;
    private Object lat;

    public void setLng(Object lng){
        this.lng = lng;
    }

    public Object getLng(){
        return lng;
    }

    public void setLat(Object lat){
        this.lat = lat;
    }

    public Object getLat(){
        return lat;
    }

    @Override
    public String toString(){
        return
                "Location{" +
                        "lng = '" + lng + '\'' +
                        ",lat = '" + lat + '\'' +
                        "}";
    }
}
