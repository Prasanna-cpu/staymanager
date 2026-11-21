# 🏨 StayManager – Hotel Management System

StayManager is a backend hotel management application built using **Java 25 and Spring Boot 4**, providing RESTful APIs with secure authentication, PostgreSQL persistence, and AWS cloud integrations.

The application incorporates **JWT authentication, Spring Security, AWS S3, AWS Lambda, Docker, Kubernetes, and GitHub Actions CI/CD** to demonstrate modern backend development and cloud-native deployment practices.

## 🚀 Key Features

### 🔐 Authentication & Authorization
- JWT-based authentication and authorization.
- Spring Security integration.
- OAuth2 Resource Server for JWT validation.
- Role-based access control for secured API endpoints.
- Centralized security configuration.

### 🏨 Hotel Management
- RESTful APIs for hotel management operations.
- Layered architecture separating controllers, services, and repositories.
- Spring Data JPA and Hibernate for database persistence.
- PostgreSQL integration for relational data management.
- Request validation using Spring Boot Validation.

### ☁️ AWS S3 Integration
- Cloud-based image storage using Amazon S3.
- Upload property images directly to an S3 bucket.
- UUID-based filenames to prevent naming collisions.
- Separation of application data and image storage.

### ⚡ Serverless Image Processing with AWS Lambda
- Event-driven image processing using AWS Lambda.
- Processes images uploaded under the `original/properties/` S3 prefix.
- Automatically resizes images to 800 × 600 dimensions.
- Handles EXIF orientation and transparent image backgrounds.
- Converts supported image formats into optimized JPEG files.
- Stores processed images under the `processed/` prefix.
- Validates file formats and enforces a 15 MB processing limit.

### 🔄 CI/CD with GitHub Actions
- Automated CI/CD workflows using GitHub Actions.
- Automated application build and validation workflows.
- Integration with the GitHub repository.
- Repeatable build processes using Maven.

### 🐳 Docker & Containerization
- Multi-stage Docker builds to separate application compilation and runtime environments.
- Docker Compose for orchestrating the Spring Boot application and PostgreSQL.
- PostgreSQL data persistence using Docker volumes.
- Container health checks to improve service startup reliability.

### ☸️ Kubernetes Deployment
- Kubernetes deployment configuration maintained in a dedicated `kubernetes/` directory.
- Kubernetes-based application deployment configuration.
- Container orchestration for running the application in a Kubernetes environment.
- Kubernetes command-line management using kubectl.

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
| Container Orchestration | Kubernetes |
| Build Tool | Apache Maven |
| Libraries | JJWT, Lombok |
| Validation | Spring Boot Validation |

## 🏗️ System Architecture

StayManager follows a layered backend architecture with AWS services handling cloud storage and serverless image processing.

```text
                       Client
                          |
                          v
                  Spring Boot API
                          |
                          v
                  Spring Security
                    JWT Validation
                          |
                          v
                   REST Controllers
                          |
                          v
                    Service Layer
                     /         \
                    /           \
                   v             v
          Repository Layer    AWS S3 Service
                   |               |
                   v               v
              PostgreSQL      Original Images
                                   |
                                   v
                              AWS Lambda
                            Image Resizer
                                   |
                                   v
                            Processed Images
                                   |
                                   v
                              AWS S3
```

### Infrastructure Architecture

```text
                  Developer
                      |
                      v
                 GitHub Repo
                      |
                      v
               GitHub Actions
                 CI Pipeline
                      |
                      v
                Maven Build
                      |
                      v
                 Docker Image
                      |
                      v
                  Kubernetes
                  Deployment
                      |
                +-----+-----+
                |           |
                v           v
          Spring Boot    PostgreSQL
          Application     Database
                |
                v
              AWS S3
                |
                v
            AWS Lambda
          Image Processing
```

*The infrastructure diagram represents the deployment workflow and logical components; actual connectivity depends on the Kubernetes and AWS configuration.*

## 🖼️ AWS Lambda Image Processing Workflow

1. The client uploads a property image through the Spring Boot application.
2. The backend stores the original image in the configured AWS S3 bucket.
3. An S3 object-created event invokes the AWS Lambda function.
4. Lambda retrieves the uploaded image and validates its format and size.
5. The image is resized to 800 × 600 and optimized as JPEG.
6. The processed image is uploaded to the `processed/` prefix in the same S3 bucket.

