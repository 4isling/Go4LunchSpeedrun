package com.Hayse.go4lunch.domain.entites.map_api.nerbysearch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

// This class has been generated with https://www.jsonschema2pojo.org/
public class RestaurantResult implements Serializable
{

    @SerializedName("html_attributions")
    @Expose
    private List<Object> htmlAttributions = null;
    @SerializedName("results")
    @Expose
    private List<Result> results = null;
    @SerializedName("status")
    @Expose
    private String status;
    private final static long serialVersionUID = -4217243151372976012L;

    /**
     * No args constructor for use in serialization
     *
     */
    public RestaurantResult() {
    }

    /**
     *
     * @param htmlAttributions
     * @param results
     * @param status
     */
    public RestaurantResult(List<Object> htmlAttributions, List<Result> results, String status) {
        super();
        this.htmlAttributions = htmlAttributions;
        this.results = results;
        this.status = status;
    }

    public List<Object> getHtmlAttributions() {
        return htmlAttributions;
    }

    public void setHtmlAttributions(List<Object> htmlAttributions) {
        this.htmlAttributions = htmlAttributions;
    }

    public RestaurantResult withHtmlAttributions(List<Object> htmlAttributions) {
        this.htmlAttributions = htmlAttributions;
        return this;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    public RestaurantResult withResults(List<Result> results) {
        this.results = results;
        return this;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public RestaurantResult withStatus(String status) {
        this.status = status;
        return this;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(RestaurantResult.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("htmlAttributions");
        sb.append('=');
        sb.append(((this.htmlAttributions == null)?"<null>":this.htmlAttributions));
        sb.append(',');
        sb.append("results");
        sb.append('=');
        sb.append(((this.results == null)?"<null>":this.results));
        sb.append(',');
        sb.append("status");
        sb.append('=');
        sb.append(((this.status == null)?"<null>":this.status));
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
        result = ((result* 31)+((this.results == null)? 0 :this.results.hashCode()));
        result = ((result* 31)+((this.htmlAttributions == null)? 0 :this.htmlAttributions.hashCode()));
        result = ((result* 31)+((this.status == null)? 0 :this.status.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof RestaurantResult) == false) {
            return false;
        }
        RestaurantResult rhs = ((RestaurantResult) other);
        return ((((this.results == rhs.results)||((this.results!= null)&&this.results.equals(rhs.results)))&&((this.htmlAttributions == rhs.htmlAttributions)||((this.htmlAttributions!= null)&&this.htmlAttributions.equals(rhs.htmlAttributions))))&&((this.status == rhs.status)||((this.status!= null)&&this.status.equals(rhs.status))));
    }

}
