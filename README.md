# Course Reg – Web đăng ký học phần (Spring Boot + PostgreSQL + Flyway)

## Yêu cầu
- JDK 17+
- Maven 3.9+
- Docker + Docker Compose (để chạy PostgreSQL)

## Khởi chạy nhanh

### 1) Khởi động PostgreSQL
```bash
docker compose -f db/docker-compose.yml up -d
```
Postgres sẽ mở ở `localhost:5432`, user `postgres` / password `postgres`, DB `course_reg`.

### 2) Chạy ứng dụng
```bash
mvn spring-boot:run
```
Ứng dụng chạy tại `http://localhost:8080`. Kiểm tra endpoint:
```
GET http://localhost:8080/api/health
```

## Ghi chú
- Flyway sẽ tự động tạo **10 bảng** theo yêu cầu trong `src/main/resources/db/migration/V1__init.sql`.
- Khi thay đổi lược đồ, tạo file mới `V2__*.sql` thay vì sửa trực tiếp V1.
