package com.Hayse.go4lunch.domain.entites.map_api.detail;

public class Viewport {
    private Southwest southwest;
    private Northeast northeast;

    public void setSouthwest(Southwest southwest){
        this.southwest = southwest;
    }

    public Southwest getSouthwest(){
        return southwest;
    }

    public void setNortheast(Northeast northeast){
        this.northeast = northeast;
    }

    public Northeast getNortheast(){
        return northeast;
    }

    @Override
    public String toString(){
        return
                "Viewport{" +
                        "southwest = '" + southwest + '\'' +
                        ",northeast = '" + northeast + '\'' +
                        "}";
    }
}
