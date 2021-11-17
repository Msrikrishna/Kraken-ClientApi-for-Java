package com.sri.KrakenJavaClientAPI.bl;

import com.fasterxml.jackson.databind.JsonNode;
import com.sri.KrakenJavaClientAPI.KrakenJavaClientAPI;
import com.sri.KrakenJavaClientAPI.apiclient.APICalls;
import com.sri.KrakenJavaClientAPI.handlers.ResponseHandlerInterface.IResponseHandler;
import com.sri.KrakenJavaClientAPI.socketclient.WebSocketCalls;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/*
    Business logic class. Use REST as well as additional socket calls to define flow
 */
public class MyDataHandler implements IResponseHandler {

    Logger logger = LoggerFactory.getLogger(KrakenJavaClientAPI.class);
    static WebSocketCalls socketAPI = new WebSocketCalls();
    static APICalls api = APICalls.getInstance();
    public MyDataHandler() {}

    @Override
    public void handleSystemStatusResponse(JsonNode input) {

    }

    @Override
    public void handleSubscriptionResponse(JsonNode input) {

    }

    @Override
    public void handlePublicDataByChannelName(JsonNode input) {

    }

    @Override
    public void handleAddOrderResponse(JsonNode input) {

    }

    @Override
    public void handleCancelOrderResponse(JsonNode input) {

    }

    @Override
    public void handleCancelAllOrderResponse(JsonNode input) {

    }

    @Override
    public void handleCancelAllAfterOrderResponse(JsonNode input) {

    }

    @Override
    public void handlePrivateDataByChannelName(JsonNode input) {

    }

    

    //TODO: Just take two pairs(BTC/ETH, ETH/USDT, BTC/USDT) and work out the algo for triangular arbitrage in real time



}
