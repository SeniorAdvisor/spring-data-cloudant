package org.springframework.data.cloudant.config;

/**
 * Created by Paul on 6/23/15.
 */
public interface ICloudantSettings {
    int getConnectionTimeout();

    void setConnectionTimeout(int connectionTimeout);

    int getMaxConnections();

    void setMaxConnections(int maxConnections);

    String getUrl();

    void setUrl(String url);

    String getUsername();

    void setUsername(String username);

    String getPassword();

    void setPassword(String password);

    String getDb();

    void setDb(String db);

    @Override
    String toString();
}
