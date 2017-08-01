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
* The Web application is configured with a context root of *jmsweb* so to invoke the servlet to write records to the simple JMS queue specify the test=putQ parameter after the context root for example:
[http://host:port/jmsweb?test=putQ](http://host:port/jmsweb?test=putq)  

If the test is successful, you will see the following response written to the browser:  
`22/06/2017 16:11:20 Message has been written to queue:///DEMO.SIMPLEQ`

* To read the records back specify the readQ parameter:
[http://host:port/jmsweb?test=readQ](http://host:port/jmsweb?test=readq)

* To write records to the MDB queue specify the putmdbQ parameter:
[http://host:port/jmsweb?test=putmdbQ](http://host:port/jmsweb?test=putmdbq)  

* To verify that the MDB has been triggered, you can read the contents of the CICS TSQ using the readTSQ test parameter:
[http://host:port/jmsweb?test=readtsq](http://host:port/jmsweb?test=readtsq)


## Reference
*  MQ Knowledge Center [Liberty and the IBM MQ resource adapter](https://www.ibm.com/support/knowledgecenter/en/SSFKSJ_9.0.0/com.ibm.mq.dev.doc/q120040_.htm)
*  Liberty Knowledge Center [Deploying message-driven beans to connect to IBM MQ](https://www.ibm.com/support/knowledgecenter/en/SS7K4U_liberty/com.ibm.websphere.wlp.zseries.doc/ae/twlp_dep_msg_mdbwmq.html)
*  MQRC_OBJECT_IN_USE if the MDB tries to get a message from a queue that is not defined as shareable [Defining MDB queues as shareable](http://www-01.ibm.com/support/docview.wss?uid=swg21232930)
*  For further details on the JCICS APIs used in this sample refer to this [CICS developer center article](https://developer.ibm.com/cics/2017/02/27/jcics-the-java-api-for-cics/)



## License
This project is licensed under [Apache License Version 2.0](LICENSE).  



