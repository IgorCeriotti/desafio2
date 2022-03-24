FROM openjdk:17
ENV APP_HOME=/usr/app
WORKDIR $APP_HOME
COPY target/*.jar app.jar
COPY aws_certs/cacerts cacerts
RUN mv -f cacerts /usr/java/openjdk-17/lib/security/
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
HEALTHCHECK --interval=5m --timeout=20s --start-period=30s CMD curl --fail http://localhost:8080/health || exit 1