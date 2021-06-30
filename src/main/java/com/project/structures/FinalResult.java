package com.project.structures;

public class FinalResult {

    private String driverName;
    private long time;

    public FinalResult(String driverName, long time) {
        this.driverName = driverName;
        this.time = time;
    }

    public void printData(){
        System.out.println("Driver: " + driverName + " Time: " + time);
    }

    public String getData(){
        return "Driver: " + driverName + " Time: " + time;
    }

    public long getTime() {
        return time;
    }

    public String getDriverName() {
        return driverName;
    }
}
