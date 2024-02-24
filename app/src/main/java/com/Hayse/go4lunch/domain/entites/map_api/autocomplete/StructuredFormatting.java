
package com.Hayse.go4lunch.domain.entites.map_api.autocomplete;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StructuredFormatting implements Serializable
{
    @SerializedName("main_text")
    @Expose
    private String mainText;
    @SerializedName("main_text_matched_substrings")
    @Expose
    private List<MainTextMatchedSubstring> mainTextMatchedSubstrings;
    @SerializedName("secondary_text")
    @Expose
    private String secondaryText;
    private final static long serialVersionUID = -6563211244037809902L;

    public String getMainText() {
        return mainText;
    }

    public void setMainText(String mainText) {
        this.mainText = mainText;
    }

    public List<MainTextMatchedSubstring> getMainTextMatchedSubstrings() {
        return mainTextMatchedSubstrings;
    }

    public void setMainTextMatchedSubstrings(List<MainTextMatchedSubstring> mainTextMatchedSubstrings) {
        this.mainTextMatchedSubstrings = mainTextMatchedSubstrings;
    }

    public String getSecondaryText() {
        return secondaryText;
    }

    public void setSecondaryText(String secondaryText) {
        this.secondaryText = secondaryText;
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = ((result* 31)+((this.mainText == null)? 0 :this.mainText.hashCode()));
        result = ((result* 31)+((this.mainTextMatchedSubstrings == null)? 0 :this.mainTextMatchedSubstrings.hashCode()));
        result = ((result* 31)+((this.secondaryText == null)? 0 :this.secondaryText.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof StructuredFormatting) == false) {
            return false;
        }
        StructuredFormatting rhs = ((StructuredFormatting) other);
        return ((((this.mainText == rhs.mainText)||((this.mainText!= null)&&this.mainText.equals(rhs.mainText)))&&((this.mainTextMatchedSubstrings == rhs.mainTextMatchedSubstrings)||((this.mainTextMatchedSubstrings!= null)&&this.mainTextMatchedSubstrings.equals(rhs.mainTextMatchedSubstrings))))&&((this.secondaryText == rhs.secondaryText)||((this.secondaryText!= null)&&this.secondaryText.equals(rhs.secondaryText))));
    }

}
