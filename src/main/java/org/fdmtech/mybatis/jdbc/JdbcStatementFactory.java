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

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.SqlSessionFactory;

/**
 *
 * @author Frank D. Martinez [mnesarco]
 */
public class JdbcStatementFactory {
  
  private final SqlSessionFactory factory;

  public JdbcStatementFactory(SqlSessionFactory factory) {
    this.factory = factory;
  }
  
  public JdbcStatement getStatement(String id) {
    MappedStatement s = factory.getConfiguration().getMappedStatement(id);
    return new JdbcStatement(s, factory);
  }
  
  public JdbcStatement getStatement(Class cls, String method) {
    final String id = cls.getName() + "." + method;
    MappedStatement s = factory.getConfiguration().getMappedStatement(id);
    return new JdbcStatement(s, factory);
  }
  
}
