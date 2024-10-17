cics-java-liberty-jdbc
=====================

Sample JDBC Java EE web application demonstrating how to access a DB2 database from a web servlet in CICS Liberty. 


## Repository structure

* [`projects/`](projects) - Eclipse projects suitable for importing into a CICS Explorer environment. 
* [`etc/`](etc) - Liberty server configuration files

## Samples overview

* `com.ibm.cicsdev.jdbc.web` - Dynamic web project containing the SimpleJDBCServlet servlet.  The servlet uses the DoJDBC class which connects 
to DB2 by obtaining a DataSource via a JNDI lookup and returns the current timestamp from DB2
* `com.ibm.cicsdev.jdbc.web.cicsbundle` - CICS bundle project that references the WAR (Dynamic web project) bundle part for deployment in a CICS bundle

## Pre-requisites
* CICS TS V5.4 
* Java SE 8 or later on the z/OS system
* CICS Explorer V5.4 with the IBM CICS SDK for Java EE and Liberty feature installed [available here](https://developer.ibm.com/mainframe/products/downloads)
* IBM Db2 for z/OS 
* Maven or Gradle build systems (optional)

## Configuration
The sample code can be deployed as a WAR file into a CICS Liberty JVM server. CICS Liberty can be configured to use either a local DB2 database with 
JDBC type 2 connectivity,  or a remote database with a JDBC type 4 connectivity. The SimpleJDBCServlet servlet can then be used to display the current 
timestamp from DB2

### To import the samples into Eclipse
1. Import the projects into CICS Explorer using **File -> Import -> General -> Existing projects into workspace**
1. Resolve the build path errors on the Dynamic web project using the following menu from the web project: **Build Path -> Configure Build Path -> Libraries -> Add Library -> CICS with Java EE and Liberty** and select the version of CICS TS for deployment (either CICS TS V5.3 or CICS TS V5.4)


### Building the Example

The sample can be built using the supplied Gradle or Maven build files to produce a WAR file and optionally a CICS Bundle archive.

#### Gradle (command line)

Run the following in a local command prompt:

`gradle clean build`

This creates a WAR file inside the `build/libs` directory and a CICS bundle ZIP file inside the `build/distributions` directory.

If using the CICS bundle ZIP, the CICS JVM server name should be modified in the  `jvmserver` property in the gradle build properties [file](gradle.properties) to match the required CICS JVMSERVER resource name, or alternatively can be set on the command line as follows.

`gradle clean build -Pjvmserver=MYJVM`

#### Maven (command line)

Run the following in a local command prompt which will create a WAR file for deployment.

`mvn clean verify`

This creates a WAR file in the `target` directory. 

If building a CICS bundle ZIP the CICS bundle plugin bundle-war goal is driven using the maven verify phase. The CICS JVM server name should be modified in the <jvmserver> property in the [`pom.xml`](pom.xml) to match the required CICS JVMSERVER resource name, or alternatively can be set on the command line as follows. 

`mvn clean verify -Djvmserver=MYJVM`

### To configure CICS for JDBC type 2 connectivity to DB2
1. Create a Liberty JVM server as described in [4 easy steps](https://developer.ibm.com/cics/2015/06/04/starting-a-cics-liberty-jvm-server-in-4-easy-steps/)
1. Update the CICS STEPLIB with the DB2 SDSNLOAD and SDSNLOD2 libraries
1. Configure CICS DB2CONN, DB2TRAN and DB2ENTRY resource definitions as described in [How you can define the CICS DB2 connection](https://www.ibm.com/support/knowledgecenter/en/SSGMCP_5.4.0/configuring/databases/dfhtk2c.html)
1. Bind the DB2 plan that is specified in the CICS DB2CONN or DB2ENTRY definition with a PKLIST of NULLID.* 
1. Add the following properties in the JVM profile to set the location of the DB2 drivers to allow CICS to automatically configure the default DataSource 

```
-Dcom.ibm.cics.jvmserver.wlp.autoconfigure=true
-Dcom.ibm.cics.jvmserver.wlp.jdbc.driver.location=/usr/lpp/db2v12/jdbc
```
Where  ```/usr/lpp/db2v12/jdbc``` is the location of the DB2 JDBC driver

An example Liberty server configuration of a DataSource with a type 2 connection is supplied in [etc/config/type-2-server.xml](etc/config/type-2-server.xml). Configuration with DataSource and a type 4 connection is in [etc/config/type-4-server.xml](etc/config/type-4-server.xml)

### To deploy the sample into a CICS region 
1. Change the name of the JVMSERVER in the .warbundle file from DFHWLP to the name of the JVMSERVER resource defined in CICS. 
1. Using the CICS Explorer export the ```com.ibm.cicsdev.jdbc.web.cicsbundle``` project to a zFS directory. 
1. Define and install a CICS BUNDLE resource definition referring to the deployed bundle directory on zFS in step 2, and ensure all resources are enabled. 

## Running the sample
* The servlet is accessed with the following URL:
[http://host:port/com.ibm.cicsdev.jdbc.web/](http://host:port/com.ibm.cicsdev.jdbc.web/)  

If the test is successful, you will see a response similar to the following written to the browser:  

`SimpleJDBCServlet: DB2 CurrentTimeStamp = 2017-08-02 11:28:46.18055`

## Reference
*  Sample SQLJ Git repository  [cics-java-liberty-sqlj](https://github.com/cicsdev/cics-java-liberty-sqlj)
*  CICS Knowledge Center [Configuring a Liberty JVM server](https://www.ibm.com/support/knowledgecenter/SSGMCP_5.4.0/configuring/java/config_jvmserver_liberty.html)
*  CICS Knowledge Center [Configuring a JVM server to support DB2](https://www.ibm.com/support/knowledgecenter/en/SSGMCP_5.4.0/applications/developing/database/dfhtk4b.html)

## License
This project is licensed under [Apache License Version 2.0](LICENSE).
