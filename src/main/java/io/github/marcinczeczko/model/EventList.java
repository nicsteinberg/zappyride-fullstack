package io.github.marcinczeczko.model;

import io.quarkus.runtime.annotations.RegisterForReflection;
import java.util.List;

@RegisterForReflection
public class EventList {

  private List<Event> events;

  public EventList() {
    //Default constructor
  }

  public EventList(List<Event> events) {
    this.events = events;
  }

  public List<Event> getEvents() {
    return events;
  }
}
