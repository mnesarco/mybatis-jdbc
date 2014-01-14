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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

/**
 *
 * @author Frank D. Martinez [mnesarco]
 */
public class JdbcStatement {
  
  private final MappedStatement mappedStatement;
  
  private final SqlSessionFactory factory;
  
  private BoundSql boundSql;
  
  private ParameterHandler parameterHandler;
  
  public boolean isCompiled() {
    return boundSql != null;      
  }
  
  protected JdbcStatement(MappedStatement s, SqlSessionFactory factory) {
    this.mappedStatement = s;
    this.factory = factory;
  }
  
  public void compile(Object parameter) {
    boundSql = mappedStatement.getBoundSql(parameter);
    parameterHandler = mappedStatement.getLang().createParameterHandler(mappedStatement, parameter, boundSql);
  }
  
  public PreparedStatement prepare(Connection c) throws RuntimeSqlException {
    if (!isCompiled()) {
      throw new RuntimeSqlException("Statement sould be compiled first");
    }
    try {
      PreparedStatement stmt = c.prepareStatement(boundSql.getSql());
      return stmt;
    }
    catch (SQLException ex) {
      throw new RuntimeSqlException(ex);
    }
  }

  public PreparedStatement prepare(Connection c, Object p) throws RuntimeSqlException {
    if (!isCompiled()) {
      compile(p);
    }
    return prepare(c);
  }
  
  public ResultSet query(Connection c, PreparedStatement s) throws RuntimeSqlException {
    return query(c, s, null);
  }
  
  public ResultSet query(Connection c, PreparedStatement s, Object p) throws RuntimeSqlException {
    try {
      parameterHandler.setParameters(s);
      return s.executeQuery();
    }
    catch (SQLException ex) {
      throw new RuntimeSqlException(ex);
    }    
  }

  public ResultSet query(Connection c) {
    return query(c, null);
  }
  
  public ResultSet query(Connection c, Object p) {
    return query(c, prepare(c, p), p);
  }

  public int perform(Connection c, PreparedStatement s) {
    return perform(c, s, null);
  }
  
  public int perform(Connection c, PreparedStatement s, Object p) {
    try {
      parameterHandler.setParameters(s);
      return s.executeUpdate();
    }
    catch (SQLException ex) {
      throw new RuntimeSqlException(ex);
    }    
  }

  public int perform(Connection c) {
    return perform(c, null);
  }
  
  public int perform(Connection c, Object p) {
    return perform(c, prepare(c, p), p);
  }
  
  public boolean execute(Connection c, PreparedStatement s) {
    return execute(c, s, null);
  }
  
  public boolean execute(Connection c, PreparedStatement s, Object p) {
    try {
      parameterHandler.setParameters(s);
      return s.execute();
    }
    catch (SQLException ex) {
      throw new RuntimeSqlException(ex);
    }    
  }

  public boolean execute(Connection c) {
    return execute(c, null);
  }
  
  public boolean execute(Connection c, Object p) {
    return execute(c, prepare(c, p), p);
  }
  
}
