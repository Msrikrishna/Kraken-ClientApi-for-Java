package com.sri.KrakenJavaClientAPI.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sri.KrakenJavaClientAPI.apiclient.APICalls;
import com.sri.KrakenJavaClientAPI.config.PrivateAPIConfig;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

public class KrakenSocketHandler implements WebSocketHandler {
    static APICalls api = APICalls.getInstance();

    private WebSocketSession session;

    public WebSocketSession getSession() {
        return session;
    }
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        this.session = session;
        System.out.println("Standard connection opened");
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        System.out.print("Message received: " );
        JsonNode input = (new ObjectMapper()).readValue((String) message.getPayload(), JsonNode.class);
        System.out.println(input.toString());
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        System.out.println("Standard connection to Kraken has been closed");
        session.close();
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
