package com.sri.KrakenTestAssignment.socketclient;


import com.sri.KrakenTestAssignment.handlers.CurrencySubscriptionHandler;
import com.sri.KrakenTestAssignment.handlers.MyPingHandler;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

/*
    Client API for private calls with API key

 */
public class PrivateWebSocketCalls {
    final String URL = "wss://ws-auth.kraken.com";


    //Tries to perform simple web socket connection to the Kraken WebSockets API
    public void pingCheck() {
        WebSocketClient client = new StandardWebSocketClient();
        client.doHandshake(new MyPingHandler(), URL);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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



}
