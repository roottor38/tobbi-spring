package spring.dao;

import java.util.List;
import spring.user.User;

public interface UserDao {

    void add(User user);
    User get(String id);
    void deleteAll();
    int getCount();
    List<User> getAll();
    void update(User user);

}
