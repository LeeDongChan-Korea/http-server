## 📦 Simple HTTP Server (Java)

도메인 기반 정적 파일 서비스 및 HTML 에러 페이지 처리 기능을 가지는 간단한 HTTP 서버입니다.

---

### ✅ 주요 기능

* **도메인(Host) 기반 가상 호스팅 (Virtual Hosting)**

  * `config.json`에 따라 `user.example.com`, `admin.example.com` 등의 도메인 요청 분기
* **정적 파일 제공**

  * 각 도메인마다 변동된 `httpRoot` 디렉터리 설정 가능
* **도메인별 index 파일 및 에러 페이지**

  * 403, 404, 500 에러 발생 시 도메인별 HTML 에러 페이지 렌더링

---

### 🛠 설정 파일: `config.json`

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

### 🚀 실행 방법

```bash
# Maven 빌드
mvn clean package

# 서버 실행
java -cp target/was-1.0-SNAPSHOT.jar com.nhn.web.server.HttpServer config.json
```

---

### 🥪 테스트 예시

```bash
curl -H "Host: user.example.com" http://localhost:8000/
curl -H "Host: admin.example.com" http://localhost:8000/notfound.html
```

---

필요 시 포트 변경, 에러 페이지 변경은 `config.json`에서 자육보건히 가능하며,
`RequestProcessor`, `ErrorHandler` 내부 로직을 통해 동적으로 처리됩니다.
# NHN 사전 과제: Java 기반 Simple WAS

## 🔧 빌드 및 실행 방법

```bash
# Maven 빌드 (JAR + 테스트)
mvn clean package

# 실행
java -jar target/was.jar
