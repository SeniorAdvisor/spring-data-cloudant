# spring-data-cloudant
Spring Data Cloudant Adapter

### Set up projects
- Setup by `setup.sh`
    - `./setup.sh`
- Setup by manually
    - `gradle idea` to create idea module
    - double click `spring-data-cloudant.ipr`
   

### Config on test
- Settings on NON-SRPING-BOOT project
 if you using `spring-data-cloudant` on NON-SPRING-BOOT project, please follow:
     1. copy example `cp src/test/resources/test-context.xml.example src/test/resources/test-context.yml`
     2. fill correct account info in the file `application.yml` on test
     Make a Cloudant account and create a test-context.xml file(path /src/test/java/resources/) with the following content:
     ```xml
     <?xml version="1.0" encoding="UTF-8"?>
     <beans xmlns="http://www.springframework.org/schema/beans"
     xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
     xmlns:context="http://www.springframework.org/schema/context"
     xmlns:repository="http://www.springframework.org/schema/data/repository"
     xmlns:util="http://www.springframework.org/schema/util"
     xsi:schemaLocation="http://www.springframework.org/schema/data/repository
     http://www.springframework.org/schema/data/repository/spring-repository
     http://www.springframework.org/schema/beans
     http://www.springframework.org/schema/beans/spring-beans
     http://www.springframework.org/schema/context
     http://www.springframework.org/schema/context/spring-context.xsd
     http://www.springframework.org/schema/util
     http://www.springframework.org/schema/util/spring-util.xsd">
     <context:component-scan base-package="org.springframework.data.cloudant">
     </context:component-scan>
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
     ```
     
- Setting projects in Spring-boot
 1. copy example `cp src/test/resources/application.yml.example src/test/resources/application.yml`
 2. fill correct account info in the file `application.yml` on test
 Make a Cloudant account and create a test-context.xml file(path /src/test/java/resources/) with the following content:
    ```yml
    cloudant:
      connectionTimeout: 2000
      maxConnections: 100
      url: YOUR_ACCOUNT_URL
      accountName: ACCOUNT_NAME
      username: API_KEY
      password: API_password
      db: CLOUDANT_DB_NAME
    ```

