package com.project.cassandra;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.CqlSessionBuilder;


import java.net.InetSocketAddress;

public class CassandraConnection {

    private CqlSession session;
    private String hostname;
    private int port;
    private String region;

    public CassandraConnection(String hostname, int port, String region){
        this.hostname = hostname;
        this.port = port;
        this.region = region;
        createSession();
    }

    private void createSession(){
        CqlSessionBuilder builder = CqlSession.builder();
        builder.addContactPoint(new InetSocketAddress(hostname, port));
        builder.withLocalDatacenter(region);
        session = builder.build();
    }

    public CqlSession getSession() {
        return session;
    }

}