This approach offloads image processing from the main Spring Boot application and uses event-driven serverless execution for image transformation.

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
├── kubernetes/                  # Kubernetes manifests
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
- kubectl
- A running Kubernetes cluster for Kubernetes deployment
- AWS account with an S3 bucket
- AWS credentials with appropriate permissions

### 1. Clone the Repository

```bash
git clone https://github.com/Prasanna-cpu/staymanager.git

cd staymanager
```

### 2. Configure Environment Variables

Create a `.env` file in the project root.

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
- Use secure AWS credential management.
- When running through Docker Compose, use the PostgreSQL service hostname instead of `localhost` in the database connection URL.
- For Kubernetes, configure sensitive values using Kubernetes Secrets rather than committing credentials into manifest files.

### 3. Run Locally

Build the application:

```bash
./mvnw clean install
```

On Windows:

```powershell
.\mvnw.cmd clean install
```

Start the Spring Boot application:

```bash
./mvnw spring-boot:run
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

## ☸️ Kubernetes Deployment

StayManager includes Kubernetes deployment configurations in:

```text
kubernetes/
```

### Prerequisites

- A running Kubernetes cluster.
- kubectl configured to communicate with the cluster.
- A built application container image accessible to the cluster.
- Required application configuration and database connectivity.

### 1. Build the Docker Image

```bash
docker build -t staymanager:latest .
```

For a remote Kubernetes cluster, push the image to a container registry accessible by the cluster and update the image reference in the deployment manifest.

### 2. Verify Cluster Connectivity

```bash
kubectl cluster-info
```

Check available nodes:

```bash
kubectl get nodes
```

### 3. Apply Kubernetes Configurations

Apply the manifests:

```bash
kubectl apply -f kubernetes/
```

### 4. Verify Deployment

Check Kubernetes resources:

```bash
kubectl get all
```

Check pods:

```bash
kubectl get pods
```

Check services:

```bash
kubectl get services
```

Inspect deployment status:

```bash
kubectl get deployments
```

### 5. Inspect Application Logs

```bash
kubectl logs deployment/<deployment-name>
```

For interactive container debugging:

```bash
kubectl exec -it <pod-name> -- sh
```

### 6. Delete Kubernetes Resources

```bash
kubectl delete -f kubernetes/
```

**Note:** Ensure the image reference, environment configuration, database service connectivity, and application secrets match your Kubernetes manifests before deployment. The commands above assume the manifests are valid YAML resources organized under `kubernetes/`.

## ⚡ AWS Lambda Deployment

The image-resizing Lambda function is located in:

```text
lambda/image_resizer/lambda_function.py
```

### Dependencies

- Boto3 for AWS S3 operations.
- Pillow for image processing.

Package the dependencies according to the AWS Lambda Python runtime and deployment requirements.

### S3 Event Configuration

Configure an S3 event notification for object-created events with the following prefix:

```text
original/properties/
```

Supported image formats:

- JPEG
- JPG
- PNG
- WebP

Processed images are stored under:

```text
processed/properties/
```

Ensure the Lambda execution role has appropriate permissions to read original objects and write processed objects to the S3 bucket.

Configure event notifications to avoid triggering the Lambda function recursively when processed images are uploaded.

## 🔄 CI/CD Pipeline

GitHub Actions workflows are maintained in:

```text
.github/workflows/
```

The CI/CD implementation automates application build and validation workflows.

This provides a repeatable development workflow and helps identify build issues before changes are integrated.

## 🧪 Build and Testing

Run automated tests:

```bash
./mvnw test
```

Build the application:

```bash
./mvnw clean package
```

## 🔐 Security Considerations

- JWT-based authentication and authorization.
- OAuth2 Resource Server for JWT validation.
- Role-based API access control.
- Secure environment-based configuration.
- AWS IAM permissions for S3 and Lambda.
- Kubernetes Secrets for sensitive configuration.
- Avoid exposing database credentials in container images or source control.

## 🔮 Future Improvements

- Integrate Amazon SES for transactional email notifications.
- Introduce Testcontainers for integration testing.
- Implement centralized logging and application monitoring.
- Extend CI/CD workflows with automated Kubernetes deployments.
- Introduce Redis caching for frequently accessed resources.
- Add Kubernetes resource limits, autoscaling, and readiness/liveness probes where appropriate.

## 👨‍💻 Author

**Prasanna Kumar M**

- GitHub: [Prasanna-cpu](https://github.com/Prasanna-cpu)
- Repository: [StayManager](https://github.com/Prasanna-cpu/staymanager)

---

⭐ If you find this project interesting, consider giving the repository a star!
