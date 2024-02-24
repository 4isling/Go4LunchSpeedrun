
package com.Hayse.go4lunch.domain.entites.map_api.autocomplete;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AutocompleteResponse implements Serializable {

    @SerializedName("predictions")
    @Expose
    private List<Prediction> predictions;
    @SerializedName("status")
    @Expose
    private String status;
    private final static long serialVersionUID = 1014875058694172564L;

    public List<Prediction> getPredictions() {
        return predictions;
    }

    public void setPredictions(List<Prediction> predictions) {
        this.predictions = predictions;
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
        result = ((result* 31)+((this.predictions == null)? 0 :this.predictions.hashCode()));
        result = ((result* 31)+((this.status == null)? 0 :this.status.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof AutocompleteResponse) == false) {
            return false;
        }
        AutocompleteResponse rhs = ((AutocompleteResponse) other);
        return (((this.predictions == rhs.predictions)||((this.predictions!= null)&&this.predictions.equals(rhs.predictions)))&&((this.status == rhs.status)||((this.status!= null)&&this.status.equals(rhs.status))));
    }

}
