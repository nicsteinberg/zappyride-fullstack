package io.github.marcinczeczko.model;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class UpdateEvent {

  private String id;
  private String date;
  private String newOrg;
  private String newVenue;
  private String newDate;

  public UpdateEvent() {
    //Default constructor
  }

  public UpdateEvent(String id, String date, String newOrg, String newVenue, String newDate) {
    this.id = id;
    this.date = date;
    this.newOrg = newOrg;
    this.newVenue = newVenue;
    this.newDate = newDate;
  }

  public String getId() {
    return id;
  }
  
  public String getDate() {
    return date;
  }

  public String getNewOrg() {
    return newOrg;
  }

  public String getNewVenue() {
    return newVenue;
  }

  public String getNewDate() {
    return newDate;
  }
}
