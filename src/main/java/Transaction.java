import java.util.Date;

// Класс для представления транзакции
// Этот класс является неизменяемым (immutable), что повышает безопасность и упрощает многопоточное использование
public class Transaction {
    public enum TransactionType {
        INCOME, EXPENSE
    }

    private final TransactionType type;
    private final double amount;
    private final String category;
    private final Date date;

    public Transaction(TransactionType type, double amount, String category) {
        this.type = type;
        this.amount = amount;
        this.category = category;
        this.date = new Date(); // Автоматически устанавливаем текущую дату
    }

    // Геттеры
    public TransactionType getType() { return type; }
    public double getAmount() { return amount; }
    public String getCategory() { return category; }
    public Date getDate() { return new Date(date.getTime()); } // Возвращаем копию даты для безопасности
}