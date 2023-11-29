package com.Hayse.go4lunch.domain.entites.map_api.detail;

import java.util.List;

public class DetailResponse {
    private Result result;
    private List<Object> htmlAttributions;
    private String status;

    public void setResult(Result result){
        this.result = result;
    }

    public Result getResult(){
        return result;
    }

    public void setHtmlAttributions(List<Object> htmlAttributions){
        this.htmlAttributions = htmlAttributions;
    }

    public List<Object> getHtmlAttributions(){
        return htmlAttributions;
    }

    public void setStatus(String status){
        this.status = status;
    }

    public String getStatus(){
        return status;
    }

    @Override
    public String toString(){
        return
                "DetailResponse{" +
                        "result = '" + result + '\'' +
                        ",html_attributions = '" + htmlAttributions + '\'' +
                        ",status = '" + status + '\'' +
                        "}";
    }
}