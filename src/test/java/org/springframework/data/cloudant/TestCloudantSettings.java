package org.springframework.data.cloudant;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cloudant.config.ICloudantSettings;
import org.springframework.stereotype.Component;
import org.springframework.cloud.context.config.annotation.RefreshScope;

@Component
@Configuration
@EnableAutoConfiguration
@RefreshScope
@ConfigurationProperties("cloudant")
public class TestCloudantSettings implements ICloudantSettings {
    private int connectionTimeout;
    private int maxConnections;
    private String url;
    private String username;
    private String password;
    private String db;

    @Override
    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    @Override
    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    @Override
    public int getMaxConnections() {
        return maxConnections;
    }

    @Override
    public void setMaxConnections(int maxConnections) {
        this.maxConnections = maxConnections;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }
    @Override
    public String getDb() {
        return db;
    }

    @Override
    public void setDb(String db) {
        this.db = db;
    }

    @Override
    public String toString() {
        return "CloudantSettings{" +
                "connectionTimeout=" + connectionTimeout +
                ", maxConnections=" + maxConnections +
                ", url='" + url + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", db='" + db + '\'' +
                '}';
    }
}