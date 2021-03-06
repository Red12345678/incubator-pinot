/**
 * Copyright (C) 2014-2018 LinkedIn Corp. (pinot-core@linkedin.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.linkedin.thirdeye.rootcause;

import com.linkedin.thirdeye.datalayer.bao.AbstractManager;
import com.linkedin.thirdeye.datalayer.dto.AbstractDTO;
import com.linkedin.thirdeye.datalayer.util.Predicate;
import java.util.List;
import java.util.Map;


public abstract class AbstractMockManager<T extends AbstractDTO> implements AbstractManager<T> {
  @Override
  public Long save(T entity) {
    throw new AssertionError("not implemented");
  }

  @Override
  public int update(T entity) {
    throw new AssertionError("not implemented");
  }

  @Override
  public int update(List<T> entities) {
    throw new AssertionError("not implemented");
  }

  @Override
  public T findById(Long id) {
    throw new AssertionError("not implemented");
  }

  @Override
  public List<T> findByIds(List<Long> id) {
    throw new AssertionError("not implemented");
  }

  @Override
  public int delete(T entity) {
    throw new AssertionError("not implemented");
  }

  @Override
  public int deleteById(Long id) {
    throw new AssertionError("not implemented");
  }

  @Override
  public int deleteByIds(List<Long> id) {
    throw new AssertionError("not implemented");
  }

  @Override
  public int deleteByPredicate(com.linkedin.thirdeye.datalayer.util.Predicate predicate) {
    throw new AssertionError("not implemented");
  }

  @Override
  public int deleteRecordsOlderThanDays(int days) {
    throw new AssertionError("not implemented");
  }

  @Override
  public List<T> findAll() {
    throw new AssertionError("not implemented");
  }

  @Override
  public List<T> findByParams(Map<String, Object> filters) {
    throw new AssertionError("not implemented");
  }

  @Override
  public List<T> findByPredicate(Predicate predicate) {
    throw new AssertionError("not implemented");
  }

  @Override
  public List<Long> findIdsByPredicate(com.linkedin.thirdeye.datalayer.util.Predicate predicate) {
    throw new AssertionError("not implemented");
  }

  @Override
  public int update(T entity, Predicate predicate) {
    throw new AssertionError("not implemented");
  }
}
