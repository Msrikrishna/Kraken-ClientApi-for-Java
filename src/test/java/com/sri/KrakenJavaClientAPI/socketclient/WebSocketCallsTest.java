package com.sri.KrakenJavaClientAPI.socketclient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sri.KrakenJavaClientAPI.handlers.KrakenAuthSocketHandler;
import org.junit.jupiter.api.*;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

class WebSocketCallsTest {

    public static WebSocketCalls socketAPI;

    @BeforeAll
    static void beforeAll() {
        socketAPI = new WebSocketCalls();
    }

    @Test
    void pingCheckTest() throws IOException, ExecutionException, InterruptedException {
        socketAPI.pingCheck();
        Thread.sleep(5000);
    }

    @Test
    void subscribeCurrencyPairSpreads() throws IOException, ExecutionException, InterruptedException {
        socketAPI.subscribePublic(new ArrayList<String>(){{add("ETH/USD");}}, null, null, "spread");
        Thread.sleep(5000);
    }

    @Test
    void subscribeCurrencyPairOHLC() throws IOException, ExecutionException, InterruptedException {
        socketAPI.subscribePublic(new ArrayList<String>(){{add("ETH/USD");}}, null, null, "ohlc");
        Thread.sleep(5000);
    }

    @Test
    void subscribeAndunsubscribeByPair() throws IOException, ExecutionException, InterruptedException {
        socketAPI.subscribePublic(new ArrayList<String>(){{add("ETH/USD");}}, null, null, "ohlc");
        Thread.sleep(5000);
        socketAPI.unsubscribePublicByPairs(new ArrayList<String>(){{add("ETH/USD");}}, null, null, "ohlc");
        Thread.sleep(5000);
    }



    //---------------------------------------------------Private Web socket calls----------------------------------------------------


    @Test
    @Disabled
    void subscribeToOpenOrders() throws ExecutionException, InterruptedException, IOException {
        socketAPI.subscribePrivate("openOrders");
        Thread.sleep(5000);
    }

    @Test
    public void subscribeToOwnTrades() throws ExecutionException, InterruptedException, IOException {
        socketAPI.subscribePrivate("ownTrades");
        Thread.sleep(5000);
    }

    @Test
    public void unsubscribeToOwnTrades() throws ExecutionException, InterruptedException, IOException {
        socketAPI.subscribePrivate("ownTrades");
        Thread.sleep(5000);
        socketAPI.unsubscribePrivateByName("ownTrades");
        Thread.sleep(5000);
    }


    @Test
    public void addMarketBuyOrder() throws IOException, ExecutionException, InterruptedException {
        socketAPI.addNewOrder("buy", "market","BTC/USDT", 0.0001, null, null, "GTC" );
        Thread.sleep(5000);
    }


    @Test
    @Disabled
    void cancelOrders() throws IOException, ExecutionException, InterruptedException {
        List<String> orders = new ArrayList<>();
        orders.add("My-Transaction-ID");
        socketAPI.cancelOrders(orders);
        Thread.sleep(5000);
    }

    @Test
    void cancelAllOrders() throws IOException, ExecutionException, InterruptedException {
        socketAPI.cancelAllOrders();
        Thread.sleep(5000);
    }

    @Test
    void cancelAllOrdersAfter() throws IOException, ExecutionException, InterruptedException {
        socketAPI.cancelAllOrdersAfter(10);
        Thread.sleep(5000);
    }


    @AfterAll
    static void afterAll() throws IOException {
       socketAPI.closeAllSockets();
    }


}