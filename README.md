

=============================== GitHub Clone Backend ====================================

A GitHub-style backend system built with Spring Boot, MongoDB, and JWT authentication.
It allows users to create repositories, upload files, track commits, follow other users, and manage profiles with image uploads using Cloudinary.
The application is deployed on Azure App Service and uses MongoDB Atlas as the cloud database.

******************************************************************************
Live API
https://mygithubclone-hwbue4edhmb2cceh.centralindia-01.azurewebsites.net
******************************************************************************

Features:
Authentication
User registration
Secure login using JWT
Stateless authentication
Protected API routes
User System
User profile management
Upload profile images via Cloudinary
Follow / Unfollow users
Followers & following count
Repository System
Create repositories
Delete repositories
View repository details
Public repository browsing
Search repositories
Code File System
Upload files inside repositories
Update file contents
Delete files
Retrieve file contents
Commit System
Automatic commit created on file changes
Commit history tracking
Snapshot of repository state
Public Repository Access
Browse all public repositories
View repositories by username
Search repositories by name

******************************************************************************
Architecture
                 ┌──────────────┐
                 │   Frontend   │
                 │  (Future UI) │
                 └───────▲──────┘
                         │
                         │ REST API
                         ▼
                ┌──────────────────┐
                │   Spring Boot     │
                │    Backend API    │
                └───────▲──────────┘
                        │
        ┌───────────────┼───────────────┐
        │                               │
        ▼                               ▼
 ┌───────────────┐               ┌───────────────┐
 │ MongoDB Atlas │               │  Cloudinary   │
 │   Database    │               │ Image Storage │
 └───────────────┘               └───────────────┘
******************************************************************************

Tech Stack

==== Backend =====
Java 21

Spring Boot

Spring Security

JWT Authentication

===== Database =====

MongoDB Atlas

Cloud Services

Azure App Service

Cloudinary (image hosting)

Tools

Maven

Postman

Git
******************************************************************************
API Overview

Authentication

POST /auth/register
POST /auth/login

User

GET  /api/users/me
PUT  /api/users/update
POST /api/users/profile-image
POST /api/users/{userId}/follow
DELETE /api/users/{userId}/follow

Repository

POST   /api/repos
GET    /api/repos/me
GET    /api/repos/{repoId}
DELETE /api/repos/{repoId}

Public Repositories

GET /api/repos/public
GET /api/repos/user/{username}
GET /api/repos/search?q=repo

Files

POST   /api/repos/{repoId}/files
PUT    /api/repos/{repoId}/files/{fileId}
DELETE /api/repos/{repoId}/files/{fileId}
GET    /api/repos/{repoId}/files/{fileId}

Commits

GET /api/repos/{repoId}/commits
******************************************************************************

Setup Instructions
1. Clone the repository
git clone https://github.com/YOUR_USERNAME/github-clone-backend.git
cd github-clone-backend
2. Configure Environment Variables

Create application.properties

spring.data.mongodb.uri=YOUR_MONGODB_URI

jwt.secret=YOUR_SECRET_KEY

cloudinary.cloud-name=YOUR_CLOUD_NAME
cloudinary.api-key=YOUR_API_KEY
cloudinary.api-secret=YOUR_API_SECRET
3. Run the application
mvn spring-boot:run

Application will start on:

http://localhost:8081
Deployment

The application is deployed using Azure App Service.

Steps used for deployment

Build the application

mvn clean package

Generate the JAR

target/demo-0.0.1-SNAPSHOT.jar

Upload to Azure App Service

Configure environment variables in Azure

Start the application

Example API Request

Create Repository

POST /api/repos

Request

{
  "name": "spring-blog",
  "description": "Spring Boot blog project",
  "private": false
}
Future Improvements

Repository star system

Repository fork system

Activity feed

Pull request system

Web UI (React frontend)

Syntax highlighting for code viewer

Author

Digvijay Karande

MCA Student — Bharati Vidyapeeth IMED
Backend Developer (Java / Spring Boot)
