FROM openjdk:8-alpine

WORKDIR /

ARG JAR_FILE_PATH=/target/formdang-auth-spring-0.0.1-SNAPSHOT.jar

ENV ACTIVE_PROFILE="local"

COPY /${JAR_FILE_PATH} ROOT.jar

EXPOSE 22081

ENTRYPOINT ["java", "-jar", "-Xincgc", "-Xmx256m", "-Dspring.profiles.active=${ACTIVE_PROFILE}",  "-Duser.timezone=Asia/Seoul", "-Dcom.sun.management.jmxremote", "-Dcom.sun.management.jmxremote.port=35020", "-Dcom.sun.management.jmxremote.rmi.port=35020", "-Djava.rmi.server.hostname=formdang-auth-spring", "-Dcom.sun.management.jmxremote.ssl=false", "-Dcom.sun.management.jmxremote.authenticate=false", "ROOT.jar"]

