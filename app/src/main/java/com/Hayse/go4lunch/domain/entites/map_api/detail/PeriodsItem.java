package com.Hayse.go4lunch.domain.entites.map_api.detail;

public class PeriodsItem {
    private Close close;
    private Open open;

    public void setClose(Close close){
        this.close = close;
    }

    public Close getClose(){
        return close;
    }

    public void setOpen(Open open){
        this.open = open;
    }

    public Open getOpen(){
        return open;
    }

    @Override
    public String toString(){
        return
                "PeriodsItem{" +
                        "close = '" + close + '\'' +
                        ",open = '" + open + '\'' +
                        "}";
    }
}
