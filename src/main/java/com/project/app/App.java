package com.project.app;

import com.datastax.oss.driver.api.core.CqlSession;
import com.project.cassandra.CassandraConnection;
import com.project.cassandra.LapTable;
import com.project.cassandra.RaceSessionDriversTable;
import com.project.cassandra.RaceSessionTable;
import com.project.structures.FinalResult;
import com.project.structures.Lap;
import com.project.structures.RaceSession;
import com.project.structures.TimeSorter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.lang.InterruptedException;

public class App {

    private LapTable lapTable;
    private RaceSessionTable raceSessionTable;
    private RaceSessionDriversTable raceSessionDriversTable;

    public App(String hostname, int port, String region, String keyspace) {
        CassandraConnection connection = new CassandraConnection(hostname, port, region);
        CqlSession session = connection.getSession();

        this.lapTable = new LapTable(keyspace, session);
        this.raceSessionTable = new RaceSessionTable(keyspace, session);
        this.raceSessionDriversTable = new RaceSessionDriversTable(keyspace, session);

        lapTable.create();
        raceSessionTable.create();
        raceSessionDriversTable.create();

    }

    public RaceSessionTable getRaceSessionTable() {
        return raceSessionTable;
    }

    public void raceStandings(int sessionId, RaceSession raceSession){
        List<Lap> laps = lapTable.select(sessionId);
        List<String> drivers = raceSessionDriversTable.getDrivers(sessionId);
        List<FinalResult> finalResults = new ArrayList<>();
        int finalTime;
        for(String driver: drivers){
            finalTime = 0;

            for(int lapNumber = 1; lapNumber <= raceSession.getLapsNumber(); lapNumber++){
                for(Lap lap: laps){
                    if(lap.getDriverName().equals(driver)){
                        finalTime += lap.getTime();
                    }
                }
            }

            finalResults.add(new FinalResult(driver, finalTime));
        }

        finalResults.sort(new TimeSorter());

        System.out.println("STANDINGS");
        int place = 1;
        for(FinalResult result: finalResults){
            System.out.println(place + ". " + result.getData());
            place++;
        }
    }

    public void bestLap(int sessionId){
        List<Lap> laps = lapTable.select(sessionId);

        Lap bestLap = laps.get(0);

        for(Lap lap: laps){
            if(bestLap.getTime() > lap.getTime()){
                bestLap = lap;
            }
        }

        System.out.println("BEST LAP");
        bestLap.printData();
    }

    public void printRaceResults(int sessionId) {
        RaceSession raceSession = getRaceSessionTable().select(sessionId);

        raceSession.printData();
        if(raceSession.getFinished()){
            raceStandings(sessionId, raceSession);
            bestLap(sessionId);
        }
        else{
            System.out.println("This session has not finished yet");
        }

        System.out.println("\n\n");
    }

    public void appInterface() throws IOException {
        while (true){
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String line = reader.readLine();
            int sessionId = Integer.parseInt(line);

            printRaceResults(sessionId);

        }
    }

    public static void main(String[] args) throws InterruptedException, IOException {

        String hostname = "34.147.13.201";
        int port = 9042;
        String region = "europe-west4";
        String keyspace = "mykeyspace";

        App app = new App(hostname, port, region, keyspace);

        app.appInterface();

    }
}