package org.springframework.data.cloudant.config;

import com.cloudant.client.api.CloudantClient;
import com.cloudant.client.api.Database;
import com.cloudant.client.api.model.ConnectOptions;

/**
 * Created by Paul Yang on 6/23/15.
 */
public class CloudantConnector implements ICloudantConnector {
    private CloudantClient cloudantClient;
    private ICloudantSettings cloudantSettings;
    public CloudantConnector(ICloudantSettings cs){
        cloudantSettings = cs;
        ConnectOptions connectOptions = new ConnectOptions()
                .setConnectionTimeout(cloudantSettings.getConnectionTimeout())
                .setMaxConnections(cloudantSettings.getMaxConnections());
        cloudantClient = new CloudantClient(cloudantSettings.getUrl(),cloudantSettings.getUsername(),cloudantSettings.getPassword(), connectOptions);
    }

    @Override
    public CloudantClient getClient(){
        return cloudantClient;
    }

    @Override
    public String getDbName(){
        return cloudantSettings.getDb();
    }
}
