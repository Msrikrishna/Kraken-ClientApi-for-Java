package com.sri.KrakenTestAssignment.handlers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.web.socket.*;

import java.util.HashMap;
import java.util.Map;


@JsonSerialize

public class CurrencySubscriptionHandler implements WebSocketHandler {
    String currencyPair;

    public CurrencySubscriptionHandler(String currencyPair) {
        this.currencyPair = currencyPair;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Map<Object,Object> request = new HashMap<>();
        request.put("event", "ping");

        //session.sendMessage(new TextMessage(new ObjectMapper().writeValueAsString(request)));
        session.sendMessage(new TextMessage("{\n" +
                "  \"event\": \"subscribe\",\n" +
                "  \"pair\": [\n" +
                "    \""+currencyPair+"\"\n" +
                "  ],\n" +
                "  \"subscription\": {\n" +
                "    \"name\": \"ticker\"\n" +
                "  }\n" +
                "}")); //send a json  as string here
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        System.out.print("Message received: " );
        System.out.println(message.getPayload().toString());
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        System.out.println(exception.getCause());
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
