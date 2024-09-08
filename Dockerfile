# jeekimdev/crew-jeekim-base 이미지를 기반으로 사용
FROM jeekimdev/crew-jeekim-base

# 여기서부터는 Spring Boot 애플리케이션 구성을 추가

# Spring Boot 애플리케이션 JAR 파일을 이미지에 복사
ARG JAR_FILE=build/libs/freetime-0.0.1.jar
COPY ${JAR_FILE} /freetime-0.0.1.jar

# 애플리케이션 실행을 위한 엔트리포인트 설정
ENTRYPOINT ["java","-jar", "-Dspring.profiles.active=prod", "-Duser.timezone=Asia/Seoul", "/freetime-0.0.1.jar"]
