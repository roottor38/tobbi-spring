package spring.user.sqlservice;

import java.io.IOException;
import javax.annotation.PostConstruct;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import lombok.Setter;
import org.springframework.oxm.Unmarshaller;
import spring.dao.SqlService;
import spring.user.sqlservice.jxb.SqlType;
import spring.user.sqlservice.jxb.Sqlmap;

public class OxmSqlService implements SqlService {

    private final OxmSqlReader oxmSqlReader = new OxmSqlReader();
    //위임 대상
    private final BaseSqlService baseSqlService = new BaseSqlService();
    @Setter
    private SqlRegistry sqlRegistry = new HashMapSqlRegistry();

    public void setUnmarshaller(Unmarshaller unmarshaller) {
        this.oxmSqlReader.unmarshaller = unmarshaller;
    }

    @PostConstruct
    public void loadSql() {
        this.baseSqlService.setSqlReader(this.oxmSqlReader);
        this.baseSqlService.setSqlRegistry(this.sqlRegistry);

        this.baseSqlService.loadSql();
    }

    @Override
    public String getSql(String key) throws SqlRetrievalFailureException {
        try {
            return this.baseSqlService.getSql(key);
        } catch (SqlNotFoundException e) {
            throw new SqlRetrievalFailureException(e);
        }
    }

    @Setter
    private static class OxmSqlReader implements SqlReader {

        private Unmarshaller unmarshaller;
        private final static String DEFAULT_SQLMAP_FILE = "/sqlmap.xml";
        private String sqlmapFile = DEFAULT_SQLMAP_FILE;


        @Override
        public void read(SqlRegistry sqlRegistry) {
            try {
                Source source = new StreamSource(
                    getClass().getResourceAsStream(this.sqlmapFile)
                );
                Sqlmap sqlmap = (Sqlmap) this.unmarshaller.unmarshal(source);

                for (SqlType sql : sqlmap.getSql())
                    sqlRegistry.registrySql(sql.getKey(), sql.getValue());
            } catch (IOException e) {
                throw new IllegalArgumentException(this.sqlmapFile +
                    "을 가져올 수 없습니다." + e);
            }
        }
    }
}
