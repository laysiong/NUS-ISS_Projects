Leave Application System
Overview
This Leave Application System is a web application developed by a group of six members, following the Model-View-Controller (MVC) architecture. The system allows users to apply for leave, track their leave status, and manage leave applications efficiently. By following the MVC pattern, the project maintains a clean separation of concerns, making it easier to scale and maintain over time.

Technologies Used
Backend
Java Spring Boot: Manages application logic, including handling leave requests, approval processes, and database interactions. The system is designed with RESTful APIs to interact with the frontend.
Frontend
HTML, CSS, and JavaScript: Provide an interactive and responsive user interface for the leave application system.
Database
MySQL (Localhost): Stores user information, leave records, and other data related to the application.
Features
User Authentication: Allows users to securely log in and manage their leave applications.
Apply for Leave: Users can apply for different types of leave, specifying dates and reasons.
View Leave Status: Users can track the status of their leave applications (e.g., pending, approved, rejected).
Admin Functionality: Admins can approve or reject leave applications.
Leave History: Users and admins can view the history of leave applications.
System Architecture
The application is structured using the Model-View-Controller (MVC) architecture:

Model: Represents the application's data, including user details and leave records.
View: The frontend interface built using HTML, CSS, and JavaScript that interacts with users.
Controller: Manages incoming requests, processes leave applications, and updates the Model accordingly.

Usage
Once the application is running locally, users can log in to apply for leave, view their leave status, and manage their applications. Admins can approve or reject leave requests through their dedicated interface.

Contributors
This project was developed by a team of six members, each contributing to different aspects such as backend development, frontend design, database management, and project coordination.