package config;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.mail.MailSender;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import spring.dao.UserDao;
import spring.user.service.DummyMailSender;
import spring.user.service.UserService;
import spring.user.service.UserServiceImpl;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = "spring")
@Import(SqlServiceContext.class)
@Profile("test")
public class TestApplicationContext {

       @Autowired
       private UserDao userDao;

    @Bean
    public DataSource dataSource() {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        //자바 코드에서 프로퍼티 driverClass의 스트링 값을 자동으로 변환하지 않기 때문에, 적절한 타입으로 변환해 주어야 함
//        dataSource.setDriverClass(Driver.class);
        try {
            dataSource.setDriverClass(com.mysql.jdbc.Driver.class);
            dataSource.setUrl("jdbc:mysql://localhost:3306/springbook");
            dataSource.setUsername("spring");
            dataSource.setPassword("book");
        } catch (Exception ignored) {

        }

        return dataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        DataSourceTransactionManager tm = new DataSourceTransactionManager();
        tm.setDataSource(dataSource());
        return tm;
    }

    @Bean
    public UserService testUserService() {
        return new UserServiceImpl.TestUserServiceImpl();
    }

    @Bean
    public MailSender mailSender(){
        return new DummyMailSender();
    }

}
