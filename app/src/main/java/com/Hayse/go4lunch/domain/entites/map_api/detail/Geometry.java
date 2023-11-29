package com.Hayse.go4lunch.domain.entites.map_api.detail;

public class Geometry {
    private Viewport viewport;
    private Location location;

    public void setViewport(Viewport viewport){
        this.viewport = viewport;
    }

    public Viewport getViewport(){
        return viewport;
    }

    public void setLocation(Location location){
        this.location = location;
    }

    public Location getLocation(){
        return location;
    }

    @Override
    public String toString(){
        return
                "Geometry{" +
                        "viewport = '" + viewport + '\'' +
                        ",location = '" + location + '\'' +
                        "}";
    }
}
