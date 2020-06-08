package io.github.marcinczeczko;

import io.github.marcinczeczko.model.DeleteEvent;
import io.github.marcinczeczko.model.Event;
import io.github.marcinczeczko.model.UpdateEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanResponse;

@ApplicationScoped
public class EventService {

  private static final String EVENT_ID = "id";
  private static final String EVENT_ORG = "organizer";
  private static final String EVENT_VENUE = "venue";
  private static final String EVENT_DATE = "date";

  @ConfigProperty(name = "dynamo.table.name")
  String tableName;

  @Inject
  DynamoDbClient dynamo;

  public List<Event> fetchAll() {
    List<Event> result = new ArrayList<>();
    ScanRequest scanRequest = ScanRequest.builder().tableName(tableName).attributesToGet(EVENT_ID, EVENT_ORG, EVENT_VENUE, EVENT_DATE).build();
    ScanResponse scanResponse = dynamo.scan(scanRequest);
    for (Map<String, AttributeValue> item : scanResponse.items()) {
      Event newEvent = new Event(item.get(EVENT_ORG).s(), item.get(EVENT_VENUE).s(), item.get(EVENT_DATE).s());
      result.add(newEvent);
    }
    return result;
  }

  public void addItem(Event event) {
    Map<String, AttributeValue> item = new HashMap<>();
    item.put(EVENT_ID, AttributeValue.builder().s(event.getId()).build());
    item.put(EVENT_ORG, AttributeValue.builder().s(event.getOrganizer()).build());
    item.put(EVENT_VENUE, AttributeValue.builder().s(event.getVenue()).build());
    item.put(EVENT_DATE, AttributeValue.builder().s(event.getDate()).build());

    dynamo.putItem(PutItemRequest.builder().tableName(tableName).item(item).build());
  }

  public void updateItem(UpdateEvent event) {
    Map<String, AttributeValue> key = new HashMap<>();
    key.put(EVENT_ID, AttributeValue.builder().s(event.getId()).build());
    key.put(EVENT_DATE, AttributeValue.builder().s(event.getDate()).build());

    // Delete the old entry and make a new one, since we're changing the primary key.
    dynamo.deleteItem(DeleteItemRequest.builder().tableName(tableName).key(key).build());
    this.addItem(new Event(event.getNewOrg(), event.getNewVenue(), event.getNewDate()));
  }

  public void deleteItem(DeleteEvent event) {
    Map<String, AttributeValue> item = new HashMap<>();
    item.put(EVENT_ID, AttributeValue.builder().s(event.getId()).build());
    item.put(EVENT_DATE, AttributeValue.builder().s(event.getDate()).build());

    dynamo.deleteItem(DeleteItemRequest.builder().tableName(tableName).key(item).build());
  }

}
