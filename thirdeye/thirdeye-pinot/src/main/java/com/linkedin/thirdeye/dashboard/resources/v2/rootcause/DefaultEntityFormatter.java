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

package com.linkedin.thirdeye.dashboard.resources.v2.rootcause;

import com.linkedin.thirdeye.dashboard.resources.v2.RootCauseEntityFormatter;
import com.linkedin.thirdeye.dashboard.resources.v2.pojo.RootCauseEntity;
import com.linkedin.thirdeye.rootcause.Entity;


/**
 * Default formatter that applies to any Entity. Provides minimal information and serves as
 * a fallback.
 */
public class DefaultEntityFormatter extends RootCauseEntityFormatter {
  public static final String TYPE_OTHER = "other";

  @Override
  public boolean applies(Entity entity) {
    return true;
  }

  @Override
  public RootCauseEntity format(Entity entity) {
    return makeRootCauseEntity(entity, TYPE_OTHER, entity.getUrn(), null);
  }
}
