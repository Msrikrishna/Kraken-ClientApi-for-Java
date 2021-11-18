package com.sri.KrakenJavaClientAPI.handlers.ResponseHandlerInterface;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

/*
    Implement methods to handle responses from the server
 */
public interface IResponseHandler {

    public void handleSystemStatusResponse(WebSocketSession session, JsonNode input);

    public void  handleSubscriptionResponse(WebSocketSession session, JsonNode input);

    public void handlePublicDataByChannelName(WebSocketSession session, JsonNode input);

    /*
        Handles various payloads Ex: Spreads, book, ohlc etc
     */
    public void handlePayloads(WebSocketSession session, JsonNode input);

   //--------------------------------------------Private responses-------------------------------------

    public void handleAddOrderResponse(WebSocketSession session, JsonNode input);

    public void  handleCancelOrderResponse(WebSocketSession session, JsonNode input);

    public void  handleCancelAllOrderResponse(WebSocketSession session, JsonNode input);

    public void handleCancelAllAfterOrderResponse(WebSocketSession session, JsonNode input);

    public void handlePrivateDataByChannelName(WebSocketSession session, JsonNode input);

    public void handlePrivatePayloads(WebSocketSession session, JsonNode input);
}
