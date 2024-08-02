package config;

import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.HSQL;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.mail.MailSender;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import spring.dao.SqlService;
import spring.dao.UserDao;
import spring.user.service.DummyMailSender;
import spring.user.service.UserService;
import spring.user.service.UserServiceImpl;
import spring.user.sqlservice.OxmSqlService;
import spring.user.sqlservice.SqlRegistry;
import spring.user.sqlservice.updatable.EmbeddedDbSqlRegistry;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = "spring")
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
    public SqlService sqlService() {
        OxmSqlService sqlService = new OxmSqlService();
        sqlService.setUnmarshaller(unmarshaller());
        sqlService.setSqlRegistry(sqlRegistry());
        return sqlService;
    }

    @Bean
    public UserService testUserService() {
        return new UserServiceImpl.TestUserServiceImpl();
    }

    @Bean
    public MailSender mailSender(){
        return new DummyMailSender();
    }

    @Bean
    public SqlRegistry sqlRegistry() {
        EmbeddedDbSqlRegistry sqlRegistry = new EmbeddedDbSqlRegistry();
        sqlRegistry.setDataSource(embeddedDatabase());
        return sqlRegistry;
    }

    @Bean
    public Unmarshaller unmarshaller() {
        Jaxb2Marshaller unmarshaller = new Jaxb2Marshaller();
        unmarshaller.setContextPath("spring.user.sqlservice.jxb");
        return unmarshaller;
    }

    @Bean
    public DataSource embeddedDatabase() {
        return new EmbeddedDatabaseBuilder()
            .setName("embeddedDatabase")
            .setType(HSQL)
            .addScript("/schema.sql")
            .build();
    }

}
