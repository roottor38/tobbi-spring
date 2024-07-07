package spring.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import spring.user.User;

public class UserServiceTest {

  static class TestUserServiceImpl extends UserServiceImpl {

    @Override
    protected void upgradeLevel(User user) {
      String id = "id4";
      if (user.getId().equals(id)) {
        throw new TestUserServiceException();
      }
      super.upgradeLevel(user);
    }
  }

  public static class TestUserServiceException extends RuntimeException {}

}
