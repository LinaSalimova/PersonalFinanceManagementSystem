import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Реализация кошелька
// Этот класс управляет транзакциями и категориями для конкретного пользователя
class Wallet implements IWallet {
    private final String username;
    private final Map<String, Category> categories = new HashMap<>();
    private final List<Transaction> transactions = new ArrayList<>();

    public Wallet(String username) {
        this.username = username;
    }

    @Override
    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
        Category category = categories.computeIfAbsent(transaction.getCategory(), Category::new);
        if (transaction.getType() == Transaction.TransactionType.INCOME) {
            category.addIncome(transaction.getAmount());
        } else {
            category.addExpense(transaction.getAmount());
        }
    }

    @Override
    public void setBudget(String categoryName, double amount) {
        Category category = categories.computeIfAbsent(categoryName, Category::new);
        category.setBudget(amount);
    }

    @Override
    public double getTotalIncome() {
        return transactions.stream()
                .filter(t -> t.getType() == Transaction.TransactionType.INCOME)
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    @Override
    public double getTotalExpenses() {
        return transactions.stream()
                .filter(t -> t.getType() == Transaction.TransactionType.EXPENSE)
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    @Override
    public Map<String, Category> getCategories() {
        return new HashMap<>(categories); // Возвращаем копию для безопасности
    }

    @Override
    public List<Transaction> getTransactions() {
        return new ArrayList<>(transactions); // Возвращаем копию для безопасности
    }
}