# cics-java-liberty-jdbc
This sample demonstrates how to code, build, and deploy a CICS Java application that makes JDBC calls to Db2 from a web servlet in CICS Liberty. It makes use of the employee sample table supplied with Db2 for z/OS, and allows you to display employee information from the table EMP.

## Contents
This is a set of sample Java projects for database interaction in CICS Java, demonstrating how you can use JDBC in a WAR in a Liberty JVM server, to allow it query items in Db2.

This sample can use either Db2 type 2 or type 4 connectivity.

* [`cics-java-liberty-jdbc-web`](cics-java-liberty-jdbc-web) - Dynamic web project containing the Java source.
* [`java-java-liberty-jdbc-bundle`](cics-java-liberty-jdbc-bundle) - CICS bundle plug-in based project, contains Web application bundle-parts. Use with Gradle and Maven builds.
* [`etc/config/liberty`](etc/config/liberty) - Liberty server configuration files
* [`etc/eclipse_projects/com.ibm.cics.server.examples.wlp.jdbc.bundle`](etc/eclipse_projects/com.ibm.cics.server.examples.wlp.jdbc.bundle) - CICS bundle project (CICS Explorer based CICS bundle project, contains Web application bundle-parts. Use with CICS Explorer 'Export to zFS' deployment capability.)

