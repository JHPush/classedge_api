# 1단계 : 빌드 및 Layered JAR 추출
FROM openjdk:17-slim as build
WORKDIR /application
ARG JAR_FILE=./build/libs/classedge-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} application.jar
RUN java -Djarmode=layertools -jar application.jar extract


# 2단계 : 실행 환경 설정
FROM openjdk:17-slim
WORKDIR /application
COPY --from=build application/dependencies/ ./
COPY --from=build application/spring-boot-loader/ ./
COPY --from=build application/snapshot-dependencies ./
COPY --from=build application/application/ ./


ENTRYPOINT [ "java", "org.springframework.boot.loader.launch.JarLauncher" ]