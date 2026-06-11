# 🛍️ ShopZone — E-Commerce Microservices Platform

A production-ready, full-stack e-commerce application built with **Java Spring Boot Microservices**, **Spring Cloud**, **JWT Security**, and a premium **HTML/CSS/JS frontend**.

![Java](https://img.shields.io/badge/Java-17-orange?style=flat-square&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.5-green?style=flat-square&logo=springboot)
![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2023.0.1-green?style=flat-square)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue?style=flat-square&logo=mysql)
![JWT](https://img.shields.io/badge/JWT-Security-red?style=flat-square)
![License](https://img.shields.io/badge/License-MIT-yellow?style=flat-square)

---

## 🏗️ Architecture

```
Client (Browser)
      ↓
API Gateway :8080  ←── JWT Validation + Routing
      ↓
Eureka Server :8761  ←── Service Discovery
      ↓
┌─────────────────────────────────────────────────┐
│  User      Product   Order    Payment  Inventory │
│  :8081     :8082     :8083    :8084    :8085     │
└─────────────────────────────────────────────────┘
      ↓
MySQL Databases (one per service)
```

---

## 🚀 Microservices

| Service | Port | Description |
|---------|------|-------------|
| Eureka Server | 8761 | Service Discovery & Registry |
| API Gateway | 8080 | Single Entry Point + JWT Auth |
| User Service | 8081 | Registration, Login, JWT Tokens |
| Product Service | 8082 | Product Catalogue CRUD |
| Order Service | 8083 | Order Management + Feign Clients |
| Payment Service | 8084 | Payment Processing |
| Inventory Service | 8085 | Stock Management |

---

## 💻 Tech Stack

**Backend**
- Java 17
- Spring Boot 3.2.5
- Spring Cloud 2023.0.1 (Eureka, Gateway)
- Spring Security + JWT (jjwt 0.11.5)
- Spring Data JPA + Hibernate
- OpenFeign (inter-service communication)
- MySQL 8.0
- Maven

**Frontend**
- HTML5 + CSS3 + JavaScript (ES6+)
- Apple-style premium UI design
- Dark / Light mode
- Fully responsive

**DevOps**
- Docker + Dockerfile per service
- Git + GitHub

---

## ⚙️ Prerequisites

- Java 17+
- Maven 3.8+
- MySQL 8.0
- Git

---

## 🛠️ Setup & Run

### 1. Clone the repository
```bash
git clone https://github.com/Dhxn-12/E-commerce-microservices.git
cd E-commerce-microservices
```

### 2. Create MySQL databases
```sql
CREATE DATABASE ecommerce_users;
CREATE DATABASE ecommerce_products;
CREATE DATABASE ecommerce_orders;
CREATE DATABASE ecommerce_payments;
CREATE DATABASE ecommerce_inventory;
```

### 3. Update MySQL password
In each service's `src/main/resources/application.yml`:
```yaml
spring:
  datasource:
    password: YOUR_MYSQL_PASSWORD
```

### 4. Start services in order
```bash
# Terminal 1
cd eureka-server && mvn package -DskipTests && java -Xms64m -Xmx128m -jar target/eureka-server-1.0.0.jar

# Terminal 2
cd api-gateway && mvn package -DskipTests && java -Xms64m -Xmx128m -jar target/api-gateway-1.0.0.jar

# Terminal 3
cd user-service && mvn package -DskipTests && java -Xms64m -Xmx128m -jar target/user-service-1.0.0.jar

# Terminal 4
cd product-service && mvn package -DskipTests && java -Xms64m -Xmx128m -jar target/product-service-1.0.0.jar

# Terminal 5
cd inventory-service && mvn package -DskipTests && java -Xms64m -Xmx128m -jar target/inventory-service-1.0.0.jar

# Terminal 6
cd payment-service && mvn package -DskipTests && java -Xms64m -Xmx128m -jar target/payment-service-1.0.0.jar

# Terminal 7
cd order-service && mvn package -DskipTests && java -Xms64m -Xmx128m -jar target/order-service-1.0.0.jar
```

### 5. Open the frontend
Open `shopzone-v2/index.html` in your browser.

---

## 🔐 API Endpoints

### Auth (Public)
```
POST /api/users/auth/register   — Register new user
POST /api/users/auth/login      — Login + get JWT token
```

### Products (JWT Required)
```
GET    /api/products            — Get all products
GET    /api/products/{id}       — Get product by ID
POST   /api/products            — Add product (ADMIN)
PUT    /api/products/{id}       — Update product (ADMIN)
DELETE /api/products/{id}       — Delete product (ADMIN)
```

### Orders (JWT Required)
```
POST   /api/orders              — Place order
GET    /api/orders/user/{id}    — Get user orders
GET    /api/orders              — Get all orders (ADMIN)
```

### Inventory (JWT Required)
```
GET    /api/inventory                        — All inventory
GET    /api/inventory/check/{id}?quantity=5  — Check stock
POST   /api/inventory                        — Add stock (ADMIN)
PUT    /api/inventory/product/{id}?quantity  — Update stock
```

---

## 🌐 Monitoring

- Eureka Dashboard: http://localhost:8761
- API Gateway Health: http://localhost:8080/actuator/health

---

## 👨‍💼 Admin Access

To make a user admin, run in MySQL:
```sql
UPDATE ecommerce_users.users SET role='ADMIN' WHERE email='your@email.com';
```

Then login → Admin panel appears in navbar automatically.

---

## 📁 Project Structure

```
E-commerce-microservices/
├── eureka-server/          Service Discovery
├── api-gateway/            Gateway + JWT Filter
├── user-service/           Auth + Users
├── product-service/        Product Catalogue
├── order-service/          Orders + Feign
├── payment-service/        Payments
├── inventory-service/      Stock Management
└── shopzone-v2/            Frontend
    ├── index.html          Login/Register
    ├── home.html           Homepage
    └── pages/
        ├── products.html   Product Listing
        ├── product-detail  Product Detail
        ├── cart.html       Shopping Cart
        ├── orders.html     My Orders
        ├── profile.html    User Profile
        └── admin.html      Admin Dashboard
```

---

## 🤝 Contributing

Pull requests are welcome!

---

## 📄 License

MIT License — free to use for portfolio and learning.

---

⭐ **If this project helped you, please give it a star!**
