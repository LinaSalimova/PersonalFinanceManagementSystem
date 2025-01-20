## Система управления личными финансами
Это Java-приложение позволяет пользователям управлять своими личными финансами, отслеживать доходы и расходы, устанавливать бюджеты и просматривать финансовые отчеты.
# Требования
- Java 11 или выше
- Maven (для сборки проекта)
# Установка и запуск
- Клонируйте репозиторий:
text
git clone https://github.com/LinaSalimova/PersonalFinanceManagementSystem.git
- Перейдите в директорию проекта:
text
cd PersonalFinanceManagementSystem
- Соберите проект с помощью Maven:
text
mvn clean install
- Запустите приложение:
text
java -cp target/classes FinanceManagerApp
# Использование
- При первом запуске вам будет предложено зарегистрироваться или войти в систему.
- После входа в систему вы можете использовать следующие команды:
  1. addIncome <сумма> <категория> - добавить доход
  2. addExpense <сумма> <категория> - добавить расход
  3. setBudget <категория> <сумма> - установить бюджет для категории
  4. getReport - получить финансовый отчет
  5. transferFunds <получатель> <сумма> - перевести средства другому пользователю
  6. logout - выйти из системы
  7. Для выхода из приложения используйте команду exit.
# Функциональность
- Регистрация и аутентификация пользователей
- Добавление доходов и расходов с категоризацией
- Установка бюджетов для различных категорий
- Генерация финансовых отчетов
- Перевод средств между пользователями
- Автоматическое сохранение данных при выходе из приложения
# Структура проекта
- FinanceManagerApp.java - главный класс приложения
- FinanceManager.java - основная логика управления финансами
- UserAuthenticator.java - аутентификация пользователей
- Wallet.java - управление кошельком пользователя
- Transaction.java - представление транзакций
- Category.java - управление категориями доходов/расходов
- FileManager.java - сохранение и загрузка данных
# Безопасность
- Пароли хешируются с использованием соли
- Реализована защита от атак перебором (блокировка после нескольких неудачных попыток входа)
