import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FinanceManagerGUI {
    private JFrame frame;
    private JPanel cards;
    private CardLayout cardLayout;
    private FinanceManager financeManager;
    private String currentUser;

    // Конструктор класса
    public FinanceManagerGUI(FinanceManager financeManager) {
        this.financeManager = financeManager;
        createAndShowGUI();
    }

    // Метод для создания и отображения графического интерфейса
    private void createAndShowGUI() {
        // Создание главного окна
        frame = new JFrame("Finance Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);

        // Создание CardLayout для переключения между панелями
        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);

        // Добавление панелей для входа, регистрации и управления кошельком
        cards.add(createLoginPanel(), "LOGIN");
        cards.add(createRegisterPanel(), "REGISTER");
        cards.add(createWalletPanel(), "WALLET");

        // Добавление панели с картами в главное окно и отображение его
        frame.add(cards);
        frame.setVisible(true);
    }

    // Метод для создания панели входа
    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 2));
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");

        // Обработчик события для кнопки входа
        loginButton.addActionListener(e -> {
            try {
                if (financeManager.authenticateUser(usernameField.getText(), new String(passwordField.getPassword()))) {
                    currentUser = usernameField.getText();
                    cardLayout.show(cards, "WALLET");
                    updateWalletPanel();
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid credentials");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage());
            }
        });

        // Обработчик события для кнопки перехода к регистрации
        registerButton.addActionListener(e -> cardLayout.show(cards, "REGISTER"));

        // Добавление компонентов на панель
        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(loginButton);
        panel.add(registerButton);

        return panel;
    }

    // Метод для создания панели регистрации
    private JPanel createRegisterPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 2));
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JButton registerButton = new JButton("Register");
        JButton backButton = new JButton("Back to Login");

        // Обработчик события для кнопки регистрации
        registerButton.addActionListener(e -> {
            try {
                financeManager.registerUser(usernameField.getText(), new String(passwordField.getPassword()));
                JOptionPane.showMessageDialog(frame, "Registration successful");
                cardLayout.show(cards, "LOGIN");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage());
            }
        });

        // Обработчик события для кнопки возврата к входу
        backButton.addActionListener(e -> cardLayout.show(cards, "LOGIN"));

        // Добавление компонентов на панель
        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(registerButton);
        panel.add(backButton);

        return panel;
    }

    // Метод для создания панели управления кошельком
    private JPanel createWalletPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JTextArea reportArea = new JTextArea();
        reportArea.setEditable(false);
        JButton addIncomeButton = new JButton("Add Income");
        JButton addExpenseButton = new JButton("Add Expense");
        JButton setBudgetButton = new JButton("Set Budget");
        JButton transferFundsButton = new JButton("Transfer Funds");
        JButton logoutButton = new JButton("Logout");

        // Обработчики событий для кнопок
        addIncomeButton.addActionListener(e -> showAddTransactionDialog(Transaction.TransactionType.INCOME));
        addExpenseButton.addActionListener(e -> showAddTransactionDialog(Transaction.TransactionType.EXPENSE));
        setBudgetButton.addActionListener(e -> showSetBudgetDialog());
        transferFundsButton.addActionListener(e -> showTransferFundsDialog());
        logoutButton.addActionListener(e -> {
            currentUser = null;
            cardLayout.show(cards, "LOGIN");
        });

        // Создание панели с кнопками
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addIncomeButton);
        buttonPanel.add(addExpenseButton);
        buttonPanel.add(setBudgetButton);
        buttonPanel.add(transferFundsButton);
        buttonPanel.add(logoutButton);

        // Добавление компонентов на панель
        panel.add(new JScrollPane(reportArea), BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    // Метод для отображения диалога добавления транзакции
    private void showAddTransactionDialog(Transaction.TransactionType type) {
        JTextField amountField = new JTextField(10);
        JTextField categoryField = new JTextField(10);
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Amount:"));
        panel.add(amountField);
        panel.add(new JLabel("Category:"));
        panel.add(categoryField);

        int result = JOptionPane.showConfirmDialog(null, panel,
                "Add " + type, JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                double amount = Double.parseDouble(amountField.getText());
                String category = categoryField.getText();
                if (type == Transaction.TransactionType.INCOME) {
                    financeManager.addIncome(currentUser, amount, category);
                } else {
                    financeManager.addExpense(currentUser, amount, category);
                }
                updateWalletPanel();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid amount");
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage());
            }
        }
    }

    // Метод для отображения диалога установки бюджета
    private void showSetBudgetDialog() {
        JTextField categoryField = new JTextField(10);
        JTextField amountField = new JTextField(10);
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Category:"));
        panel.add(categoryField);
        panel.add(new JLabel("Budget Amount:"));
        panel.add(amountField);

        int result = JOptionPane.showConfirmDialog(null, panel,
                "Set Budget", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String category = categoryField.getText();
                double amount = Double.parseDouble(amountField.getText());
                financeManager.setBudget(currentUser, category, amount);
                updateWalletPanel();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid amount");
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage());
            }
        }
    }

    // Метод для отображения диалога перевода средств
    private void showTransferFundsDialog() {
        JTextField toUserField = new JTextField(10);
        JTextField amountField = new JTextField(10);
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("To User:"));
        panel.add(toUserField);
        panel.add(new JLabel("Amount:"));
        panel.add(amountField);

        int result = JOptionPane.showConfirmDialog(null, panel,
                "Transfer Funds", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String toUser = toUserField.getText();
                double amount = Double.parseDouble(amountField.getText());
                financeManager.transferFunds(currentUser, toUser, amount);
                updateWalletPanel();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid amount");
            } catch (IllegalArgumentException | IllegalStateException ex) {
                JOptionPane.showMessageDialog(frame, ex.getMessage());
            }
        }
    }

    // Метод для обновления информации на панели кошелька
    private void updateWalletPanel() {
        JTextArea reportArea = (JTextArea) ((JScrollPane) ((JPanel) cards.getComponent(2)).getComponent(0)).getViewport().getView();
        reportArea.setText(financeManager.getFinancialReport(currentUser));
    }
}
