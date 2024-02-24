
package com.Hayse.go4lunch.domain.entites.map_api.autocomplete;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Term implements Serializable
{

    @SerializedName("offset")
    @Expose
    private Long offset;
    @SerializedName("value")
    @Expose
    private String value;
    private final static long serialVersionUID = -136700936462764628L;

    public Long getOffset() {
        return offset;
    }

    public void setOffset(Long offset) {
        this.offset = offset;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = ((result* 31)+((this.offset == null)? 0 :this.offset.hashCode()));
        result = ((result* 31)+((this.value == null)? 0 :this.value.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Term) == false) {
            return false;
        }
        Term rhs = ((Term) other);
        return (((this.offset == rhs.offset)||((this.offset!= null)&&this.offset.equals(rhs.offset)))&&((this.value == rhs.value)||((this.value!= null)&&this.value.equals(rhs.value))));
    }

}
