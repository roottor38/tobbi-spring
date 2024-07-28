package spring.user.sqlservice;

import java.util.Map;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import spring.user.exception.SqlNotFoundException;
import spring.user.exception.SqlUpdateFailureException;

public class EmbeddedDbSqlRegistry implements UpdatableSqlRegistry {
    NamedParameterJdbcTemplate jdbc;

    /*
        내장 DB 빌더가 Datasource의 서브 인스턴스인 EmbeddedDatebase를 반환 해도
        Datasource로 인자를 받는 이유는, 인터페이스 분리 원칙을 지키기 위함임.
        클라이언트는 자신이 필요한 기능을 가진 인터페이스를 DI 받아야함.
        SQL 레지스트리는 JDBC를 이용해 DB에 접근만 하면 되므로 Datasource가 가장 적합함
     */
    public void setDataSource(DataSource jdbc) {
        this.jdbc =  new NamedParameterJdbcTemplate(jdbc);
    }

    public void registerSql(String key, String sql) {
        jdbc.update(
            "INSERT INTO sqlmap(key_, sql_) VALUES(:key, :sql)",
            Map.of("key", key, "sql", sql)
        );
    }

    public String findSql(String key) throws SqlNotFoundException {
        try{
            return jdbc.queryForObject(
                "SELECT sql_ FROM sqlmap WHERE key_ = :key",
                Map.of("key", key),
                String.class
            );
        }catch (EmptyResultDataAccessException e){
            throw new SqlNotFoundException(key + "에 해당하는 SQL을 찾을 수 없습니다.");
        }
    }

    public void updateSql(String key, String sql) throws SqlUpdateFailureException {
        int affected = jdbc.update("update sqlmap set sql_ = :sql where key_ = :key",
            Map.of("key", key, "sql", sql));
        if (affected == 0) {
            throw new SqlNotFoundException(key + "에 해당하는 SQL을 찾을 수 없습니다.");
        }
    }

    public void updateSql(Map<String, String> sqlmap) throws SqlUpdateFailureException {
        for (Map.Entry<String, String> entry : sqlmap.entrySet()) {
            updateSql(entry.getKey(), entry.getValue());
        }
    }

}
