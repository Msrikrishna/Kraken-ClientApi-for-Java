package com.sri.KrakenJavaClientAPI.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.*;



public class KrakenSocketHandler implements WebSocketHandler {
    Logger logger = LoggerFactory.getLogger(KrakenSocketHandler.class);


    private WebSocketSession session;

    public WebSocketSession getSession() {
        return session;
    }
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        this.session = session;
        logger.debug("Standard connection opened");
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        JsonNode input = (new ObjectMapper()).readValue((String) message.getPayload(), JsonNode.class);
        logger.debug("Message received: " + input.toString());
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
