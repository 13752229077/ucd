FROM openjdk:8-jre-alpine
COPY target/core-1.0-jar-with-dependencies.jar /core-1.0-jar-with-dependencies.jar
CMD ["/usr/bin/java", "-cp", "/core-1.0-jar-with-dependencies.jar", "core"]