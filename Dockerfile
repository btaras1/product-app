FROM eclipse-temurin:17.0.6_10-jre-jammy

VOLUME /tmp
COPY target/*.jar product-app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/product-app.jar"]