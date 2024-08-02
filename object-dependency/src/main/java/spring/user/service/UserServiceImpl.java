package spring.user.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import spring.dao.UserDao;
import spring.domain.Level;
import spring.user.User;
import spring.user.exception.TestUserServiceException;

@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private MailSender mailSender;

    private static final int MIN_LOGCOUNT_FOR_SILVER = 50;
    private static final int MIN_RECOMMEND_FOR_GOLD = 30;

    public void upgradeLevels() {
        List<User> users = userDao.getAll();
        users.forEach(this::upgradeLevel);
    }

    private void sendUpgradeEmail(User user) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setFrom("useradmin@sug.org");
        mailMessage.setSubject("Upgrade 안내");
        mailMessage.setText("사용자 등급이 " + user.getLevel().name() + "로 업그레이드 되었습니다.");

        this.mailSender.send(mailMessage);

    }

    protected void upgradeLevel(User user) {
        if (canUpgradeLevel(user)) {
            user.upgradeLevel();
            userDao.update(user);
            sendUpgradeEmail(user);
        }
    }

    public void add(User user) {
        if (user.getLevel() == null) {
            user.setLevel(Level.BASIC);
        }
        userDao.add(user);
    }

    @Override
    public User get(String id) {
        return userDao.get(id);
    }

    @Override
    public List<User> getAll() {
        return userDao.getAll();
    }

    @Override
    public void update(User user) {
        userDao.update(user);
    }

    @Override
    public void deleteAll() {
        userDao.deleteAll();
    }

    private boolean canUpgradeLevel(User user) {
        return switch (user.getLevel()) {
            case BASIC -> user.getLogin() >= MIN_LOGCOUNT_FOR_SILVER;
            case SILVER -> user.getRecommend() >= MIN_RECOMMEND_FOR_GOLD;
            case GOLD -> false;
        };
    }

    public static class TestUserServiceImpl extends UserServiceImpl {

        @Override
        protected void upgradeLevel(User user) {
            System.out.println("Test upgradeLevel 실행");
            String id = "id4";
            if (user.getId().equals(id)) {
                throw new TestUserServiceException("TestUserServiceException 발생");
            }
            super.upgradeLevel(user);
        }

        @Override
        public List<User> getAll() {
            System.out.println("Test getAll 실행");
            for (User user : super.getAll()) {
                super.update(user);
            }
            return null;
        }
    }
}
