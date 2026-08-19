package com.studentmanagement;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    public void addStudent(Student student) throws SQLException {
        String sql = """
                INSERT INTO students (name, department, email, phone)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, student.getName());
            statement.setString(2, student.getDepartment());
            statement.setString(3, student.getEmail());
            statement.setString(4, student.getPhone());
            statement.executeUpdate();
        }
    }

    public void updateStudent(Student student) throws SQLException {
        String sql = """
                UPDATE students
                SET name = ?, department = ?, email = ?, phone = ?
                WHERE id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, student.getName());
            statement.setString(2, student.getDepartment());
            statement.setString(3, student.getEmail());
            statement.setString(4, student.getPhone());
            statement.setInt(5, student.getId());
            statement.executeUpdate();
        }
    }

    public void deleteStudent(int id) throws SQLException {
        String sql = "DELETE FROM students WHERE id = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    public List<Student> getAllStudents() throws SQLException {
        String sql = "SELECT id, name, department, email, phone FROM students ORDER BY id";
        List<Student> students = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                students.add(new Student(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("department"),
                        resultSet.getString("email"),
                        resultSet.getString("phone")
                ));
            }
        }

        return students;
    }

    public List<Student> searchStudents(String keyword) throws SQLException {
        String sql = """
                SELECT id, name, department, email, phone
                FROM students
                WHERE name LIKE ? OR department LIKE ? OR email LIKE ?
                ORDER BY id
                """;

        List<Student> students = new ArrayList<>();
        String searchValue = "%" + keyword + "%";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, searchValue);
            statement.setString(2, searchValue);
            statement.setString(3, searchValue);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    students.add(new Student(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("department"),
                            resultSet.getString("email"),
                            resultSet.getString("phone")
                    ));
                }
            }
        }

        return students;
    }
}
