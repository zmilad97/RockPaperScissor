
FROM maven:3.8.1-openjdk-16 AS build
COPY src src
COPY pom.xml /
RUN mvn package

EXPOSE 6060
EXPOSE 4567


ENTRYPOINT ["mvn","exec:java"]
