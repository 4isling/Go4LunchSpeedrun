
package com.Hayse.go4lunch.domain.entites.map_api.detail;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DetailResponse implements Serializable
{

    @SerializedName("html_attributions")
    @Expose
    private List<Object> htmlAttributions;
    @SerializedName("result")
    @Expose
    private Result result;
    @SerializedName("status")
    @Expose
    private String status;
    private final static long serialVersionUID = -5080715163520045632L;

    public List<Object> getHtmlAttributions() {
        return htmlAttributions;
    }

    public void setHtmlAttributions(List<Object> htmlAttributions) {
        this.htmlAttributions = htmlAttributions;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = ((result* 31)+((this.result == null)? 0 :this.result.hashCode()));
        result = ((result* 31)+((this.htmlAttributions == null)? 0 :this.htmlAttributions.hashCode()));
        result = ((result* 31)+((this.status == null)? 0 :this.status.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof DetailResponse) == false) {
            return false;
        }
        DetailResponse rhs = ((DetailResponse) other);
        return ((((this.result == rhs.result)||((this.result!= null)&&this.result.equals(rhs.result)))&&((this.htmlAttributions == rhs.htmlAttributions)||((this.htmlAttributions!= null)&&this.htmlAttributions.equals(rhs.htmlAttributions))))&&((this.status == rhs.status)||((this.status!= null)&&this.status.equals(rhs.status))));
    }

}
