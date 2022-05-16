FROM arm64v8/openjdk:11-slim-buster
RUN mkdir /app
WORKDIR /app
COPY build/libs/project-greenthumb-0.1-all.jar project-greenthumb.jar
EXPOSE 8080 81
CMD java -XX:+UnlockExperimentalVMOptions -XX:+EnableJVMCI -XX:+UseJVMCICompiler -Doracle.jdbc.fanEnabled=false -Dcom.sun.management.jmxremote -noverify ${JAVA_OPTS} -jar project-greenthumb.jar