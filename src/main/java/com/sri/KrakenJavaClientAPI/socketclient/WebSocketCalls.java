package com.sri.KrakenJavaClientAPI.socketclient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sri.KrakenJavaClientAPI.handlers.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/*
Refer to https://docs.kraken.com/websockets/#connectionDetails for more details
*/

public class WebSocketCalls {
    Logger logger = LoggerFactory.getLogger(WebSocketCalls.class);


    final String URL = "wss://ws.kraken.com";
    final String privateURL = "wss://ws-auth.kraken.com";
    static KrakenSocketHandler handler;
    public static KrakenSocketHandler getHandler() {
        return handler;
    }
    static KrakenAuthSocketHandler secureHandler;
    public static KrakenAuthSocketHandler getSecureHandler() {
        return secureHandler;
    }

    public void closeAllSockets() throws IOException {
        if(handler != null) handler.getSession().close();
        if(secureHandler != null) secureHandler.getSession().close();
    }

    public KrakenSocketHandler openSocket() throws ExecutionException, InterruptedException {
        if (getHandler() != null) return getHandler();

        //Request a new standard connection
        WebSocketClient client = new StandardWebSocketClient();
        handler = new KrakenSocketHandler();
        client.doHandshake(handler, URL).get();
        return handler;
    }

    public void pingCheck() throws ExecutionException, InterruptedException, IOException {
        KrakenSocketHandler handler = openSocket();
        WebSocketSession session = handler.getSession();
        if (session.isOpen()) {   //Create payload
            Map<Object,Object> request = new HashMap<>();
            request.put("event", "ping");
            session.sendMessage(new TextMessage(new ObjectMapper().writeValueAsString(request)));
            logger.debug("Message sent: " + request);
        }
    }

    /*
    Server sends message whenever system changes
     */
    public void systemStatus() throws ExecutionException, InterruptedException, IOException {
        KrakenSocketHandler handler = openSocket();
        WebSocketSession session = handler.getSession();
        if (session.isOpen()) {   //Create payload
            Map<Object,Object> request = new HashMap<>();
            request.put("event", "systemStatus");
            session.sendMessage(new TextMessage(new ObjectMapper().writeValueAsString(request)));
            logger.debug("Message sent: " + request);
        }
    }

    /*
       currency pairs - Format of each currency pair is "A/B", where A and B are ISO 4217-A3 for standardized assets and popular unique symbol if not standardized.
       depth - Optional - depth associated with book subscription in number of levels each side, default 10. Valid Options are: 10, 25, 100, 500, 1000
       time interval -  Optional - Time interval associated with ohlc subscription in minutes. Default 1. Valid Interval values: 1|5|15|30|60|240|1440|10080|21600
       name-  book|ohlc|spread|ticker|trade|*, * for all available channels depending on the connected environment
     */
    public void subscribePublic(List<String> currencyPairs, Integer depth, Integer interval, String name) throws ExecutionException, InterruptedException, IOException {
        KrakenSocketHandler handler = openSocket();
        WebSocketSession session = handler.getSession();
        if (session.isOpen()) {   //Create payload
            Map<Object,Object> request = new HashMap<>();
            request.put("event", "subscribe");
            request.put("pair", currencyPairs);
            Map<Object, Object> subscription = new HashMap<>();
            if (depth != null) subscription.put("depth", depth);
            if (interval != null) subscription.put("interval", interval);
            subscription.put("name", name);
            request.put("subscription", subscription);
            session.sendMessage(new TextMessage(new ObjectMapper().writeValueAsString(request)));
            logger.debug("Message sent: " + request);
        }
    }

    public void unsubscribePublicByPairs(List<String> currencyPairs, Integer depth, Integer interval, String name) throws ExecutionException, InterruptedException, IOException {
        KrakenSocketHandler handler = openSocket();
        WebSocketSession session = handler.getSession();
        if (session.isOpen()) {   //Create payload
            Map<Object,Object> request = new HashMap<>();
            request.put("event", "unsubscribe");
            request.put("pair", currencyPairs);
            Map<Object, Object> subscription = new HashMap<>();
            if (depth != null) subscription.put("depth", depth);
            if (interval != null) subscription.put("interval", interval);
            subscription.put("name", name);
            request.put("subscription", subscription);
            session.sendMessage(new TextMessage(new ObjectMapper().writeValueAsString(request)));
            logger.debug("Message sent: " + request);

        }
    }

    public void unsubscribePublicByChannelId(String channelID) throws ExecutionException, InterruptedException, IOException {
        KrakenSocketHandler handler = openSocket();
        WebSocketSession session = handler.getSession();
        if (session.isOpen()) {   //Create payload
            Map<Object,Object> request = new HashMap<>();
            request.put("event", "unsubscribe");
            request.put("channelID", channelID);
            session.sendMessage(new TextMessage(new ObjectMapper().writeValueAsString(request)));
            logger.debug("Message sent: " + request);
        }
    }

    //----------------------------------------------------Private web sockets---------------------------------------------------
    //At least one private message should be subscribed to keep the authenticated client connection open.

    /*
      Socket for authenticated messages
      Find example subscription usages in the tests
     */
    public KrakenAuthSocketHandler openSecureSocket() throws ExecutionException, InterruptedException {
        if (getSecureHandler() != null) return getSecureHandler();

        //Request a new connection
        WebSocketClient client = new StandardWebSocketClient();
        secureHandler = new KrakenAuthSocketHandler();
        client.doHandshake(secureHandler, privateURL).get();
        return secureHandler;
    }

