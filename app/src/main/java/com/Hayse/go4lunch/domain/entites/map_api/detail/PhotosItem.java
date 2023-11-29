package com.Hayse.go4lunch.domain.entites.map_api.detail;

import java.util.List;

public class PhotosItem {
    private String photoReference;
    private int width;
    private List<String> htmlAttributions;
    private int height;

    public void setPhotoReference(String photoReference){
        this.photoReference = photoReference;
    }

    public String getPhotoReference(){
        return photoReference;
    }

    public void setWidth(int width){
        this.width = width;
    }

    public int getWidth(){
        return width;
    }

    public void setHtmlAttributions(List<String> htmlAttributions){
        this.htmlAttributions = htmlAttributions;
    }

    public List<String> getHtmlAttributions(){
        return htmlAttributions;
    }

    public void setHeight(int height){
        this.height = height;
    }

    public int getHeight(){
        return height;
    }

    @Override
    public String toString(){
        return
                "PhotosItem{" +
                        "photo_reference = '" + photoReference + '\'' +
                        ",width = '" + width + '\'' +
                        ",html_attributions = '" + htmlAttributions + '\'' +
                        ",height = '" + height + '\'' +
                        "}";
    }
}