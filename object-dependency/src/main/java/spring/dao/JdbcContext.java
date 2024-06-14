package spring.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.sql.DataSource;
import lombok.Getter;
import lombok.Setter;

@Setter
public class JdbcContext {
    private DataSource dataSource;

    public void workWithStatementStrategy(StatementStrategy strategy) throws SQLException {

        try (Connection c = dataSource.getConnection(); PreparedStatement ps = strategy.makeStatement(c)) {
            ps.executeUpdate();
        }
    }

}
