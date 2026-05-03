# CodeMarketplace 🚀

> **A Full-Stack Developer Marketplace** — Buy and sell ready-to-use code projects with secure payments, reviews, and role-based access.

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-v4.0.1-brightgreen?logo=spring-boot)](https://spring.io/projects/spring-boot)
[![Angular](https://img.shields.io/badge/Angular-v16.2-red?logo=angular)](https://angular.io/)
[![Java](https://img.shields.io/badge/Java-17-orange?logo=java)](https://www.oracle.com/java/)
[![MySQL](https://img.shields.io/badge/MySQL-Database-blue?logo=mysql)](https://www.mysql.com/)

## 📖 Overview

**CodeMarketplace** is a full-stack **developer marketplace platform** enabling developers to **buy and sell ready-to-use code projects**. With role-based access control, secure JWT authentication, and an intuitive user interface, CodeMarketplace streamlines the process of sharing and monetizing coding projects.

### 🎯 Perfect For:
- **Developers** looking to monetize their code projects
- **Businesses** seeking production-ready code solutions  
- **Teams** collaborating on code sharing platforms

---

## ✨ Key Features

### 🔐 **Authentication & Security**
- JWT-based authentication with Spring Security
- Role-based access control (Buyer, Seller, Admin, Super Admin)
- Secure password encryption
- Refresh token mechanism for extended sessions

### 💳 **Marketplace Core**
- Browse and search code projects by category and programming language
- Detailed project pages with full documentation
- Project listing with pagination and filtering
- Category and language management system

### 👤 **User Dashboards**
- **Seller Dashboard**: Manage projects, track sales, monitor analytics
- **Buyer Dashboard**: Purchase history, order management, downloads
- **Admin Panel**: User management, content moderation
- **Super Admin Dashboard**: Platform analytics and system administration

### ⭐ **Engagement Features**
- Rating and review system for purchased projects
- Wishlist functionality for saving projects
- View count tracking for analytics
- Real-time project status updates

### 📥 **File Management**
- Secure project file uploads and downloads
- Multi-file project support
- Streaming downloads for large files

### 📊 **Analytics & Management**
- Dashboard statistics and insights
- Seller revenue tracking
- Purchase history and analytics

---

## 🛠 Tech Stack

| Layer | Technology | Version |
|-------|-----------|---------|
| **Backend** | Spring Boot | 4.0.1 |
| **Language** | Java | 17 |
| **Frontend** | Angular | 16.2 |
| **Frontend Language** | TypeScript | 5.1.3 |
| **Styling** | Bootstrap | 5.3.8 |
| **Database** | MySQL | Latest |
| **Security** | JWT + Spring Security | JJWT 0.11.5 |
| **Build Tool** | Maven | 3.x |

---

## 🏗 Project Structure

```
code-marketplace/
├── frontend/                                 # Angular Frontend (TypeScript)
│   ├── src/
│   │   ├── app/
│   │   │   ├── core/                        # Core Services & Guards
│   │   │   │   ├── guards/                  # Route guards
│   │   │   │   ├── interceptor/             # HTTP interceptors
│   │   │   │   ├── model/                   # Data models
│   │   │   │   ├── services/                # Core services
│   │   │   │   └── utils/                   # Utility functions
│   │   │   ├── features/                    # Feature modules
│   │   │   │   ├── auth/                    # Authentication module
│   │   │   │   ├── buyer/                   # Buyer features
│   │   │   │   ├── seller/                  # Seller features
│   │   │   │   ├── home/                    # Home page
│   │   │   │   ├── project-details/         # Project details
│   │   │   │   └── super-admin/             # Admin features
│   │   │   ├── shared/                      # Shared components
│   │   │   │   ├── footer/                  # Footer component
│   │   │   │   ├── module/                  # Shared modules
│   │   │   │   ├── nav/                     # Navigation component
│   │   │   │   ├── review-modal/            # Review modal
│   │   │   │   ├── seller-head/             # Seller header
│   │   │   │   └── under-maintenance/       # Maintenance page
│   │   │   ├── app.component.ts             # Root component
│   │   │   ├── app.module.ts                # Root module
│   │   │   └── app-routing.module.ts        # Routing config
│   │   ├── assets/                          # Static assets
│   │   ├── environments/                    # Environment configs
│   │   │   ├── environment.prod.ts          # Production
│   │   │   └── environment.ts               # Development
│   │   └── styles/                          # Global styles
│   ├── angular.json                         # Angular CLI config
│   ├── package.json                         # Dependencies
│   └── tsconfig.json                        # TypeScript config
│
├── backend/                                 # Spring Boot Backend (Java 17)
│   ├── src/main/java/com/example/projecthub/
│   │   ├── controller/                      # REST API Controllers
│   │   │   ├── PublicController.java        # Public endpoints (auth, browse)
│   │   │   ├── SellerController.java        # Seller operations
│   │   │   ├── PurchaseController.java      # Buyer & purchase endpoints
│   │   │   ├── SuperAdminController.java    # Admin management
│   │   │   ├── CategoryController.java      # Category management
│   │   │   └── LanguageController.java      # Language management
│   │   ├── service/                         # Business Logic
│   │   │   ├── admin/                       # Admin services
│   │   │   ├── auth/                        # Authentication
│   │   │   ├── download/                    # File downloads
│   │   │   ├── language/                    # Language management
│   │   │   ├── project/                     # Project management
│   │   │   ├── purchase/                    # Purchase handling
│   │   │   ├── review/                      # Reviews
│   │   │   ├── seller/                      # Seller operations
│   │   │   └── wishlist/                    # Wishlist
│   │   ├── dto/                             # Data Transfer Objects
│   │   ├── exception/                       # Custom exceptions
│   │   ├── helper/                          # Helper classes
│   │   ├── jwt/                             # JWT configuration
│   │   ├── model/                           # JPA Entity Models
│   │   ├── repository/                      # Data Access Layer
│   │   ├── security/                        # Security configuration
│   │   └── ProjecthubApplication.java       # Application entry point
│   ├── src/main/resources/
│   │   ├── application.properties           # Configuration
│   │   └── application-prod.properties      # Production config
│   ├── src/test/                            # Unit tests
│   ├── pom.xml                              # Maven dependencies
│   └── .mvn/                                # Maven wrapper
│
└── README.md                                # This file
```

---

## 🚀 Quick Start

### Prerequisites
- **Java 17** or higher
- **Node.js 16+** and **npm**
- **MySQL 8.0+**
- **Git**

### 1️⃣ Clone Repository
```bash
git clone https://github.com/JivanDevkota/code-marketplace.git
cd code-marketplace
```

### 2️⃣ Backend Setup (Spring Boot)

```bash
cd backend

# Configure database in: src/main/resources/application.properties
# Update these properties:
# spring.datasource.url=jdbc:mysql://localhost:3306/codemarket
# spring.datasource.username=root
# spring.datasource.password=your_password

# Run the application
./mvnw spring-boot:run

# Or build & run
./mvnw clean package
java -jar target/projecthub-0.0.1-SNAPSHOT.jar
```

**Backend runs on**: `http://localhost:8080`

### 3️⃣ Frontend Setup (Angular)

```bash
cd frontend

# Install dependencies
npm install

# Start development server
npm start
# or
ng serve
```

**Frontend runs on**: `http://localhost:4200`

### 4️⃣ Access Application
- **Web**: http://localhost:4200
- **API**: http://localhost:8080/api

---

## 📡 API Endpoints

### 🔓 **Public Endpoints** (`/api`)

#### Authentication
| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/register` | Register new user account |
| `POST` | `/login` | Login user (returns JWT tokens) |
| `POST` | `/refresh` | Refresh access token |

#### Browse Projects
| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/languages` | Get all programming languages |
| `GET` | `/categories` | Get all project categories |
| `GET` | `/projects` | Get all public projects (paginated) |
| `GET` | `/projects/{id}` | Get detailed project information |
| `GET` | `/language/{languageId}/top` | Get top projects by language |

---

### 🛍️ **Buyer Endpoints** (`/api/buyer`)

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/{buyerId}/purchases` | Purchase a project |
| `GET` | `/{buyerId}/my-purchases` | Get purchase history |
| `POST` | `/reviews/project/{projectId}/user/{buyerId}` | Submit project review |
| `GET` | `/reviews/project/{userId}` | Get user's reviews |
| `GET` | `/{buyerId}/download/{purchaseId}` | Download project files |

---

### 🎨 **Seller Endpoints** (`/api/seller`)

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/create/project` | Create new project listing |
| `POST` | `/project/add-file` | Add files to project |
| `GET` | `/my-projects` | Get seller's projects |
| `PUT` | `/project/{projectId}` | Update project details |
| `DELETE` | `/project/{projectId}` | Delete project |
| `GET` | `/dashboard` | Seller dashboard stats |

---

### 🔧 **Admin Endpoints** (`/api/admin`)

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/add/category` | Create category |
| `GET` | `/recent/categories` | Get recent categories |
| `POST` | `/create/language` | Create programming language |
| `GET` | `/languages` | Get all languages |
| `PATCH` | `/{userId}/status` | Update user status |
| `GET` | `/recent/users` | Get recently joined users |
| `PUT` | `/project/{projectId}/approve` | Approve pending project |
| `PUT` | `/project/{projectId}/suspend` | Suspend project |
| `GET` | `/dashboard` | Admin dashboard stats |

---

## 🔐 User Roles

| Role | Capabilities |
|------|--------------|
| **Buyer** | Browse, purchase, review, manage wishlist |
| **Seller** | Upload projects, manage listings, track sales |
| **Admin** | Manage content, approve projects, moderate users |
| **Super Admin** | Full platform control, system administration |

---

## 🛠 Development Commands

### Backend
```bash
cd backend

# Build
./mvnw clean package

# Run
./mvnw spring-boot:run

# Run tests
./mvnw test
```

### Frontend
```bash
cd frontend

# Install dependencies
npm install

# Development server
npm start

# Build
npm run build

# Run tests
npm test
```

---

## 📝 Environment Configuration

### Frontend (`frontend/environments/`)
- `environment.ts` - Development
- `environment.prod.ts` - Production

### Backend (`backend/src/main/resources/`)
- `application.properties` - Development
- `application-prod.properties` - Production

---

## 🤝 Contributing

Contributions are welcome! Please feel free to submit pull requests or open issues.

---

## 📧 Contact & Support

For questions or support, please open an issue on the repository.

---

<div align="center">

Made with ❤️ by [JivanDevkota](https://github.com/JivanDevkota)

</div>
