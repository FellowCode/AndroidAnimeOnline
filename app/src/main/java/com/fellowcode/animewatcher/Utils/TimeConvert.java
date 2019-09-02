package com.fellowcode.animewatcher.Utils;

public class TimeConvert {
    public static int toSeconds(int time){
        return time/1000;
    }
    public static int toMinutes(int time){
        return toSeconds(time)/60;
    }
    public static int toHours(int time){
        return toMinutes(time)/60;
    }
    public static int toDays(int time){
        return toHours(time)/24;
    }
}
