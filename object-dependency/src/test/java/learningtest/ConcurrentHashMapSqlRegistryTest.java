package learningtest;

import spring.user.sqlservice.UpdatableSqlRegistry;
import spring.user.sqlservice.updatable.ConcurrentHashMapSqlRegistry;

public class ConcurrentHashMapSqlRegistryTest extends AbstractUpdateSqlRegistryTest {

    protected UpdatableSqlRegistry createUpdatableSqlRegistry() {
        return new ConcurrentHashMapSqlRegistry();
    }

}
