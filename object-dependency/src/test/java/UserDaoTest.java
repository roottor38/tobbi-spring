import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.sql.SQLException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import spring.object.dependency.User;
import spring.dao.UserDao;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class UserDaoTest {

  @Autowired
  private UserDao userDao;

  private static User user1;
  private static User user2;
  private static User user3;

  @BeforeAll
  public static void setUp() {
    user1 = new User("id1", "name1", "password1");
    user2 = new User("id2", "name2", "password2");
    user3 = new User("id3", "name3", "password3");
  }

  @Test
  public void addAndGet() throws SQLException, ClassNotFoundException {
    User user1 = new User("id1", "name1", "password1");
    User user2 = new User("id2", "name2", "password2");

    userDao.deleteAll();
    assertThat(userDao.getCount()).isEqualTo(0);

    userDao.add(user1);
    userDao.add(user2);
    assertThat(userDao.getCount()).isEqualTo(2);

    User userget1 = userDao.get(user1.getId());
    assertThat(userget1.getName()).isEqualTo(user1.getName());
    assertThat(userget1.getPassword()).isEqualTo(user1.getPassword());

    User userget2 = userDao.get(user2.getId());
    assertThat(userget2.getName()).isEqualTo(user2.getName());
    assertThat(userget2.getPassword()).isEqualTo(user2.getPassword());
  }

  @Test
  public void count() throws SQLException, ClassNotFoundException {
    User user1 = new User("id1", "name1", "password1");
    User user2 = new User("id2", "name2", "password2");
    User user3 = new User("id3", "name3", "password3");

    userDao.deleteAll();
    assertThat(userDao.getCount()).isEqualTo(0);

    userDao.add(user1);
    assertThat(userDao.getCount()).isEqualTo(1);

    userDao.add(user2);
    assertThat(userDao.getCount()).isEqualTo(2);

    userDao.add(user3);
    assertThat(userDao.getCount()).isEqualTo(3);
    assertNotSame(userDao.getCount(), 2);
  }

  public void getUserFailure() throws SQLException, ClassNotFoundException {
    userDao.deleteAll();
    assertThat(userDao.getCount()).isEqualTo(0);
    assertThrows(EmptyResultDataAccessException.class, () -> userDao.get("unknown_id"));
  }

  @Test
  public void getAll() throws SQLException, ClassNotFoundException {
    userDao.deleteAll();
    assertThat(userDao.getCount()).isEqualTo(0);

    userDao.add(user1);
    assertThat(userDao.getCount()).isEqualTo(1);

    userDao.add(user2);
    assertThat(userDao.getCount()).isEqualTo(2);

    userDao.add(user3);
    assertThat(userDao.getCount()).isEqualTo(3);

    userDao.getAll().forEach(user -> {
      assertThat(user.getId()).isIn("id1", "id2", "id3");
      assertThat(user.getName()).isIn("name1", "name2", "name3");
      assertThat(user.getPassword()).isIn("password1", "password2", "password3");
    });

    assertThat(userDao.getAll().size()).isEqualTo(3);
  }

}
