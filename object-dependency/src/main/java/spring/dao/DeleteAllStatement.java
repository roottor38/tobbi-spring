package spring.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DeleteAllStatement implements StatementStrategy {

    @Override
    public PreparedStatement makeStatement(Connection c) throws SQLException {
        return c.prepareStatement("DELETE from users");
    }

}