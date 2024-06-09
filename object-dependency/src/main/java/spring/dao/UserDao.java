package spring.dao;

import java.sql.*;
import javax.sql.DataSource;
import lombok.Setter;
import spring.object.dependency.User;

@Setter
public class UserDao {

    private DataSource dataSource;
    private JdbcContext jdbcContext;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcContext = new JdbcContext();
        this.jdbcContext.setDataSource(dataSource);
    }

    public void add(User user) throws SQLException {
        jdbcContext.workWithStatementStrategy(c -> {
            PreparedStatement ps = c.prepareStatement("INSERT INTO users (id, name, password) VALUES (?, ?, ?)");
            ps.setString(1, user.getId());
            ps.setString(2, user.getName());
            ps.setString(3, user.getPassword());
            return ps;
        });
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
        StatementStrategy st = c -> c.prepareStatement("DELETE from users");
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
        try (Connection c = dataSource.getConnection(); PreparedStatement ps = stmt.makeStatement(c)) {
            ps.executeUpdate();
        }
    }
}
