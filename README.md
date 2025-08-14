```markdown
# AI Background Remover (Spring Boot + React + Clerk)

Full-stack scaffold for an AI background remover.

- **Backend:** Spring Boot (Java 17), Spring Security, JPA/Hibernate, MySQL  
- **Auth:** Clerk (JWT validation via custom filter)  
- **Frontend:** React + Vite, Clerk React SDK  
- **Monorepo Layout:** `BGremover/` (backend), `client/` (frontend)

> This version provides authentication, user persistence, and a user endpoint to create/update records with an initial **credits** model. Image background removal endpoints are the logical next step.

---

# ✨ Features

- JWT authentication with Clerk
- CORS configured for local dev (Vite @ `http://localhost:5173`)
- MySQL persistence via Spring Data JPA / Hibernate
- User management endpoint: create or update a user (with initial credits)

---

# 🧱 Tech Stack

- Java 17, Spring Boot 3
- Spring Security 6, JJWT
- MySQL 8, Spring Data JPA
- React 19 + Vite, Clerk React SDK

---

# ✅ Prerequisites

- Java **17+**
- Maven **3.9+**
- Node.js **18+**
- MySQL **8+**
- A **Clerk** Dev instance (Publishable Key + instance URL)

---

# 🚀 Backend Setup (Spring Boot)

1) **Configure MySQL and Clerk** in `BGremover/src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/removebgDB
spring.datasource.username=<your_mysql_user>
spring.datasource.password=<your_mysql_password>

spring.jpa.hibernate.ddl-auto=update

# Clerk configuration
clerk.issuer=https://<your-clerk-instance>.clerk.accounts.dev
clerk.jwks-url=https://<your-clerk-instance>.clerk.accounts.dev/.well-known/jwks.json
````

**Notes**

* Create the database `removebgDB` in MySQL (or change the URL to match your DB).
* `spring.jpa.hibernate.ddl-auto=update` lets Hibernate create/update tables in development.
* `clerk.issuer` must **exactly** match the `iss` claim in tokens from your Clerk instance.

2. **Install & run the backend**

```bash
cd BGremover
./mvnw spring-boot:run
```

Backend starts at: `http://localhost:8080`

3. **CORS**

* Allowed origin is `http://localhost:5173` (set in `SecurityConfig`).
* Update/add origins when deploying the frontend elsewhere.

---

## 💻 Frontend Setup (React + Vite)

1. **Environment variables**

Create `client/.env`:

```env
VITE_CLERK_PUBLISHABLE_KEY=pk_test_xxx...
```

2. **Install & run**

```bash
cd client
npm install
npm run dev
```

* Frontend runs at: `http://localhost:5173`

---

## 🔐 Authentication Flow

* Frontend obtains a Clerk **session token** using the Clerk React SDK.
* Include the token on backend requests:

  * Header: `Authorization: Bearer <token>`
* Backend validates the JWT using a **custom filter** (`ClerkJwtAuthFilter`) and **Clerk JWKS**.

---

## 🧪 API

**Base URL:** `http://localhost:8080`

### POST `/api/users` (Authenticated)

Creates or updates the user in the database; seeds initial credits if not present.

**Headers**

```
Authorization: Bearer <your_clerk_jwt>
Content-Type: application/json
```

**Request Body**

```json
{
  "clerkId": "user_123",
  "email": "jane.doe@example.com",
  "firstName": "Jane",
  "lastName": "Doe",
  "photoUrl": "https://.../photo.jpg",
  "credits": 10
}
```

**Response (wrapped)**

```json
{
  "success": true,
  "statusCode": "CREATED",
  "data": {
    "clerkId": "user_123",
    "email": "jane.doe@example.com",
    "firstName": "Jane",
    "lastName": "Doe",
    "photoUrl": "https://.../photo.jpg",
    "credits": 10
  }
}
```

**cURL**

```bash
TOKEN=<your_clerk_jwt>
curl -i -X POST http://localhost:8080/api/users \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "clerkId": "user_123",
    "email": "jane.doe@example.com",
    "firstName": "Jane",
    "lastName": "Doe",
    "photoUrl": "https://.../photo.jpg",
    "credits": 10
  }'
```

---

## 🛡️ Security Notes

* All backend endpoints require authentication by default.
  To open public endpoints, update the Spring Security config.
* CORS is enabled and allows the Vite dev server origin for local dev.

---

## 🧰 Troubleshooting

**403 Forbidden (preflight)**

* Ensure security config permits `OPTIONS` requests and the auth filter skips preflight.

**403 Forbidden (Invalid JWT token)**

* Frontend must send `Authorization: Bearer <token>`.
* Confirm `clerk.issuer` equals your instance issuer (e.g., `https://<subdomain>.clerk.accounts.dev`).
* Ensure JWKS verification checks **RS256**.

**500 / DB errors**

* Check MySQL credentials and DB existence.
* For unique constraint violations (email/clerkId), ensure your service handles **update vs create**.

**CORS errors in browser**

* Confirm CORS setup allows `http://localhost:5173` and includes headers like `Authorization` and `Content-Type`.

---

## 📦 Build & Run (Production)

**Backend**

```bash
cd BGremover
./mvnw clean package
java -jar target/BGremover-0.0.1-SNAPSHOT.jar
```

**Frontend**

```bash
cd client
npm run build
```

* Serve the `client/dist` folder with your static host (e.g., Nginx, Vercel, Netlify).
* Update backend CORS allowed origins to your production URL(s).

---

## 🗺️ Roadmap

* Add background removal endpoints (e.g., third-party AI API or local model).
* Track & decrement credits when processing images.
* Add usage history and image storage.

---

## 🧩 Scripts (Quick Reference)

**Backend**

```bash
# from repo root or BGremover/
cd BGremover
./mvnw spring-boot:run
./mvnw test
./mvnw clean package
```

**Frontend**

```bash
# from repo root or client/
cd client
npm install
npm run dev
npm run build
npm run preview
```
