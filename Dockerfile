FROM amazoncorretto:17.0.7-alpine

COPY ./link-shortener-*.jar ./link-shortener.jar

#ENV SPRING_PROFILES_ACTIVE=docker
ENV TZ=Europe/Moscow

EXPOSE 8081

CMD ["java", "-jar", "./link-shortener.jar"]
