package com.sri.KrakenJavaClientAPI.socketclient;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class WebSocketCallsTest {

    public static WebSocketCalls api;

    @BeforeAll
    static void beforeAll() {
         api = new WebSocketCalls();
    }

    @Test
    void pingCheckTest() {
        api.pingCheck();
    }

    @Test
    void subscribeCurrencyPair() {
        api.subscribeCurrencyPair("ETH/USD");
    }

    @AfterAll
    static void afterAll() {

    }


}