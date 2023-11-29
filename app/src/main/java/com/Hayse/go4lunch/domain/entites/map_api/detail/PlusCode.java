package com.Hayse.go4lunch.domain.entites.map_api.detail;

public class PlusCode {
    private String compoundCode;
    private String globalCode;

    public void setCompoundCode(String compoundCode){
        this.compoundCode = compoundCode;
    }

    public String getCompoundCode(){
        return compoundCode;
    }

    public void setGlobalCode(String globalCode){
        this.globalCode = globalCode;
    }

    public String getGlobalCode(){
        return globalCode;
    }

    @Override
    public String toString(){
        return
                "PlusCode{" +
                        "compound_code = '" + compoundCode + '\'' +
                        ",global_code = '" + globalCode + '\'' +
                        "}";
    }
}
