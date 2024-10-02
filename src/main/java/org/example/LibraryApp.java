package org.example;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class LibraryApp {
    private static final String URL = "jdbc:postgresql://localhost:5432/library";
    private static final String USER = "postgres";
    private static final String PASSWORD = "1234";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nБиблиотечное приложение");
            System.out.println("1. Добавить книгу");
            System.out.println("2. Просмотреть все книги");
            System.out.println("3. Обновить книгу по id");
            System.out.println("4. Удалить книгу по id");
            System.out.println("5. Выход");
            System.out.print("Введите выбор: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // очистка буфера

            switch (choice) {
                case 1:
                    addBook(scanner);
                    break;
                case 2:
                    viewAllBooks();
                    break;
                case 3:
                    updateBook(scanner);
                    break;
                case 4:
                    deleteBook(scanner);
                    break;
                case 5:
                    System.out.println("Выход из приложения.");
                    return;
                default:
                    System.out.println("Некорректный выбор. Пожалуйста, введите 1, 2, 3, 4 или 5.");
            }
        }
    }

    private static void addBook(Scanner scanner) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            System.out.print("Введите название книги: ");
            String title = scanner.nextLine();
            System.out.print("Введите автора книги: ");
            String author = scanner.nextLine();
            System.out.print("Введите год издания: ");
            int year = scanner.nextInt();
            scanner.nextLine(); // очистка буфера

            String sql = "INSERT INTO books (title, author, year) VALUES (?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, title);
                statement.setString(2, author);
                statement.setInt(3, year);
                statement.executeUpdate();
                System.out.println("Книга успешно добавлена.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void viewAllBooks() {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            String sql = "SELECT * FROM books";
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {
                List<Book> books = new ArrayList<>();
                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String title = resultSet.getString("title");
                    String author = resultSet.getString("author");
                    int year = resultSet.getInt("year");
                    books.add(new Book(id, title, author, year));
                }
                books.forEach(System.out::println);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void updateBook(Scanner scanner) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            System.out.print("Введите id книги для обновления: ");
            int id = scanner.nextInt();
            scanner.nextLine(); // очистка буфера
            System.out.print("Введите новое название книги: ");
            String title = scanner.nextLine();
            System.out.print("Введите нового автора книги: ");
            String author = scanner.nextLine();
            System.out.print("Введите новый год издания: ");
            int year = scanner.nextInt();
            scanner.nextLine(); // очистка буфера

            String sql = "UPDATE books SET title = ?, author = ?, year = ? WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, title);
                statement.setString(2, author);
                statement.setInt(3, year);
                statement.setInt(4, id);
                int rowsUpdated = statement.executeUpdate();
                if (rowsUpdated > 0) {
                    System.out.println("Книга успешно обновлена.");
                } else {
                    System.out.println("Книга с таким id не найдена.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void deleteBook(Scanner scanner) {
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)) {
            System.out.print("Введите id книги для удаления: ");
            int id = scanner.nextInt();
            scanner.nextLine(); // очистка буфера

            String sql = "DELETE FROM books WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, id);
                int rowsDeleted = statement.executeUpdate();
                if (rowsDeleted > 0) {
                    System.out.println("Книга успешно удалена.");
                } else {
                    System.out.println("Книга с таким id не найдена.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
