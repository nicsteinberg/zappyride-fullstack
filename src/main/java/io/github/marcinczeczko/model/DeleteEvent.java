package io.github.marcinczeczko.model;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class DeleteEvent {

  private String id;
  private String date;

  public DeleteEvent() {
    //Default constructor
  }

  public DeleteEvent(String id, String date) {
    this.date = date;
    this.id = id;
  }

  public String getId() {
    return id;
  }

  public String getDate() {
    return date;
  }
}
