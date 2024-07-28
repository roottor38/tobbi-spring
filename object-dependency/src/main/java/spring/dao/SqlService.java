package spring.dao;

import spring.user.exception.SqlRetrievalFailureException;

public interface SqlService {
  String getSql(String id) throws SqlRetrievalFailureException;

}
