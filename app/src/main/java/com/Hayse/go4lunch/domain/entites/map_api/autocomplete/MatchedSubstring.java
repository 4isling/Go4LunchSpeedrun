
package com.Hayse.go4lunch.domain.entites.map_api.autocomplete;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MatchedSubstring implements Serializable
{

    @SerializedName("length")
    @Expose
    private Long length;
    @SerializedName("offset")
    @Expose
    private Long offset;
    private final static long serialVersionUID = 6963343864830733665L;

    public Long getLength() {
        return length;
    }

    public void setLength(Long length) {
        this.length = length;
    }

    public Long getOffset() {
        return offset;
    }

    public void setOffset(Long offset) {
        this.offset = offset;
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = ((result* 31)+((this.length == null)? 0 :this.length.hashCode()));
        result = ((result* 31)+((this.offset == null)? 0 :this.offset.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof MatchedSubstring) == false) {
            return false;
        }
        MatchedSubstring rhs = ((MatchedSubstring) other);
        return (((this.length == rhs.length)||((this.length!= null)&&this.length.equals(rhs.length)))&&((this.offset == rhs.offset)||((this.offset!= null)&&this.offset.equals(rhs.offset))));
    }

}
