/**
 * The MIT License (MIT) Copyright (c) 2014 University of Applied Sciences, Berlin, Germany
 * For more detailed information, please read the licence.txt in the root directory.
 **/

package org.onepercent.Jobs



import org.onepercent.utils.ApacheFlumeController
import org.onepercent.{JobExecutor, JobResult}

/**
 * Job to control the Apache Flume Service through the Scala Application.
 */
class ApacheFlumeJob extends JobExecutor {

  /**
   * Executes the ApacheFlumeController with the given parameters.
   * @param params Expected parameters are: start, stop, restart, status, log
   * @return Error or success message
   */
  override def executeJob(params: List[String]): JobResult = {
    val afc = new ApacheFlumeController()
    afc.execute(params(0))
  }

}