#WEATHER FORECAST REST API
Simple REST API created with the help of  spring boot.

###Application is made to find out the weather info such as: 
-current weather;
-today`s weather of any country and city in the world;
-weather by hour of any country and city in the world;
-compare weather in different cities within one country;
The aim of creating a project is learning how to create REST API and  make REST calls to other services.

###Technologies:
-Java 17;
-PostgreSQL 14;
-Spring Boot 3;
-Spring Data 3;
-Spring Security 3 with JWT
-FlyWay for DB migrations;

###Requirements
For building and running the application you need:
-JDK 17;
-Maven 3X;
-PostgreSQL 14 (if not using docker);
-Docker(optional);

### How to run the API
There 2 ways of running the API:
1. With Maven:
-Clone the repository by running in your terminal(command line):git clone https://github.com/GalinaPylyptiy/WeatherForecast.git;
-Go to project src/main/resource/application-local.property and complete username and password for your PostgreSQL;
(Database schemas and role table is created and filled automatically on the application startup with a Flyway SQL-based migration)
-Open terminal/command line in your project directory, start project with command: mvn spring-boot:run 

2.With Docker and docker-compose files:
-Clone the repository by running in your terminal(command line):git clone https://github.com/GalinaPylyptiy/WeatherForecast.git;
-Open terminal and navigate to project directory;
-Run : mvn clean package - to build project into a jar file;
-Build image from Dockerfile, run : docker build -t weather:latest .
-Run docker-compose.yml : docker-compose up

## How to use the WeatherForecast API
Open Postman and enter your request:
http://localhost:8080/register (Because users table is empty)
choose method  POST
Choose tab body -> raw -> JSON
Insert:
{
    "login":"user",
    "password":"password"
}
Copy you JWT token from response.(It will look something like this: eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwiaWF0IjoxNjgyNTcwMDE1LCJleHAiOjE2ODI1NzE4MTV9.Geq4tCdl3RstnDxtFhhrhM04JJUA_Ec_5IfqpBD5rfs)
Insert this JWT token to "Authorization" tab, choose Type = Bearer Token
Now you can make calls:
###To get current weather:
- http://localhost:8080/current?city={your city}&country={your country};
###Today`s weather of any country and city in the world:
(eachHour is a boolean value: type true, if you want to know weather for each hour a day, false, 
if you want to get 3 hour interval )
- http://localhost:8080/today?city={your city}&country={your country}&eachHour={true/false};
###Weather by hour of any country and city in the world:
- http://localhost:8080/hour?city={your city}&country={your country}&hour = {from 0 to 23};
###Compare weather in different cities within one country:
- http://localhost:8080/compare?city1={city1}&city2={city2}&country={country};
-choose method  GET;
-send the request;
As a response you will get status 200OK and JSON object



