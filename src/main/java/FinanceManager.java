import java.util.HashMap;
import java.util.Map;

// Основной класс управления финансами
class FinanceManager {
    private final IUserAuthenticator userAuth;
    private final Map<String, IWallet> wallets = new HashMap<>();
    private final IFileManager fileManager;
    private String currentUser; // Текущий пользователь

    public FinanceManager(IUserAuthenticator userAuth, IFileManager fileManager) {
        this.userAuth = userAuth;
        this.fileManager = fileManager;
    }

    // Метод для обработки команд пользователя
    public void processCommand(String command, String... args) {
        switch (command) {
            case "login":
                login(args[0], args[1]);
                break;
            case "register":
                register(args[0], args[1]);
                break;
            case "addIncome":
                addIncome(currentUser, Double.parseDouble(args[0]), args[1]);
                break;
            case "addExpense":
                addExpense(currentUser, Double.parseDouble(args[0]), args[1]);
                break;
            case "setBudget":
                setBudget(currentUser, args[0], Double.parseDouble(args[1]));
                break;
            case "getReport":
                System.out.println(getFinancialReport(currentUser));
                break;
            case "transferFunds":
                transferFunds(currentUser, args[0], Double.parseDouble(args[1]));
                break;
            case "logout":
                logout();
                break;
            default:
                System.out.println("Unknown command");
        }
    }

    // Метод для входа пользователя
    private void login(String username, String password) {
        try {
            if (authenticateUser(username, password)) {
                currentUser = username;
                System.out.println("Login successful");
            } else {
                System.out.println("Invalid credentials");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // Метод для регистрации пользователя
    private void register(String username, String password) {
        try {
            registerUser(username, password);
            System.out.println("Registration successful");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // Метод для выхода пользователя
    private void logout() {
        currentUser = null;
        System.out.println("Logged out");
    }

    // Метод для регистрации нового пользователя
    public void registerUser(String username, String password) throws Exception {
        userAuth.registerUser(username, password);
        wallets.put(username, new Wallet(username));
    }

    // Метод для аутентификации пользователя
    public boolean authenticateUser(String username, String password) throws Exception {
        boolean authenticated = userAuth.authenticateUser(username, password);
        if (authenticated && !wallets.containsKey(username)) {
            wallets.put(username, fileManager.loadWallet(username));
        }
        return authenticated;
    }

    // Метод для добавления дохода с валидацией
    public void addIncome(String username, double amount, String category) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        IWallet wallet = wallets.get(username);
        wallet.addTransaction(new Transaction(Transaction.TransactionType.INCOME, amount, category));
    }

    // Метод для добавления расхода с валидацией
    public void addExpense(String username, double amount, String category) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        IWallet wallet = wallets.get(username);
        wallet.addTransaction(new Transaction(Transaction.TransactionType.EXPENSE, amount, category));
    }

    // Метод для установки бюджета категории с валидацией
    public void setBudget(String username, String category, double amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Budget cannot be negative");
        }
        IWallet wallet = wallets.get(username);
        wallet.setBudget(category, amount);
    }

    // Метод для перевода средств между пользователями
    public void transferFunds(String fromUser, String toUser, double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Transfer amount must be positive");
        }
        IWallet fromWallet = wallets.get(fromUser);
        IWallet toWallet = wallets.get(toUser);
        if (fromWallet == null || toWallet == null) {
            throw new IllegalArgumentException("Invalid user(s)");
        }
        if (fromWallet.getTotalIncome() - fromWallet.getTotalExpenses() < amount) {
            throw new IllegalStateException("Insufficient funds");
        }
        fromWallet.addTransaction(new Transaction(Transaction.TransactionType.EXPENSE, amount, "Transfer to " + toUser));
        toWallet.addTransaction(new Transaction(Transaction.TransactionType.INCOME, amount, "Transfer from " + fromUser));
    }

    // Метод для получения финансового отчета
    public String getFinancialReport(String username) {
        IWallet wallet = wallets.get(username);
        StringBuilder report = new StringBuilder();
        report.append("Общий доход: ").append(wallet.getTotalIncome()).append("\n");
        report.append("Общие расходы: ").append(wallet.getTotalExpenses()).append("\n");
        report.append("Доходы по категориям:\n");
        wallet.getCategories().values().stream()
                .filter(c -> c.getIncome() > 0)
                .forEach(c -> report.append(c.getName()).append(": ").append(c.getIncome()).append("\n"));
        report.append("Бюджет по категориям:\n");
        for (Category category : wallet.getCategories().values()) {
            report.append(category.getName()).append(": ")
                    .append("Бюджет: ").append(category.getBudget())
                    .append(", Расходы: ").append(category.getExpenses())
                    .append(", Оставшийся бюджет: ").append(category.getRemainingBudget())
                    .append("\n");
            if (category.getExpenses() > category.getBudget()) {
                report.append("ВНИМАНИЕ: Превышен лимит бюджета в категории ").append(category.getName()).append("\n");
            }
        }
        if (wallet.getTotalExpenses() > wallet.getTotalIncome()) {
            report.append("ВНИМАНИЕ: Общие расходы превысили доходы!\n");
        }
        return report.toString();
    }
}
