import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

// Реализация аутентификации пользователей
// Этот класс отвечает за регистрацию новых пользователей и проверку учетных данных при входе
class UserAuthenticator implements IUserAuthenticator {
    private static final int SALT_LENGTH = 16; // Длина соли для хеширования пароля
    private static final int MAX_LOGIN_ATTEMPTS = 5; // Максимальное количество попыток входа
    private static final long LOCKOUT_DURATION_MS = 15 * 60 * 1000; // Длительность блокировки аккаунта (15 минут)

    private Map<String, User> users = new HashMap<>(); // Хранение пользователей
    private Map<String, Integer> loginAttempts = new HashMap<>(); // Отслеживание попыток входа
    private Map<String, Long> lockedAccounts = new HashMap<>(); // Хранение заблокированных аккаунтов

    // Метод для регистрации нового пользователя
    @Override
    public void registerUser(String username, String password) throws Exception {
        // Проверка на существование пользователя
        if (users.containsKey(username)) {
            throw new Exception("Пользователь уже существует");
        }
        // Генерация соли и хеширование пароля
        byte[] salt = generateSalt();
        String hashedPassword = hashPassword(password, salt);
        users.put(username, new User(username, hashedPassword, salt));
    }

    // Метод для аутентификации пользователя
    @Override
    public boolean authenticateUser(String username, String password) throws Exception {
        // Проверка на блокировку аккаунта
        if (isAccountLocked(username)) {
            throw new Exception("Аккаунт заблокирован. Попробуйте позже.");
        }

        User user = users.get(username);
        if (user == null) {
            // Выполняем хеширование даже для несуществующего пользователя
            // Это защищает от атак по времени, которые могут определить существование пользователя
            MessageDigest.getInstance("SHA-256").digest(password.getBytes());
            incrementLoginAttempt(username);
            return false;
        }
        // Проверка пароля
        String hashedPassword = hashPassword(password, user.getSalt());
        if (hashedPassword.equals(user.getHashedPassword())) {
            loginAttempts.remove(username); // Сбрасываем счетчик попыток при успешном входе
            return true;
        } else {
            incrementLoginAttempt(username);
            return false;
        }
    }

    // Проверка, заблокирован ли аккаунт
    private boolean isAccountLocked(String username) {
        Long lockTime = lockedAccounts.get(username);
        if (lockTime != null) {
            if (System.currentTimeMillis() - lockTime > LOCKOUT_DURATION_MS) {
                // Если время блокировки истекло, разблокируем аккаунт
                lockedAccounts.remove(username);
                loginAttempts.remove(username);
                return false;
            }
            return true;
        }
        return false;
    }

    // Увеличение счетчика неудачных попыток входа
    private void incrementLoginAttempt(String username) {
        int attempts = loginAttempts.getOrDefault(username, 0) + 1;
        loginAttempts.put(username, attempts);
        if (attempts >= MAX_LOGIN_ATTEMPTS) {
            lockedAccounts.put(username, System.currentTimeMillis());
        }
    }

    // Генерация случайной соли для усиления безопасности хеширования пароля
    private byte[] generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return salt;
    }

    // Хеширование пароля с использованием соли
    private String hashPassword(String password, byte[] salt) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(salt);
        byte[] hashedPassword = md.digest(password.getBytes());
        return bytesToHex(hashedPassword);
    }

    // Преобразование массива байтов в шестнадцатеричную строку
    private static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }

    // Внутренний класс для хранения информации о пользователе
    private static class User {
        private String username;
        private String hashedPassword;
        private byte[] salt;

        public User(String username, String hashedPassword, byte[] salt) {
            this.username = username;
            this.hashedPassword = hashedPassword;
            this.salt = salt;
        }

        public String getHashedPassword() {
            return hashedPassword;
        }

        public byte[] getSalt() {
            return salt;
        }
    }
}

