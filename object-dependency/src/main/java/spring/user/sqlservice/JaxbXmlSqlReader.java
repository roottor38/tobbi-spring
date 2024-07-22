package spring.user.sqlservice;

import java.io.InputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import lombok.Setter;
import spring.user.sqlservice.jxb.SqlType;
import spring.user.sqlservice.jxb.Sqlmap;

@Setter
public class JaxbXmlSqlReader implements SqlReader {
    private static final String DEFAULT_SQLMAP_FILE = "/sqlmap.xml";
    private String sqlmapFile = DEFAULT_SQLMAP_FILE;

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

}
