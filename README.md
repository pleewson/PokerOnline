# PokerOnline

## Overview
This project is a real-time poker game for two players. The game allows two players to play poker in real-time, utilizing various modern technologies to ensure smooth gameplay and efficient data handling.

## Features
- **Real-time Gameplay**: Players can play poker in real-time thanks to WebSocket communication.
- **User Registration and Authentication**: Players can register and log in to their accounts.
- **Dynamic Ranking**: Display of player rankings based on trophies won.
- **Profile Management**: Players can update their email, password, and personal details.

## Technologies Used
- **Backend**:
  - **Spring Boot**: Provides the server-side logic and REST API.
  - **WebSocket**: Enables real-time communication between the server and clients.
  - **JPA/Hibernate**: Object-relational mapping and database operations.
  - **MySQL**: Relational database to store player and game data.
- **Frontend**:
  - **Thymeleaf**: Template engine for dynamic rendering of HTML pages.
  - **JavaScript (ES6+)**: Handles client-side logic and user interactions.
  - **CSS**: Styles the application.
- **Others**:
  - **BCrypt**: For password hashing and security.

## Getting Started

### Prerequisites
- Java 11 or higher
- MySQL
- Maven

### Setup
1. **Clone the repository**:
    ```
    git clone https://github.com/pleewson/PokerOnline.git
    cd PokerOnline
    ```

2. **Configure the database**:
    - Create a MySQL database named `poker_game`.
    - Update the `application.properties` file in `src/main/resources` with your database username and password:
      ```properties
      spring.datasource.url=jdbc:mysql://localhost:3306/poker_game
      spring.datasource.username=your_username
      spring.datasource.password=your_password
      spring.jpa.hibernate.ddl-auto=update
      ```

3. **Run the application**:
    ```
    docker-compose up --build
    ```

4. **Access the application**:
    - Open your web browser and go to `http://localhost:8080`.

## Usage

### User Registration and Login
- Go to `/register` to create a new account.
- Use `/login` to access your account.

### Playing the Game
- Once logged in, navigate to `/game` and click the JOIN button to enter the game.
- The game will start automatically once two players have joined.
- The game logic ensures real-time updates and interactions between players.

### Managing Your Account
- Access the `My Account` section to update your email, password, and other personal details.
- Your current trophies and other game statistics are also displayed here.

### Viewing the Scoreboard
- Go to `/scoreboard` to see the ranking of players based on the number of trophies won.
