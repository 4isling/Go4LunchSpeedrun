package com.Hayse.go4lunch.domain.entites.map_api.nerbysearch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

// This class has been generated with https://www.jsonschema2pojo.org/
public class OpeningHours implements Serializable
{

    @SerializedName("open_now")
    @Expose
    private boolean openNow;
    private final static long serialVersionUID = 1292748001651430399L;

    /**
     * No args constructor for use in serialization
     *
     */
    public OpeningHours() {
    }

    /**
     *
     * @param openNow
     */
    public OpeningHours(boolean openNow) {
        super();
        this.openNow = openNow;
    }

    public boolean isOpenNow() {
        return openNow;
    }

    public void setOpenNow(boolean openNow) {
        this.openNow = openNow;
    }

    public OpeningHours withOpenNow(boolean openNow) {
        this.openNow = openNow;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(OpeningHours.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("openNow");
        sb.append('=');
        sb.append(this.openNow);
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = ((result* 31)+(this.openNow? 1 : 0));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof OpeningHours) == false) {
            return false;
        }
        OpeningHours rhs = ((OpeningHours) other);
        return (this.openNow == rhs.openNow);
    }

}
