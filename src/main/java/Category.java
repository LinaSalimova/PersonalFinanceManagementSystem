// Класс для представления категории
// Этот класс отслеживает доходы, расходы и бюджет для каждой категории
class Category {
    private final String name;
    private double income;
    private double expenses;
    private double budget;

    public Category(String name) {
        this.name = name;
        this.income = 0;
        this.expenses = 0;
        this.budget = 0;
    }

    public void addIncome(double amount) {
        income += amount;
    }

    public void addExpense(double amount) {
        expenses += amount;
    }

    public void setBudget(double amount) {
        budget = amount;
    }

    // Геттеры
    public String getName() { return name; }
    public double getIncome() { return income; }
    public double getExpenses() { return expenses; }
    public double getBudget() { return budget; }
    public double getRemainingBudget() { return budget - expenses; }
}
