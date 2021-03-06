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
package com.linkedin.pinot.realtime.converter;

import com.linkedin.pinot.common.data.DimensionFieldSpec;
import com.linkedin.pinot.common.data.FieldSpec;
import com.linkedin.pinot.common.data.Schema;
import com.linkedin.pinot.common.data.TimeFieldSpec;
import com.linkedin.pinot.core.realtime.converter.RealtimeSegmentConverter;
import com.linkedin.pinot.core.segment.virtualcolumn.VirtualColumnProviderFactory;
import java.util.concurrent.TimeUnit;
import org.testng.Assert;
import org.testng.annotations.Test;


public class RealtimeSegmentConverterTest {

  @Test
  public void testNoVirtualColumnsInSchema() {
    Schema schema = new Schema();
    FieldSpec spec = new DimensionFieldSpec("col1", FieldSpec.DataType.STRING, true);
    schema.addField(spec);
    TimeFieldSpec tfs = new TimeFieldSpec("col1", FieldSpec.DataType.LONG, TimeUnit.MILLISECONDS,
        "col2", FieldSpec.DataType.LONG, TimeUnit.DAYS);
    schema.addField(tfs);
    VirtualColumnProviderFactory.addBuiltInVirtualColumnsToSchema(schema);
    Assert.assertEquals(schema.getColumnNames().size(), 5);
    Assert.assertEquals(schema.getTimeFieldSpec().getIncomingGranularitySpec().getTimeType(), TimeUnit.MILLISECONDS);

    RealtimeSegmentConverter converter = new RealtimeSegmentConverter(null, "", schema,
        "testTable", "col1", "segment1", "col1");

    Schema newSchema = converter.getUpdatedSchema(schema);
    Assert.assertEquals(newSchema.getColumnNames().size(), 2);
    Assert.assertEquals(newSchema.getTimeFieldSpec().getIncomingGranularitySpec().getTimeType(), TimeUnit.DAYS);
  }
}
