## NHN HTTP Server (Java)

도메인 기반 서비스 및 에러 페이지 처리 기능을 가지는 간단한 HTTP 서버입니다.

---

### 주요 기능

* SimpleServlet 기반 동적 요청 처리
* `config.json` 기반 호스트별 포트 및 경로 설정
* `.exe`, `..` 등 금지된 경로 접근 차단
* 403, 404, 500 오류 페이지 지원
* Logback 기반 날짜별 로깅
* JUnit 기반 단위 테스트 포함

---

### 실행 환경

* Java 11
* Maven 3.6.3 이상

---

### 실행 방법

```bash
# Maven 빌드
mvn clean package

# 빌드 후 생성된 JAR 실행
java -jar target/was.jar
```

---

### 디렉터리 구조

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
├── src/
│   ├── main/java/com/nhn/web/server/
│   └── test/java/com/nhn/web/server/
└── pom.xml
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
curl -H "Host: admin.example.com" http://localhost:8000/service.Time
```

---

### hosts 설정 예시 (Windows)

`user.example.com` 등의 도메인을 테스트하려면 로컬 hosts 파일에 아래와 같이 등록하세요:

```
127.0.0.1 user.example.com
127.0.0.1 admin.example.com
127.0.0.1 localhost
```

---

### 브라우저 테스트 예시

```bash
http://localhost:8000/
http://localhost:8000/service.Time
```

---
