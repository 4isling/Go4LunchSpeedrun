
package com.Hayse.go4lunch.domain.entites.map_api.detail;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result implements Serializable
{
    @SerializedName("formatted_address")
    @Expose
    private String formattedAddress;
    @SerializedName("formatted_phone_number")
    @Expose
    private String formattedPhoneNumber;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("photos")
    @Expose
    private List<Photo> photos;
    @SerializedName("place_id")
    @Expose
    private String placeId;
    @SerializedName("rating")
    @Expose
    private Float rating;
    @SerializedName("types")
    @Expose
    private List<String> types;
    @SerializedName("website")
    @Expose
    private String website;
    private final static long serialVersionUID = 4590850312801698114L;

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
    }

    public String getFormattedPhoneNumber() {
        return formattedPhoneNumber;
    }

    public void setFormattedPhoneNumber(String formattedPhoneNumber) {
        this.formattedPhoneNumber = formattedPhoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = ((result* 31)+((this.formattedPhoneNumber == null)? 0 :this.formattedPhoneNumber.hashCode()));
        result = ((result* 31)+((this.types == null)? 0 :this.types.hashCode()));
        result = ((result* 31)+((this.website == null)? 0 :this.website.hashCode()));
        result = ((result* 31)+((this.formattedAddress == null)? 0 :this.formattedAddress.hashCode()));
        result = ((result* 31)+((this.name == null)? 0 :this.name.hashCode()));
        result = ((result* 31)+((this.placeId == null)? 0 :this.placeId.hashCode()));
        result = ((result* 31)+((this.rating == null)? 0 :this.rating.hashCode()));
        result = ((result* 31)+((this.photos == null)? 0 :this.photos.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Result) == false) {
            return false;
        }
        Result rhs = ((Result) other);
        return (((((((((this.formattedPhoneNumber == rhs.formattedPhoneNumber)||((this.formattedPhoneNumber!= null)&&this.formattedPhoneNumber.equals(rhs.formattedPhoneNumber)))&&((this.types == rhs.types)||((this.types!= null)&&this.types.equals(rhs.types))))&&((this.website == rhs.website)||((this.website!= null)&&this.website.equals(rhs.website))))&&((this.formattedAddress == rhs.formattedAddress)||((this.formattedAddress!= null)&&this.formattedAddress.equals(rhs.formattedAddress))))&&((this.name == rhs.name)||((this.name!= null)&&this.name.equals(rhs.name))))&&((this.placeId == rhs.placeId)||((this.placeId!= null)&&this.placeId.equals(rhs.placeId))))&&((this.rating == rhs.rating)||((this.rating!= null)&&this.rating.equals(rhs.rating))))&&((this.photos == rhs.photos)||((this.photos!= null)&&this.photos.equals(rhs.photos))));
    }

}
