package spring.dao;

import java.util.List;
import spring.user.User;

public interface UserDao {
    void add(User user);
    void update(User user);
    User get(String id);
    List<User> getAll();
    void deleteAll();
    int getCount();

}
