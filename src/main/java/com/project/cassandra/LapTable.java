package com.project.cassandra;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.*;
import com.datastax.oss.driver.api.core.type.DataTypes;

import com.datastax.oss.driver.api.querybuilder.insert.RegularInsert;
import com.datastax.oss.driver.api.querybuilder.schema.CreateTable;
import com.project.structures.Lap;
import com.project.structures.RaceSession;

import java.util.ArrayList;
import java.util.List;

import static com.datastax.oss.driver.api.querybuilder.SchemaBuilder.*;
import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.*;

public class LapTable {

    private String tableName = "lap";
    private String keyspace;
    private CqlSession session;

    public LapTable(String keyspace, CqlSession session){
        this.keyspace = keyspace;
        this.session = session;
    }

    public void create() {

        CreateTable create = createTable(keyspace, tableName)
                .ifNotExists()
                .withPartitionKey("session_id", DataTypes.INT)
                .withPartitionKey("driver_name", DataTypes.TEXT)
                .withPartitionKey("lap_number", DataTypes.INT)
                .withColumn("time_start", DataTypes.BIGINT)
                .withColumn("time_stop", DataTypes.BIGINT)
                .withColumn("time", DataTypes.BIGINT);

        SimpleStatement statement = create.build();
        session.execute(statement);

    }

    public void insert(int sessionId, String driverName, int lapNumber, long timeStart, long timeStop, long time){
        RegularInsert insert = insertInto(keyspace, tableName)
                .value("session_id", literal(sessionId))
                .value("driver_name", literal(driverName))
                .value("lap_number", literal(lapNumber))
                .value("time_start", literal(timeStart))
                .value("time_stop", literal(timeStop))
                .value("time", literal(time));

        SimpleStatement statement = insert.build();
        session.execute(statement);
    }

    public List<Lap> select(int sessionId){

        RaceSessionTable raceSessionTable = new RaceSessionTable(keyspace, session);
        RaceSessionDriversTable raceSessionDriversTable = new RaceSessionDriversTable(keyspace, session);

        RaceSession raceSession = raceSessionTable.select(sessionId);
        List<String> drivers = raceSessionDriversTable.getDrivers(sessionId);

        ResultSet rs;
        List<Lap> laps = new ArrayList<>();
        for(int lapNumber = 1; lapNumber <= raceSession.getLapsNumber(); lapNumber++){
            for(String driver: drivers){
                SimpleStatement statement = selectFrom(keyspace, tableName)
                        .all()
                        .whereColumn("session_id").isEqualTo(literal(sessionId))
                        .whereColumn("driver_name").isEqualTo(literal(driver))
                        .whereColumn("lap_number").isEqualTo(literal(lapNumber))
                        .build();

                rs = session.execute(statement);

                Row result = rs.one();

                long startTime = result.getLong("time_start");
                long stopTime = result.getLong("time_stop");
                long time = result.getLong("time");

                Lap lap = new Lap(sessionId, driver, lapNumber, startTime, stopTime, time);
                laps.add(lap);
            }
        }

        return laps;

    }


}