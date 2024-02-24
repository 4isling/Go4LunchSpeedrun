
package com.Hayse.go4lunch.domain.entites.map_api.autocomplete;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Prediction implements Serializable
{

    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("matched_substrings")
    @Expose
    private List<MatchedSubstring> matchedSubstrings;
    @SerializedName("place_id")
    @Expose
    private String placeId;
    @SerializedName("reference")
    @Expose
    private String reference;
    @SerializedName("structured_formatting")
    @Expose
    private StructuredFormatting structuredFormatting;
    @SerializedName("terms")
    @Expose
    private List<Term> terms;
    @SerializedName("types")
    @Expose
    private List<String> types;
    private final static long serialVersionUID = -7033844076355218095L;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<MatchedSubstring> getMatchedSubstrings() {
        return matchedSubstrings;
    }

    public void setMatchedSubstrings(List<MatchedSubstring> matchedSubstrings) {
        this.matchedSubstrings = matchedSubstrings;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public StructuredFormatting getStructuredFormatting() {
        return structuredFormatting;
    }

    public void setStructuredFormatting(StructuredFormatting structuredFormatting) {
        this.structuredFormatting = structuredFormatting;
    }

    public List<Term> getTerms() {
        return terms;
    }

    public void setTerms(List<Term> terms) {
        this.terms = terms;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = ((result* 31)+((this.reference == null)? 0 :this.reference.hashCode()));
        result = ((result* 31)+((this.types == null)? 0 :this.types.hashCode()));
        result = ((result* 31)+((this.structuredFormatting == null)? 0 :this.structuredFormatting.hashCode()));
        result = ((result* 31)+((this.terms == null)? 0 :this.terms.hashCode()));
        result = ((result* 31)+((this.matchedSubstrings == null)? 0 :this.matchedSubstrings.hashCode()));
        result = ((result* 31)+((this.placeId == null)? 0 :this.placeId.hashCode()));
        result = ((result* 31)+((this.description == null)? 0 :this.description.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Prediction) == false) {
            return false;
        }
        Prediction rhs = ((Prediction) other);
        return ((((((((this.reference == rhs.reference)||((this.reference!= null)&&this.reference.equals(rhs.reference)))&&((this.types == rhs.types)||((this.types!= null)&&this.types.equals(rhs.types))))&&((this.structuredFormatting == rhs.structuredFormatting)||((this.structuredFormatting!= null)&&this.structuredFormatting.equals(rhs.structuredFormatting))))&&((this.terms == rhs.terms)||((this.terms!= null)&&this.terms.equals(rhs.terms))))&&((this.matchedSubstrings == rhs.matchedSubstrings)||((this.matchedSubstrings!= null)&&this.matchedSubstrings.equals(rhs.matchedSubstrings))))&&((this.placeId == rhs.placeId)||((this.placeId!= null)&&this.placeId.equals(rhs.placeId))))&&((this.description == rhs.description)||((this.description!= null)&&this.description.equals(rhs.description))));
    }

}
