# SeveNet

## Overview
**SeveNet** is a web and mobile application aimed at creating a safe social space while detecting early symptoms of cyberbullying using Machine Learning (**Multi-Tag Classification**). The system provides real-time monitoring and feedback to enhance the online social experience and ensure user safety.

The project has been deployed on **AWS**, though it is currently taken down for maintenance or updates.

## Technologies Used

### Backend
- **Java Spring Boot**: Handles core business logic, user management, and other back-end services.
- **Flask API**: Supports the machine learning model for multi-tag classification to detect cyberbullying patterns.

### Frontend
- **ReactJS**: Provides a dynamic and responsive user interface for the web application.

### Database
- **MySQL (Localhost)**: Stores user data, posts, and other application-related information.
  
### Mobile
- **Android Studio**: Mobile application development, providing similar features for mobile users with a native Android app.

### Deployment
- **AWS**: The application was deployed on AWS for scalability and performance. Currently, the application is taken down for further updates.

## Features
- **User Authentication**: Secure login and user management.
- **Cyberbullying Detection**: Real-time detection of cyberbullying patterns using machine learning.
- **Safe Social Space**: Provides a secure environment for users to engage with each other.
- **Multi-Tag Classification**: Classifies messages to identify potential harmful content.
- **Web and Mobile Access**: Available on both web and mobile platforms for flexibility.

## System Architecture

SeveNet follows a modular architecture:

### Backend (Java Spring Boot)
- Manages APIs for user actions, posts, and social interactions.
- Coordinates with the Flask API to run the machine learning model.

### Machine Learning (Flask API)
- Processes social media interactions to detect cyberbullying using Multi-Tag Classification.

### Frontend (ReactJS)
- Dynamic and interactive interface for web users to engage in social activities.

### Mobile (Android Studio)
- Native mobile app allowing users to engage in a safe social experience on-the-go.

## Usage
Once the backend and frontend are running locally, users can interact with the web or mobile interface to create accounts, post content, and experience the cyberbullying detection feature in action.

## Contributors
This project was developed by a team of six members, each contributing to different aspects such as backend development, machine learning, frontend design, and mobile development.
