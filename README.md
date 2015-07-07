# spring-data-cloudant
Spring Data Cloudant Adapter

### Set up projects
- `gradle idea` to create idea module
- double click `spring-data-cloudant.ipr`

### Config on test
- Settings on NON-SRPING-BOOT project
 if you using `spring-data-cloudant` on NON-SPRING-BOOT project, please follow:
     To run the tests, you will need to have a Cloudant account and you will need to create a test-context.xml file in /src/test/java/resources/ with the following XML:
     
     ```xml
     <?xml version="1.0" encoding="UTF-8"?>
     <beans xmlns="http://www.springframework.org/schema/beans
      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance 
      xmlns:context="http://www.springframework.org/schema/context
      xmlns:repository="http://www.springframework.org/schema/data/repository
      xmlns:util="http://www.springframework.org/schema/util
      xsi:schemaLocation="http://www.springframework.org/schema/data/repository
      http://www.springframework.org/schema/data/repository/spring-repository
      http://www.springframework.org/schema/beans 
      http://www.springframework.org/schema/beans/spring-beans
      http://www.springframework.org/schema/context 
      http://www.springframework.org/schema/context/spring-context.xsd
      http://www.springframework.org/schema/util 
      http://www.springframework.org/schema/util/spring-util.xsd>
         <context:component-scan base-package="org.springframework.data.cloudant">
         </context:component-scan 
         <bean id="connectOptions" class="com.cloudant.client.api.model.ConnectOptions">
             <property name="connectionTimeout" value="6000"/>
             <property name="maxConnections" value="10"/>
         </bean>
         <bean id="cloudantClient" class="com.cloudant.client.api.CloudantClient">
             <constructor-arg index="0" value="YOUR_ACCOUNT_HERE"/>
             <constructor-arg index="1" value="YOUR_USERNAME_HERE"/>
             <constructor-arg index="2" value="YOUR_PASSWORD_HERE"/>
             <constructor-arg index="3" ref="connectOptions"/>
         </bean>
         <bean id="cloudantTemplate" class="org.springframework.data.cloudant.core.CloudantTemplate">
             <constructor-arg index="0" ref="cloudantClient"/>
             <constructor-arg index="1" value="YOUR_DATABASE_HERE"/>
         </bean>
     </beans>
     ```%