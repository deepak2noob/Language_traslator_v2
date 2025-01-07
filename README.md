

---

# 🌐 Language Translator Application



<div align="center">

![Spring Boot](https://img.shields.io/badge/Spring_Boot-2.7.0-green.svg)
![Java](https://img.shields.io/badge/Java-17-orange.svg)
![HTML](https://img.shields.io/badge/HTML-56.1%25-red.svg)
![JavaScript](https://img.shields.io/badge/JavaScript-22.2%25-yellow.svg)
![Bootstrap](https://img.shields.io/badge/Bootstrap-5.0-purple.svg)

A modern, secure, and user-friendly language translation application built with Spring Boot and powered by MyMemory Translation API.

[Features](#-features) •  
[Tech Stack](#-tech-stack) •  
[Getting Started](#-getting-started) •  
[API Usage](#-api-usage)

</div>

---

## ✨ Features

- **🔐 Secure Authentication System**
  - User registration with validation
  - Secure login functionality
  - Password encryption
  - Session management

- **🔄 Translation Capabilities**
  - Support for multiple languages
  - Real-time translation
  - Both GET and POST request handling
  - Error handling and recovery

- **💻 User Interface**
  - Clean, responsive design
  - Bootstrap 5 styling
  - Form validation
  - Real-time feedback
  - Mobile-friendly layout

- **🛡️ Security Features**
  - Spring Security integration
  - Protected endpoints
  - Secure password storage
  - CSRF protection

---

## 🔧 Tech Stack

### Backend
- **Framework:** Spring Boot 2.7.0  
- **Security:** Spring Security  
- **Database:** H2 Database  
- **ORM:** Spring Data JPA  
- **API Integration:** MyMemory Translation API  

### Frontend
- **Template Engine:** Thymeleaf  
- **Styling:** Bootstrap 5  
- **Scripting:** JavaScript  
- **CSS:** Custom styling  

---

## 🚀 Getting Started

### Prerequisites
- Java 17 or higher  
- Maven 3.6+  
- Git  

### Installation Steps

1. **Clone the Repository**  
   ```bash
   git clone https://github.com/deepak2testing/translator.git
   cd translator
   ```

2. **Build the Project**  
   Navigate to the project directory and use Maven to build the project:  
   ```bash
   mvn clean install
   ```

3. **Run the Application**  
   Start the application using the Spring Boot Maven plugin:  
   ```bash
   mvn spring-boot:run
   ```

4. **Access the Application**  
   Open your browser and navigate to:  
   [http://localhost:8080](http://localhost:8080)

---

## 📖 API Usage

### Endpoints

- **GET /translate**  
  - **Description:** Fetches translation for a given text and language.  
  - **Query Parameters:**  
    - `text`: The text to be translated.  
    - `targetLang`: The target language code.  
  - **Example Request:**  
    ```http
    GET http://localhost:8080/translate?text=hello&targetLang=es
    ```
  - **Example Response:**  
    ```json
    {
      "translatedText": "hola"
    }
    ```

- **POST /translate**  
  - **Description:** Accepts a JSON payload for translation requests.  
  - **Body:**  
    ```json
    {
      "text": "hello",
      "targetLang": "es"
    }
    ```
  - **Example Response:**  
    ```json
    {
      "translatedText": "hola"
    }
    ```

---

