FROM amazoncorretto:21-alpine
WORKDIR hotel-service
COPY rest/target/rest-0.0.1-SNAPSHOT.jar /hotel-service/hotel.jar
ENTRYPOINT ["java","-jar","/hotel-service/hotel.jar"]