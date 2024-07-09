package spring.user.service;

import java.util.List;
import spring.user.User;

public interface UserService {
    void add(User user);
    User get(String id);
    List<User> getAll();
    void deleteAll();
    void upgradeLevels();
    void update(User user);
}
