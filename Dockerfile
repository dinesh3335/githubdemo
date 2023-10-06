FROM openjdk:8
EXPOSE 8080
ADD build/libs/springbatchdemo-0.0.1-SNAPSHOT.jar springbatchexample.jar
ENTRYPOINT ["java" , "-jar" , "springbatchexample.jar"]