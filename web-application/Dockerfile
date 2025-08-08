FROM maven:3.9.7-sapmachine-17 AS builder
WORKDIR /build
COPY . .
RUN mvn --no-transfer-progress -Dmaven.test.skip=true clean package

FROM azul/zulu-openjdk:17-latest
WORKDIR /app
COPY --from=builder /build/target/web-application-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8000
ENTRYPOINT ["java","-jar","/app/app.jar"]