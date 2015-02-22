/**
 * The MIT License (MIT) Copyright (c) 2014 University of Applied Sciences, Berlin, Germany
 * For more detailed information, please read the licence.txt in the root directory.
 **/

package org.onepercent.Jobs

/**
 * Job to control the Apache Spark Service through the Scala Application
 */

import org.onepercent.utils.ApacheSparkController
import org.onepercent.{JobExecutor, JobResult}

class ApacheSparkJob extends JobExecutor {

  override def executeJob(params: List[String]): JobResult = {
    val asc = new ApacheSparkController()
    asc.execute(params(0))
  }
  
}
