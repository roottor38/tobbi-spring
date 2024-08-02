package spring.config;

import java.sql.Driver;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
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
@PropertySource("/database.properties")
@EnableSqlService
public class AppContext implements SqlMapConfig {

    @Value("${db.driverClass}") Class<? extends Driver> driverClass;
    @Value("${db.url}") String url;
    @Value("${db.username}") String username;
    @Value("${db.password}") String password;

    @Bean
    public DataSource dataSource() {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        //자바 코드에서 프로퍼티 driverClass의 스트링 값을 자동으로 변환하지 않기 때문에, 적절한 타입으로 변환해 주어야 함
//        dataSource.setDriverClass(Driver.class);
        try {
            dataSource.setDriverClass(driverClass);
            dataSource.setUrl(url);
            dataSource.setUsername(username);
            dataSource.setPassword(password);
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
    public MailSender mailSender() {
        return new DummyMailSender();
    }

//    @Bean
//    public static PropertySourcesPlaceholderConfigurer placeholderConfigurer(){
//        return new PropertySourcesPlaceholderConfigurer();
//    }

    @Override
    public Resource getSqlMapResource() {
        return new ClassPathResource("/sqlmap.xml", UserDao.class);
    }

    @Configuration
    @Profile("test")
    public static class TestAppContext {

        @Bean
        public UserService testUserService() {
            return new UserServiceImpl.TestUserServiceImpl();
        }

        @Bean
        public MailSender mailSender(){
            return new DummyMailSender();
        }
    }

}