## Requirements
* CICS TS V6.1 or later
* A connected CICS DB2CONN resource. For more information, see [CONFIGURING](#configuring)
* A Liberty JVM server
* Java SE 1.8 or later on the workstation
* IBM Db2 V11 or later on z/OS

## Downloading

- Clone the repository using your IDEs support, such as the Eclipse Git plugin
- **or**, download the sample as a [ZIP](https://github.com/cicsdev/cics-java-liberty-jdbc/archive/cicsts/v6.1.zip) and unzip onto the workstation

> [!TIP]
> Eclipse Git provides an 'Import existing Projects' check-box when cloning a repository.


## Building 

You can build the sample in a variety of ways:
- Using the implicit compile/build of the Eclipse based CICS Explorer SDK
- Using the built-in Gradle or Maven support of your IDE (For example: *buildship* or *m2e* in Eclipse which integrate with the "Run As..." menu.)
- Using the supplied Gradle or Maven Wrapper scripts (no requirement for an IDE or Gradle/Maven install)
- or you can build it from the command line if you have Gradle or Maven installed on your workstation
  

> [!IMPORTANT]
> The sample comes pre-configured for use with a JDK 17 and CICS TS V6.1 Libraries for Jakarta EE 9. When you initially import the project to your IDE, if your IDE is not configured for a JDK 17, or does not have CICS Explorer SDK installed, you might experience local project compile errors. To resolve issues you should configure the Project's build-path to add/remove your preferred combination of CICS TS, JDK, and Liberty's Enterprise Java libraries (Java EE or Jakarta EE). Resolving errors might also depend on how you wish to build and deploy the sample. If you are building and deploying through CICS Explorer SDK and 'Export to zFS' you should edit the link-app's Project properties. Select 'Java Build Path', on the Libraries tab select 'Classpath', click 'Add Library', select 'CICS with Enterprise Java and Liberty' Library, and choose the appropriate CICS and Enterprise Java versions.
If you are building and deploying with Gradle or Maven then you don't necessarily need to fix the local errors, but to do so, you can do as above, or you can run a tooling refresh on the link-app project. For example, in Eclipse: right-click on "Project", select "Gradle -> Refresh Gradle Project", **or** right-click on "Project", select "Maven -> Update Project...".

> [!TIP]
> In Eclipse, Gradle (buildship) is able to fully refresh and resolve the local classpath even if the project was previously updated by Maven. However, Maven (m2e) does not currently reciprocate that capability. If you previously refreshed the project with Gradle, you'll need to manually remove the 'Project Dependencies' entry on the Java build-path of your Project Properties to avoid duplication errors when performing a Maven Project Update.



### Option 1: Building with Eclipse

If you are using the Egit client to clone the repo, remember to tick the button to import all projects. Otherwise, you should manually Import the projects into CICS Explorer using File &rarr; Import &rarr; General &rarr; Existing projects into workspace, then follow the error resolution advice above.

### Option 2: Building with Gradle

For a complete build you should run the settings.gradle file in the top-level 'cics-java-liberty-jdbc' directory which is designed to invoke the individual build.gradle files for each project. 

If successful, a WAR file is created inside the `cics-java-liberty-jdbc-web/build/libs` directory and a CICS bundle ZIP file inside the `cics-java-liberty-jdbc-bundle/build/distribution` directory. 

[!NOTE]
In Eclipse, the output 'build' directory is often hidden by default. From the Package Explorer pane, select the three dot menu, choose filters and un-check the Gradle build folder to view its contents.

The JVM server the CICS bundle is targeted at is controlled through the `cics.jvmserver` property, defined in the [`cics-java-liberty-jdbc-bundle/build.gradle`](cics-java-liberty-jdbc-bundle/build.gradle) file, or alternatively can be set on the command line:

**Gradle Wrapper (Linux/Mac):**
```shell
./gradlew clean build
```
**Gradle Wrapper (Windows):**
```shell
gradle.bat clean build
```
**Gradle (command-line):**
```shell
gradle clean build
```
**Gradle (command-line & setting jvmserver):**
```shell
gradle clean build -Pcics.jvmserver=MYJVM
```

### Option 3: Building with Apache Maven

For a complete build you should run the pom.xml file in the top-level 'cics-java-liberty-jdbc' directory. A WAR file is created inside the `cics-java-liberty-jdbc-web/target` directory and a CICS bundle ZIP file inside the `cics-java-liberty-jdbc-bundle/target` directory.

If building a CICS bundle ZIP the CICS JVM server name for the WAR bundle part should be modified in the 
 `cics.jvmserver` property, defined in [`cics-java-liberty-jdbc-bundle/pom.xml`](cics-java-liberty-jdbc-bundle/pom.xml) file under the `defaultjvmserver` configuration property, or alternatively can be set on the command line.

**Maven Wrapper (Linux/Mac):**
```shell
./mvnw clean verify
```
**Maven Wrapper (Windows):**
```shell
mvnw.cmd clean verify
```
**Maven (command-line):**
```shell
mvn clean verify
```
**Maven (command-line & setting jvmserver):**
```shell
mvn clean verify -Dcics.jvmserver=MYJVM
```

## Configuring

### Configure CICS for Db2
To allow your CICS region to connect to DB2, we need to add some configuration to the JCL.

```
//  SET DB2=V13                           - DB2 Version
...
<variables>
...
DB2CONN=YES

//STEPLIB
...
//         DD DISP=SHR,DSN=SYS2.DB2.&DB2..SDSNLOAD
//         DD DISP=SHR,DSN=SYS2.DB2.&DB2..SDSNLOD2
```

### Configure the Liberty with DB2
This sample uses the [EMP table](https://www.ibm.com/docs/en/db2-for-zos/latest?topic=tables-employee-table-dsn8d10emp) provided with the [Db2 sample tables](https://www.ibm.com/docs/en/db2-for-zos/latest?topic=zos-db2-sample-tables).
Configure the JVM profile of the Liberty JVM server to include the Db2 driver location.
> [!TIP]
> There are several options to configuring the database schema. The following config defines the schema in the JVM profile, however, the schema can be defined directly in the datasource in the server.xml in the datasource properties element. Or it can be defined in the application code itself once setting a connection. 

> Note: The name of your JVM profile is assumed to be 'DFHWLP'

```
-Dcom.ibm.cics.jvmserver.wlp.jdbc.driver.location=/usr/lpp/db2v13/jdbc
-Ddb2.jcc.override.currentSchema=DBADMIN
```
> Note: This example is using db2v13, this version must be consistent to the version set in your JCL.

As an example, see the provided [JVM profile template](etc/config/jvmprofile/DFHWLP.jvmprofile). If necessary, restart the JVM server.

Ensure you have the following feature defined in your Liberty server.xml:
* `jdbc-4.3`

A template server.xml is provided [here](./etc/config/liberty/server.xml).

### Option 1 - Configure the DB2CONN with CEDA at a terminal

Ensure a CICS DB2CONN is installed and connected. 

```
CEDA DEFINE DB2CONN(JODBCONN) GROUP(CDEVJLDB)
```
```
CEDA INSTALL DB2CONN(JODBCONN) GROUP(CDEVJLDB)
```

### Option 2 - Configure the DB2CONN with CICS Explorer
1. Definitions > Db2 > Db2 Connection Definitions
2. Right-click > New...
3. Fill in the Name and Group with `CDEVJLDB`
4. Right-click and install the new definition
5. Ensure it is CONNECTED

> Note: The DB2ID differs between DB2 versions and the system you are running your CICS region on. Consult your CICS system programmer if you are unsure.


---

## Deploying to CICS

### Option 1 - Deploying using CICS Explorer SDK and the provided CICS bundle project
1. Deploy the CICS bundle project 'com.ibm.cics.server.examples.wlp.jdbc.bundle' from CICS Explorer using the **Export Bundle Project to z/OS UNIX File System** wizard. This CICS bundle includes the WAR bundlepart to run the sample.


### Option 2 - Deploying using CICS Explorer SDK with own CICS bundle project
1. Copy and paste the built WAR from your *projects/cics-java-liberty-jdbc-web/target* or *projects/cics-java-liberty-jdbc-web/build/libs* directory into a new Eclipse CICS bundle project.
2. Create a new bundlepart that references the WAR file. 
3. Optionally customise the CICS bundle contents, perhaps adding a TRANDEF of your choice
4. Right click using the ** Export Bundle Project to z/OS UNIX File System ** wizard.


### Option 3 - Deploying using CICS Explorer (Remote System Explorer) and CICS Bundle ZIP
1. Connect to USS on the host system
2. Create the bundle directory for the project.
3. Copy & paste the built CICS bundle ZIP file from your *projects/cics-java-liberty-jdbc-bundle/target* or *projects/cics-java-liberty-jdbc-bundle/build/distributions* directory to z/FS on the host system into the bundle directory.
4. Extract the ZIP by right-clicking on the ZIP file > User Action > unjar...
5. Refresh the bundle directory


### Option 4 - Deploying using command line tools
1. Upload the built CICS bundle ZIP file from your *projects/cics-java-liberty-jdbc-bundle/target* or *projects/cics-java-liberty-jdbc-bundle/build/distributions* directory to z/FS on the host system (e.g. FTP).
2. Connect to USS on the host system (e.g. SSH).
3. Create the bundle directory for the project.
4. Move the CICS bundle ZIP file into the bundle directory.
5. Change directory into the bundle directoy.
6. Extract the CICS bundle ZIP file. This can be done using the `jar` command. For example:
   ```shell
   jar xf file.zip
   ```

---


## Running the sample
The servlet is accessed with the following URL: [http://zos.example.com:9080/cics-java-liberty-jdbc-web/database](http://zos.example.com:9080/cics-java-liberty-jdbc-web/database).

If the test is successful, you will see a response similar to the following written to the browser:

`Db2 current timestamp: 2024-01-01 09:30:00.000000.`

If the EMP table is available, the full sample can be accessed with the following URL: [http://zos.example.com:9080/cics-java-liberty-jdbc-web/](http://zos.example.com:9080/cics-java-liberty-jdbc-web/). This is a HTML page that communicates with a servlet backend to display the employees in the EMP table.


## Reference
*  Sample SQLJ Git repository  [cics-java-liberty-sqlj](https://github.com/cicsdev/cics-java-liberty-sqlj)
*  CICS Knowledge Center [Configuring a Liberty JVM server](https://www.ibm.com/docs/en/cics-ts/latest?topic=server-configuring-liberty-jvm)
*  CICS Knowledge Center [Configuring a JVM server to support Db2](https://www.ibm.com/docs/en/cics-ts/latest?topic=programs-configuring-jvm-server-support-db2)

## License
This project is licensed under [Apache License Version 2.0](LICENSE).
