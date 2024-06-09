package spring.object.dependency;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDaoDeleteAll extends UserDao {

    protected PreparedStatement makeStatement(Connection e) throws SQLException {
        return e.prepareStatement("DELETE from users");
    }

}
