FROM openjdk:8

RUN apt-get -y update

RUN mkdir /home/app && \
groupadd -r appuser -g 433 && \
useradd -u 431 -r -g appuser -d /home/app -s /sbin/nologin -c "Docker app user" appuser && \
chown -R appuser:appuser /home/app

USER appuser
WORKDIR /home/app
EXPOSE 8080

ADD ./backend/build/libs/**.jar /home/app/app.jar

ENTRYPOINT exec java $JAVA_OPTS -jar app.jar
