## NHN HTTP Server (Java)

도메인 기반 서비스 및 에러 페이지 처리 기능을 가지는 간단한 HTTP 서버입니다.

---

### 주요 기능

* SimpleServlet 기반의 동적 요청 처리
* config.json을 통한 호스트별 포트 및 경로 설정
* .exe, .. 등 금지된 경로 접근 차단
* 403, 404, 500 에러 페이지 지원
* Logback을 이용한 날짜별 로깅
* JUnit 기반의 단위 테스트 포함
* URL 쿼리 파라미터 처리 지원
* 현재 시간을 반환하는 Time 서블릿 제공

---

### 실행 환경

* Java 11
* Maven 3.6.3 이상

---

## 실행 방법

>  본 프로젝트에는 실행을 위한 JDK 11과 Maven이 함께 포함되어 있습니다.
** 압축파일을 풀고 폴더 내부에서 실행 **

### 1. 기본 실행

```bash
# Maven 빌드
mvn clean package

# 빌드 후 JAR 실행
java -jar was.jar
```

### 2. 실행 환경이 다르거나 Maven/JDK가 구성되지 않은 경우

- 시스템에 Maven이 없거나 환경변수가 구성되지 않은 경우, 아래처럼 포함된 실행 파일 사용:

```bash
# 포함된 JDK 및 Maven 사용 예시 
.\maven-3.9.9\bin\mvn clean package
.\jdk-11\bin\java -jar was.jar

```
---

### 디렉터리 구조

```
was/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/nhn/web/server/
│   │   │       ├── config/
│   │   │       ├── service/
│   │   │       └── servlet/
│   │   └── resources/
│   │       ├── config.json
│   │       └── public/
│   │           ├── user/
│   │           ├── admin/
│   │           └── localhost/
│   └── test/
│       ├── java/com/nhn/web/server/...
│       └── resources/
├── maven-3.9.9/... ##빌드도구
├── jdk-11/...      ##실행도구
├── pom.xml
└── README.md

```

---

### 설정 파일 예시: `config.json`

```json
{
  "port": 8000,
  "hosts": {
    "user.example.com": {
      "httpRoot": "public/user",
      "indexFile": "index.html",
      "errorPages": {
        "403": "403.html",
        "404": "404.html",
        "500": "500.html"
      }
    },
    "admin.example.com": {
      "httpRoot": "public/admin",
      "indexFile": "index.html",
      "errorPages": {
        "403": "403.html",
        "404": "404.html",
        "500": "500.html"
      }
    },
    "localhost": {
      "httpRoot": "public/localhost",
      "indexFile": "index.html",
      "errorPages": {
        "403": "403.html",
        "404": "404.html",
        "500": "500.html"
      }
    }
  }
}
```

---

### 테스트 예시 (curl)

```bash
curl -H "Host: user.example.com" http://localhost:8000/
curl -H "Host: user.example.com" http://localhost:8000/Hello?name=Lee
curl -H "Host: admin.example.com" http://localhost:8000/service.Time
curl -H "Host: admin.example.com" http://localhost:8000/Not ##존재하지않는 파일테스트
```

---

### hosts 설정 예시 (Windows)

`user.example.com` 등의 도메인을 테스트하려면 로컬 hosts 파일에 아래와 같이 등록하세요:

```
127.0.0.1 user.example.com
127.0.0.1 admin.example.com
```

---

### 브라우저 테스트 예시

```bash
http://localhost:8000/
http://localhost:8000/Hello
```

---
