import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import ru.aston.cruddd.entity.User;

public class ConsoleUI {
    private final UserDAO userDAO;
    private final Scanner scanner;
    private final DateTimeFormatter formatter;

    public ConsoleUI(UserDAO userDAO) {
        this.userDAO = userDAO;
        this.scanner = new Scanner(System.in);
        this.formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    }

    public void start() {
        while (true) {
            printMenu();
            int choice = readInt("Выберите действие: ");

            switch (choice) {
                case 1 -> createUser();
                case 2 -> getUser();
                case 3 -> updateUser();
                case 4 -> deleteUser();
                case 5 -> listAllUsers();
                case 6 -> {
                    System.out.println("Выход...");
                    return;
                }
                default -> System.out.println("Неверный выбор!");
            }
        }
    }

    private void printMenu() {
        System.out.println("\n=== Меню управления пользователями ===");
        System.out.println("1. Создать нового пользователя");
        System.out.println("2. Найти пользователя по ID");
        System.out.println("3. Обновить пользователя");
        System.out.println("4. Удалить пользователя");
        System.out.println("5. Список всех пользователей");
        System.out.println("6. Выход");
    }

    private void createUser() {
        System.out.println("\n--- Создание пользователя ---");
        String name = readString("Введите имя: ");
        String email = readString("Введите email: ");
        int age = readInt("Введите возраст: ");

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setAge(age);

        try {
            userDAO.createUser(user);
            System.out.println("Пользователь создан! ID: " + user.getId());
        } catch (Exception e) {
            System.err.println("Ошибка создания: " + e.getMessage());
        }
    }

    private void getUser() {
        System.out.println("\n--- Поиск пользователя ---");
        long id = readLong("Введите ID пользователя: ");

        try {
            User user = userDAO.getUserById(id);
            if (user != null) {
                printUser(user);
            } else {
                System.out.println("Пользователь не найден!");
            }
        } catch (Exception e) {
            System.err.println("Ошибка поиска: " + e.getMessage());
        }
    }

    private void updateUser() {
        System.out.println("\n--- Обновление пользователя ---");
        long id = readLong("Введите ID пользователя: ");

        try {
            User user = userDAO.getUserById(id);
            if (user == null) {
                System.out.println("Пользователь не найден!");
                return;
            }

            printUser(user);
            System.out.println("\nВведите новые данные:");

            user.setName(readString("Имя [" + user.getName() + "]: "));
            user.setEmail(readString("Email [" + user.getEmail() + "]: "));
            user.setAge(readInt("Возраст [" + user.getAge() + "]: "));

            userDAO.updateUser(user);
            System.out.println("Данные обновлены!");
        } catch (Exception e) {
            System.err.println("Ошибка обновления: " + e.getMessage());
        }
    }

    private void deleteUser() {
        System.out.println("\n--- Удаление пользователя ---");
        long id = readLong("Введите ID пользователя: ");

        try {
            User user = userDAO.getUserById(id);
            if (user == null) {
                System.out.println("Пользователь не найден!");
                return;
            }

            System.out.print("Вы уверены, что хотите удалить ");
            printUser(user);
            System.out.print("? (y/N): ");

            if (scanner.nextLine().equalsIgnoreCase("y")) {
                userDAO.deleteUser(id);
                System.out.println("Пользователь удалён!");
            } else {
                System.out.println("Удаление отменено");
            }
        } catch (Exception e) {
            System.err.println("Ошибка удаления: " + e.getMessage());
        }
    }

    private void listAllUsers() {
        try {
            List<User> users = userDAO.getAllUsers();
            System.out.println("\n--- Список пользователей (" + users.size() + ") ---");
            users.forEach(this::printUser);
        } catch (Exception e) {
            System.err.println("Ошибка получения списка: " + e.getMessage());
        }
    }

    private void printUser(User user) {
        System.out.println("\nID: " + user.getId());
        System.out.println("Имя: " + user.getName());
        System.out.println("Email: " + user.getEmail());
        System.out.println("Возраст: " + user.getAge());
        System.out.println("Создан: " + formatter.format(user.getCreatedAt()));
    }

    private String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private int readInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Некорректный ввод! Введите целое число");
            }
        }
    }

    private long readLong(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Long.parseLong(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Некорректный ввод! Введите число");
            }
        }
    }

    public static void main(String[] args) {
        new ConsoleUI(new UserDAO()).start();
    }
}
