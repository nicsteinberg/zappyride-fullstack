package io.github.marcinczeczko;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;

import io.github.marcinczeczko.model.DeleteEvent;
import io.github.marcinczeczko.model.ErrorResponse;
import io.github.marcinczeczko.model.Event;
import io.github.marcinczeczko.model.UpdateEvent;
import io.github.marcinczeczko.model.EventList;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import org.jboss.logging.Logger;

public class RequestUtils {

  private static final Logger LOG = Logger.getLogger(EventAdd.class);

  static ObjectWriter writer = new ObjectMapper().writerFor(ErrorResponse.class);
  static ObjectReader eventReader = new ObjectMapper().readerFor(Event.class);
  static ObjectWriter eventWriter = new ObjectMapper().writerFor(Event.class);
  static ObjectReader updateEventReader = new ObjectMapper().readerFor(UpdateEvent.class);
  static ObjectWriter updateEventWriter = new ObjectMapper().writerFor(UpdateEvent.class);
  static ObjectReader deleteEventReader = new ObjectMapper().readerFor(DeleteEvent.class);
  static ObjectWriter deleteEventWriter = new ObjectMapper().writerFor(DeleteEvent.class);
  static ObjectWriter eventListWriter = new ObjectMapper().writerFor(EventList.class);
  static Map<String, String> headers = new HashMap<>();
  static {
    headers.put("X-Requested-With", "*");
    headers.put("Access-Control-Allow-Origin", "*");
    headers.put("Access-Control-Allow-Methods", "DELETE,GET,OPTIONS,POST,PUT");
    headers.put("Access-Control-Allow-Headers", "Content-Type,X-Amz-Date,Authorization,X-Api-Key,X-Amz-Security-Token");
  }

  // Patterns to strip to prevent cross-scripting attacks
  private static Pattern[] patterns = new Pattern[] {
    Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE),
    Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
    Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
    Pattern.compile("</script>", Pattern.CASE_INSENSITIVE),
    Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
    Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
    Pattern.compile("expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
    Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE),
    Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE),
    Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL)
  };

  private static String stripXSS(String value) {
    if (value != null) {
        // value = ESAPI.encoder().canonicalize(value);

        // Avoid null characters
        value = value.replaceAll("\0", "");

        // Remove all sections that match a pattern
        for (Pattern scriptPattern : patterns){
            value = scriptPattern.matcher(value).replaceAll("");
        }
    }
    return value;
  }

  public static APIGatewayProxyResponseEvent errorResponse(int errorCode, String message) {
    APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
    try {
      return response.withStatusCode(errorCode).withHeaders(headers).withBody(writer.writeValueAsString(new ErrorResponse(message, errorCode)));
    } catch (JsonProcessingException e) {
      LOG.error(e);
      return response.withStatusCode(500).withBody("Internal error");
    }
  }

  public static APIGatewayProxyResponseEvent successResponse(Event event) {
    try {
      return new APIGatewayProxyResponseEvent().withStatusCode(200).withHeaders(headers).withBody(eventWriter.writeValueAsString(event));
    } catch (JsonProcessingException e) {
      return errorResponse(500, "Unable to write Event to JSON");
    }
  }

  public static APIGatewayProxyResponseEvent successUpdateResponse(UpdateEvent event) {
    try {
      return new APIGatewayProxyResponseEvent().withStatusCode(200).withHeaders(headers).withBody(updateEventWriter.writeValueAsString(event));
    } catch (JsonProcessingException e) {
      return errorResponse(500, "Unable to write Event to JSON");
    }
  }

  public static APIGatewayProxyResponseEvent successDeleteResponse(DeleteEvent event) {
    try {
      return new APIGatewayProxyResponseEvent().withStatusCode(200).withHeaders(headers).withBody(deleteEventWriter.writeValueAsString(event));
    } catch (JsonProcessingException e) {
      return errorResponse(500, "Unable to write Event to JSON");
    }
  }

  public static APIGatewayProxyResponseEvent successResponse(EventList eventList) {
    try {
      return new APIGatewayProxyResponseEvent().withStatusCode(200).withHeaders(headers).withBody(eventListWriter.writeValueAsString(eventList));
    } catch (JsonProcessingException e) {
      return errorResponse(500, "Unable to write Event List to JSON");
    }
  }

  public static Event fromAddRequest(APIGatewayProxyRequestEvent reqEvent) {
    Event event;
    try {
      event = eventReader.readValue(stripXSS(reqEvent.getBody()));
    } catch (IOException e) {
      throw new EventJsonException("Unable to parse Event in body", e);
    }
    return event;
  }

  public static UpdateEvent fromUpdateRequest(APIGatewayProxyRequestEvent reqEvent) {
    UpdateEvent event;
    try {
      event = updateEventReader.readValue(stripXSS(reqEvent.getBody()));
    } catch (IOException e) {
      LOG.error(e);
      throw new EventJsonException("Unable to parse Update Event in body", e);
    }
    return event;
  }

  public static DeleteEvent fromDeleteRequest(APIGatewayProxyRequestEvent reqEvent) {
    DeleteEvent event;
    try {
      event = deleteEventReader.readValue(stripXSS(reqEvent.getBody()));
    } catch (IOException e) {
      throw new EventJsonException("Unable to parse Update Event in body", e);
    }
    return event;
  }
}
