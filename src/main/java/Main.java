import ru.aston.cruddd.entity.User;

public class Main {
    public static void main(String[] args) {
        UserDAO userDAO = new UserDAO();

        User user = new User();
        user.setName("Иван");
        userDAO.createUser(user);

        User retrievedUser = userDAO.getUserById(user.getId());
        System.out.println("ru.aston.cruddd.entity.User name: " + retrievedUser.getName());

        retrievedUser.setName("Пётр");
        userDAO.updateUser(retrievedUser);
        userDAO.deleteUser(retrievedUser.getId());
    }
}
