import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.sql.SQLException;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import spring.dao.UserDao;
import spring.domain.Level;
import spring.user.User;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class UserDaoTest {

    @Autowired
    private UserDao dao;

    @Autowired
    private DataSource dataSource;

    private static User user1;
    private static User user2;
    private static User user3;

    @BeforeAll
    public static void setUp() {
        user1 = new User("id1", "name1", "password1", Level.BASIC, 1, 0, "user@user.co.kr");
        user2 = new User("id2", "name2", "password2", Level.SILVER, 55, 10, "user@user.co.kr");
        user3 = new User("id3", "name3", "password3", Level.GOLD, 100, 40, "user@user.co.kr");
    }

    @Test
    public void sqlExceptionTranslate() {
        dao.deleteAll();
        try {
            dao.add(user1);
            dao.add(user1);
        } catch (DuplicateKeyException e) {
            SQLException sqlException = (SQLException) e.getRootCause();
            SQLExceptionTranslator set = new SQLErrorCodeSQLExceptionTranslator(this.dataSource);
            assertThat(set.translate(null, null, sqlException).getMessage()).contains("Duplicate entry");
        }
        assertThat(dataSource).isNotNull();
    }

    @Test
    public void addAndGet() throws SQLException, ClassNotFoundException {
        dao.deleteAll();
        assertThat(dao.getCount()).isEqualTo(0);

        dao.add(user1);
        dao.add(user2);
        assertThat(dao.getCount()).isEqualTo(2);

        User userget1 = dao.get(user1.getId());
        checkSumUser(user1, userget1);

        User userget2 = dao.get(user2.getId());
        checkSumUser(user2, userget2);
    }

    @Test
    public void count() throws SQLException, ClassNotFoundException {
        dao.deleteAll();
        assertThat(dao.getCount()).isEqualTo(0);

        dao.add(user1);
        assertThat(dao.getCount()).isEqualTo(1);

        dao.add(user2);
        assertThat(dao.getCount()).isEqualTo(2);

        dao.add(user3);
        assertThat(dao.getCount()).isEqualTo(3);
        assertNotSame(dao.getCount(), 2);
    }

    public void getUserFailure() throws SQLException, ClassNotFoundException {
        dao.deleteAll();
        assertThat(dao.getCount()).isEqualTo(0);
        assertThrows(EmptyResultDataAccessException.class, () -> dao.get("unknown_id"));
    }

    @Test
    public void getAll() throws SQLException, ClassNotFoundException {
        dao.deleteAll();
        assertThat(dao.getCount()).isEqualTo(0);

        dao.add(user1);
        assertThat(dao.getCount()).isEqualTo(1);

        dao.add(user2);
        assertThat(dao.getCount()).isEqualTo(2);

        dao.add(user3);
        assertThat(dao.getCount()).isEqualTo(3);

        dao.getAll().forEach(user -> {
            assertThat(user.getId()).isIn("id1", "id2", "id3");
            assertThat(user.getName()).isIn("name1", "name2", "name3");
            assertThat(user.getPassword()).isIn("password1", "password2", "password3");
        });

        assertThat(dao.getAll().size()).isEqualTo(3);
    }

    @Test
    public void update() throws SQLException, ClassNotFoundException {
        dao.deleteAll();
        assertThat(dao.getCount()).isEqualTo(0);

        dao.add(user1);
        dao.add(user2);
        assertThat(dao.getCount()).isEqualTo(2);

        user1.setName("오민규");
        user1.setPassword("springno1");
        user1.setLevel(Level.GOLD);
        user1.setLogin(1000);
        user1.setRecommend(999);

        dao.update(user1);

        User userget1 = dao.get(user1.getId());
        checkSumUser(user1, userget1);

        User userget2 = dao.get(user2.getId());
        checkSumUser(user2, userget2);
    }

    private void checkSumUser(User user, User userget) {
        assertThat(user.getId()).isEqualTo(userget.getId());
        assertThat(user.getName()).isEqualTo(userget.getName());
        assertThat(user.getPassword()).isEqualTo(userget.getPassword());
        assertThat(user.getLevel()).isEqualTo(userget.getLevel());
        assertThat(user.getLogin()).isEqualTo(userget.getLogin());
        assertThat(user.getRecommend()).isEqualTo(userget.getRecommend());
//        assertThat(user).isEqualTo(userget);


    }
}
