FROM amazoncorretto:17
ADD /target/demo-v1.jar demo-v1.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","demo-v1.jar"]