package com.project.structures;

public class Lap {

    int sessionId;
    String driverName;
    int lapNumber;
    long timeStart;
    long timeStop;
    long time;

    public Lap(int sessionId, String driverName, int lapNumber, long timeStart, long timeStop, long time) {
        this.sessionId = sessionId;
        this.driverName = driverName;
        this.lapNumber = lapNumber;
        this.timeStart = timeStart;
        this.timeStop = timeStop;
        this.time = time;
    }

    public void printData(){
        String data = "Lap Number: " + lapNumber + " Driver name: " + driverName + " Time: " + time;
        System.out.println(data);
    }

    public int getSessionId() {
        return sessionId;
    }

    public String getDriverName() {
        return driverName;
    }

    public int getLapNumber() {
        return lapNumber;
    }

    public long getTimeStart() {
        return timeStart;
    }

    public long getTimeStop() {
        return timeStop;
    }

    public long getTime() {
        return time;
    }
}
