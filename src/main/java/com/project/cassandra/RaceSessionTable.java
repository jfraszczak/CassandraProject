package com.project.cassandra;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.*;
import com.datastax.oss.driver.api.core.type.DataTypes;

import com.datastax.oss.driver.api.querybuilder.insert.RegularInsert;
import com.datastax.oss.driver.api.querybuilder.schema.CreateTable;
import com.project.structures.RaceSession;

import java.io.IOException;

import static com.datastax.oss.driver.api.querybuilder.SchemaBuilder.*;
import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.*;

public class RaceSessionTable {

    private String tableName = "race_session";
    private String keyspace;
    private CqlSession session;

    public RaceSessionTable(String keyspace, CqlSession session){
        this.keyspace = keyspace;
        this.session = session;
    }

    public void create() {
        CreateTable create = createTable(keyspace, tableName)
                .ifNotExists()
                .withPartitionKey("id", DataTypes.INT)
                .withColumn("circuit_name", DataTypes.TEXT)
                .withColumn("type", DataTypes.TEXT)
                .withColumn("laps_number", DataTypes.INT)
                .withColumn("finished", DataTypes.BOOLEAN);

        SimpleStatement statement = create.build();
        session.execute(statement);

    }

    public void insert(int id, String circuitName, String type, int lapsNumber, Boolean finished){

        RegularInsert insert = insertInto(keyspace, tableName)
                .value("id", literal(id))
                .value("circuit_name", literal(circuitName))
                .value("type", literal(type))
                .value("laps_number", literal(lapsNumber))
                .value("finished", literal(finished));

        SimpleStatement statement = insert.build();
        session.execute(statement);
    }

    public RaceSession select(int sessionId) {

        SimpleStatement statement = selectFrom(keyspace, tableName)
                .all()
                .whereColumn("id").isEqualTo(literal(sessionId))
                .build();

        ResultSet rs = session.execute(statement);

        Row result = rs.one();

        int id = result.getInt("id");
        String circuitName = result.getString("circuit_name");
        String type = result.getString("type");
        int lapsNumber = result.getInt("laps_number");
        Boolean finished = result.getBoolean("finished");

        RaceSession raceSession = new RaceSession(id, circuitName, type, lapsNumber, finished);

        return raceSession;

    }

}