# 🏨 StayManager – Hotel Management System

StayManager is a backend application built using **Java 25 and Spring Boot** that provides RESTful APIs for hotel management operations. It integrates secure authentication, PostgreSQL persistence, AWS S3 cloud storage, and serverless image processing using AWS Lambda.

The project also incorporates Docker-based containerization and GitHub Actions CI/CD workflows to streamline application builds and delivery.

## 🚀 Key Features

### 🔐 Authentication & Authorization
- JWT-based authentication and authorization.
- Spring Security integration.
- OAuth2 Resource Server for JWT validation.
- Role-based access control for secured API endpoints.

### 🏨 Hotel Management
- RESTful APIs for hotel management operations.
- Layered architecture separating controllers, services, and repositories.
- Spring Data JPA and Hibernate for database persistence.
- PostgreSQL integration for relational data management.

### ☁️ AWS S3 Integration
- Cloud-based image storage using Amazon S3.
- Upload property images directly to an S3 bucket.
- UUID-based filenames to prevent naming collisions.
- Separation of application data and image storage.

### ⚡ Serverless Image Processing with AWS Lambda
- Event-driven image processing using AWS Lambda.
- Processes images uploaded under the `original/properties/` S3 prefix.
- Automatically resizes images to **800 × 600** dimensions.
- Handles EXIF orientation and transparent image backgrounds.
- Converts supported image formats into optimized JPEG files.
- Stores processed images under the `processed/` prefix.
- Validates file formats and enforces a 15 MB upload-processing limit.

### 🔄 CI/CD with GitHub Actions
- Automated CI/CD workflows using GitHub Actions.
- Automates application build and validation workflows.
- Integrates the build pipeline with the GitHub repository.

### 🐳 Docker & Containerization
- Multi-stage Docker builds to separate application compilation and runtime environments.
- Docker Compose for orchestrating the Spring Boot application and PostgreSQL.
- PostgreSQL data persistence using Docker volumes.
- Container health checks to improve service startup reliability.

## 🛠️ Tech Stack

| Category | Technologies |
|---|---|
| Programming Language | Java 25 |
| Backend Framework | Spring Boot 4 |
| Security | Spring Security, JWT, OAuth2 Resource Server |
| ORM | Spring Data JPA, Hibernate |
| Database | PostgreSQL 16 |
| Cloud Storage | AWS S3 |
| Serverless Computing | AWS Lambda |
| Lambda Runtime | Python, Boto3, Pillow |
| CI/CD | GitHub Actions |
| Containerization | Docker, Docker Compose |
| Build Tool | Apache Maven |
| Libraries | JJWT, Lombok |

## 🏗️ Architecture

StayManager uses a layered backend architecture with AWS services handling cloud storage and asynchronous image processing.

```text
                   Client
                      |
                      v
              Spring Boot API
                      |
              Spring Security
                (JWT Validation)
                      |
                      v
               REST Controllers
                      |
                      v
                Service Layer
                 /          \
                /            \
               v              v
        Repository Layer   AWS S3 Service
               |              |
               v              v
          PostgreSQL      Original Images
                              |
                              v
                        AWS Lambda
                      (Image Resizer)
                              |
                              v
                       Processed Images
                           (S3)
```

### Image Processing Workflow

1. The client uploads a property image through the Spring Boot application.
2. The backend stores the original image in the configured AWS S3 bucket.
3. An S3 object-created event invokes the AWS Lambda function.
4. Lambda retrieves the uploaded image and validates its format and size.
5. The image is resized to 800 × 600 and optimized as JPEG.
6. The processed image is uploaded to the `processed/` prefix in the same S3 bucket.

This approach offloads image processing from the main Spring Boot application and uses serverless execution for image transformation.

## 📂 Project Structure

