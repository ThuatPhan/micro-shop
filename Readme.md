# 🛒 MicroShop – Backend for Modern E-commerce

**MicroShop** is the backend system for a modern e-commerce platform, built with a **distributed microservices architecture**. The project is designed with a strong focus on **scalability**, **resilience**, and **maintainability**, supporting the full business flow from product management to order processing and asynchronous notifications.

---
## System Architecture
![System Architecture](https://res.cloudinary.com/dmz26yafu/image/upload/v1754449098/Untitled-2025-08-05-1401_vhmjmq.png)

---
## ✨ Key Features

* **Product Management**
  Provides a complete set of CRUD APIs to manage product data.

* **Shopping Cart**
  Enables users to add, update, or remove items from their shopping cart.

* **Order Processing**
  Handles the entire order creation workflow — from receiving the request to persisting data and triggering notifications.

* **Order Confirmation Emails**
  Automatically sends confirmation emails upon successful order placement, powered by **Resend API**.

* **Centralized Security**
  Uses **OAuth 2.0** and **Auth0** for authentication and authorization at the **API Gateway** layer, ensuring secure access across all microservices.

* **Inter-service Communication**

    * **Synchronous**: Uses **OpenFeign** for declarative REST API calls between services.
    * **Asynchronous**: Utilizes **RabbitMQ** as a message broker for decoupled communication.

* **Centralized API Documentation at Gateway**
  All Swagger documentation from individual services is aggregated and served via the **API Gateway**, allowing developers to browse and test endpoints from a single access point.

---

## 🛠 Tech Stack

* **Framework**: Spring Boot, Spring Cloud
* **Security**: OAuth 2.0, Auth0
* **Service Communication**: OpenFeign (sync), RabbitMQ (async)
* **Email Service**: Resend SDK
* **API Docs**: Swagger (aggregated at API Gateway)
* **Deployment**: Docker & Docker Compose

---

## 🚀 Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/ThuatPhan/micro-shop.git
cd micro-shop
```

### 2. Create `.env` Configuration Files

#### In the `api-gateway` directory, create a `.env` file:

```env
AUTH0_DOMAIN=<your-auth0-domain>
AUTH0_AUDIENCE=<your-auth0-audience>
```

#### In the `notification-service` directory, create a `.env` file:

```env
RESEND_API_KEY=<your-resend-api-key>
EMAIL_DOMAIN=<your-email-domain>
```

### 3. Start the Application with Docker Compose

```bash
docker-compose up -d --build
```

### 4. Access API Documentation

You can view the full, centralized API documentation at:

[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

