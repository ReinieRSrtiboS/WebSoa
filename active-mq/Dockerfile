FROM amazoncorretto:8-alpine3.12-jre

# Prepare environment
RUN mkdir /activemq/
WORKDIR /activemq/

# Add required dependencies
RUN apk add curl

# Download and install ActiveMQ
RUN curl https://archive.apache.org/dist/activemq/5.15.8/apache-activemq-5.15.8-bin.tar.gz -o activemq.tar.gz
RUN tar xvf activemq.tar.gz
RUN rm activemq.tar.gz
RUN mv ./apache-activemq-5.15.8/* ./

# Start ActiveMQ
EXPOSE 8161 61616
CMD /bin/sh -c "bin/activemq console"
