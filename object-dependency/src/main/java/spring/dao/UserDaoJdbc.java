package spring.dao;

import java.util.List;
import javax.sql.DataSource;
import lombok.Setter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import spring.domain.Level;
import spring.user.User;

@Setter
public class UserDaoJdbc implements UserDao {

    private JdbcTemplate jdbcTemplate;
    private DataSource dataSource;

    private RowMapper<User> userMapper = (rs, rowNum) -> {
        User user = new User();
        user.setId(rs.getString("id"));
        user.setName(rs.getString("name"));
        user.setPassword(rs.getString("password"));
        user.setLevel(Level.valueOf(rs.getInt("LEVEL")));
        user.setLogin(rs.getInt("LOGIN"));
        user.setRecommend(rs.getInt("RECOMMEND"));
        return user;
    };

    public void setJdbcTemplate(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void setDataSource(SimpleDriverDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void add(User user) {
        jdbcTemplate.update(
            "INSERT INTO users (id, name, password, LEVEL, LOGIN, RECOMMEND) VALUES (?, ?, ?, ?, ?, ?)",
            user.getId(), user.getName(), user.getPassword(),
            user.getLevel().intValue(), user.getLogin(), user.getRecommend()
        );
    }

    public void update(User user) {
        jdbcTemplate.update(
            """
                UPDATE users
                SET name = ?, password = ?, LEVEL = ?, LOGIN = ?, RECOMMEND = ? WHERE id = ?
                """,
            user.getName(), user.getPassword(), user.getLevel().intValue(), user.getLogin(), user.getRecommend(), user.getId()
        );
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
            user.setLevel(Level.valueOf(rs.getInt("LEVEL")));
            user.setLogin(rs.getInt("LOGIN"));
            user.setRecommend(rs.getInt("RECOMMEND"));
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
