package org.springframework.data.cloudant.core.repository;

import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.data.cloudant.config.BeanNames;
import org.springframework.data.cloudant.core.mapping.Document;
import org.springframework.data.config.ParsingUtils;
import org.springframework.data.repository.config.AnnotationRepositoryConfigurationSource;
import org.springframework.data.repository.config.RepositoryConfigurationExtensionSupport;
import org.springframework.data.repository.config.RepositoryConfigurationSource;
import org.springframework.data.repository.config.XmlRepositoryConfigurationSource;
import org.w3c.dom.Element;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by kevin on 6/18/15.
 */
public class CloudantRepositoryConfigurationExtension extends RepositoryConfigurationExtensionSupport {

    private static final String CLOUDANT_TEMPLATE_REF = "cloud-template-ref";

    private boolean fallbackMappingContextCreated = false;

    /*
     * (non-Javadoc)
     * @see org.springframework.data.repository.config.RepositoryConfigurationExtensionSupport#getModuleName()
     */
    @Override
    public String getModuleName() {
        return "Cloudant";
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.repository.config.RepositoryConfigurationExtensionSupport#getModulePrefix()
     */
    @Override
    protected String getModulePrefix() {
        return "cloudant";
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.repository.config.RepositoryConfigurationExtension#getRepositoryFactoryClassName()
     */
    public String getRepositoryFactoryClassName() {
        return CloudantRepositoryFactoryBean.class.getName();
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.repository.config.RepositoryConfigurationExtensionSupport#getIdentifyingAnnotations()
     */
    @Override
    protected Collection<Class<? extends Annotation>> getIdentifyingAnnotations() {
        return Collections.<Class<? extends Annotation>> singleton(Document.class);
    }

    /*
     * (non-Javadoc)
     * @see org.springframework.data.repository.config.RepositoryConfigurationExtensionSupport#getIdentifyingTypes()
     */
    @Override
    protected Collection<Class<?>> getIdentifyingTypes() {
        return Collections.<Class<?>> singleton(CloudantCrudRepository.class);
    }

//    /*
//     * (non-Javadoc)
//     * @see org.springframework.data.repository.config.RepositoryConfigurationExtensionSupport#postProcess(org.springframework.beans.factory.support.BeanDefinitionBuilder, org.springframework.data.repository.config.RepositoryConfigurationSource)
//     */
//    @Override
//    public void postProcess(BeanDefinitionBuilder builder, RepositoryConfigurationSource source) {
//
//        if (fallbackMappingContextCreated) {
////            builder.addPropertyReference("mappingContext", BeanNames.MAPPING_CONTEXT_BEAN_NAME);
//        }
//    }
//
//    /*
//     * (non-Javadoc)
//     * @see org.springframework.data.repository.config.RepositoryConfigurationExtensionSupport#postProcess(org.springframework.beans.factory.support.BeanDefinitionBuilder, org.springframework.data.repository.config.XmlRepositoryConfigurationSource)
//     */
//    @Override
//    public void postProcess(BeanDefinitionBuilder builder, XmlRepositoryConfigurationSource config) {
//
////        Element element = config.getElement();
////
////        ParsingUtils.setPropertyReference(builder, element, MONGO_TEMPLATE_REF, "mongoOperations");
////        ParsingUtils.setPropertyValue(builder, element, CREATE_QUERY_INDEXES, "createIndexesForQueryMethods");
//    }
//
//    /*
//     * (non-Javadoc)
//     * @see org.springframework.data.repository.config.RepositoryConfigurationExtensionSupport#postProcess(org.springframework.beans.factory.support.BeanDefinitionBuilder, org.springframework.data.repository.config.AnnotationRepositoryConfigurationSource)
//     */
//    @Override
//    public void postProcess(BeanDefinitionBuilder builder, AnnotationRepositoryConfigurationSource config) {
//
//        AnnotationAttributes attributes = config.getAttributes();
//
////        builder.addPropertyReference("mongoOperations", attributes.getString("mongoTemplateRef"));
////        builder.addPropertyValue("createIndexesForQueryMethods", attributes.getBoolean("createIndexesForQueryMethods"));
//    }
//
//    /*
//     * (non-Javadoc)
//     * @see org.springframework.data.repository.config.RepositoryConfigurationExtensionSupport#registerBeansForRoot(org.springframework.beans.factory.support.BeanDefinitionRegistry, org.springframework.data.repository.config.RepositoryConfigurationSource)
//     */
//    @Override
//    public void registerBeansForRoot(BeanDefinitionRegistry registry, RepositoryConfigurationSource configurationSource) {
//
//        super.registerBeansForRoot(registry, configurationSource);
//
//        if (!registry.containsBeanDefinition(BeanNames.MAPPING_CONTEXT_BEAN_NAME)) {
//
////            RootBeanDefinition definition = new RootBeanDefinition(MongoMappingContext.class);
////            definition.setRole(AbstractBeanDefinition.ROLE_INFRASTRUCTURE);
////            definition.setSource(configurationSource.getSource());
////
////            registry.registerBeanDefinition(BeanNames.MAPPING_CONTEXT_BEAN_NAME, definition);
//        }
//    }
}
