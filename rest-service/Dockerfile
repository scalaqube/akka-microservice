FROM java:openjdk-8u92-jdk-alpine
MAINTAINER uchenm<uchenm@gmail.com>

RUN mkdir /home/lab


ADD dev-tool-api-assembly-1.0.jar /home/lab/

WORKDIR /home/lab

EXPOSE 9001

ENTRYPOINT ["java", "-jar", "dev-tool-api-assembly-1.0.jar"]
