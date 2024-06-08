package spring.object.dependency;

import java.sql.*;
import javax.sql.DataSource;
import lombok.Setter;

@Setter
public class UserDao {

    private DataSource dataSource;

    public void add(User user) throws SQLException {

        try (
            Connection c = dataSource.getConnection();
            PreparedStatement ps = c.prepareStatement(
                "insert into users(id, name, password) values(?, ?, ?)")
        ) {
            ps.setString(1, user.getId());
            ps.setString(2, user.getName());
            ps.setString(3, user.getPassword());
            ps.executeUpdate();
        }
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
        try (
            Connection c = dataSource.getConnection();
            PreparedStatement ps = c.prepareStatement("DELETE from users")
        ) {
            ps.executeUpdate();
        }
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
}
