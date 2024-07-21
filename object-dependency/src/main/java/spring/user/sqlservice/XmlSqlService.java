package spring.user.sqlservice;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import spring.dao.SqlService;
import spring.user.sqlservice.jxb.SqlType;
import spring.user.sqlservice.jxb.Sqlmap;

public class XmlSqlService implements SqlService {

  private Map<String, String> sqlMap = new HashMap<>();
  private String sqlmapFile;

  public void setSqlmapFile(String sqlmapFile) {
    this.sqlmapFile = sqlmapFile;
  }

  @PostConstruct
  public void loadSql() {
    String contextPath = SqlService.class.getPackage().getName();
    try {
      JAXBContext context = JAXBContext.newInstance(contextPath);
      Unmarshaller unmarshaller = context.createUnmarshaller();
      InputStream is = getClass().getResourceAsStream(this.sqlmapFile);
      Sqlmap sqlmap = (Sqlmap) unmarshaller.unmarshal(is);

      for (SqlType sql : sqlmap.getSql()) {
        sqlMap.put(sql.getKey(), sql.getValue());
      }

    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public String getSql(String id) throws SqlRetrievalFailureException {
    String sql = sqlMap.get(id);
    if (sql != null) {
      throw new SqlRetrievalFailureException(id + "를 이용해서 sql을 찾을 수 없습니다.");
    }
    return sql;
  }
}
