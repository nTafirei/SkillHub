package com.marotech.skillhub.components.config;


import com.hazelcast.config.Config;
import com.hazelcast.config.XmlConfigBuilder;
import com.hazelcast.core.Hazelcast;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;

@Configuration
public class HazelcastConfig {

    @Bean
    public com.hazelcast.core.HazelcastInstance hazelcastInstance(){
            InputStream stream = getClass().
                    getClassLoader().getResourceAsStream("hazelcast.xml");
            Config config = new XmlConfigBuilder(stream).build();

            return Hazelcast.newHazelcastInstance(config);
    }
}
