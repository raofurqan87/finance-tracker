package com.furqan.financetracker;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.math.BigDecimal;
import java.security.Key;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import javafx.scene.chart.*;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.scene.layout.VBox;

public class HelloController {
    @FXML private VBox rootPane;
    @FXML private ComboBox<String> typeComboBox;
    @FXML private TextField amountField;
    @FXML private ComboBox<String> categoryComboBox;
    @FXML private TextField descriptionField;
    @FXML private DatePicker datePicker;
    @FXML private Label statusLabel;
    @FXML private Label totalIncomeLabel;
    @FXML private Label totalExpenseLabel;
    @FXML private Label balanceLabel;
    @FXML private TableView<Transaction> transactionTable;
    @FXML private TableColumn<Transaction, Integer> idColumn;
    @FXML private TableColumn<Transaction, String> typeColumn;
    @FXML private TableColumn<Transaction, BigDecimal> amountColumn;
    @FXML private TableColumn<Transaction, String> categoryColumn;
    @FXML private TableColumn<Transaction, String> descriptionColumn;
    @FXML private TableColumn<Transaction, LocalDate> dateColumn;
    @FXML private PieChart categoryPieChart;
    @FXML private BarChart<String, Number> incomeExpenseBarChart;
    @FXML private Button addButton;
    @FXML private Button deleteButton;
    private final TransactionDAO transactionDAO = new TransactionDAO();
    private final ObservableList<Transaction> transactionList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        typeComboBox.setItems(FXCollections.observableArrayList("INCOME", "EXPENSE"));
        categoryComboBox.setItems(FXCollections.observableArrayList(
                "Food", "Transport", "Entertainment", "Shopping", "Health", "Education", "Salary", "Freelance", "Other"
        ));
        datePicker.setValue(LocalDate.now());
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        transactionTable.setItems(transactionList);
        loadTransactions();
        applyButtonEffects();
        applyFadeIn(transactionTable);
        styleTable();
    }
    private void applyButtonEffects() {
        styleButton(addButton, "#27AE60", "#2ECC71");
        styleButton(deleteButton, "#C0392B", "#E74C3C");
    }
    private void styleTable() {
        transactionTable.setRowFactory(tv -> {
            javafx.scene.control.TableRow<Transaction> row = new javafx.scene.control.TableRow<>();
            row.setOnMouseEntered(e -> {
                if (!row.isEmpty()) {
                    row.setStyle("-fx-background-color: #D5E8F0;");
                }
            });
            row.setOnMouseExited(e -> {
                if (!row.isEmpty()) {
                    row.setStyle("-fx-background-color: transparent;");
                }
            });
            return row;
        });
        transactionTable.setStyle(
                "-fx-font-size: 13px;" +
                "-fx-background-color: white;" +
                "-fx-border-color: #BDC3C7;" +
                "-fx-border-radius: 5;"
        );
    }
    private void applyFadeIn(javafx.scene.Node node) {
        FadeTransition fade = new FadeTransition(Duration.millis(800), node);
        fade.setFromValue(0.0);
        fade.setToValue(1.0);
        fade.play();
    }
    private void showSuccessMessage(String message) {
        statusLabel.setText(message);
        statusLabel.setStyle("-fx-text-fill: #27AE60");
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(0.0),
                        new KeyValue(statusLabel.opacityProperty(), 1.0)),
                new KeyFrame(Duration.millis(2000),
                        new KeyValue(statusLabel.opacityProperty(), 1.0)),
                new KeyFrame(Duration.millis(3000),
                        new KeyValue(statusLabel.opacityProperty(), 0.0))
        );
        timeline.setOnFinished(e -> {
            statusLabel.setText("");
            statusLabel.setStyle("-fx-text-fill: #E74C3C");
            statusLabel.setOpacity(1.0);
        });
        timeline.play();
    }
    private void styleButton(Button button, String normalColor, String hoverColor) {
        button.setStyle("-fx-background-color: " + normalColor + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;");
        button.setOnMouseEntered(e -> {
            button.setStyle("-fx-background-color: " + hoverColor + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;");
            button.setCursor(javafx.scene.Cursor.HAND);
        });
        button.setOnMouseExited(e -> {
            button.setStyle("-fx-background-color: " + normalColor + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 5;");
            button.setCursor(javafx.scene.Cursor.DEFAULT);
        });
    }
    private void loadTransactions() {
        try {
            List<Transaction> transactions = transactionDAO.getAllTransactions();
            transactionList.setAll(transactions);
            updateSummary();
            updateChart();
        } catch (SQLException exception) {
            statusLabel.setText("Error loading transactions: " + exception.getMessage());
        }
    }
    private void updateSummary() {
        BigDecimal totalIncome = BigDecimal.ZERO;
        BigDecimal totalExpense = BigDecimal.ZERO;
        for (Transaction t : transactionList) {
            if (t.getType().equals("INCOME")) {
                totalIncome = totalIncome.add(t.getAmount());
            }
            else {
                totalExpense = totalExpense.add(t.getAmount());
            }
        }
        BigDecimal balance = totalIncome.subtract(totalExpense);
        totalIncomeLabel.setText("Total income: $ " + totalIncome);
        totalExpenseLabel.setText("Total expense: $ " + totalExpense);
        balanceLabel.setText("Balance: $: " + balance);
        if (balance.compareTo(BigDecimal.ZERO) < 0) {
            balanceLabel.setStyle("-fx-text-fill: #E74C3C; -fx-font-size: 14; -fx-font-weight: bold;");
        }
        else {
            balanceLabel.setStyle("-fx-text-fill: white; -fx-font-size: 14; -fx-font-weight: bold;");
        }
    }
    private void updateChart() {
        ObservableList<PieChart.Data> pieData = FXCollections.observableArrayList();
        java.util.Map<String, BigDecimal> categoryTotals = new java.util.HashMap<>();
        for (Transaction t : transactionList) {
            if (t.getType().equals("EXPENSE")) {
                categoryTotals.merge(t.getCategory(), t.getAmount(), BigDecimal::add);
            }
        }
        for (java.util.Map.Entry<String, BigDecimal> entry : categoryTotals.entrySet()) {
            pieData.add(new PieChart.Data(entry.getKey(), entry.getValue().doubleValue()));
        }
        categoryPieChart.setData(pieData);
        XYChart.Series<String, Number> incomeSeries = new XYChart.Series<>();
        incomeSeries.setName("Income");
        XYChart.Series<String, Number> expenseSeries = new XYChart.Series<>();
        expenseSeries.setName("Expense");
        java.util.Map<String, BigDecimal> monthlyIncome = new java.util.TreeMap<>();
        java.util.Map<String, BigDecimal> monthlyExpense = new java.util.TreeMap<>();
        for (Transaction t : transactionList) {
            String month = t.getDate().getYear() + "-" + String.format("%02d", t.getDate().getMonthValue());
            if (t.getType().equals("INCOME")) {
                monthlyIncome.merge(month, t.getAmount(), BigDecimal::add);
            }
            else {
                monthlyExpense.merge(month, t.getAmount(), BigDecimal::add);
            }
        }
        for (String month : monthlyIncome.keySet()) {
            incomeSeries.getData().add(new XYChart.Data<>(month, monthlyIncome.get(month).doubleValue()));
        }
        for (String month : monthlyExpense.keySet()) {
            expenseSeries.getData().add(new XYChart.Data<>(month, monthlyExpense.get(month).doubleValue()));
        }
        incomeExpenseBarChart.getData().clear();
        incomeExpenseBarChart.getData().addAll(incomeSeries, expenseSeries);
    }
    @FXML
    private void handleAddTransaction() {
        String type = typeComboBox.getValue();
        String amountText = amountField.getText();
        String category = categoryComboBox.getValue();
        String description = descriptionField.getText();
        LocalDate date = datePicker.getValue();

        if (type == null || amountText.isEmpty() || category == null || date == null) {
            statusLabel.setText("Please fill in all required fields.");
            return;
        }
        try {
            BigDecimal amount = new BigDecimal(amountText);
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                statusLabel.setText("Amount must be greater than 0.");
                return;
            }
            Transaction t = new Transaction(0, type, amount, category, description, date);
            transactionDAO.addTransaction(t);
            amountField.clear();
            descriptionField.clear();
            datePicker.setValue(LocalDate.now());
            showSuccessMessage("Transaction added successfully!");
            loadTransactions();
        } catch (NumberFormatException e) {
            statusLabel.setText("Please enter a valid amount.");
        } catch (SQLException e) {
            statusLabel.setText("Error adding transaction: " + e.getMessage());
        }
    }
    @FXML
    private void handleDeleteTransaction() {
        Transaction selected = transactionTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            statusLabel.setText("Please select a transaction to delete.");
            return;
        }
        try {
            transactionDAO.deleteTransaction(selected.getId());
            statusLabel.setText("");
            loadTransactions();
        } catch (SQLException e) {
            statusLabel.setText("Error deleting transaction: " + e.getMessage());
        }
    }
}