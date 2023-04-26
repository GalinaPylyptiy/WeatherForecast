FROM openjdk:17
VOLUME /tmp
EXPOSE 8080
ARG JAR_FILE=target/weather-forecast.jar
ADD ${JAR_FILE} weather.jar
ENTRYPOINT ["java","-jar","/weather.jar"]