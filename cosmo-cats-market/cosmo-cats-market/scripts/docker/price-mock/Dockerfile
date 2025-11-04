FROM amazoncorretto:21-alpine

ENV WIREMOCK_VERSION 3.9.1

RUN mkdir -p /var/wiremock/mappings
WORKDIR /var/wiremock

RUN wget -O wiremock-standalone.jar \
  https://repo1.maven.org/maven2/org/wiremock/wiremock-standalone/${WIREMOCK_VERSION}/wiremock-standalone-${WIREMOCK_VERSION}.jar

ADD ./stubs.json /var/wiremock/mappings

EXPOSE 8080 8443

CMD java -jar wiremock-standalone.jar -global-response-templating
