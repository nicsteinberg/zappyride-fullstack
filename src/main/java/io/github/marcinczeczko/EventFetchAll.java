package io.github.marcinczeczko;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import io.github.marcinczeczko.model.EventList;
import javax.inject.Inject;
import javax.inject.Named;
import org.jboss.logging.Logger;

@Named("fetchAll")
public class EventFetchAll implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

  private static final Logger LOG = Logger.getLogger(EventFetchAll.class);

  @Inject
  EventService service;

  @Override
  public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, final Context context) {
    try {
      return RequestUtils.successResponse(new EventList(service.fetchAll()));
    } catch (Exception e) {
      LOG.error("Error processing request", e);
      return RequestUtils.errorResponse(500, "[Lambda] Unable to fetch Event from DynamoDB");
    }
  }
}
