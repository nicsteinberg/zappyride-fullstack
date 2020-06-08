package io.github.marcinczeczko;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import io.github.marcinczeczko.model.UpdateEvent;
import javax.inject.Inject;
import javax.inject.Named;
import org.jboss.logging.Logger;

@Named("update")
public class EventUpdate implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

  private static final Logger LOG = Logger.getLogger(EventUpdate.class);

  @Inject
  EventService service;

  @Override
  public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, final Context context) {
    String contentType = request.getHeaders().get("Content-Type");

    if (contentType == null) {
      contentType = request.getHeaders().get("content-type");
    }

    if (contentType == null || !contentType.matches("application/json")) {
      return RequestUtils.errorResponse(415, contentType);
    }

    try {
      UpdateEvent event = RequestUtils.fromUpdateRequest(request);
      service.updateItem(event);
      return RequestUtils.successUpdateResponse(event);

    } catch (EventJsonException e) {
      LOG.error("Error processing request", e);
      return RequestUtils.errorResponse(400, "[Lambda] unable to read Event JSON from the body");
    } catch (Exception e) {
      LOG.error("Error processing request", e);
      return RequestUtils.errorResponse(500, "[Lambda] Unable to update item in DynamoDB");
    }
  }
}
