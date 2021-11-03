package com.sri.KrakenTestAssignment.apiclient;

import com.fasterxml.jackson.databind.JsonNode;
import com.sri.KrakenTestAssignment.entity.OrderBookWrapper;
import com.sri.KrakenTestAssignment.entity.SystemStatus;
import com.sri.KrakenTestAssignment.entity.TickerInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

//Singleton
public class APICalls {

    Logger logger = LoggerFactory.getLogger(APICalls.class);
    final static APICalls instance = null;

    private APICalls(){}

    public static APICalls getInstance() {
        if (instance == null) {
            return new APICalls();
        } else {
            return instance;
        }
    }

    //URL templates to query
    final static String uri_systemStatus = "https://api.kraken.com/0/public/SystemStatus";
    final static String uri_tickerInformation = "https://api.kraken.com/0/public/Ticker?pair={pair_id}";
    final static String uri_orderBookDataSimple = "https://api.kraken.com/0/public/Depth?pair={pair_id}";
    final static String uri_orderBookData = "https://api.kraken.com/0/public/Depth?pair={pair_id}&count={count_id}";
    final static String uri_recentTradesSimple = "https://api.kraken.com/0/public/Trades?pair={pair_id}";
    final static String uri_recentTrades = "https://api.kraken.com/0/public/Trades?pair={pair_id}&since={since_id}";

    public ResponseEntity<SystemStatus> fetchSystemStatus() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<SystemStatus> result = restTemplate.getForEntity(uri_systemStatus, SystemStatus.class);
        logger.debug("System status received" + result.toString());
        return result;
    }


    public ResponseEntity<TickerInfo> fetchTickerInformation(String pairId) {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> params = new HashMap<String, String>();
        params.put("pair_id", pairId);
        ResponseEntity<TickerInfo> result = restTemplate.getForEntity(uri_tickerInformation, TickerInfo.class, params);
        logger.debug("Ticker info for " + pairId + " received: " + result.toString());
        return result;
    }

    public ResponseEntity<JsonNode> fetchTickerInformationAsJSON(String pairId) {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> params = new HashMap<String, String>();
        params.put("pair_id", pairId);
        ResponseEntity<JsonNode> result = restTemplate.getForEntity(uri_tickerInformation, JsonNode.class, params);
        logger.debug("Ticker info for " + pairId + " received: " + result.toString());
        return result;
    }


    public ResponseEntity<OrderBookWrapper> fetchOrderBookData(String pairId) {
        return fetchOrderBookData(pairId, 0);
    }

    public ResponseEntity<OrderBookWrapper> fetchOrderBookData(String pairId, int countId) {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> params = new HashMap<String, String>();
        params.put("pair_id", pairId);
        ResponseEntity<OrderBookWrapper> result = null;
        if (countId == 0) { //No countId provided
             result = restTemplate.getForEntity(uri_orderBookDataSimple, OrderBookWrapper.class, params);
        } else  {  //Any value of countId is theoretically accepted by the original REST end point
             params.put("count_id", Integer.toString(countId));
             result = restTemplate.getForEntity(uri_orderBookData, OrderBookWrapper.class, params);
        }
        params.put("count_id", Integer.toString(countId));
        logger.debug("Recent trades for " + pairId + " received: " + result.toString());
        return result;
    }


    //Tests not implemented
    public ResponseEntity<JsonNode> fetchRecentTrades(String pairId) {
        return fetchRecentTrades(pairId, "");
    }


    public ResponseEntity<JsonNode> fetchRecentTrades(String pairId, String sinceId) {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> params = new HashMap<String, String>();
        params.put("pair_id", pairId);
        ResponseEntity<JsonNode> result;
        if (sinceId.isEmpty()) {
            result = restTemplate.getForEntity(uri_recentTradesSimple, JsonNode.class, params);
        } else {
            params.put("since_id", sinceId);
            result = restTemplate.getForEntity(uri_recentTrades, JsonNode.class, params);
        }
        logger.debug("Recent trades for " + pairId + " received: " + result.toString());
        return result;
    }



}
