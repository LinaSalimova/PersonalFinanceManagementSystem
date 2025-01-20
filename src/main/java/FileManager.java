import java.io.*;

// Реализация работы с файлами
// Этот класс отвечает за сохранение и загрузку данных кошелька в/из файла
class FileManager implements IFileManager {
    private static final String FILE_PATH = "wallets/";

    @Override
    public void saveWallet(String username, IWallet wallet) throws IOException {
        File directory = new File(FILE_PATH);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH + username + ".dat"))) {
            oos.writeObject(wallet);
        }
    }

    @Override
    public IWallet loadWallet(String username) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH + username + ".dat"))) {
            return (IWallet) ois.readObject();
        } catch (FileNotFoundException e) {
            return new Wallet(username); // Если файл не найден, создаем новый кошелек
        }
    }
}
