package infrastructure

import org.joda.time.{Interval, Instant}

/**
 * Created by Felipe on 24/06/2015.
 */
class Timer {
  val initialTime = new Instant()

  def getCurrentTime() = {
    val difference = new Interval(initialTime, new Instant())
    difference.toDurationMillis()
  }

}
