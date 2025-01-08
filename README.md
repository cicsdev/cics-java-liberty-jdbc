# cics-java-liberty-jdbc
[![Build](https://github.com/SoftlySplinter/cics-java-liberty-jdbc/actions/workflows/java.yaml/badge.svg?branch=cicsts%2Fv5.5)](https://github.com/SoftlySplinter/cics-java-liberty-jdbc/actions/workflows/java.yaml)

Sample JDBC Java EE web application demonstrating how to access a Db2 database from a web servlet in CICS Liberty. 

## Contents
This is a set of sample Java projects for database interaction in CICS Java, demonstrating how you can use JDBC in a WAR in a Liberty JVM server, to allow it query items in Db2.

This sample can use either Db2 type 2 or type 4 connectivity.

* [`cics-java-liberty-jdbc-web`](cics-java-liberty-jdbc-web) - Dynamic web project containing the Java source.
* [`java-java-liberty-jdbc-bundle`](cics-java-liberty-jdbc-bundle) - CICS bundle project (Gradle/Maven)
* [`etc/config/liberty`](etc/config/liberty) - Liberty server configuration files
* [`etc/eclipse_projects/com.ibm.cicsdev.jdbc.web.cicsbundle`](etc/eclipse_projects/com.ibm.cicsdev.jdbc.web.cicsbundle) - CICS bundle project (CICS Exp

## Prerequisites
* CICS TSf for z/OS V5.5
* Java SE 8 or later on the z/OS system
* CICS Explorer with the IBM CICS SDK for Java EE and Liberty feature installed [available here](https://developer.ibm.com/mainframe/products/downloads), Gradle, or Apache Maven.
* IBM Db2 for z/OS

## Downloading
* Clone the repository using your IDEs support, such as the Eclipse Git plugin
* or, download the sample as a ZIP and unzip onto the workstation

> [!Tip]
> Eclipse Git provides an 'Import existing Projects' check-box when cloning a repository.

### Check dependencies
Before building this sample, you should verify that the correct CICS TS bill of materials (BOM) is specified for your target release of CICS. The BOM specifies a consistent set of artifacts, and adds information about their scope. In the example below the version specified is compatible with CICS TS V5.5 with JCICS APAR PH25409, or newer. That is, the Java byte codes built by compiling against this version of JCICS will be compatible with later CICS TS versions and subsequent JCICS APARs. 
You can browse the published versions of the CICS BOM at [Maven Central.](https://mvnrepository.com/artifact/com.ibm.cics/com.ibm.cics.ts.bom)
 
Gradle (build.gradle): 

`compileOnly enforcedPlatform("com.ibm.cics:com.ibm.cics.ts.bom:5.5-20200519131930-PH25409")`

Maven (POM.xml):

``` xml	
<dependencyManagement>
    <dependencies>
      <dependency>
        <groupId>com.ibm.cics</groupId>
        <artifactId>com.ibm.cics.ts.bom</artifactId>
        <version>5.5-20200519131930-PH25409</version>
        <type>pom</type>
        <scope>import</scope>
      </dependency>
    </dependencies>
  </dependencyManagement>
  ```

## Building 

You can build the sample using an IDE of your choice, or you can build it from the command line. For both approaches, using the Gradle or Maven wrappers will give a consistent version of build tooling. 
  
For an IDE, taking Eclipse as an example, the plug-ins for Gradle *buildship* and Maven *m2e* will integrate with the "Run As..." capability, allowing you to specify a specific version of your chosen build tool.

> [!NOTE]
> If you import the project to your IDE, you might experience local project compile errors. To resolve these errors you should run a tooling refresh on that project. For example, in Eclipse: right-click on "Project", select "Gradle -> Refresh Gradle Project", **or** right-click on "Project", select "Maven -> Update Project...".

> [!TIP]
> In Eclipse, Gradle (buildship) is able to fully refresh and resolve the local classpath even if the project was previously updated by Maven. However, Maven (m2e) does not currently reciprocate that capability. If you previously refreshed the project with Gradle, you'll need to manually remove the 'Project Dependencies' entry on the Java build-path of your Project Properties to avoid duplication errors when performing a Maven Project Update.


### Eclipse

Import the projects into CICS Explorer using File &rarr; Import &rarr; General &rarr; Existing projects into workspace.
> [!NOTE]
> If using the egit client, you can just clone the repo and tick the button to import all projects.

### Building with Gradle

A WAR file is created inside the `cics-java-liberty-jdbc-app/build/libs` directory and a CICS bundle ZIP file inside the `cics-java-liberty-jdbc-bundle/build/distribution` directory.

The JVM server the CICS bundle is targeted at is controlled through the `cics.jvmserver` property, or in the command line.

| Tool | Command |
| ----------- | ----------- |
| Gradle Wrapper (Linux/Mac) | ```./gradlew clean build``` |
| Gradle Wrapper (Windows) | ```gradle.bat clean build``` |
| Gradle (command-line) | ```gradle clean build``` |
| Gradle (command-line & setting jvmserver) | ```gradle clean build -Pcics.jvmserver=MYJVM``` |

### Building with Maven

A WAR file is created inside the `cics-java-liberty-jdbc-app/target` directory and a CICS bundle ZIP file inside the `cics-java-liberty-jdbc-bundle/target` directory.

The JVM server the CICS bundle is targeted at is controlled throught the `cics.jvmserver` property, defined in [`cics-java-liberty-jdbc-bundle/pom.xml`](cics-java-liberty-jdbc-bundle/pom.xm) file under the `defaultjvmserver` configuration property, or in the command line.

| Tool | Command |
| ----------- | ----------- |
| Maven Wrapper (Linux/Mac) | ```./mvnw clean verify``` |
| Maven Wrapper (Windows) | ```mvnw.cmd clean verify``` |
| Maven (command-line) | ```mvn clean verify``` |
| Maven (command-line & setting jvmserver) | ```mvn clean verify -Dcics.jvmserver=MYJVM``` |

## Configuring

### Configure Db2
THis sample uses the [EMP table](https://www.ibm.com/docs/en/db2-for-zos/latest?topic=tables-employee-table-dsn8d10emp) provided with the [Db2 sample tables](https://www.ibm.com/docs/en/db2-for-zos/latest?topic=zos-db2-sample-tables).

### Configure CICS for JDBC type 2 connectivity to Db2
_If using Db2 type 2 connectivity to Db2_

1. Update the CICS STEPLIB with the Db2 SDSNLOAD and SDSNLOD2 libraries
2. Configure CICS DB2CONN, Db2TRAN and Db2ENTRY resource definitions as described in [How you can define the CICS Db2 connection](https://www.ibm.com/docs/en/cics-ts/latest?topic=sources-defining-cics-db2-connection).
3. Bind the Db2 plan that is specified in the CICS DB2CONN or DB2ENTRY definition with a PKLIST of NULLID.*

## Deploying to a Liberty JVM server

Ensure you have the following features defined in your Liberty server.xml:
* `jdbc-4.0` (or above)
* If using Db2 type 2, add the following property to the JVM profile:
  
  ```
  -Dcom.ibm.cics.jvmserver.wlp.jdbc.driver.location=/usr/lpp/db2v13/jdbc
  ```
A template server.xml is provided [here](etc/config/liberty/server.xml).

### Deploying CICS Bundles with CICS Explorer
1. Change the name of the JVMSERVER in the .warbundle file of the CICS bundle project from DFHWLP to the name of the JVMSERVER resource defined in CICS. 
2. Export the bundle project to zFS by selecting 'Export Bundle project to z/OS Unix File System' from the context menu.
3. Create a bundle definition, setting the bundle directory attribute to the zFS location you just exported to, and install it. 
4. Check the CICS region for the dynamically created PROGRAM resource HELLOWLP using the Programs view in CICS Explorer, or the CEMT INQUIRE PROGRAM command.

### Deploying CICS Bundles from Gradle or Maven
1. Manually upload the ZIP file from the _cics-java-liberty-jdbc-bundle/target_ or _cics-java-liberty-jdbc-bundle/build/distributions_ directory to zFS.
2. Unzip this ZIP file on zFS (e.g. `${JAVA_HOME}/bin/jar xf /path/to/bundle.zip`).
3. Create a CICS BUNDLE resource definition, setting the bundle directory attribute to the zFS location you just extracted to, and install it into the CICS region. 
4. Check the CICS region for the dynamically created PROGRAM resource HELLOWLP using the Programs view in CICS Explorer, or the CEMT INQUIRE PROGRAM command.

### Deploying with Liberty configuration 
1. Manually upload the WAR file from the _cics-java-liberty-jdbc-app/target_ or _cics-java-liberty-jdbc-app/build/libs_ directory to zFS.
2. Add an `<application>` element to the Liberty server.xml to define the web application.
3. Check the CICS region for the dynamically created PROGRAM resource HELLOWLP using the Programs view in CICS Explorer, or the CEMT INQUIRE PROGRAM command.

## Running the sample
The servlet is accessed with the following URL: [http://zos.example.com:9080/com.ibm.cicsdev.jdbc.web/database](http://zos.example.com:9080/com.ibm.cicsdev.jdbc.web/database).

If the test is successful, you will see a response similar to the following written to the browser:

`Db2 current timestamp: 2024-01-01 09:30:00.000000.`

If the EMP table is available, the full sample can be accessed with the following URL: [http://zos.example.com:9080/com.ibm.cicsdev.jdbc.web/](http://zos.example.com:9080/com.ibm.cicsdev.jdbc.web/). This is a HTML page that communicates with a servlet backend to display the employees in the EMP table.


## Reference
*  Sample SQLJ Git repository  [cics-java-liberty-sqlj](https://github.com/cicsdev/cics-java-liberty-sqlj)
*  CICS Knowledge Center [Configuring a Liberty JVM server](https://www.ibm.com/docs/en/cics-ts/latest?topic=server-configuring-liberty-jvm)
*  CICS Knowledge Center [Configuring a JVM server to support Db2](https://www.ibm.com/docs/en/cics-ts/latest?topic=programs-configuring-jvm-server-support-db2)

## License
This project is licensed under [Apache License Version 2.0](LICENSE).
