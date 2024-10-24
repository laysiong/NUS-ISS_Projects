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

## My Contributions
In this project, I primarily focused on the frontend development using ReactJS, creating a responsive and user-friendly interface. I also contributed to the Android app development using Android Studio (Java) to deliver a consistent experience across platforms. Additionally, I handled the integration of the Flask API for machine learning, deployed the ReactJS frontend on AWS S3, and set up the Flask API and Spring Boot backend on EC2. Apart from the technical work, I took charge of project management tasks, organizing team meetings to ensure we stayed on track and met our deadlines.

## Features (My Part)
- **Like/Unlike**: Users can like or unlike posts and messages.
- **Follow/Unfollow/Block Users**: Users can follow, unfollow, or block other users they are interested in.
- **User Profile**: Displays messages posted by the user.
- **Report Post/Message/User**: A pop-up screen allows users to fill out a form to report inappropriate content or behavior.
- **Administrator Dashboard**: A dashboard is provided for administrators to manage users, posts, and reports.
- **Authentication**: Determines what users can view in posts and comments based on their roles.
- **Admin Controls**: Admins can edit post tags and input new tags as needed.
- **Post and Comment Display**: A component that shows posts and their associated comments.
- **Flask API for Machine Learning**: Created a Flask API to support machine learning functionality.
- **AWS Deployment**: The entire project was deployed on AWS, ensuring scalability and availability.

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