    /*
       name-  openOrders|ownTrades|*, * for all available channels depending on the connected environment
     */
    public void subscribePrivate(String name) throws ExecutionException, InterruptedException, IOException {
        KrakenAuthSocketHandler handler = openSecureSocket();
        WebSocketSession session = handler.getSession();
        String token = handler.getToken();
        if (session.isOpen()) {
            if (session.isOpen()) {   //Create payload
                Map<Object, Object> request = new HashMap<>();
                request.put("event", "subscribe");
                Map<Object, Object> subscription = new HashMap<>();
                subscription.put("name", name);
                subscription.put("token", token);
                request.put("subscription", subscription);
                session.sendMessage(new TextMessage(new ObjectMapper().writeValueAsString(request)));
                logger.debug("Message sent: " + request);
            }
        }
    }

    /*
       name-  openOrders|ownTrades|*, * for all available channels depending on the connected environment
     */
    public void unsubscribePrivateByName(String name) throws ExecutionException, InterruptedException, IOException {
            KrakenAuthSocketHandler handler = openSecureSocket();
            WebSocketSession session = handler.getSession();
            String token = handler.getToken();
            if (session.isOpen()) {   //Create payload
                Map<Object,Object> request = new HashMap<>();
                request.put("event", "unsubscribe");
                Map<Object, Object> subscription = new HashMap<>();
                subscription.put("name", name);
                subscription.put("token", token);
                request.put("subscription", subscription);
                session.sendMessage(new TextMessage(new ObjectMapper().writeValueAsString(request)));
                logger.debug("Message sent: " + request);
            }
    }

    public void unsubscribePrivateByChannelId(String channelID) throws ExecutionException, InterruptedException, IOException {
        KrakenAuthSocketHandler handler = openSecureSocket();
        WebSocketSession session = handler.getSession();
        String token = handler.getToken();
        if (session.isOpen()) {   //Create payload
            Map<Object,Object> request = new HashMap<>();
            request.put("event", "unsubscribe");
            request.put("channelID", channelID);
            request.put("token", token);
            session.sendMessage(new TextMessage(new ObjectMapper().writeValueAsString(request)));
            logger.debug("Message sent: " + request);
        }
    }




    /* Usage:
       buyOrSell - buy or sell
       Order type - market|limit|stop-loss|take-profit|stop-loss-limit|take-profit-limit|settle-position
       pair: Ex BTCUSDT
       volume: Volume of BTC want to buy - mandatory
       price: contingent on the orderType
       price2: contingent on the orderType. Ex Stop loss price
       timeInForce: Supported values include GTC (good-til-cancelled; default), IOC (immediate-or-cancel), GTD (good-til-date; expiretm must be specified).
    */
    public void addNewOrder(String buyOrSell, String orderType, String pair, double volume, Double price
    , Double price2, String timeInForce) throws ExecutionException, InterruptedException, IOException {
        KrakenAuthSocketHandler handler = openSecureSocket();
        assert handler != null;
        WebSocketSession session = handler.getSession();
        String token = handler.getToken();
        if (session.isOpen()) {   //Create payload
            Map<Object,Object> request = new HashMap<>();
            request.put("event", "addOrder");
            request.put("token", token);
            request.put("ordertype", orderType);
            request.put("type", buyOrSell);
            request.put("pair", pair);
            request.put("volume", Double.toString(volume));
            if (price!= null) request.put("price", Double.toString(price));
            if (price2 != null) request.put("price2", Double.toString(price2));
            if (timeInForce != null) request.put("timeinforce", timeInForce);
            session.sendMessage(new TextMessage(new ObjectMapper().writeValueAsString(request)));
            logger.debug("Message sent: " + request);
        }
    }

    public void cancelOrders(List<String> orderIds) throws ExecutionException, InterruptedException, IOException {
        KrakenAuthSocketHandler handler = openSecureSocket();
        assert handler != null;
        WebSocketSession session = handler.getSession();
        String token = handler.getToken();
        if (session.isOpen()) {   //Create payload
            Map<Object,Object> request = new HashMap<>();
            request.put("event", "cancelOrder");
            request.put("token", token);
            request.put("txid", orderIds);
            session.sendMessage(new TextMessage(new ObjectMapper().writeValueAsString(request)));
            logger.debug("Message sent: " + request);

        }
    }

    public void cancelAllOrders() throws ExecutionException, InterruptedException, IOException {
        KrakenAuthSocketHandler handler = openSecureSocket();
        assert handler != null;
        WebSocketSession session = handler.getSession();
        String token = handler.getToken();
        if (session.isOpen()) {   //Create payload
            Map<Object,Object> request = new HashMap<>();
            request.put("event", "cancelAll");
            request.put("token", token);
            session.sendMessage(new TextMessage(new ObjectMapper().writeValueAsString(request)));
            logger.debug("Message sent: " + request);
        }
    }

    /*
    Acts as a dead man's switch. After the specified non-zero timeout all the orders shall be cancelled no matter what
    Use timeout 0 to disable this or keep sending
    The recommended use is to make a call every 15 to 30 seconds, providing a timeout of 60 seconds
     */
    public void cancelAllOrdersAfter(int timeout) throws ExecutionException, InterruptedException, IOException {
        KrakenAuthSocketHandler handler = openSecureSocket();
        assert handler != null;
        WebSocketSession session = handler.getSession();
        String token = handler.getToken();
        if (session.isOpen()) {   //Create payload
            Map<Object,Object> request = new HashMap<>();
            request.put("event", "cancelAllOrdersAfter");
            request.put("token", token);
            request.put("timeout", timeout);
            session.sendMessage(new TextMessage(new ObjectMapper().writeValueAsString(request)));
            logger.debug("Message sent: " + request);
        }
    }





}
