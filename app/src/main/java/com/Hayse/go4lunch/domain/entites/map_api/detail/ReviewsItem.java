package com.Hayse.go4lunch.domain.entites.map_api.detail;

public class ReviewsItem {
    private String authorName;
    private String profilePhotoUrl;
    private String authorUrl;
    private int rating;
    private String language;
    private String text;
    private int time;
    private String relativeTimeDescription;

    public void setAuthorName(String authorName){
        this.authorName = authorName;
    }

    public String getAuthorName(){
        return authorName;
    }

    public void setProfilePhotoUrl(String profilePhotoUrl){
        this.profilePhotoUrl = profilePhotoUrl;
    }

    public String getProfilePhotoUrl(){
        return profilePhotoUrl;
    }

    public void setAuthorUrl(String authorUrl){
        this.authorUrl = authorUrl;
    }

    public String getAuthorUrl(){
        return authorUrl;
    }

    public void setRating(int rating){
        this.rating = rating;
    }

    public int getRating(){
        return rating;
    }

    public void setLanguage(String language){
        this.language = language;
    }

    public String getLanguage(){
        return language;
    }

    public void setText(String text){
        this.text = text;
    }

    public String getText(){
        return text;
    }

    public void setTime(int time){
        this.time = time;
    }

    public int getTime(){
        return time;
    }

    public void setRelativeTimeDescription(String relativeTimeDescription){
        this.relativeTimeDescription = relativeTimeDescription;
    }

    public String getRelativeTimeDescription(){
        return relativeTimeDescription;
    }

    @Override
    public String toString(){
        return
                "ReviewsItem{" +
                        "author_name = '" + authorName + '\'' +
                        ",profile_photo_url = '" + profilePhotoUrl + '\'' +
                        ",author_url = '" + authorUrl + '\'' +
                        ",rating = '" + rating + '\'' +
                        ",language = '" + language + '\'' +
                        ",text = '" + text + '\'' +
                        ",time = '" + time + '\'' +
                        ",relative_time_description = '" + relativeTimeDescription + '\'' +
                        "}";
    }
}