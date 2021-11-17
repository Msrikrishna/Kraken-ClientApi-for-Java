package com.sri.KrakenJavaClientAPI.handlers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sri.KrakenJavaClientAPI.apiclient.APICalls;
import com.sri.KrakenJavaClientAPI.bl.MyDataHandler;
import com.sri.KrakenJavaClientAPI.config.PrivateAPIConfig;
import com.sri.KrakenJavaClientAPI.handlers.ResponseHandlerInterface.IResponseHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.*;

import java.util.HashMap;
import java.util.Map;

public class KrakenAuthSocketHandler implements WebSocketHandler {
    Logger logger = LoggerFactory.getLogger(KrakenAuthSocketHandler.class);

    static PrivateAPIConfig config = new PrivateAPIConfig();
    static APICalls api = APICalls.getInstance();

    IResponseHandler responseHandler;

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
        this.responseHandler = new MyDataHandler();
        Map<Object,Object> request = new HashMap<>();
        request.put("event", "subscribe");

        Map<Object, Object> subscription = new HashMap<>();
        subscription.put("name", "openOrders");
        subscription.put("token", token);

        request.put("subscription", subscription);
        logger.debug("Message sent: " + request.toString());

        session.sendMessage(new TextMessage(new ObjectMapper().writeValueAsString(request)));
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        JsonNode input = (new ObjectMapper()).readValue((String) message.getPayload(), JsonNode.class);
        logger.debug("Message received: " + input.toString());
        if (input.has("event")) {
            String event = input.get("event").toString();
            switch (event) {
                case "addOrderStatus":
                    responseHandler.handleAddOrderResponse(input);
                    break;
                case "cancelOrderStatus":
                    responseHandler.handleCancelOrderResponse(input);
                    break;
                case "cancelAllStatus":
                    responseHandler.handleCancelAllOrderResponse(input);
                    break;
                case "cancelAllOrdersAfterStatus":
                    responseHandler.handleCancelAllAfterOrderResponse(input);
                    break;
                default:
                    break;
            }
        }
        if (input.has("channelName")) { //Use stored channel Ids to route the event
            responseHandler.handlePrivateDataByChannelName(input);
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        logger.debug("Authenticated connection to Kraken has been closed");
        session.close();
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
