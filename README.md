# cics-java-liberty-jdbc
[![Build](https://github.com/cicsdev/cics-java-liberty-jdbc/actions/workflows/build.yaml/badge.svg?branch=cicsts/v5.5)](https://github.com/cicsdev/cics-java-liberty-jdbc/actions/workflows/build.yaml)
[![License](https://img.shields.io/badge/License-Apache%202.0-green.svg)](https://www.apache.org/licenses/LICENSE-2.0)

## Overview

This sample demonstrates how to code, build, and deploy a CICS Java application that makes JDBC calls to Db2 from a web servlet running in a CICS Liberty JVM server. It uses the employee sample table (`EMP`) supplied with Db2 for z/OS, and allows you to display and query employee information.

The sample uses the Java EE 7 Servlet API and is targeted for deployment in a CICS TS V5.5 Liberty JVM server. It supports both Db2 type 2 and type 4 connectivity.

**Key Features:**
- Demonstrates JDBC connectivity to Db2 for z/OS from a Liberty JVM server
- Supports both Db2 type 2 (local) and type 4 (network) JDBC drivers
- Provides a REST-style servlet that queries the Db2 `EMP` sample table
- Includes a simple HTML front-end for browsing employee records

## Table of Contents

1. [Overview](#overview)
2. [Prerequisites](#prerequisites)
3. [Reference](#reference)
4. [Downloading](#downloading)
5. [Building the Sample](#building-the-sample)
6. [Deploying to a CICS Liberty JVM server](#deploying-to-a-cics-liberty-jvm-server)
7. [Running the Sample](#running-the-sample)
8. [Additional Resources](#additional-resources)
9. [License](#license)
10. [Contributing](#contributing)

## Prerequisites

- CICS TS V5.5 or later
- A configured Liberty JVM server in CICS
- Java SE 8 or later on the workstation
- Eclipse with the IBM CICS SDK for Java EE, Jakarta EE and Liberty (optional)
- Gradle or Apache Maven on the workstation (optional — wrappers are supplied)
- IBM Db2 V13 or later on z/OS with the `EMP` sample table
- A connected CICS `DB2CONN` resource (see [Deploying to a CICS Liberty JVM server](#deploying-to-a-cics-liberty-jvm-server))

## Reference

- Sample SQLJ Git repository: [cics-java-liberty-sqlj](https://github.com/cicsdev/cics-java-liberty-sqlj)
- CICS Knowledge Center: [Configuring a Liberty JVM server](https://www.ibm.com/docs/en/cics-ts/latest?topic=server-configuring-liberty-jvm)
- CICS Knowledge Center: [Configuring a JVM server to support Db2](https://www.ibm.com/docs/en/cics-ts/latest?topic=programs-configuring-jvm-server-support-db2)

## Downloading

**If using Eclipse:** the simplest approach is to clone the repository using the Eclipse Git plugin (EGit) perspective.

**If using the command line:**
```shell
git clone https://github.com/cicsdev/cics-java-liberty-jdbc
```
Alternatively, download the sample as a [ZIP](https://github.com/cicsdev/cics-java-liberty-jdbc/archive/main.zip) and unzip onto the workstation.

**If importing into Eclipse:**
1. In the **Git Repositories** view, right-click the repository → **Import as Project**
   *(if you cloned from the command line, use **File → Import → Existing Projects into Workspace** instead, browse to the cloned directory, select all projects)*
2. Switch to the **Java EE** perspective
3. In the **Project Explorer**, right-click the `cics-java-liberty-jdbc-app` folder → **Import as Project**
4. Right-click the `cics-java-liberty-jdbc-cicsbundle` folder → **Import as Project**
5. Right-click the `cics-java-liberty-jdbc-cicsbundle-eclipse` folder → **Import as Project**
6. **Required:** Right-click the root project → **Gradle → Refresh Gradle Project** or **Maven → Update Project...** — this resolves CICS and framework dependencies into the project classpath.

**Package `com.ibm.cicsdev.liberty.jdbc`**
- [`DatabaseService`](cics-java-liberty-jdbc-app/src/main/java/com/ibm/cicsdev/liberty/jdbc/DatabaseService.java) — obtains a JDBC connection and runs a timestamp query
- [`DatabaseServlet`](cics-java-liberty-jdbc-app/src/main/java/com/ibm/cicsdev/liberty/jdbc/DatabaseServlet.java) — servlet that returns the current Db2 timestamp

**Package `com.ibm.cicsdev.liberty.jdbc.employee`**
- [`Employee`](cics-java-liberty-jdbc-app/src/main/java/com/ibm/cicsdev/liberty/jdbc/employee/Employee.java) — model class for an employee record
- [`EmployeeService`](cics-java-liberty-jdbc-app/src/main/java/com/ibm/cicsdev/liberty/jdbc/employee/EmployeeService.java) — queries the `EMP` table via JDBC
- [`EmployeeServlet`](cics-java-liberty-jdbc-app/src/main/java/com/ibm/cicsdev/liberty/jdbc/employee/EmployeeServlet.java) — servlet that returns employee data as JSON

**Supporting files:**
- [`etc/config/liberty/server.xml`](etc/config/liberty/server.xml) — Liberty server template
- [`etc/config/jvmprofile/DFHWLP.jvmprofile`](etc/config/jvmprofile/DFHWLP.jvmprofile) — JVM profile template

## Building the Sample

You can build the sample using an IDE of your choice, or from the command line. Using the supplied Gradle or Maven wrapper is the recommended approach to get a consistent build tool version.

The required build tasks are `clean build` for Gradle and `clean verify` for Maven. Gradle generates a WAR file in `cics-java-liberty-jdbc-app/build/libs`; Maven generates it in `cics-java-liberty-jdbc-app/target`.

### Gradle Wrapper (command line)

On Linux or Mac:

```shell
./gradlew clean build
```

On Windows:

```shell
gradlew.bat clean build
```

This creates a WAR file inside the `cics-java-liberty-jdbc-app/build/libs` directory.

> **Note:** In Eclipse, the `build` directory may be hidden by default. To view it: **Package Explorer → ⋮ → Filters and Customization → uncheck "Gradle build folder"**.

### Maven Wrapper (command line)

On Linux or Mac:

```shell
./mvnw clean verify
```

On Windows:

```shell
mvnw.cmd clean verify
```

This creates a WAR file inside the `cics-java-liberty-jdbc-app/target` directory.

### Building with Eclipse (IDE)

Once imported (see [Downloading](#downloading)), use the IDE's built-in Gradle or Maven integration:

**With Gradle (Buildship):**
1. Right-click the root project → **Run As → Gradle Build...**
2. Enter `clean build` in the Gradle Tasks field → **Run**
3. After the build completes, right-click the root project → **Gradle → Refresh Gradle Project**

**With Maven (m2e):**
1. Right-click the root project → **Maven → Update Project...** → check **Force Update of Snapshots/Releases** → **OK**
2. Right-click the root project → **Run As → Maven build...** → enter `clean verify` → **Run**

## Deploying to a CICS Liberty JVM server

### Configure CICS for Db2

Configure the JVM profile of the Liberty JVM server to include the Db2 driver location:

```
-Dcom.ibm.cics.jvmserver.wlp.jdbc.driver.location=/usr/lpp/db2v13/jdbc
-Ddb2.jcc.override.currentSchema=DBADMIN
```

See the provided [JVM profile template](etc/config/jvmprofile/DFHWLP.jvmprofile). Restart the JVM server after changes.

Ensure the following feature is defined in your Liberty `server.xml`:

```xml
<featureManager>
    <feature>cicsts:core-1.0</feature>
    <feature>servlet-3.1</feature>
    <feature>jdbc-4.1</feature>
</featureManager>
```

A template `server.xml` is provided [here](./etc/config/liberty/server.xml).

### Install a CICS DB2CONN resource

Ensure a CICS `DB2CONN` is installed and connected:

```
CEDA DEFINE DB2CONN(JODBCONN) GROUP(CDEVJLDB)
CEDA INSTALL DB2CONN(JODBCONN) GROUP(CDEVJLDB)
```

### CICS Bundle Plugin Deployment (Gradle/Maven)

**Configure your JVM server name** (default is `DFHWLP`):

Gradle:
```shell
./gradlew clean build "-Pcics.jvmserver=MYJVM"
```

Maven:
```shell
./mvnw clean verify "-Dcics.jvmserver=MYJVM"
```

**Deploy the bundle:**

1. Upload the CICS bundle ZIP to zFS:
   - Gradle: `cics-java-liberty-jdbc-cicsbundle/build/distributions/`
   - Maven: `cics-java-liberty-jdbc-cicsbundle/target/`
2. On z/OS, extract the bundle:
   ```shell
   jar xf cics-java-liberty-jdbc-cicsbundle.zip
   ```
3. Create and install a CICS BUNDLE resource definition pointing to the extracted directory:
   ```
   CEDA DEFINE BUNDLE(JDBCBNDL) GROUP(CDEVJLDB) BUNDLEDIR(/path/to/bundle)
   CEDA INSTALL BUNDLE(JDBCBNDL) GROUP(CDEVJLDB)
   ```

### CICS Explorer SDK Deployment

This repository includes a pre-configured Eclipse CICS bundle project `cics-java-liberty-jdbc-cicsbundle-eclipse`.

1. Right-click `cics-java-liberty-jdbc-cicsbundle-eclipse` → **Export Bundle Project to z/OS UNIX File System** and follow the wizard.

### Direct Liberty Application Deployment

1. Build the WAR using Gradle or Maven (see [Building the Sample](#building-the-sample))
2. Upload the WAR file to zFS
3. Add an `<application>` element to your Liberty `server.xml`:

```xml
<application id="cics-java-liberty-jdbc"
    location="${server.config.dir}/apps/cics-java-liberty-jdbc.war"
    name="cics-java-liberty-jdbc" type="war">
    <application-bnd>
        <security-role name="cicsAllAuthenticated">
            <special-subject type="ALL_AUTHENTICATED_USERS"/>
        </security-role>
    </application-bnd>
</application>
```

## Running the Sample

1. Verify the application started successfully in Liberty by checking for `CWWKT0016I` in `messages.log`:
   ```
   CWWKT0016I: Web application available (default_host): http://hostname:9080/cics-java-liberty-jdbc/
   ```
2. Test basic Db2 connectivity at:
   `http://hostname:9080/cics-java-liberty-jdbc/database`

   If successful, you will see a response like:
   ```
   Db2 current timestamp: 2024-01-01 09:30:00.000000.
   ```
3. If the `EMP` table is available, access the full employee browser at:
   `http://hostname:9080/cics-java-liberty-jdbc/`

   This HTML page communicates with the servlet backend to display employees from the `EMP` table.

## Additional Resources

- [CICS TS Documentation](https://www.ibm.com/docs/en/cics-ts)
- [WebSphere Liberty Documentation](https://www.ibm.com/docs/en/was-liberty)
- [Db2 EMP sample table](https://www.ibm.com/docs/en/db2-for-zos/latest?topic=tables-employee-table-dsn8d10emp)
- [Db2 sample tables](https://www.ibm.com/docs/en/db2-for-zos/latest?topic=zos-db2-sample-tables)

## License

This project is licensed under [Apache License Version 2.0](LICENSE).

## Contributing

This sample is maintained by IBM CICS development. We welcome bug reports and feature requests via GitHub Issues. Contributions are welcome and reviewed on a case-by-case basis — please read the [contributing guidelines](https://github.com/cicsdev/.github/blob/main/CONTRIBUTING.md) before opening a pull request. For CICS product questions, contact IBM Support.
