FROM openjdk:16-slim-buster

COPY target/OpenSCAPResults-1.0-SNAPSHOT-jar-with-dependencies.jar .
CMD ["java", "-jar", "OpenSCAPResults-1.0-SNAPSHOT-jar-with-dependencies.jar", "/container/oval77.xml", "/container/output.json", "oval"]
