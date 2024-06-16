package spring.user.service;

import java.util.List;
import lombok.Setter;
import spring.dao.UserDao;
import spring.domain.Level;
import spring.user.User;

@Setter
public class UserService {

    private UserDao userDao;

    public void upgradeLevels() {
        userDao.getAll().forEach(user -> {
            if (canUpgradeLevel(user)) {
                upgradeLevel(user);
            }
        });
    }

    public void add(User user) {
        if (user.getLevel() == null) {
            user.setLevel(Level.BASIC);
        }
        userDao.add(user);
    }

    private boolean canUpgradeLevel(User user) {
        return switch (user.getLevel()) {
            case BASIC -> user.getLogin() >= 50;
            case SILVER -> user.getRecommend() >= 30;
            case GOLD -> false;
        };
    }

    private void upgradeLevel(User user) {
        user.upgradeLevel();
        userDao.update(user);
    }

}
