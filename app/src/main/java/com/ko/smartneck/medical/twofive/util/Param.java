package com.ko.smartneck.medical.twofive.util;

public class Param {

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

    public String getValue(){
        return param;
    }

}
