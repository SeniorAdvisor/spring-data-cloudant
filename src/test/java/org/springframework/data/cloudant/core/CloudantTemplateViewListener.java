package org.springframework.data.cloudant.core;

import com.cloudant.client.api.CloudantClient;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

/**
 * Created by justinsaul on 6/10/15.
 */
public class CloudantTemplateViewListener extends DependencyInjectionTestExecutionListener {
    @Override
    public void beforeTestClass(TestContext testContext) throws Exception {
//        CloudantClient client = (CloudantClient) testContext.getApplicationContext().getBean("cloudantClient");
        //populate data if needed
        //create design docs -- assume they exist for now

    }


}
