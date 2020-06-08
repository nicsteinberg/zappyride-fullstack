package io.github.marcinczeczko.model;

import org.apache.commons.codec.digest.DigestUtils;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class Event {

  private String id;
  private String organizer;
  private String venue;
  private String date;

  public Event() {
    //Default constructor
  }

  public Event(String organizer, String venue, String date) {
    this.organizer = organizer;
    this.venue = venue;
    this.date = date;
    this.id = DigestUtils.sha256Hex(organizer.concat(":").concat(venue).concat(":").concat(date));
  }

  public String getId() {
    return id;
  }

  public String getOrganizer() {
    return organizer;
  }

  public String getVenue() {
    return venue;
  }

  public String getDate() {
    return date;
  }

  public void setId(String newId) {
    id = newId;
  }

  public void setHashId() {
    id = DigestUtils.sha256Hex(organizer.concat(":").concat(venue).concat(":").concat(date));
  }
}
