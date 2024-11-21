# Java Web Application for Database CRUD Operations

This project is a Java Web Application that connects to a PostgreSQL database and provides a web-based interface for performing CRUD (Create, Read, Update, Delete) operations. Users can interact with the application through a user-friendly interface hosted at [http://localhost:8080/](http://localhost:8080/).

## Project Overview

### Key Features:
- **Database Integration**: Connects to a PostgreSQL database for data storage and manipulation.
- **CRUD Operations**: Supports adding, reading, updating, and deleting records.
- **Web Interface**: A simple and intuitive UI to interact with the database.
- **Data Validation**: Includes input validation and protection against SQL injection attacks.

### Project Structure:
The application consists of the following essential files:

1. **`Laba3restApplication`**:  
   - Entry point of the project.
   - Connects the components of the application.
   - Configures and launches the web server.

2. **`Controller`**:  
   - Contains all the core logic for handling CRUD operations.
   - Ensures data security and performs validation to prevent SQL injection.

3. **`index.html`**:  
   - Frontend interface for the web application.
   - Provides a form for user input, a submit button for sending queries, and displays results dynamically.
   - Styled with CSS for a polished user experience.

4. **`application.yml`**:  
   - Configures application settings.
   - Includes database connection details and server configurations for `http://localhost:8080/`.

5. **`pom.xml`**:  
   - Maven configuration file.
   - Defines project dependencies, plugins, properties, and licenses.

---

## Getting Started

### Prerequisites:
- Java Development Kit (JDK) 17 or later.
- PostgreSQL database.
- Apache Maven for dependency management.

### Setup Instructions:
1. Clone the repository.
2. Set up a PostgreSQL database and update the connection details in `application.yml`.
3. Build the project using Maven:
   ```bash
   mvn clean install
4. Run the application:
```bash
java -jar target/laba3restapplication.jar
```
5. Open http://localhost:8080/ in your browser.

# Database Configuration

The application is designed to interact with a PostgreSQL database. Ensure that your database contains the required tables. The following example fields are expected:

- `country`: `id`, `name'`, `priority`.
- Additional tables can be added as needed.

# Usage

1. Launch the application.
2. Access the web interface via http://localhost:8080/.
3. Use the input form to perform CRUD operations:
- Add, update, or delete records.
- Query and display information dynamically.

# Security Measures

- Implements SQL injection protection.
- Includes robust data validation mechanisms.

# Technologies Used

- Java Spring Boot: Backend framework.
- HTML, CSS: Frontend design.
- PostgreSQL: Database.
- Maven: Build automation.

# License

This project is licensed under the MIT License.
