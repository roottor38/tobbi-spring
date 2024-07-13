package spring.user.service;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import spring.user.User;

@Transactional
public interface UserService {

  void add(User user);

  void deleteAll();

  void upgradeLevels();

  void update(User user);

  @Transactional(readOnly = true)
  User get(String id);

  @Transactional(readOnly = true)
  List<User> getAll();

}
