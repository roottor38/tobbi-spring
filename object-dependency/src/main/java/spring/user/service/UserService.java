package spring.user.service;

import spring.user.User;

public interface UserService {
    void upgradeLevels();
    void add(User user);
}
