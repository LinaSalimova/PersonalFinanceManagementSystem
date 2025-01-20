// Интерфейс для аутентификации пользователей
interface IUserAuthenticator {
    // Регистрирует нового пользователя
    void registerUser(String username, String password) throws Exception;

    // Аутентифицирует существующего пользователя
    boolean authenticateUser(String username, String password) throws Exception;
}