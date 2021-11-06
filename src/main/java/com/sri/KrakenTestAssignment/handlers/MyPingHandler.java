package com.sri.KrakenTestAssignment.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.*;

import java.util.HashMap;
import java.util.Map;

public class MyPingHandler implements WebSocketHandler {
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Map<Object,Object> request = new HashMap<>();
        request.put("event", "ping");
        session.sendMessage(new TextMessage(new ObjectMapper().writeValueAsString(request)));
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        System.out.print("Message received: " );
        System.out.println(message.getPayload().toString());
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        System.out.println("Connection has been closed");
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
