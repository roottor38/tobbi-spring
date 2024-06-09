package spring.object.dependency;

import java.sql.*;
import javax.sql.DataSource;
import lombok.Setter;
import spring.dao.AddStatement;
import spring.dao.DeleteAllStatement;
import spring.dao.StatementStrategy;

@Setter
public class UserDao {

    private DataSource dataSource;

    public void add(User user) throws SQLException {
        StatementStrategy st = new AddStatement(user);
        jdbcContextWithStatementStrategy(st);
    }

    public User get(String id) throws SQLException {
        User user = null;
        try (
            Connection c = dataSource.getConnection();
            PreparedStatement ps = c.prepareStatement(
                "select * from users where id = ?")
        ) {
            ps.setString(1, id);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                user = new User();
                user.setId(rs.getString("id"));
                user.setName(rs.getString("name"));
                user.setPassword(rs.getString("password"));
            }
        }
        return user;
    }

    public void deleteAll() throws SQLException {
        StatementStrategy st = new DeleteAllStatement();
        jdbcContextWithStatementStrategy(st);
    }

    public int getCount() throws SQLException {
        int count = 0;

        try (Connection c = dataSource.getConnection();
            PreparedStatement ps = c.prepareStatement("SELECT COUNT(*) FROM users");
            ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                count = rs.getInt(1);
            }
        }
        return count;
    }

    public void jdbcContextWithStatementStrategy(StatementStrategy stmt) throws SQLException {
        Connection c = null;
        PreparedStatement ps = null;
        try {
            c = dataSource.getConnection();
            ps = stmt.makeStatement(c);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }
        finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                }
            }
            if (c != null) {
                try {
                    c.close();
                } catch (SQLException e) {
                }
            }
        }
    }
}
