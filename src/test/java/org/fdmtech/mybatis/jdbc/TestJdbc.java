/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.fdmtech.mybatis.jdbc;

import java.io.Reader;
import java.sql.Connection;
import java.sql.ResultSet;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Frank D. Martinez [mnesarco]
 */
public class TestJdbc {
  
  private SqlSessionFactory createFactory() throws Exception {
    Reader reader = Resources.getResourceAsReader("mybatis-config.xml");
    SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(reader);
    reader.close();
    SqlSession session = factory.openSession();
    Connection conn = session.getConnection();
    reader = Resources.getResourceAsReader("CreateDB_1.sql");
    ScriptRunner runner = new ScriptRunner(conn);
    runner.setLogWriter(null);
    runner.runScript(reader);
    reader.close();
    session.close();
    return factory;
  }
  
  @Test
  public void basic() throws Exception {
    
    SqlSessionFactory factory = createFactory();
    JdbcStatementFactory jdbc = new JdbcStatementFactory(factory);
    JdbcStatement statement = jdbc.getStatement("org.fdmtech.mybatis.jdbc.UserMapper.getUser");
    
    SqlSession session = factory.openSession();
    Connection connection = session.getConnection();
    
    ResultSet rs = statement.query(connection, 2);
    Assert.assertTrue(rs.next());
    Assert.assertEquals("1-User2", rs.getString("name"));
    
    session.close();
    
  }
  
}
