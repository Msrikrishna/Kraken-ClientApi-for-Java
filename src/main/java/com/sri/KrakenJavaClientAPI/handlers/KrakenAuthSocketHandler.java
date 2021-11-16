package com.sri.KrakenJavaClientAPI.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sri.KrakenJavaClientAPI.apiclient.APICalls;
import com.sri.KrakenJavaClientAPI.config.PrivateAPIConfig;
import org.springframework.web.socket.*;

import java.util.HashMap;
import java.util.Map;

public class KrakenAuthSocketHandler implements WebSocketHandler {
    static PrivateAPIConfig config = new PrivateAPIConfig();
    static APICalls api = APICalls.getInstance();

    private String token;
    private WebSocketSession session;


    public String getToken() {
        return token;
    }
    public WebSocketSession getSession() {
        return session;
    }

    //It is mandatory to subscribe to atleast one private message to keep it open. I use openOrders
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String token = api.getWebSocketsTokenAsString(config.getApiKey(), config.getPrivateKey());
        if (token == null) {
            session.close();
            return;
        }
        this.token = token;
        this.session = session;
        Map<Object,Object> request = new HashMap<>();
        request.put("event", "subscribe");

        Map<Object, Object> subscription = new HashMap<>();
        subscription.put("name", "openOrders");
        subscription.put("token", token);

        request.put("subscription", subscription);
        System.out.print("Message sent: " + request.toString());

        session.sendMessage(new TextMessage(new ObjectMapper().writeValueAsString(request)));
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
        System.out.println("Authenticated connection to Kraken has been closed");
        session.close();
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}