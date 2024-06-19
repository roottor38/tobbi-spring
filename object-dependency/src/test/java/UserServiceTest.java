import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;
import javax.sql.DataSource;
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
import spring.user.service.UserService.TestUserService;
import spring.user.service.UserService.TestUserServiceException;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class UserServiceTest {

  @Autowired
  private UserService userService;

  @Autowired
  private DataSource dataSource;

  @Autowired
  private UserDao userDao;

  private User user;
  private List<User> users;

  @BeforeEach
  public void setUp() {
    user = new User();
    this.users = List.of(
        new User("id1", "name", "password", Level.BASIC, 50, 0),
        new User("id2", "name", "password", Level.SILVER, 55, 30),
        new User("id3", "name", "password", Level.SILVER, 100, 40),
        new User("id4", "name", "password", Level.BASIC, 60, 0),
        new User("id5", "name", "password", Level.GOLD, 100, 100)
    );

  }

  @Test
  public void upgradeAllOrNoting() throws Exception {
    UserService testUserService = new TestUserService(users.get(3).getId());
    testUserService.setUserDao(this.userDao);       // UserDao 를 직접 DI 해준다.
    testUserService.setDataSource(this.dataSource); // DataSource 를 직접 DI 해준다.
    userDao.deleteAll();
    users.forEach(userDao::add);

    try {
      // 작업 중에 예외가 발생해야 한다. 정상 종료라면 문제
      testUserService.upgradeLevels();
      testUserService.setDataSource(this.dataSource);
      //정상적 종료라면 fail() 때문에 실패 할 것이다.
      fail("TestUserServiceException expected");

    } catch (TestUserServiceException ignored) {
      System.out.println("TestUserServiceException 발생");
    }
    // 예외가 발생하기 전에 정상적으로 작업을 마무리해야 한다.
    checkLevelUpgraded(users.get(1), false);
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
