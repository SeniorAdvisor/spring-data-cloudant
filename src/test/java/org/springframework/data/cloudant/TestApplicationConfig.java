package org.springframework.data.cloudant;

import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.model.ConnectOptions;
import com.google.gson.GsonBuilder;
import junit.framework.Assert;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.cloudant.config.AbstractCloudantConfiguration;
import org.springframework.data.cloudant.config.CloudantConnector;
import org.springframework.data.cloudant.core.CloudantFactory;
import org.springframework.data.cloudant.core.CloudantTemplate;
import org.springframework.data.cloudant.core.UnmappedDataAdapter;
import org.springframework.data.cloudant.core.model.BaseDocument;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNotSame;
import static junit.framework.TestCase.assertEquals;

/**
 * Created by justinsaul on 6/10/15.
 */

@Configuration
@EnableAutoConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = TestSpringBootApplication.class)
public class TestApplicationConfig {
    @Autowired
    TestCloudantSettings testCloudantSettings;

    @Test
    public void loadCloudantSettings(){
        assertNotNull(testCloudantSettings.getUsername());
        assertNotSame(testCloudantSettings.getUsername(), "");
    }
}
