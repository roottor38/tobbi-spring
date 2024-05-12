package spring.object.dependency;

public class DaoFactory {
    public UserDao userDao() {
        ConnectionMaker connectionMaker = new DConnectionMaker();
        return new UserDao(connectionMaker);
    }
    public ConnectionMaker connectionMaker() {
        return new DConnectionMaker();
    }
}
