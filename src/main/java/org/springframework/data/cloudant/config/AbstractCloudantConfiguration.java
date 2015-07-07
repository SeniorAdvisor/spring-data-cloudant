package org.springframework.data.cloudant.config;

import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.data.annotation.Persistent;
import org.springframework.data.cloudant.core.CloudantFactoryBean;
import org.springframework.data.cloudant.core.mapping.Document;
//import org.springframework.test.context.ContextConfiguration;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.springframework.data.mapping.model.CamelCaseAbbreviatingFieldNamingStrategy;
import org.springframework.data.mapping.model.FieldNamingStrategy;
import org.springframework.data.mapping.model.PropertyNameFieldNamingStrategy;

import java.util.Set;
import java.util.HashSet;
import java.util.Properties;

/**
 * Created by justinsaul on 6/9/15.
 */
//@Configuration
public abstract class AbstractCloudantConfiguration {

//
//    protected abstract String getUsername();
//
//    protected abstract String getPassword();
//    protected abstract String getDatabase();
//    protected abstract String getAccount();
//    protected abstract int getMaxConnections();
//    protected abstract int getConnectionTimeout();

//    @Bean
//    public CloudantClient cloudantClient() throws Exception {
//        // setLoggerProperty(cloudantLogger());
//        ConnectOptions connectOptions = new ConnectOptions().setConnectionTimeout(getConnectionTimeout()).setMaxConnections(getMaxConnections());
//        return new CloudantClient(getAccount(),getUsername(),getPassword(),connectOptions);
//    }
//
//    protected String cloudantLogger() {
//        return CloudantFactoryBean.DEFAULT_LOGGER_PROPERTY;
//    }
//
//    protected static void setLoggerProperty(final String logger) {
//        Properties systemProperties = System.getProperties();
//        systemProperties.setProperty("net.spy.log.LoggerImp", logger);
//        System.setProperties(systemProperties);
//    }
//
//    @Bean
//    public CloudantTemplate cloudantTemplate() throws Exception {
//        return new CloudantTemplate(cloudantClient(), getDatabase());
//    }
//
//    private void mappingCloudantConverter() {
//    }
//
//    protected Set<Class<?>> getInitialEntitySet() throws ClassNotFoundException {
//        String basePackage = getMappingBasePackage();
//        Set<Class<?>> initialEntitySet = new HashSet<Class<?>>();
//
//        if (StringUtils.hasText(basePackage)) {
//            ClassPathScanningCandidateComponentProvider componentProvider = new ClassPathScanningCandidateComponentProvider(false);
//            componentProvider.addIncludeFilter(new AnnotationTypeFilter(Document.class)); //not needed?
//            componentProvider.addExcludeFilter(new AnnotationTypeFilter(Persistent.class));
//            for (BeanDefinition candidate : componentProvider.findCandidateComponents(basePackage)) {
//                initialEntitySet.add(ClassUtils.forName(candidate.getBeanClassName(),AbstractCloudantConfiguration.class.getClassLoader()));
//            }
//        }
//        return initialEntitySet;
//    }
//
//    protected String getMappingBasePackage() {
//        return getClass().getPackage().getName();
//    }
//
//    protected boolean abbreviateFieldNames() {
//        return false;
//    }
//
//    protected FieldNamingStrategy fieldNamingStrategy() {
//        return abbreviateFieldNames() ? new CamelCaseAbbreviatingFieldNamingStrategy() : PropertyNameFieldNamingStrategy.INSTANCE;
//    }
}
