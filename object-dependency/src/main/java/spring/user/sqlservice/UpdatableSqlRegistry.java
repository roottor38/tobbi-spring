package spring.user.sqlservice;

import java.util.Map;
import spring.user.exception.SqlUpdateFailureException;

public interface UpdatableSqlRegistry extends SqlRegistry {
    void updateSql(String key, String sql) throws SqlUpdateFailureException;
    void updateSql(Map<String, String> sqlmap) throws SqlUpdateFailureException;

}
