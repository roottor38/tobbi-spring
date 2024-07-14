package spring.dao;

import spring.user.sqlservice.SqlRetrievalFailureException;

public interface SqlService {
  String getSql(String id) throws SqlRetrievalFailureException;

}
