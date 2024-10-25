# CONTENT PAGE
Here are some projects that I have worked on:

- [SeveNet](#sevenet)
- [Leave Application System](#leave-application-system)
- [Shopping Cart Web Application](#shopping-cart-web-application)
- [TheMemoryGame_Android](#thememorygame_android)

# SeveNet

## Overview
**SeveNet** is a web and mobile application designed to create a safe social space while detecting early symptoms of cyberbullying using Machine Learning (**Multi-Tag Classification**). The system provides real-time monitoring and feedback to enhance the online social experience and ensure user safety. Developed by a team of six, the project was completed within a one-month timeline, with each member contributing to different aspects of the system, from backend services to machine learning integration and mobile development. <br>
[View the SeveNet folder](https://github.com/laysiong/NUS-ISS_Projects/tree/root/SeveNet)

## My Contributions:
In this project, I primarily focused on the **frontend development** using **ReactJS**, creating a responsive and user-friendly interface. I also contributed to the **Android app development** using **Android Studio (Java)** to deliver a consistent experience across platforms. Additionally, I handled the integration of the **Flask API** for machine learning, deployed the **ReactJS frontend on AWS S3**, and set up the **Flask API** and **Spring Boot backend** on **EC2**. Apart from the technical work, I took charge of project management tasks, organizing team meetings to ensure we stayed on track and met our deadlines.

## Technologies Used

### Backend
- **Java Spring Boot**: Handles core business logic, user management, and other back-end services.
- **Flask API**: Supports the machine learning model for multi-tag classification to detect cyberbullying patterns.

### Frontend
- **ReactJS**: Provides a dynamic and responsive user interface for the web application.

### Database
- **MySQL**: Stores user data, posts, and other application-related information.

### Mobile
- **Android Studio**: Mobile application development with a native Android app, offering similar features to the web version.

### Deployment
- **AWS**: The application was deployed on AWS for scalability and performance. Currently, the application is taken down for further updates.

---

# Leave Application System

## Overview
This **Leave Application Processing System (LAPS)** is a web application developed by a team of six members, following the **Model-View-Controller (MVC)** architecture. The system enables employees of NUS-ISS to apply for leave, track their leave status, and manage their applications. It accommodates three employee roles: **Administrators**, **Managers**, and **Employees**, and is accessible through the NUS-ISS intranet. The use of the MVC pattern allows for easy scaling and maintenance of the project. <br>
[View the Leave Application System folder](https://github.com/laysiong/NUS-ISS_Projects/tree/root/Leave_Application_System)

## My Contributions:
I contributed to both the **frontend** development using **HTML** and **CSS** and the **backend** REST API built with **Spring Boot (Java)**. The database used was **MySQL**. I worked on features related to claims, applying for leave, and the approval and rejection of compensation. Additionally, I collaborated with one other teammate to merge code through Git and provided solutions to business problems, ensuring that the system met its functional requirements.

### Technologies Used

#### Backend
- **Java Spring Boot**: Manages data and application logic, including RESTful APIs.

#### Frontend
- **HTML, CSS, JavaScript**: Provides an interactive and responsive user interface.

#### Database
- **MySQL (Localhost)**: Stores user information, leave records, and other data related to the application.

---

# Shopping Cart Web Application

## Overview
This is a **Shopping Cart** web application developed using the **Model-View-Controller (MVC)** architecture with **ASP.NET**. The project simulates the functionality of a shopping cart without using a database. Users must log in to the system to map their activities, such as adding items to the cart and completing purchases, to entries in the backend. The frontend of the application was built using **HTML**, **CSS**, and **JavaScript**. <br>
[View the Shopping Cart Web Application folder](https://github.com/laysiong/NUS-ISS_Projects/tree/root/ShoppingCart_ASPNET)

## My Contributions:
I ensured that products were correctly added to the cart and that the total sum was accurately calculated and displayed. Additionally, I implemented the functionality to generate an activation code for the user after a successful purchase.

### Technologies Used

#### Backend
- **C# (ASP.NET MVC)**: Manages the server-side logic for the shopping cart.

#### Frontend
- **HTML, CSS, JavaScript**: Provides a dynamic shopping cart experience where users can add, update, and remove items.
  
---

# TheMemoryGame_Android

## Overview
**TheMemoryGame_Android** is a memory card-matching game developed over 2-3 weeks by a team of six. The game allows players to flip cards and try to match identical images across two activities. The game fetches images from a specified URL, downloads the first 20 images, and allows players to select 6 images to play the game. <br>
[View TheMemoryGame_Android folder](https://github.com/laysiong/NUS-ISS_Projects/tree/root/TheMemoryGame_Android)


## My Contributions
I ensured that users could successfully fetch the first 20 images from a specified URL. Users were able to interrupt the process and choose another set of images, with previously fetched images being removed. I also implemented the functionality for users to select 6 images to start the game.

Additionally, I implemented the **two-player mode**, allowing players to take turns, and integrated sound effects and animations to make the game more engaging when images matched or mismatched.

## Features
1. **Fetch Images from URL**: Users can specify a URL and fetch the first 20 images from the webpage. A progress bar shows the number of images downloaded, with descriptive text (e.g., "Downloading 10 of 20 images…").
2. **Grid Selection**: Once the images are fetched, the user selects 6 images to play the game.
3. **Game Play**: 
   - In the second activity, 12 placeholders are displayed (two for each selected image). When touched, a placeholder reveals the image behind it.
   - If the two selected images match, they remain visible. If they don't match, they are hidden again after a brief delay.
4. **Two-Player Mode**: Players take turns, with a winner declared at the end based on who matches more pairs.
5. **Sound Effects and Animations**: Special sound effects and animations are triggered for matches and mismatches to enhance the game experience.


