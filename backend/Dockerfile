FROM eclipse-temurin:26-jre-alpine
VOLUME /tmp
# Copy your built jar file into the container
COPY ./websiteapi/target/websiteapi-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]