FROM openjdk:11.0.3-jdk-slim-stretch
RUN mkdir /app
WORKDIR /app
COPY build/libs/project-greenthumb-0.1-all.jar project-greenthumb.jar
EXPOSE 8080
CMD java -XX:+UnlockExperimentalVMOptions -XX:+EnableJVMCI -XX:+UseJVMCICompiler -Doracle.jdbc.fanEnabled=false -Dcom.sun.management.jmxremote -noverify ${JAVA_OPTS} -jar project-greenthumb.jar