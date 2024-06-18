package spring.user.service;

import java.util.List;
import lombok.Setter;
import spring.dao.UserDao;
import spring.domain.Level;
import spring.user.User;

@Setter
public class UserService {

  private UserDao userDao;
  private static final int MIN_LOGCOUNT_FOR_SILVER = 50;
  private static final int MIN_RECOMMEND_FOR_GOLD = 30;

  public void upgradeLevels() {
    List<User> users = userDao.getAll();
    users.forEach(this::upgradeLevel);
  }

  protected void upgradeLevel(User user) {
    if (canUpgradeLevel(user)) {
      user.upgradeLevel();
      userDao.update(user);
    }
  }

  public void add(User user) {
    if (user.getLevel() == null) {
      user.setLevel(Level.BASIC);
    }
    userDao.add(user);
  }

  private boolean canUpgradeLevel(User user) {
    return switch (user.getLevel()) {
      case BASIC -> user.getLogin() >= MIN_LOGCOUNT_FOR_SILVER;
      case SILVER -> user.getRecommend() >= MIN_RECOMMEND_FOR_GOLD;
      case GOLD -> false;
    };
  }
  public static class TestUserService extends UserService {

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
}
