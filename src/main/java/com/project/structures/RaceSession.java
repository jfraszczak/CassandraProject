package com.project.structures;

public class RaceSession {

    int id;
    String circuitName;
    String type;
    int lapsNumber;
    Boolean finished;

    public RaceSession(int id, String circuitName, String type, int lapsNumber, Boolean finished){
        this.id = id;
        this.circuitName = circuitName;
        this.type = type;
        this.lapsNumber = lapsNumber;
        this.finished = finished;
    }

    public void printData(){
        String data = "Session id: " + id + " Circuit Name: " + circuitName + " Type: " + type;
        System.out.println(data);
    }

    public int getId() {
        return id;
    }

    public String getCircuitName() {
        return circuitName;
    }

    public String getType() {
        return type;
    }

    public int getLapsNumber() {
        return lapsNumber;
    }

    public Boolean getFinished() { return finished; }

}
