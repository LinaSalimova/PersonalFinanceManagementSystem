public class Main {
    public static void main(String[] args) {
        IUserAuthenticator userAuth = new UserAuthenticator();
        IFileManager fileManager = new FileManager();
        FinanceManager financeManager = new FinanceManager(userAuth, fileManager);
        FinanceManagerGUI gui = new FinanceManagerGUI(financeManager);
    }
}
