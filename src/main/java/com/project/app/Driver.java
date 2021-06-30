package com.project.app;

import com.datastax.oss.driver.api.core.CqlSession;
import com.project.cassandra.LapTable;

import java.util.concurrent.ThreadLocalRandom;
import java.util.Date;
import java.lang.Thread;
import java.lang.InterruptedException;
import java.lang.Runnable;


public class Driver implements Runnable {

    private int sessionId;
    private String driverName;
    CqlSession session;
    String keyspace;

    public Driver(int sessionId, String driverName, CqlSession session, String keyspace) {
        this.sessionId = sessionId;
        this.driverName = driverName;
        this.session = session;
        this.keyspace = keyspace;
    }

    public void run() {
        int laps = ThreadLocalRandom.current().nextInt(10, 20 + 1);

        LapTable lapTable = new LapTable(keyspace, session);

        for (int i=1; i<=laps; i++) {
            long start_time = new Date().getTime();
            try {
                Thread.sleep(ThreadLocalRandom.current().nextInt(20000, 25000 + 1));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            long stop_time = new Date().getTime();

            lapTable.insert(sessionId, driverName, i, start_time, stop_time, stop_time - start_time);
        }
    }
}