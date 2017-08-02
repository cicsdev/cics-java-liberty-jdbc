CICS Liberty DataSource JDBC sample

Sample JDBC Java EE web application demonstrating how to use a JDBC in a web servlet.


## Repository structure

* [`projects/`](projects) - Eclipse projects suitable for importing into a CICS Explorer environment.

## Samples overview

* `com.ibm.cicsdev.jdbc.web` - Dynamic web project containing the SimpleJDBCServlet servlet.  The servlet uses the static DoJDBC class which connects to DB2 by obtaining a DataSource via a JNDI lookup and that uses JMS and returns the current DB2 timestamp from DB2
* `com.ibm.cicsdev.jdbc.web.cicsbundle` - CICS bundle project that references the WAR (Dynamic web project) bundle part for deployment in a CICS bundle

## Pre-requisites
* CICS TS V5.3 with APAR PI67640 and APAR PI58375, or CICS TS V5.4
* Java SE 7 or later on the z/OS system
* Java SE 7 or later on the workstation
* CICS Explorer V5.4 with the IBM CICS SDK for Java EE and Liberty feature installed [available here](https://developer.ibm.com/mainframe/products/downloads)

## Configuration
The sample code can be deployed as an WAR file into a CICS Liberty JVM server. The SimpleJDBCServlet servlet can then be used to display the current timestam from DB2

### To import the samples into Eclipse
1. Import the projects into CICS Explorer using **File -> Import -> General -> Existing** projects into workspace
1. Resolve the build path errors on the Dynamic web project using the following menu from each project: **Build Path -> Configure Build Path -> Libraries -> Add Library -> CICS with Java EE and Liberty** and select the version of CICS TS for deployment (either CICS TS V5.3 or CICS TS V5.4)

### To configure CICS
1. Create a Liberty JVM server as described in [4 easy steps](https://developer.ibm.com/cics/2015/06/04/starting-a-cics-liberty-jvm-server-in-4-easy-steps/)
1. Add the following properties to the JVM profile:
 ```
-Dcom.ibm.cics.jvmserver.wlp.autoconfigure=true
-Dcom.ibm.cics.jvmserver.wlp.jdbc.driver.location=/usr/lpp/db2v12/jdbc
```
where  ```/usr/lpp/db2v12/jdbc``` is the location of your JDBC drivers

### To deploy the sample into a CICS region 
1. Change the name of the JVMSERVER in the .warbundle file from DFHWLP to the name of the JVMSERVER resource defined in CICS. 
1. Using the CICS Explorer export the ```com.ibm.cicsdev.jdbc.web.cicsbundle``` project to a zFS directory. 
1. Define and install a CICS BUNDLE resource definition referring to the deployed bundle directory on zFS in step 2, and ensure all resources are enabled. 

## Running the sample
* The servlet is accessed with the following URL:
[http://host:port/com.ibm.cicsdev.jdbc.web/](http://host:port/com.ibm.cicsdev.jdbc.web/)  

If the test is successful, you will see the a response similar to the following written to the browser:  
`SimpleJDBCServlet: DB2 CurrentTimeStamp = 2017-08-02 11:28:46.18055`

## Reference
*  CICS Knowledge Center [Configuring a Liberty JVM server](https://www.ibm.com/support/knowledgecenter/SSGMCP_5.4.0/configuring/java/config_jvmserver_liberty.html)

## License
This project is licensed under [Apache License Version 2.0](LICENSE).
