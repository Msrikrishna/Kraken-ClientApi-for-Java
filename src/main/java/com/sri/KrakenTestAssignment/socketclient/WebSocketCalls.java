package com.sri.KrakenTestAssignment.socketclient;

import com.sri.KrakenTestAssignment.handlers.CurrencySubscriptionHandler;
import com.sri.KrakenTestAssignment.handlers.MyPingHandler;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

public class WebSocketCalls {

    final String URL = "wss://ws.kraken.com";


    //Tries to perform simple web socket connection to the Kraken WebSockets API
    public void pingCheck() {
        WebSocketClient client = new StandardWebSocketClient();
        client.doHandshake(new MyPingHandler(), URL);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        /* Stomp client does not seem to be supported
        */
        //WebSocketStompClient stompClient = new WebSocketStompClient(client);
        //Converts JSON text payload to object
        //stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        //Create a handler inside which all the ways to deal with the session are handled
        //StompSessionHandler sessionHandler = new MyStompSessionHandler();
        //stompClient.connect(URL, sessionHandler);
    }


    //TODO: Subscribe multiple pairs simultaneously ie List<String>
    public void subscribeCurrencyPair(String currencyPair) {
        WebSocketClient client = new StandardWebSocketClient();
        client.doHandshake(new CurrencySubscriptionHandler(currencyPair), URL);

        try {
            Thread.sleep(100000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //TODO: Store the results in a Blocking Queue after which operations can be performed

    //TODO: Add support for private transactions

    //TODO: Use @WebSocket in the handler class for simplicity






}
