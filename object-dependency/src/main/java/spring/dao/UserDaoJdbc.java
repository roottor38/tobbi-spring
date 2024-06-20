package spring.dao;

import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import spring.domain.Level;
import spring.user.User;

public class UserDaoJdbc implements UserDao{

  private JdbcTemplate jdbcTemplate;

  public void setDataSource(DataSource dataSource) {
      this.jdbcTemplate = new JdbcTemplate(dataSource);
  }

    private final RowMapper<User> userMapper = (rs, rowNum) -> {
        User user = new User();
        user.setId(rs.getString("id"));
        user.setName(rs.getString("name"));
        user.setPassword(rs.getString("password"));
        user.setLevel(Level.valueOf(rs.getInt("LEVEL")));
        user.setLogin(rs.getInt("LOGIN"));
        user.setRecommend(rs.getInt("RECOMMEND"));
        user.setEmail(rs.getString("email"));
        return user;
    };

    public void add(User user) {
        jdbcTemplate.update(
            """
                INSERT INTO users (id, name, password, LEVEL, LOGIN, RECOMMEND, email)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """,
            user.getId(), user.getName(), user.getPassword(),
            user.getLevel().intValue(), user.getLogin(), user.getRecommend(), user.getEmail()
        );
    }

    public void update(User user) {
        jdbcTemplate.update(
            """
                UPDATE users
                SET name = ?, password = ?, LEVEL = ?, LOGIN = ?, RECOMMEND = ?, email = ? WHERE id = ?
                """,
            user.getName(), user.getPassword(), user.getLevel().intValue(), user.getLogin(), user.getRecommend(), user.getEmail(), user.getId()
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
          userMapper.mapRow(rs, rowNum);
            return userMapper.mapRow(rs, rowNum);
        });
    }

    public void deleteAll() {
      jdbcTemplate.update("DELETE FROM users");
    }

    public int getCount() {
      return jdbcTemplate.queryForObject("SELECT COUNT(*) FROM users", Integer.class);
    }

  public void setJdbcTemplate(SimpleDriverDataSource jdbcTemplate) {
  }
}
