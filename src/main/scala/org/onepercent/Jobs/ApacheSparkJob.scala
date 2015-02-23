/**
 * The MIT License (MIT) Copyright (c) 2014 University of Applied Sciences, Berlin, Germany
 * For more detailed information, please read the licence.txt in the root directory.
 **/

package org.onepercent.Jobs

import org.onepercent.utils.ApacheSparkController
import org.onepercent.{JobExecutor, JobResult}

/**
 * Job to control the Apache Spark Service through the Scala Application
 */
class ApacheSparkJob extends JobExecutor {

  /**
   * Executes the ApacheSparkController with the given parameters.
   * @param params Expected parameters are: restart, status, log, debug, update
   * @return Error or success message
   */
  override def executeJob(params: List[String]): JobResult = {
    val asc = new ApacheSparkController()
    asc.execute(params(0))
  }

}
