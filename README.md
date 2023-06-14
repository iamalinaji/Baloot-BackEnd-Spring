# Baloot

Baloot is an online marketplace designed to connect buyers and sellers. It provides a platform for users to buy and sell various products.

## Backend

The backend of Baloot is implemented using Spring Boot, a Java-based framework for building web applications. It offers a robust and scalable solution for handling the business logic and data management of the marketplace. The backend interacts with a MySQL database to store and retrieve data.

Key features of the backend:

- **Spring Boot:** A powerful Java framework for building web applications.
- **MySQL:** A popular open-source relational database management system.
- **API Endpoints:** The backend exposes various RESTful API endpoints for performing CRUD operations on products, user management, and order processing.

## Frontend

The frontend of Baloot is developed using React, a popular JavaScript library for building user interfaces. React enables the creation of dynamic and interactive components, providing users with a seamless and responsive experience.

Key features of the frontend:

- **React:** A JavaScript library for building user interfaces.
- **Component-Based Architecture:** The frontend is structured into reusable components, promoting code reusability and maintainability.
- **Responsive Design:** The user interface is designed to adapt and scale across different devices and screen sizes.

## Deployment

To deploy Baloot, you can follow these steps:

1. Run database

        docker run --name mysqldb --network baloot -e MYSQL_ROOT_PASSWORD=1234 -e MYSQL_DATABASE=baloot -d mysql
2. Run backend
   
       docker run --network baloot --name springboot -p 8080:8080 -d springboot
3. Run frontend

       docker run --network baloot --name react-app -p 5173:80 -d vite-app

Once the deployment is complete, Baloot will be accessible through the specified server URL, allowing users to browse and purchase products seamlessly.

---

Please note that the above description is for illustrative purposes only, and you will need to adapt it to your specific project requirements and implementation details.
