package com.Hayse.go4lunch.domain.entites.map_api.detail;

public class Northeast {
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
                "Northeast{" +
                        "lng = '" + lng + '\'' +
                        ",lat = '" + lat + '\'' +
                        "}";
    }
}
