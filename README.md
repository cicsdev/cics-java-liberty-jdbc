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

## Versions
| CICS TS for z/OS Version | Branch                                 | Minimum Java Version | Build Status |
|--------------------------|----------------------------------------|----------------------|--------------|
| 5.5, 5.6                 | [cicsts/v5.5](/../../tree/cicsts/v5.5) | 8                    | [![Build](https://github.com/cicsdev/cics-java-liberty-jdbc/actions/workflows/java.yaml/badge.svg?branch=cicsts%2Fv5.5)](https://github.com/cicsdev/cics-java-liberty-jdbc/actions/workflows/java.yaml) |
| 6.1                      | [cicsts/v6.1](/../../tree/cicsts/v6.1) | 8                    | [![Build](https://github.com/cicsdev/cics-java-liberty-jdbc/actions/workflows/java.yaml/badge.svg?branch=cicsts%2Fv6.1)](https://github.com/cicsdev/cics-java-liberty-jdbc/actions/workflows/java.yaml) |


## License
This project is licensed under [Apache License Version 2.0](LICENSE).
