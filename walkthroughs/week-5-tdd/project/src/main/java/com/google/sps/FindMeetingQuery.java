// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Map;

public final class FindMeetingQuery {
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    Collection<TimeRange> possibleTimes = new ArrayList();
    TimeRange lastTime = events.stream()
    .filter(event -> areAnyAttendeeBusy(event, request))
    .map(Event::getWhen)
    .sorted(TimeRange.ORDER_BY_START)
    .reduce(TimeRange.fromStartDuration(0, 0), (previous, current) -> 
      getInvalidTime(previous, current, request, possibleTimes)
    );
    getInvalidTime(lastTime, TimeRange.fromStartDuration(TimeRange.END_OF_DAY+1, 0), request, possibleTimes); 
    

    return possibleTimes;
  }

  public Boolean areAnyAttendeeBusy(Event event, MeetingRequest request) {
    return !event.getAttendees()
      .stream()
      .filter(attendee->request.getAttendees().contains(attendee))
      .collect(Collectors.toList())
      .isEmpty();
  }

  //Evaluates an invalid timerange based on if the current range intersects with most recent invalid time block
  public TimeRange getInvalidTime(TimeRange previous, TimeRange current, MeetingRequest request, Collection<TimeRange> possibleTimes) {
    if (current.overlaps(previous)){
      //Sub-Case 1: Time ranges are the same
      if (previous == current) {
        return current;
      }
      //Sub-Case 2: One time range is subset of the other so return the larger time range
      if (previous.contains(current)){
        return previous;
      } else if (current.contains(previous)) {
        return current;
      }
      //Sub-case 3: Return the union of both time ranges
      int start = Math.min(previous.start(), current.start());
      int end = Math.max(previous.end(), current.end());
      return TimeRange.fromStartEnd(start, end, false);
    }
    TimeRange timeInBetween = TimeRange.fromStartEnd(previous.end(), current.start(), false);
    if (timeInBetween.duration() < request.getDuration()) {
      return TimeRange.fromStartEnd(previous.start(), current.end(), true);
    }
    possibleTimes.add(timeInBetween);
    return current;
  }
}
