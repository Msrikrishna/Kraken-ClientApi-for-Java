package com.sri.KrakenJavaClientAPI.handlers.ResponseHandlerInterface;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;

/*
    Implement methods to handle responses from the server
 */
public interface IResponseHandler {

    public void handleSystemStatusResponse(JsonNode input);

    public void  handleSubscriptionResponse(JsonNode input);

    public void handlePublicDataByChannelName(JsonNode input);

   //--------------------------------------------Private responses-------------------------------------

    public void handleAddOrderResponse(JsonNode input);

    public void  handleCancelOrderResponse(JsonNode input);

    public void  handleCancelAllOrderResponse(JsonNode input);

    public void handleCancelAllAfterOrderResponse(JsonNode input);

    public void  handlePrivateDataByChannelName(JsonNode input);
}
