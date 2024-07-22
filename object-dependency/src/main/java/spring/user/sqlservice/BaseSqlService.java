package spring.user.sqlservice;

import javax.annotation.PostConstruct;
import lombok.Setter;
import spring.dao.SqlService;

@Setter
public class BaseSqlService implements SqlService {
    protected SqlReader sqlReader;
    protected SqlRegistry sqlRegistry;

    @PostConstruct
    public void loadSql() {
        this.sqlReader.read(this.sqlRegistry);
    }

    public String getSql(String key) throws SqlRetrievalFailureException {
        try {
            return this.sqlRegistry.findSql(key);
        } catch(SqlNotFoundException e) {
            throw new SqlRetrievalFailureException(e);
        }
    }

}
