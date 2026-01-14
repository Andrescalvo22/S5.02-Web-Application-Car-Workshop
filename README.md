# 🛠️ Workshop Management System (Absolut Workshop Barcelona)

Full-stack workshop management system with **Spring Boot + MySQL backend** and **Next.js frontend**.  
Includes **JWT Authentication** and **Role-based Authorization (USER / ADMIN)**.

---

## 🚀 Tech Stack

**Backend**
- Java 21, Spring Boot 3.5.7 , Spring Security (JWT)
- JPA/Hibernate, MySQL
- Lombok, Jakarta Validation
- Swagger OpenAPI

**Frontend**
- Next.js (App Router), TypeScript
- TailwindCSS, shadcn/ui
- Axios

---

## ✨ Features

### Auth & Security
- Register / Login
- JWT Token authentication
- Roles: `ROLE_USER` / `ROLE_ADMIN`
- Protected routes with Spring Security + `@PreAuthorize`

### Client (USER)
- Register cars
- Request repairs
- Track repair order status

### Admin (ADMIN)
- Admin Dashboard
- Manage users, cars, repair orders
- Update repair status: `PENDING`, `IN_PROGRESS`, `READY_FOR_PICKUP`, `CLOSED`
- Add mechanic notes per repair order

---

## 🌐 Main API Endpoints

**Auth**
- `POST /api/auth/register`
- `POST /api/auth/login`

**Cars**
- `GET /api/cars/my` (USER/ADMIN)
- `POST /api/cars` (USER)
- `GET /api/cars` (ADMIN)

**Repair Orders**
- `POST /api/repair-orders/car/{carId}` (USER/ADMIN)
- `GET /api/repair-orders/my` (USER/ADMIN)
- `GET /api/repair-orders` (ADMIN)
- `GET /api/repair-orders/{id}/details` (ADMIN)

**Notes**
- `GET /api/repair-orders/{id}/notes` (USER/ADMIN)
- `POST /api/repair-orders/{id}/notes` (ADMIN)

---

## ⚙️ Running the Project

### Backend
Set DB config in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/taller_db
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
```
Run:
```bash
mvn spring-boot:run
```
Frontend:
```bash
npm install
npm run dev
```
---

## 👨‍💻 Author

Andres Calvo
