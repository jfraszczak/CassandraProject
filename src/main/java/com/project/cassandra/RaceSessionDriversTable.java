package com.project.cassandra;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.*;
import com.datastax.oss.driver.api.core.type.DataTypes;

import com.datastax.oss.driver.api.querybuilder.insert.RegularInsert;
import com.datastax.oss.driver.api.querybuilder.schema.CreateTable;

import java.util.ArrayList;
import java.util.List;

import static com.datastax.oss.driver.api.querybuilder.SchemaBuilder.*;
import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.*;

public class RaceSessionDriversTable {

    private int maxDriversNumber = 20;
    private String tableName = "race_session_drivers";
    private String keyspace;
    private CqlSession session;

    public RaceSessionDriversTable(String keyspace, CqlSession session){
        this.keyspace = keyspace;
        this.session = session;
    }

    public void create() {

        CreateTable create = createTable(keyspace, tableName)
                .ifNotExists()
                .withPartitionKey("session_id", DataTypes.INT);


        for(int i = 0; i < maxDriversNumber; i++){
            String columnName = "driver" + i;
            create = create.withColumn(columnName, DataTypes.TEXT);
        }


        SimpleStatement statement = create.build();
        session.execute(statement);

    }

    public void insert(int sessionId, List<String> driversNames){
        RegularInsert insert = insertInto(keyspace, tableName)
                .value("session_id", literal(sessionId));

        for(int i = 0; i < maxDriversNumber; i++) {
            String columnName = "driver" + i;
            insert = insert.value(columnName, literal(driversNames.get(i)));
        }

        SimpleStatement statement = insert.build();
        session.execute(statement);
    }

    public List<String> getDrivers(int sessdionId){

        SimpleStatement statement = selectFrom(keyspace, tableName)
                .all()
                .whereColumn("session_id").isEqualTo(literal(sessdionId))
                .build();

        ResultSet rs = session.execute(statement);

        List<String> drivers = new ArrayList<>();
        List<Row> result = rs.all();

        for(Row row: result){
            String driverName;

            for(int i = 0; i < maxDriversNumber; i++) {
                String columnName = "driver" + i;
                driverName = row.getString(columnName);
                drivers.add(driverName);
            }

        }

        return drivers;

    }


}