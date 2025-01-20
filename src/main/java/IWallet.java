import java.util.List;
import java.util.Map;

// Интерфейс для работы с кошельком
// Этот интерфейс определяет основные операции, которые можно выполнять с кошельком
interface IWallet {
    void addTransaction(Transaction transaction);
    void setBudget(String categoryName, double amount);
    double getTotalIncome();
    double getTotalExpenses();
    Map<String, Category> getCategories();
    List<Transaction> getTransactions();
}
