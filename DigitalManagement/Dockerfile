# 改用Java17镜像，匹配你jar包编译版本
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY app.jar /app/app.jar
EXPOSE 2005
ENTRYPOINT ["java", "-jar", "app.jar"]