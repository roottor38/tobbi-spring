import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import spring.dao.UserDao;
import spring.domain.Level;
import spring.user.User;
import spring.user.service.UserService;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserDao userDao;

    private List<User> users;

    @BeforeEach
    public void setUp() {
        this.users = List.of(
            new User("id1", "name", "password", Level.BASIC, 49, 0),
            new User("id2", "name", "password", Level.SILVER, 55, 10),
            new User("id3", "name", "password", Level.SILVER, 100, 40),
            new User("id4", "name", "password", Level.BASIC, 60, 0),
            new User("id5", "name", "password", Level.GOLD, 100, 100)
        );

    }

    @Test
    public void upgradeLevels() {
        List<User> users = userDao.getAll();
        userDao.deleteAll();
        users.forEach(userDao::add);

        userService.upgradeLevels();
        assertThat(users.get(0).getLevel()).isEqualTo(Level.BASIC);
        assertThat(users.get(1).getLevel()).isEqualTo(Level.SILVER);
        assertThat(users.get(2).getLevel()).isEqualTo(Level.GOLD);
        assertThat(users.get(3).getLevel()).isEqualTo(Level.SILVER);
        assertThat(users.get(4).getLevel()).isEqualTo(Level.GOLD);
    }


}
