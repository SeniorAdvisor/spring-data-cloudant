package org.springframework.data.cloudant;

import com.cloudant.client.api.CloudantClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cloudant.config.CloudantConnector;
import org.springframework.data.cloudant.core.CloudantTemplate;

/**
 * Created by Paul on 6/26/15.
 */
@EnableAutoConfiguration
@SpringBootApplication
public class TestSpringBootApplication {

    @Autowired
    TestCloudantSettings testCloudantSettings;

//    @Bean
//    public CloudantClient getCloudantClient(){
//        return new CloudantConnector(testCloudantSettings).getClient();
//    };

    @Bean
    public CloudantTemplate getCloudantTemplate(){
        return new CloudantTemplate(new CloudantConnector(testCloudantSettings));
    }
}
