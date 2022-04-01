FROM amazoncorretto:11

# Prepare work dir
RUN mkdir /websoa/
WORKDIR /websoa/

# Copy gradle files
COPY ./gradle/ /websoa/gradle/
COPY ./gradlew /websoa/
COPY ./settings.gradle /websoa/
RUN chmod u+x ./gradlew

# Add & compile service
COPY ./valdation-service/ /websoa/
RUN ./gradlew bootJar

CMD java -jar build/libs/*.jar
