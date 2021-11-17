package com.sri.KrakenJavaClientAPI.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.sri.KrakenJavaClientAPI.bl.MyDataHandler;
import com.sri.KrakenJavaClientAPI.handlers.ResponseHandlerInterface.IResponseHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.*;



public class KrakenSocketHandler implements WebSocketHandler {
    Logger logger = LoggerFactory.getLogger(KrakenSocketHandler.class);

    private WebSocketSession session;

    IResponseHandler responseHandler;

    public WebSocketSession getSession() {
        return session;
    }
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        this.session = session;
        this.responseHandler = new MyDataHandler();
        logger.debug("Standard connection opened");
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        JsonNode input = (new ObjectMapper()).readValue((String) message.getPayload(), JsonNode.class);
        logger.debug("Message received: " + input.toString());
        if (input.has("event")) {
            String event = input.get("event").asText();
            switch (event) {
                case "systemStatus":
                    responseHandler.handleSystemStatusResponse(input);
                    break;
                case "subscriptionStatus":    //Store channel Ids for further processing
                    responseHandler.handleSubscriptionResponse(input);
                    break;
                default:
                    break;
            }
        }
        if (input.has("channelName")) { //Use stored channel Ids to route the event
            responseHandler.handlePublicDataByChannelName(input);
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        logger.debug("Standard connection to Kraken has been closed");
        session.close();
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
