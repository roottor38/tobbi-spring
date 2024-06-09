package spring.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface StatementStrategy {

    PreparedStatement makeStatement(Connection c) throws SQLException;

}
