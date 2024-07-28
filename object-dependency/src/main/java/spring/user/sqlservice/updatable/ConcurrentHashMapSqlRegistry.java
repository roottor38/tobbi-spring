package spring.user.sqlservice.updatable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import spring.user.exception.SqlNotFoundException;
import spring.user.sqlservice.UpdatableSqlRegistry;

public class ConcurrentHashMapSqlRegistry implements UpdatableSqlRegistry {

    private final Map<String, String> sqlMap = new ConcurrentHashMap<>();

    public String findSql(String key) throws SqlNotFoundException {
        String sql = sqlMap.get(key);
        if (sql == null) {
            throw new SqlNotFoundException(key + "에 대한 SQL을 찾을 수 없습니다.");
        } else {
            return sql;
        }
    }

    public void registerSql(String key, String sql) {
        sqlMap.put(key, sql);
    }

    public void updateSql(String key, String sql) {
        if (sqlMap.get(key) == null) {
            throw new SqlNotFoundException(key + "에 대한 SQL을 찾을 수 없습니다.");
        }
        sqlMap.put(key, sql);
    }

    public void updateSql(Map<String, String> sqlmap) {
        sqlmap.forEach(this::updateSql);
    }

}