```text
staymanager/
│
├── .github/
│   └── workflows/               # GitHub Actions CI/CD
│
├── .mvn/
│   └── wrapper/                 # Maven Wrapper
│
├── lambda/
│   └── image_resizer/
│       └── lambda_function.py   # AWS Lambda image processor
│
├── src/
│   ├── main/
│   │   ├── java/                # Spring Boot source code
│   │   └── resources/           # Application configuration
│   │
│   └── test/                    # Automated tests
│
├── Dockerfile
├── docker-compose.yaml
├── pom.xml
├── mvnw
├── mvnw.cmd
└── README.md
```

## ⚙️ Getting Started

### Prerequisites

Ensure you have the following installed:

- Java 25
- Apache Maven 3.9+
- Docker and Docker Compose
- AWS account with an S3 bucket
- AWS credentials with appropriate S3 permissions

### 1. Clone the Repository

```bash
git clone https://github.com/Prasanna-cpu/staymanager.git

cd staymanager
```

### 2. Configure Environment Variables

Create a `.env` file in the project root:

```env
# Application
PORT=4000

# Database
DATABASE_NAME=staymanager
DATABASE_USERNAME_REGULAR=postgres
DATABASE_PASSWORD_REGULAR=your_password
DATABASE_URL_LOCALHOST=jdbc:postgresql://localhost:5432/staymanager

# JWT
JWT_HEADER=Authorization
JWT_SECRET=your_secure_jwt_secret

# AWS S3
AWS_ACCESS_KEY=your_aws_access_key
AWS_SECRET_KEY=your_aws_secret_key
AWS_S3_BUCKET_NAME=your_bucket_name
AWS_REGION=your_aws_region
```

**Important:**
- Never commit `.env` files or expose sensitive credentials.
- Configure AWS credentials securely.
- When running through Docker Compose, ensure the database URL uses the PostgreSQL service hostname (`postgres`) rather than `localhost`.

### 3. Run Locally

Build the application:

```bash
mvn clean install
```

Start the Spring Boot application:

```bash
mvn spring-boot:run
```

### 4. Run with Docker Compose

Build and start the application containers:

```bash
docker compose up --build
```

Stop the containers:

```bash
docker compose down
```

To remove containers and associated database volumes:

```bash
docker compose down -v
```

## ⚡ AWS Lambda Deployment

The image-resizing Lambda function is located in:

```text
lambda/image_resizer/lambda_function.py
```

### Dependencies

The Lambda function uses:

- `boto3` for AWS S3 operations.
- `Pillow` for image processing.

Package the dependencies according to the AWS Lambda Python runtime and deployment requirements.

### S3 Event Configuration

Configure an S3 event notification for object-created events with the following prefix:

```text
original/properties/
```

The Lambda function processes supported image formats:

- JPEG
- JPG
- PNG
- WebP

Processed images are stored under:

```text
processed/properties/
```

Ensure the Lambda execution role has appropriate permissions to read original objects and write processed objects to the S3 bucket.

**Note:** Configure the S3 event notification so that it only triggers for original images, avoiding recursive Lambda invocations when processed images are uploaded.

## 🔄 CI/CD Pipeline

GitHub Actions is used to automate the project's CI/CD workflows.

The workflow definitions are maintained in:

```text
.github/workflows/
```

This provides an automated approach to application integration and delivery, reducing repetitive manual build and validation steps.

## 🧪 Build and Testing

Run automated tests:

```bash
mvn test
```

Build the application:

```bash
mvn clean package
```

## 🔮 Future Improvements

- Integrate Amazon SES for transactional email notifications.
- Introduce Testcontainers for integration testing.
- Implement application monitoring and centralized logging.
- Extend CI/CD workflows with automated cloud deployment.
- Introduce caching for frequently accessed resources.

## 👨‍💻 Author

**Prasanna Kumar M**

- GitHub: [Prasanna-cpu](https://github.com/Prasanna-cpu)
- Repository: [StayManager](https://github.com/Prasanna-cpu/staymanager)

---

⭐ If you find this project interesting, consider giving the repository a star!
