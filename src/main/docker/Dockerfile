FROM java:openjdk-8-alpine
MAINTAINER Aug70 Co <aug70co@gmail.com>

ADD @project.build.finalName@.jar app.jar

RUN sh -c 'touch /app.jar'
CMD ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app.jar"]