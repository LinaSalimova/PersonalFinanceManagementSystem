import java.io.IOException;

// Интерфейс для работы с файлами
// Этот интерфейс определяет методы для сохранения и загрузки данных кошелька
interface IFileManager {
    void saveWallet(String username, IWallet wallet) throws IOException;
    IWallet loadWallet(String username) throws IOException, ClassNotFoundException;
}