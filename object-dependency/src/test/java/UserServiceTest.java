import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;
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

  private User user;
  private List<User> users;

  @BeforeEach
  public void setUp() {
    user = new User();
    this.users = List.of(
        new User("id1", "name", "password", Level.BASIC, 49, 0),
        new User("id2", "name", "password", Level.SILVER, 55, 10),
        new User("id3", "name", "password", Level.SILVER, 100, 40),
        new User("id4", "name", "password", Level.BASIC, 60, 0),
        new User("id5", "name", "password", Level.GOLD, 100, 100)
    );

  }

  @Test
  public void upgradeLevel() {
    Level[] levels = Level.values();
    Arrays.stream(levels).forEach(level -> {
      user.setLevel(level);
      user.upgradeLevel();
      assertThat(user.getLevel()).isEqualTo(level.nextLevel());
    });
  }

  @Test
  public void cannotUpgradeLevel() {
    Level[] levels = Level.values();
    for (Level level : levels) {
      if (level.nextLevel() != null) {
        continue;
      }
      user.setLevel(level);
      assertThrows(IllegalStateException.class, user::upgradeLevel);
    }
  }

  @Test
  public void add() {
    userDao.deleteAll();
    User userWithLevel = users.get(4);
    User userWithoutLevel = users.get(0);
    userWithoutLevel.setLevel(null);

    userService.add(userWithLevel);
    userService.add(userWithoutLevel);

    User userWithLevelRead = userDao.get(userWithLevel.getId());
    User userWithoutLevelRead = userDao.get(userWithoutLevel.getId());

    assertThat(userWithLevelRead.getLevel()).isEqualTo(userWithLevel.getLevel());
    assertThat(userWithoutLevelRead.getLevel()).isEqualTo(Level.BASIC);
  }

  private void checkLevelUpgraded(User user, boolean upgraded) {
    User userUpdate = userDao.get(user.getId());
    if (upgraded) {
      assertThat(userUpdate.getLevel()).isEqualTo(user.getLevel().nextLevel());
    } else {
      assertThat(userUpdate.getLevel()).isEqualTo(user.getLevel());
    }
  }


}
