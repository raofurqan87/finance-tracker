# Personal Finance Tracker

A desktop application built with JavaFX and PostgreSQL for tracking personal income and expenses.

## Tech Stack
- Java
- JavaFX
- JDBC
- PostgreSQL
- Maven

## Features
- Add and delete income and expense transactions
- Real-time balance tracking (turns red when negative)
- Pie chart showing spending breakdown by category
- Bar chart showing monthly income vs expenses
- Data persists in PostgreSQL database

## Visual
- Fade in animation on app load
- Hover effects on buttons
- Success message animation on transaction add
- Row highlight on table hover

## Setup
1. Clone the repository
2. Create a PostgreSQL database named `financetracker`
3. Run the SQL:
```sql
CREATE TABLE transactions (
    id SERIAL PRIMARY KEY,
    type VARCHAR(10) NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    category VARCHAR(50) NOT NULL,
    description VARCHAR(255),
    date DATE NOT NULL
);
```
4. Update `DatabaseConnection.java` with your PostgreSQL password
5. Run the application through IntelliJ

## Screenshots
(Add screenshots here)
