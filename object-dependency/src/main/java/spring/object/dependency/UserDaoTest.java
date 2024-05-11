package spring.object.dependency;

public class UserDaoTest {

    public static void main(String[] args) {
        ConnectionMaker connectionMaker = new NConnectionMaker();
        UserDao userDao = new UserDao(connectionMaker);
    }
}
