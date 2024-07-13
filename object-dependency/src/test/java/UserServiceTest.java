import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.TransientDataAccessResourceException;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;
import spring.dao.UserDaoJdbc;
import spring.domain.Level;
import spring.user.User;
import spring.user.service.UserService;
import spring.user.service.UserServiceImpl;
import spring.user.service.UserServiceTest.TestUserServiceException;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
@DirtiesContext
public class UserServiceTest {

  @Autowired
  PlatformTransactionManager transactionManager;

  @Autowired
  private UserService userService;

//  @Autowired
//  private MailSender mailSender;

  @Autowired
  private UserDaoJdbc userDao;

  @Autowired
  ApplicationContext context;

  @Autowired
  private UserService testUserService;

  private User user;
  private List<User> users;

  @BeforeEach
  public void setUp() {
    user = new User();
    this.users = List.of(
        new User("id1", "name", "password", Level.BASIC, 50, 0, "user1@user.co.kr"),
        new User("id2", "name", "password", Level.SILVER, 55, 30, "user2@user.co.kr"),
        new User("id3", "name", "password", Level.SILVER, 100, 40, "user3@user.co.kr"),
        new User("id4", "name", "password", Level.BASIC, 60, 0, "user4@user.co.kr"),
        new User("id5", "name", "password", Level.GOLD, 100, 100, "user5@user.co.kr")
    );

  }

  @Test
  public void transactionSync() {
    userService.deleteAll();
    userService.add(users.get(0));
    userService.add(users.get(1));
  }

  @Test()
  public void readOnlyTransactionAttribute() {
    assertThrows(TransientDataAccessResourceException.class, () -> testUserService.getAll());
  }

  @Test
  public void advisorAutoProxyCreator() {
    assertThat(testUserService).isInstanceOf(java.lang.reflect.Proxy.class);
  }

  @Test
  public void upgradeAllOrNoting() throws Exception {

    userDao.deleteAll();
    users.forEach(userDao::add);

    try {
      // 작업 중에 예외가 발생해야 한다. 정상 종료라면 문제
      this.testUserService.upgradeLevels();
      //정상적 종료라면 fail() 때문에 실패 할 것이다.
      fail("TestUserServiceException expected");

    } catch (TestUserServiceException ignored) {
      System.out.println("TestUserServiceException 발생");
    }
    // 예외가 발생하기 전에 정상적으로 작업을 마무리해야 한다.
    checkLevelUpgraded(users.get(1), false);
  }

  private void checkUserAndLevel(User updated, String expectedId, Level expectedLevel) {
    assertThat(updated.getId()).isEqualTo(expectedId);
    assertThat(updated.getLevel()).isEqualTo(expectedLevel);
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

  @Getter
  @NoArgsConstructor
  static class MockMailSender implements MailSender {

    private final List<String> requests = new ArrayList<>();

    public List<String> getRequests() {
      return requests;
    }

    @Override
    public void send(SimpleMailMessage simpleMessage) throws MailException {
      requests.add(Objects.requireNonNull(simpleMessage.getTo())[0]);
    }

    @Override
    public void send(SimpleMailMessage... simpleMessages) throws MailException {
    }
  }

}
