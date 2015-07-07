package org.springframework.data.cloudant.config;

import com.cloudant.client.api.CloudantClient;

/**
 * Created by Paul on 6/25/15.
 */
public interface ICloudantConnector {
    CloudantClient getClient();

    String getDbName();
}
