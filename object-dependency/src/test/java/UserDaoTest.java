import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.sql.SQLException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import spring.object.dependency.User;
import spring.object.dependency.UserDao;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class UserDaoTest {

  @Autowired
  private UserDao userDao;

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


}
