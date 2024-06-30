import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;
import spring.dao.UserDao;
import spring.dao.UserDaoJdbc;
import spring.domain.Level;
import spring.user.User;
import spring.user.service.TransactionHandler;
import spring.user.service.UserService;
import spring.user.service.UserServiceImpl;
import spring.user.service.UserServiceTx;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
@DirtiesContext
public class UserServiceTest {

    @Autowired
    PlatformTransactionManager transactionManager;

    @Autowired
    private UserService userService;

    @Autowired
    private MailSender mailSender;

    @Autowired
    private UserDaoJdbc userDao;

    private User user;
    private List<User> users;
    @Autowired
    private UserServiceImpl userServiceImpl;

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
    public void upgradeAllOrNoting() throws Exception {
        UserServiceImpl testUserService = new UserServiceImpl();
        MockUserDao mockUserDao = new MockUserDao(users);
        testUserService.setUserDao(mockUserDao);
        testUserService.setMailSender(mailSender);

        TransactionHandler txHandler = new TransactionHandler();
        txHandler.setTarget(testUserService);
        txHandler.setTransactionManager(transactionManager);
        txHandler.setPattern("upgradeLevels");

        UserService txUserService = (UserService) Proxy.newProxyInstance(
            getClass().getClassLoader(),
            new Class[]{UserService.class},
            txHandler
        );

        userDao.deleteAll();
        users.forEach(userDao::add);

        try {
            // 작업 중에 예외가 발생해야 한다. 정상 종료라면 문제
            txUserService.upgradeLevels();
            //정상적 종료라면 fail() 때문에 실패 할 것이다.
            fail("TestUserServiceException expected");

        } catch (TestUserServiceException ignored) {
            System.out.println("TestUserServiceException 발생");
        }
        // 예외가 발생하기 전에 정상적으로 작업을 마무리해야 한다.
        checkLevelUpgraded(users.get(1), true);
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

    @Test
    public void upgradeLevels() {
        userDao.deleteAll();
        users.forEach(userDao::add);

        MockMailSender mockMailSender = new MockMailSender();
        userServiceImpl.setMailSender(mockMailSender);

        userService.upgradeLevels();

        checkLevelUpgraded(users.get(0), true);
        checkLevelUpgraded(users.get(1), true);
        checkLevelUpgraded(users.get(2), true);
        checkLevelUpgraded(users.get(3), true);
        checkLevelUpgraded(users.get(4), false);

        List<String> requests = mockMailSender.getRequests();
        assertThat(requests).hasSize(4);
        assertThat(requests.get(0)).isEqualTo( users.get(0).getEmail());


    }

    private void checkLevelUpgraded(User user, boolean upgraded) {
        User userUpdate = userDao.get(user.getId());
        if (upgraded) {
            assertThat(userUpdate.getLevel()).isEqualTo(user.getLevel().nextLevel());
        } else {
            assertThat(userUpdate.getLevel()).isEqualTo(user.getLevel());
        }
    }

    static class TestUserService extends UserServiceImpl {

        private String id;

        public TestUserService(String id) {
            this.id = id;
        }

        @Override
        protected void upgradeLevel(User user) {
            if (user.getId().equals(this.id)) {
                throw new TestUserServiceException();
            }
            super.upgradeLevel(user);
        }
    }

    public static class TestUserServiceException extends RuntimeException {

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

    static class MockUserDao implements UserDao {

        private final List<User> users;
        private final List<User> updated = new ArrayList<>();

        private MockUserDao(List<User> users) {
            this.users = users;
        }

        public List<User> getUpdated() {
            return updated;
        }

        @Override
        public void add(User user) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void update(User user) {
            updated.add(user);
        }

        @Override
        public User get(String id) {
            return null;
        }


        @Override
        public List<User> getAll() {
            return users;
        }

        @Override
        public void deleteAll() {
            throw new UnsupportedOperationException();
        }

        @Override
        public int getCount() {
            throw new UnsupportedOperationException();
        }
    }


}
