import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.aston.cruddd.entity.User;

import java.util.List;

@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserDAOIntegrationTest {

    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    private UserDAO userDAO;

    @BeforeAll
    void setUp() {
        System.setProperty("hibernate.connection.url", postgres.getJdbcUrl());
        System.setProperty("hibernate.connection.username", postgres.getUsername());
        System.setProperty("hibernate.connection.password", postgres.getPassword());
        userDAO = new UserDAO();
    }

    @Test
    void testCreateAndFindUser() {
        User user = new User();
        user.setName("Test");
        user.setEmail("test@example.com");
        user.setAge(20);
        userDAO.createUser(user);

        User found = userDAO.getUserById(user.getId());
        Assertions.assertNotNull(found);
        Assertions.assertEquals("Test", found.getName());
    }

    @Test
    void testUpdateUser() {
        User user = new User();
        user.setName("Old");
        user.setEmail("old@example.com");
        user.setAge(30);
        userDAO.createUser(user);

        user.setName("New");
        userDAO.updateUser(user);

        User updated = userDAO.getUserById(user.getId());
        Assertions.assertEquals("New", updated.getName());
    }

    @Test
    void testDeleteUser() {
        User user = new User();
        user.setName("ToDelete");
        user.setEmail("del@example.com");
        user.setAge(18);
        userDAO.createUser(user);

        userDAO.deleteUser(user.getId());
        Assertions.assertNull(userDAO.getUserById(user.getId()));
    }

    @Test
    void testListUsers() {
        List<User> users = userDAO.getAllUsers();
        Assertions.assertNotNull(users);
    }
}
