FROM gradle:7.3.0-jdk-alpine as builder

COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src

RUN gradle shadowJar

RUN mkdir /narwhal
RUN mv /home/gradle/src/build/libs/* /narwhal

FROM openjdk:11

EXPOSE 8080
COPY --from=builder /narwhal/narwhal-0.0.1-all.jar /narwhal/narwhal.jar
WORKDIR /narwhal
ENTRYPOINT ["java", "-jar", "/narwhal/narwhal.jar"]
