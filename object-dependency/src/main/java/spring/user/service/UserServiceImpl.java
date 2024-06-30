package spring.user.service;

import java.util.List;
import lombok.Setter;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import spring.dao.UserDao;
import spring.domain.Level;
import spring.user.User;

@Setter
public class UserServiceImpl implements UserService{

  private UserDao userDao;
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

  private boolean canUpgradeLevel(User user) {
    return switch (user.getLevel()) {
      case BASIC -> user.getLogin() >= MIN_LOGCOUNT_FOR_SILVER;
      case SILVER -> user.getRecommend() >= MIN_RECOMMEND_FOR_GOLD;
      case GOLD -> false;
    };
  }
}
