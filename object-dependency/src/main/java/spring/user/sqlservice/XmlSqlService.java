package spring.user.sqlservice;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import lombok.Setter;
import spring.dao.SqlService;
import spring.user.exception.SqlNotFoundException;
import spring.user.exception.SqlRetrievalFailureException;
import spring.user.sqlservice.jxb.SqlType;
import spring.user.sqlservice.jxb.Sqlmap;

@Setter
public class XmlSqlService implements SqlService, SqlRegistry, SqlReader {

    private Map<String, String> sqlMap = new HashMap<>();
    private String sqlmapFile;
    private SqlReader sqlReader;
    private SqlRegistry sqlRegistry;

//    public void setSqlReader(SqlReader sqlReader) {
//        this.sqlReader = sqlReader;
//    }
//
//    public void setSqlRegistry(SqlRegistry sqlRegistry) {
//        this.sqlRegistry = sqlRegistry;
//    }
//
//    public void setSqlmapFile(String sqlmapFile) {
//        this.sqlmapFile = sqlmapFile;
//    }

    @Override
    public String findSql(String key) throws SqlNotFoundException {
        String sql = sqlMap.get(key);
        if (sql == null) {
            throw new SqlNotFoundException(key + "를 이용해서 sql을 찾을 수 없습니다.");
        }
        return sql;
    }

    @Override
    public void registerSql(String key, String sql) {
        sqlMap.put(key, sql);

    }

    public void read(SqlRegistry sqlRegistry) {
        String contextPath = Sqlmap.class.getPackage().getName();
        try {
            JAXBContext context = JAXBContext.newInstance(contextPath);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            InputStream is = getClass().getResourceAsStream(this.sqlmapFile);
            Sqlmap sqlmap = (Sqlmap) unmarshaller.unmarshal(is);

            for (SqlType sql : sqlmap.getSql()) {
                sqlRegistry.registerSql(sql.getKey(), sql.getValue());
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getSql(String id) throws SqlRetrievalFailureException {
        try {
            return this.sqlRegistry.findSql(id);
        } catch (SqlNotFoundException e) {
            throw new SqlRetrievalFailureException(e);
        }
    }

    @PostConstruct
    public void loadSql() {
        this.sqlReader.read(this.sqlRegistry);
    }
}
