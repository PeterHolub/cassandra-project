package com.petroholub.cassandra;

import com.datastax.oss.driver.api.core.CqlSession;

import java.net.InetSocketAddress;

public class CassandraConnector {
    public CqlSession getSession() {
        return CqlSession.builder()
                .withLocalDatacenter("datacenter1")
                .addContactPoint(new InetSocketAddress("127.0.0.1", 9042))
                .build();
    }
}
