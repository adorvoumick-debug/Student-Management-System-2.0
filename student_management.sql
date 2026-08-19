CREATE DATABASE IF NOT EXISTS student_management;
USE student_management;

CREATE TABLE IF NOT EXISTS students (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    department VARCHAR(50) NOT NULL,
    email VARCHAR(120) NOT NULL UNIQUE,
    phone VARCHAR(30) NOT NULL
);

INSERT INTO students (name, department, email, phone)
VALUES
('Ayesha Rahman', 'CSE', 'ayesha@example.com', '01700000001'),
('Tanvir Hasan', 'BBA', 'tanvir@example.com', '01700000002');
