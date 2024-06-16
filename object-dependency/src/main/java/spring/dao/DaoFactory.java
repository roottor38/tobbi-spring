package spring.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import javax.sql.DataSource;

@Configuration
public class DaoFactory {

  @Bean
  public UserDaoJdbc userDao() {
    UserDaoJdbc userDaoJdbc = new UserDaoJdbc();
    userDaoJdbc.setDataSource(dataSource2());
    userDaoJdbc.setJdbcTemplate(dataSource());
    return userDaoJdbc;
  }

  @Bean
  public SimpleDriverDataSource dataSource2() {
    SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
    dataSource.setDriverClass(com.mysql.cj.jdbc.Driver.class);
    dataSource.setUrl("jdbc:mysql://localhost/springbook");
    dataSource.setUsername("spring");
    dataSource.setPassword("book");
    return dataSource;
  }

  @Bean
  public DataSource dataSource() {
    SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
    dataSource.setDriverClass(com.mysql.cj.jdbc.Driver.class);
    dataSource.setUrl("jdbc:mysql://localhost/springbook");
    dataSource.setUsername("spring");
    dataSource.setPassword("book");
    return dataSource;
  }
}
