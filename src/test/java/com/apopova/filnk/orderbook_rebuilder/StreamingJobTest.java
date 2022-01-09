package com.apopova.filnk.orderbook_rebuilder;

import org.apache.flink.test.util.AbstractTestBase;
import org.junit.jupiter.api.Test;

class StreamingJobTest extends AbstractTestBase {

  @Test
  void jobRuns() throws Exception {
    StreamingJob.main(new String[] {"--elements", "7"});
  }
}
