package com.Hayse.go4lunch.domain.entites.map_api.detail;
import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Photo implements Serializable
{

    @SerializedName("height")
    @Expose
    private Long height;
    @SerializedName("html_attributions")
    @Expose
    private List<String> htmlAttributions;
    @SerializedName("photo_reference")
    @Expose
    private String photoReference;
    @SerializedName("width")
    @Expose
    private Long width;
    private final static long serialVersionUID = -6148124466470141147L;

    public Long getHeight() {
        return height;
    }

    public void setHeight(Long height) {
        this.height = height;
    }

    public List<String> getHtmlAttributions() {
        return htmlAttributions;
    }

    public void setHtmlAttributions(List<String> htmlAttributions) {
        this.htmlAttributions = htmlAttributions;
    }

    public String getPhotoReference() {
        return photoReference;
    }

    public void setPhotoReference(String photoReference) {
        this.photoReference = photoReference;
    }

    public Long getWidth() {
        return width;
    }

    public void setWidth(Long width) {
        this.width = width;
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = ((result* 31)+((this.width == null)? 0 :this.width.hashCode()));
        result = ((result* 31)+((this.htmlAttributions == null)? 0 :this.htmlAttributions.hashCode()));
        result = ((result* 31)+((this.photoReference == null)? 0 :this.photoReference.hashCode()));
        result = ((result* 31)+((this.height == null)? 0 :this.height.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Photo) == false) {
            return false;
        }
        Photo rhs = ((Photo) other);
        return (((((this.width == rhs.width)||((this.width!= null)&&this.width.equals(rhs.width)))&&((this.htmlAttributions == rhs.htmlAttributions)||((this.htmlAttributions!= null)&&this.htmlAttributions.equals(rhs.htmlAttributions))))&&((this.photoReference == rhs.photoReference)||((this.photoReference!= null)&&this.photoReference.equals(rhs.photoReference))))&&((this.height == rhs.height)||((this.height!= null)&&this.height.equals(rhs.height))));
    }

}
