package learningtest;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

public class EmbeddedDbTest {
    EmbeddedDatabase db;

    NamedParameterJdbcTemplate template;

    @BeforeEach
    public void setUp() {
        db = new EmbeddedDatabaseBuilder()
            .setType(EmbeddedDatabaseType.HSQL)
            .addScript("schema.sql")
            .addScript("data.sql")
            .build();
        template = new NamedParameterJdbcTemplate(db);
    }

    @AfterEach
    public void tearDown() {
        db.shutdown();
    }

    @Test
    public void initData(){
        String sql = "select count(*) from sqlmap";

        Map<String, String> params = Collections.singletonMap(":null", "null");
        // 두 번째 값은 쓰레기 값임
        assertThat(template.queryForObject(sql, params,Integer.class)).isEqualTo(2);

        sql = "select * from sqlmap order by key_";
        params = Collections.singletonMap(":key_", "key_");
        List<Map<String, Object>> list = template.queryForList(sql, params);
        list.forEach(x -> {
        });
        assertThat((String) list.get(0).get("KEY_")).isEqualTo("KEY1");
        assertThat((String) list.get(0).get("SQL_")).isEqualTo("SQL1");
        assertThat((String) list.get(1).get("KEY_")).isEqualTo("KEY2");
        assertThat((String) list.get(1).get("SQL_")).isEqualTo("SQL2");
    }

    @Test
    public void insert(){
        String sql = "insert into sqlmap(key_, sql_) values(:key_, :sql_)";
        Map<String,String> params = new HashMap<>();
        params.put("key_", "KEY3");
        params.put("sql_", "SQL3");

        template.update(sql, params);

        sql = "select count(*) from sqlmap";
        params = Collections.singletonMap(":null", "null");
        assertThat(template.queryForObject(sql, params,Integer.class)).isEqualTo(3);

    }

}
