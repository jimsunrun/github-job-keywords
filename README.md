# Job Keywords Web Application
This web application calculates the term frequency of keywords extracted from job descriptions posted on indeed.com. 

The app demonstrates an integraiton between several REST APIs with Spring Boot, Spring-4.1, Hibernate (JPA) and Liquibase. The app is running on Amazon's AWS cloud at the following URLs:

Job Keywords Search Web App: http://54.148.208.180:8080/job/history

Job Keywords JavaDoc: http://54.148.208.180:8080/javadoc/index.html

Job Keywords Source Code: https://github.com/jhaood/github-public

Architecture:

The architecture of the this application is based on recent publications about Micro Services and the Spring-4 platform. 
The "term extractor" Micro Service provides a Java API as opposed to a REST API that most other Micro Services provide.

The application is broken into two top-level components: 
1) the "term extractor" service integrates 2 REST services (jobs from indeed.com and keyword-extraction from FiveFilters.com)
2) the web client which connects to the "term extractor" service. 

The service architecture leverages spring-data as the ORM that binds JPA entities to a MySQL database. Spring-web provides the RestTemplate and Jackson marshalling to deserialize JSON responses read by the rest-clients that hit the indeed.com and five-filters REST APIs. 

The "term extractor" Java API has a single client: the web package. There are two HTTP-based interfaces: HTML5 and REST. HTML5 is generated by the Thymeleaf template engine. The REST API is provided by spring-web RestControllers. 

The "termextractor" package exposes the sercice API, domain classes and the confiruation classes required to 
deploy the service. The top-level package also contains the JPA repositories built with spring-data and JPA (Hibernate). 

MySQL schema evolution is implemented with Liquibase which is integrated into the Web application runtime. The maven pom.xml file also configures the Liquibase plugin to run during a build and a unit test is provided to run Liquibase. Liquibase is necessary at runtime because the Amazon production linux environment does not support maven or any of the development tools. This implementation also allows the schemas to be compared (Liquibase "diff") and validated by by both HBM2DDL and Liquibase. Liquibase can also generate the XML "changesets" required to evolve the schema as a new release is being prepared.  

Build:

Here are a few command lines to help build and deploy the app:

	"mvn clean javadoc:javadoc install" => build and run tests against an embedded H2 database
	
	"mvn clean javadoc:javadoc install -P dev" => run tests against the MySQL "job_db" database
	
	"mvn -DskipTests spring-boot:run -P prod-liquibase" => launch the app and run liquibase-validate 
	
	"mvn -DskipTests spring-boot:run -P prod-liquibase -Djobkeywords.liquibase.update=true"
			=> launch the app and run a liquibase-update with Spring Boot configures the app-context. 
		
	"java -Dspring.profiles.active=mysql,liquibase -Dspring.jpa.hibernate.ddl-auto=validate 
		-Djobkeywords.liquibase.update=false -jar /home/ec2-user/job-keywords-2.0-SNAPSHOT.jar"
			=> launch the JAR as a Spring Boot app and run HBM2DDL-validate and liquibase-validate
		
	"java -Dspring.profiles.active=mysql,liquibase -Dspring.jpa.hibernate.ddl-auto=none 
		-Djobkeywords.liquibase.update=true -jar /home/ec2-user/job-keywords-2.0-SNAPSHOT.jar"
			=> launch the Spring Boot app and run liquibase-update (HBM2DDL does NOT run)


Spring Boot: 

Spring-Boot version "1.2.1.RELEASE" launches a web container and configures the app-context based on a ton of automatic configuration. The job-keywords application defines specific Configuration classes to override or extend the auto-generated configuration from Spring-Boot. 

Testing: 

The naming conventions for test classes is

*UnitTest - a true unit test that may mock dependent classes. A unit test can test a cluster of classes but must not require Spring or any external resources. An external resource is something that lives outside the JVM.

*Test - an integration test that requires spring to configure the class or it's dependencies. The integration test may or may not hit resources outside the JVM.

*IT - An integration test that lives outside the web-app and requires a container to be launched by the failsafe plugin. The only *IT.java test is JobKeywordsApplicationIT - which itself is not a true out-of-container integration test.


