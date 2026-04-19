package com.furqan.financetracker;

import javafx.scene.chart.PieChart;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {
    public List<Transaction> getAllTransactions() throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions ORDER BY date DESC";
        try (Connection connection = DatabaseConnection.getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                transactions.add(new Transaction(
                        resultSet.getInt("id"),
                        resultSet.getString("type"),
                        resultSet.getBigDecimal("amount"),
                        resultSet.getString("category"),
                        resultSet.getString("description"),
                        resultSet.getDate("date").toLocalDate()
                ));
            }
        }
        return transactions;
    }
    public void addTransaction(Transaction t) throws SQLException {
        String sql = "INSERT INTO Transactions (type, amount, category, description, date) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, t.getType());
            preparedStatement.setBigDecimal(2, t.getAmount());
            preparedStatement.setString(3, t.getCategory());
            preparedStatement.setString(4, t.getDescription());
            preparedStatement.setDate(5, Date.valueOf(t.getDate()));
            preparedStatement.executeUpdate();
        }
    }
    public void deleteTransaction(int id) throws SQLException {
        String sql = "DELETE FROM transactions WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }
}