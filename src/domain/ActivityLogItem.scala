package domain

import domain.ResourceState.ResourceState

/**
 * Created by Felipe on 19/06/2015.
 */
class ActivityLogItem(_time: Long, _state : ResourceState) {
  def time = _time
  def state = _state

}


