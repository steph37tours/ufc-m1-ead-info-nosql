package org.codingmatters.ufc.ead.m1.nosql.data.injectors.twitter.cassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.codingmatters.ufc.ead.m1.nosql.data.injectors.sensor.SensorSampleInjector;
import org.codingmatters.ufc.ead.m1.nosql.data.injectors.sensor.cassandra.CassandraInjector;
import org.codingmatters.ufc.ead.m1.nosql.data.injectors.twitter.TweetSampleInjector;
import org.codingmatters.ufc.ead.m1.nosql.data.injectors.twitter.elasticsearch.ESTweetInjector;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.codingmatters.ufc.ead.m1.nosql.data.utils.HostResolver.resolver;

public class CassandraTweetSampleInjector {
    public static void main(String[] args) {
        Cluster cluster = Cluster.builder().addContactPoint(resolver().resolve("cassandra")).build();
        Session client = cluster.connect();
        client.execute("CREATE KEYSPACE IF NOT EXISTS ufcead WITH REPLICATION = { 'class' : 'NetworkTopologyStrategy', 'datacenter1' : 1 }");
        client.execute("CREATE TABLE IF NOT EXISTS ufcead.twitter_data ( " +
                "user_name text, " +
                "user_followersCount double, " +
                "text text, " +
                "lang text, " +
                "createdAt timestamp, " +
                "mentions text, " +
                "htags text, " +
                "links text, " +
                "PRIMARY KEY ((user_name, user_followersCount), createdAt)" +
                ")");
//        try {
//            //LocalDateTime start = LocalDateTime.now().minusYears(2);
//            //LocalDateTime end = LocalDateTime.now();
//
//            //new SensorSampleInjector(new CassandraInjector(client), start, end).run();
//            ObjectMapper mapper = new ObjectMapper();
//            new TweetSampleInjector(args, new ESTweetInjector(client, mapper), 10).run();
//        } finally {
//            client.close();
//            cluster.close();
//        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            new TweetSampleInjector(args, new CassandraTweetInjector(client), 10).run();
        } catch (InterruptedException | IOException e) {
            throw  new RuntimeException("error running sample injector", e);
        } finally {
            client.close();
        }
    }
}
