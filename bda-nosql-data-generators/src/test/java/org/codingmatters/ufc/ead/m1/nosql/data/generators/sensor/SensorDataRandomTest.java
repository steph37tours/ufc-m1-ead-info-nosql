package org.codingmatters.ufc.ead.m1.nosql.data.generators.sensor;

import org.junit.Test;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

import static java.time.LocalDateTime.now;
import static org.codingmatters.ufc.ead.m1.nosql.data.generators.sensor.matcher.BetweenMatchers.between;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isOneOf;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Created by vagrant on 2/15/16.
 */
public class SensorDataRandomTest {
    @Test
    public void testRandom() throws Exception {
        LocalDateTime before = now();
        SensorData actual = new SensorDataRandom.Builder()
                .withSensors("s1", "s2")
                .withTemperatureRange(10, 15)
                .withHygrometryRange(0.2, 0.5)
                .build().next();
        System.out.println(actual);

        assertThat(actual.getName(), is(isOneOf("s1", "s2")));
        assertThat(actual.getTemperature(), is(between(10, 15)));
        assertThat(actual.getHygrometry(), is(between(0.2, 0.5)));
        assertThat(actual.getAt(), is(notNullValue()));
    }

}