package spring.dao;

import java.sql.*;
import java.util.List;
import javax.sql.DataSource;
import lombok.Setter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import spring.object.dependency.User;

@Setter
public class UserDao {

    private JdbcTemplate jdbcTemplate;

    private RowMapper<User> userMapper = (rs, rowNum) -> {
        User user = new User();
        user.setId(rs.getString("id"));
        user.setName(rs.getString("name"));
        user.setPassword(rs.getString("password"));
        return user;
    };

    public void setJdbcTemplate(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void add(User user) {
      jdbcTemplate.update("INSERT INTO users (id, name, password) VALUES (?, ?, ?)", user.getId(), user.getName(), user.getPassword());
    }

    public User get(String id) {
        return jdbcTemplate.queryForObject(
            "SELECT * FROM users WHERE id = ?"
            , new Object[] {id}
            , userMapper
        );
    }

    public List<User> getAll() {
        return jdbcTemplate.query("SELECT * FROM users", (rs, rowNum) -> {
            User user = new User();
            user.setId(rs.getString("id"));
            user.setName(rs.getString("name"));
            user.setPassword(rs.getString("password"));
            return user;
        });
    }

    public void deleteAll() {
      jdbcTemplate.update("DELETE FROM users");
    }

    public int getCount() {
      return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM users", Integer.class);
    }
}
