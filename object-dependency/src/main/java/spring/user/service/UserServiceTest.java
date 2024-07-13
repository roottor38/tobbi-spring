package spring.user.service;

import java.util.List;
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

    @Override
    public List<User> getAll() {
      for (User user : super.getAll()) {
        super.update(user);
      }
      return null;
    }
  }

  public static class TestUserServiceException extends RuntimeException {}

}
