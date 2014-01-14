mybatis-jdbc
============

Utility to use MyBatis sql from plain old jdbc

Basic usage
-----------

    SqlSessionFactory factory = ...
    JdbcStatementFactory jdbc = new JdbcStatementFactory(factory);
    JdbcStatement statement = jdbc.getStatement(statementId);
    
    Connection connection = ...
    ResultSet rs = statement.query(connection, parameter);
    ...
