package spring.dao;

import java.util.List;
import javax.sql.DataSource;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.stereotype.Repository;
import spring.domain.Level;
import spring.user.User;

@Repository
public class UserDaoJdbc implements UserDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    private SqlService sqlService;

    @Autowired
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
        jdbcTemplate.update(sqlService.getSql("userAdd"),
            user.getId(), user.getName(), user.getPassword(),
            user.getLevel().intValue(), user.getLogin(), user.getRecommend(), user.getEmail()
        );
    }

    public void update(User user) {
        jdbcTemplate.update(sqlService.getSql("userUpdate"),
            user.getName(), user.getPassword(), user.getLevel().intValue(), user.getLogin(),
            user.getRecommend(), user.getEmail(), user.getId()
        );
    }

    public User get(String id) {
        return jdbcTemplate.queryForObject(
            sqlService.getSql("userGet")
            , new Object[]{id}
            , userMapper
        );
    }

    public List<User> getAll() {
        return jdbcTemplate.query(sqlService.getSql("getAll"), (rs, rowNum) -> {
            userMapper.mapRow(rs, rowNum);
            return userMapper.mapRow(rs, rowNum);
        });
    }

    public void deleteAll() {
        jdbcTemplate.update(sqlService.getSql("deleteAll"));
    }

    public int getCount() {
        return jdbcTemplate.queryForObject(sqlService.getSql("getCount"), Integer.class);
    }

    public void setJdbcTemplate(SimpleDriverDataSource jdbcTemplate) {
    }

}
