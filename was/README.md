## NHN HTTP Server (Java)

도메인 기반 서비스 및 에러 페이지 처리 기능을 가지는 간단한 HTTP 서버입니다.

---

### 주요 기능

- SimpleServlet 기반 동적 처리

- config.json 기반 호스트별 포트 및 경로 설정

- .exe, .. 등 금지된 경로 접근 차단

- 403, 404, 500 오류 처리 페이지 지원

- Logback 기반 로깅, 날짜별 로깅

- JUnit 기반 단위 테스트 포함

---
 ### 실행 환경
- Java 11

- Maven 3.6.3 이상

 ### 구성
- /src/main/java: 서버 코드

- /src/test/java: 단위 테스트 (JUnit 4)

- config.json: 서버 설정 파일

### 설정 파일: `config.json`

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

### 📁 프로젝트 구조 예시

```
was/
├── config.json
├── public/
│   ├── user/
│   │   ├── index.html
│   │   └── 404.html ...
│   └── admin/
│       ├── index.html
│       └── 403.html ...
└── src/
    └── main/java/com/nhn/web/server/
```

---

### 실행 방법

```bash
# Maven 빌드
mvn clean package

# 서버 실행
java -jar was.jar
```

---

### curl 테스트 예시

```bash
curl -H "Host: user.example.com" http://localhost:8000/
curl -H "Host: admin.example.com" http://localhost:8000/service.Time
```
---
### 

* 실제 브라우저에서 `user.example.com` 와 같은 주소처럼 접속하고자 할 경우,
* 개발 프로필에서 도메인이 IP로 변환되어야 합니다.

#### hosts 파일 (예: Windows)

```
127.0.0.1 user.example.com
127.0.0.1 admin.example.com
127.0.0.1 localhost
```
---

### 브라우저 테스트

```bash
http://localhost:8000/
http://localhost:8000/service.Time
```
---
