package learningtest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spring.user.exception.SqlNotFoundException;
import spring.user.sqlservice.UpdatableSqlRegistry;

public abstract class AbstractUpdateSqlRegistryTest {

    UpdatableSqlRegistry sqlRegistry;

    @BeforeEach
    public void setUp() {
        sqlRegistry = createUpdatableSqlRegistry();
        sqlRegistry.registerSql("KEY1", "SQL1");
        sqlRegistry.registerSql("KEY2", "SQL2");
        sqlRegistry.registerSql("KEY3", "SQL3");
    }

    abstract protected UpdatableSqlRegistry createUpdatableSqlRegistry();

    protected void checkFindResult(String expected1, String expected2, String expected3) {
        assertThat(sqlRegistry.findSql("KEY1")).isEqualTo(expected1);
        assertThat(sqlRegistry.findSql("KEY2")).isEqualTo(expected2);
        assertThat(sqlRegistry.findSql("KEY3")).isEqualTo(expected3);
    }

    @Test
    public void find() {
        checkFindResult("SQL1", "SQL2", "SQL3");
    }

    //주어진 키에 해당하는 SQL을 찾을 수 없을 때 예외가 발생하는지를 확인
    @Test
    public void unknownKey() {
        assertThatThrownBy(() -> sqlRegistry.findSql("SQL9999!@#$"))
            .isInstanceOf(SqlNotFoundException.class);
    }

    //하나의 sql을 변경하는 기능에 대한 테스트
    @Test
    public void updateSingle() {
        sqlRegistry.updateSql("KEY2", "Modified2");
        checkFindResult("SQL1", "Modified2", "SQL3");
    }

    @Test
    public void updateMulti() {
        Map<String, String> sqlmap = Map.of("KEY1", "Modified1", "KEY3", "Modified3");

        sqlRegistry.updateSql(sqlmap);
        checkFindResult("Modified1", "SQL2", "Modified3");
    }

    @Test
    public void updateWithNotExistingKey() {
        assertThatThrownBy(() -> sqlRegistry.updateSql("SQL9999!@#$", "Modified2"))
            .isInstanceOf(SqlNotFoundException.class);
    }

}
