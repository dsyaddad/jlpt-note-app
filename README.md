# JLPT Note App

## Overview

JLPT Note App is a web-based application designed to help users prepare for the Japanese Language Proficiency Test (JLPT). It provides a centralized place to manage grammar notes, vocabulary lists, and verb conjugations, tailored for different JLPT levels.

## Features

- **Grammar Notes Management (Notes):**
  - Create, edit, delete, and view detailed grammar notes.
  - Each note includes a pattern name, identifier, function, usage explanation, and personal notes.
  - Ability to add multiple formulas and example sentences for each grammar point.
  - Filter notes by JLPT level, section, or content.

- **Vocabulary Dictionary (Jisho):**
  - Manage a personal vocabulary list with Kanji, Kana, Romaji, and meanings (in English and Indonesian).
  - Assign Part of Speech (POS) and JLPT level to each word.
  - Add multiple example sentences for each vocabulary word.
  - Advanced search and filter capabilities.

- **Verb Conjugation (Katsuyou):**
  - A tool to check and practice Japanese verb conjugations.

- **Dynamic Forms:**
  - Dynamically add/remove formula and example fields on the creation pages without a page reload.

- **Data Management:**
  - Bulk deletion for notes and vocabulary.
  - DML export feature for database seeding.

## Technologies Used

- **Backend:**
  - Java
  - Spring Boot (Web, Data JPA)
  - Maven

- **Frontend:**
  - Thymeleaf
  - HTML, CSS, JavaScript
  - Bootstrap 5

- **Database:**
  - MySQL running via Docker.

- **Deployment:**
  - Docker & Docker Compose

## Setup and Installation

1.  **Prerequisites:**
    - Java JDK (version 17 or higher)
    - Apache Maven
    - Docker and Docker Compose

2.  **Clone the repository:**
    ```bash
    git clone <your-repository-url>
    cd jlpt-note-app
    ```

3.  **Configure the database:**
    - Open `src/main/resources/application.properties`.
    - Update the `spring.datasource.url`, `spring.datasource.username`, and `spring.datasource.password` properties to match your database configuration.

4.  **Run the application:**

    - **Using Docker Compose (Recommended):**
      This will build the Spring Boot application and start the required services (like the database).
      ```bash
      docker-compose up --build
      ```

    - **Using Maven:**
      If you have a database instance running separately.
      ```bash
      mvn spring-boot:run
      ```

5.  **Access the application:**
    Open your web browser and navigate to `http://localhost:8080`.

## Usage

-   Navigate to the **Notes** section to manage your grammar notes.
-   Use the **Jisho** section to build and search your personal vocabulary dictionary.
-   Explore the **Katsuyou** page for verb conjugation practice.
-   Use the filter and search functionalities on the list pages to quickly find what you need.
