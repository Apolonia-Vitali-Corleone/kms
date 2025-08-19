# 知识管理系统 KMS

## 运行后端
```bash
cd bend
mvn -q spring-boot:run
```
应用运行在 `http://localhost:19988/api/v1`，Swagger UI `/swagger-ui.html`。

## 运行前端
```bash
cd fend
npm i
npm run serve
```
默认访问 `http://localhost:8080`。
前端会请求运行在 `http://localhost:19988` 的后端服务。

## 初始化数据库
将 `bend/src/main/resources/db/schema.sql` 在 MySQL 中执行以创建并导入样例数据。

## 数据库导出
```bash
./scripts/export_db.sh
```
