
FROM openjdk:8
#设置时区
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
COPY target/*.jar work.jar
VOLUME ["/opt/log"]
ENTRYPOINT ["java","-jar","work.jar"]

