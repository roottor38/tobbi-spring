package spring;

import spring.object.dependency.DaoFactory;
import spring.object.dependency.User;
import spring.object.dependency.UserDao;

import java.sql.SQLException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        DaoFactory daoFactory = new DaoFactory();
        UserDao userDao = daoFactory.userDao();
        User user = new User();
        user.setId("id");
        user.setName("name");
        user.setPassword("password");

        userDao.add(user);

//        System.out.println(user.getId() + " 등록 성공");
    }
}