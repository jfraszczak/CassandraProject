package com.project.app;

import com.datastax.oss.driver.api.core.CqlSession;
import com.project.cassandra.CassandraConnection;
import com.project.cassandra.RaceSessionDriversTable;
import com.project.cassandra.RaceSessionTable;

import java.util.ArrayList;
import java.util.List;
import java.lang.Thread;
import java.lang.Runnable;


public class Simulation {

    public static boolean isAnyAlive(ArrayList<Thread> threads) {
        for(int i=0; i<threads.size(); i++) {
            if (threads.get(i).isAlive()) return true;
        }
        return false;
    }

    public static void main(String[] args) {

        String hostname = "34.147.13.201";
        int port = 9042;
        String region = "europe-west4";
        String keyspace = "mykeyspace";

        CassandraConnection connection = new CassandraConnection(hostname, port, region);
        CqlSession session = connection.getSession();

        int sessionId = 1;

        RaceSessionTable raceSessionTable = new RaceSessionTable(keyspace, session);
        raceSessionTable.insert(sessionId, "gostyn", "training", 10, false);

        List<String> drivers = new ArrayList<>();
        ArrayList<Thread> threads = new ArrayList<Thread>();
        for (int i=1; i<=20; i++) {
            String driverName = String.format("Kierowca-%d", i);
            drivers.add(driverName);

            Runnable driver = new Driver(sessionId, driverName, session, keyspace);
            Thread thread = new Thread(driver);
            thread.start();
            threads.add(thread);
        }
        System.out.println("ESSSA");
        while(isAnyAlive(threads)) {
            System.out.println("ALIVE");
        };

        System.out.println("KONIEC");
        raceSessionTable.insert(sessionId, "gostyn", "training", 10, true);

        RaceSessionDriversTable raceSessionDriversTable = new RaceSessionDriversTable(keyspace, session);
        raceSessionDriversTable.insert(sessionId, drivers);
    }
}