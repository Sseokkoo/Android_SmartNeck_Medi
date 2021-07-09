package com.smartneck.twofive.Fit.util;

public class Fit_Param {

    String param = "";
    public void add(String name , String param){
        if (this.param.length() == 0){
            this.param += name + "=" + param;
        }else{
            this.param += "&" + name + "=" + param;
        }
    }
    public void add(String name , int param){
        if (this.param.length() == 0){
            this.param += name + "=" + param;
        }else{
            this.param += "&" + name + "=" + param;
        }
    }
    public void add(String name , double param){
        if (this.param.length() == 0){
            this.param += name + "=" + param;
        }else{
            this.param += "&" + name + "=" + param;
        }
    }
    public void add(String name , boolean param){
        if (this.param.length() == 0){
            this.param += name + "=" + param;
        }else{
            this.param += "&" + name + "=" + param;
        }
    }

    public String getParam(){
        return param;
    }

}
