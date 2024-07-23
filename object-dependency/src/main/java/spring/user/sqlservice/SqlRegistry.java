package spring.user.sqlservice;

public interface SqlRegistry {
    void registrySql(String key, String sql);
    String findSql(String key) throws SqlNotFoundException;

}
