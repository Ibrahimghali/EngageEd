# EngageEd

## Overview  
**EngageEd** is a robust educational platform built with Spring Boot, designed to seamlessly connect students, professors, and department chiefs within a unified digital learning environment. It supports the management of educational content, subject organization, and file sharing through a secure, role-based system.


## Architecture  
The application follows a typical Spring Boot project structure:



    com.EngageEd.EngageEd
    ├── controllers    # REST API endpoints
    ├── services       # Business logic interfaces
    ├── serviceImpl    # Implementations of service interfaces
    ├── repositories   # Data access layer (JPA)
    ├── models         # Domain entities
    ├── dto            # Data transfer objects
    ├── config         # Configuration classes
    ├── security       # Authentication and authorization logic
    ├── exception      # Custom exception handling
    └── utils          # Utility classes


## Key Features  
- **User Role Hierarchy**: Supports three user roles (Students, Professors, Department Chiefs) with inheritance-based design  
- **Authentication**: Integrated with Firebase and uses custom JWT token management  
- **Subject Management**: Create and organize subjects with auto-generated unique codes  
- **Material Management**: Upload, categorize, and retrieve educational resources  
- **File Handling**: Secure file upload and download operations  
- **Search Functionality**: Full-text search across educational content  
- **Pagination**: Efficient data retrieval using pagination  


## API Endpoints  
- Authentication: `/api/auth/*`  
- Department Chiefs: `/api/department-chiefs/*`  
- Files: `/api/files/*`  
- Materials: `/api/materials/*`  
- Students: `/api/students/*`  
- Subjects: `/api/subjects/*`  
- Professors: `/api/professors/*`  


## Setup & Installation  

1. **Clone the repository**  
   ```bash
   git clone https://github.com/Ibrahimghali/EngageEd.git
   cd EngageEd


2. **Configure application properties**
   Update the `src/main/resources/application.properties` file with your:

   * Database settings
   * File storage paths
   * Firebase credentials

3. **Run the application using Maven**

   ```bash
   ./mvnw spring-boot:run
   ```

---

## Security

The platform implements several security measures:

* Firebase-based user authentication
* Role-based access control (RBAC)
* JWT token validation
* CORS configuration for frontend integration

---

## Technologies Used

* **Framework**: Spring Boot
* **Security**: Spring Security + Firebase Auth
* **Database Access**: JPA / Hibernate
* **API Documentation**: Swagger / OpenAPI
* **Build Tool**: Maven

---

## Contributing

1. Fork the repository
2. Create a new feature branch:

   ```bash
   git checkout -b feature/amazing-feature
   ```
3. Commit your changes:

   ```bash
   git commit -m "Add some amazing feature"
   ```
4. Push to your branch:

   ```bash
   git push origin feature/amazing-feature
   ```
5. Open a pull request

---



