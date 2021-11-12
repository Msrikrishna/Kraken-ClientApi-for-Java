package com.sri.KrakenTestAssignment.apiclient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sri.KrakenTestAssignment.entity.OrderBookWrapper;
import com.sri.KrakenTestAssignment.entity.SystemStatus;
import com.sri.KrakenTestAssignment.entity.TickerInfo;
import org.junit.jupiter.api.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/*
    Initially checks the systems sanity
*/
class SystemStatusTest {
    static APICalls api;
    static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    @BeforeAll
    static void setUp() {
        api = APICalls.getInstance();
    }

    @Test
    void systemStatusHttpGetOkTest() {
        ResponseEntity<SystemStatus> result = api.fetchSystemStatus();
        SystemStatus status = result.getBody();
        assert result.getStatusCode() == HttpStatus.OK;
    }

    @Test
    void systemOnlineTest() {
        ResponseEntity<SystemStatus> result = api.fetchSystemStatus();
        SystemStatus status = result.getBody();
        assert status.getStatus() == SystemStatus.Status.ONLINE;
    }

    @Test  // Sequence of calls receive non decreasing time stamps
    void systemStatusTimeStampsInOrderTest() {
        int size = 5;
        List<SystemStatus> sequentialCalls = new LinkedList<SystemStatus>();
        Date prev = null;      //The server time may be diff from the local time. So null is preferred
        for (int i = 0; i < size; i++) {
            sequentialCalls.add(api.fetchSystemStatus().getBody());
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        while (!sequentialCalls.isEmpty()) {
            SystemStatus front = sequentialCalls.remove(0);
            try {
                Date curr = format.parse(front.getTimeStamp());
                if (prev != null) {
                    assert prev.compareTo(curr) <= 0 ;
                }
                prev = curr;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    @AfterAll
    static void tearDown() {

    }
}
class TickerInformationTest {
    static APICalls api;
    @BeforeAll
    static void setUp() {
        api = APICalls.getInstance();
    }

    @Test
    void noExceptionTextWhenSuccessful() {
        ResponseEntity<TickerInfo> result = api.fetchTickerInformation("ETHUSD");
        TickerInfo info = (TickerInfo) result.getBody();
        assert result.getStatusCode() == HttpStatus.OK;
    }

    @Test  //Check expected exceptions
    void incorrectTickerSymbolAsInput() throws JsonProcessingException {
        ResponseEntity<JsonNode> result = api.fetchTickerInformationAsJSON("ETHRTUSDY");
        JsonNode node = result.getBody();
        ObjectMapper m = new ObjectMapper();
        String[] exceptions = m.treeToValue(node.get("error"), String[].class);
        assert result.getStatusCode() == HttpStatus.OK;
        assert exceptions[0].contentEquals("EQuery:Unknown asset pair");
    }

    @Test   //Check data types of all the fields in a correct response
    void checkAskPriceResponseDataType() throws JsonProcessingException {
        ResponseEntity<JsonNode> result = api.fetchTickerInformationAsJSON("ETHUSD");
        JsonNode node = result.getBody();
        ObjectMapper m = new ObjectMapper();
        assert m.treeToValue(node.findValue("a"), String[].class) != null;
        assert m.treeToValue(node.findValue("b"), String[].class) != null;
        assert m.treeToValue(node.findValue("c"), String[].class) != null;
        assert m.treeToValue(node.findValue("v"), String[].class) != null;
        assert m.treeToValue(node.findValue("p"), String[].class) != null;
        assert m.treeToValue(node.findValue("t"), Integer[].class) != null;
        assert m.treeToValue(node.findValue("l"), String[].class) != null;
        assert m.treeToValue(node.findValue("h"), String[].class) != null;
        assert m.treeToValue(node.findValue("o"), String.class) != null;
    }



    @Test
    void bidLowerThanAskTest() throws InterruptedException {
        TickerInfo[] tickerInfos = new TickerInfo[10];
        //10 ticker info calls
        for (int i = 0; i < tickerInfos.length; i++) {
            ResponseEntity<TickerInfo> result = api.fetchTickerInformation("ETHUSD");
            tickerInfos[i] = result.getBody();
            Thread.sleep(10);
        }
        //Check if bid stays below asks
        for (int i = 1; i < tickerInfos.length; i++) {
            double ask = Double.parseDouble(tickerInfos[i].getAsk()[0]);
            double bid = Double.parseDouble(tickerInfos[i].getBid()[0]);
            assert bid <= ask;    //
        }
    }


    @Test
    void openPriceLessThanHighButGreaterThanLow() throws InterruptedException {
        ResponseEntity<TickerInfo> result = api.fetchTickerInformation("ETHUSD");
        TickerInfo info =  result.getBody();
        //Today
        assert Double.parseDouble(info.getTodaysOpeningPrice()) <  Double.parseDouble(info.getHigh()[0]);
        assert Double.parseDouble(info.getTodaysOpeningPrice()) >  Double.parseDouble(info.getLow()[0]);
        //Last 24 hrs
        assert Double.parseDouble(info.getTodaysOpeningPrice()) <  Double.parseDouble(info.getHigh()[1]);
        assert Double.parseDouble(info.getTodaysOpeningPrice()) >  Double.parseDouble(info.getLow()[1]);

    }

    @AfterAll
    static void tearDown() {

    }
}

class OrderBookDataTest {
    static APICalls api;
    @BeforeAll
    static void setUp() {
        api = APICalls.getInstance();
    }

    @Test
    void byDefaultRetrieve100BidsAndAsksTest() {
        ResponseEntity<OrderBookWrapper> result = api.fetchOrderBookData("ETHUSD");
        assert result.getStatusCode() == HttpStatus.OK;
        System.out.println(result);
        OrderBookWrapper wrapper = result.getBody();
        assert wrapper.getAsksLength() + wrapper.getBidsLength() == 200;
    }

    @Test
    void countNumberOfEntriesTest() {
        int count = 20; //
        ResponseEntity<OrderBookWrapper> result = api.fetchOrderBookData("ETHUSD", count);
        OrderBookWrapper wrapper = result.getBody();
        assert wrapper.getAsksLength() + wrapper.getBidsLength() == 40;
    }

    @Test
    void countNegativeTest() {
        int count = -100; //
        ResponseEntity<OrderBookWrapper> result = api.fetchOrderBookData("ETHUSD", count);
        OrderBookWrapper wrapper = result.getBody();
        assert wrapper.getAsksLength() + wrapper.getBidsLength() == 200;
    }

    @Test
    void numberOfEntriesCappedAt1000Test() {
        int count = 1500; //
        ResponseEntity<OrderBookWrapper> result = api.fetchOrderBookData("ETHUSD", count);
        OrderBookWrapper wrapper = result.getBody();
        assert wrapper.getAsksLength() + wrapper.getBidsLength() == 1000;
    }

    @Test
    void maxBidLessThanMinAsk() {
        int count = 20; //Verify for count number of entries
        ResponseEntity<OrderBookWrapper> result = api.fetchOrderBookData("ETHUSD", count);
        OrderBookWrapper wrapper = result.getBody();
        OrderBookWrapper.Bid[] bids = wrapper.getBids();
        Optional<OrderBookWrapper.Bid> maxBid = Arrays.stream(wrapper.getBids()).max((a, b) -> a.price.compareTo(b.price));
        Optional<OrderBookWrapper.Ask> minAsk = Arrays.stream(wrapper.getAsks()).min((a, b) -> a.price.compareTo(b.price));
        assert  maxBid.get().price < minAsk.get().price;
        //TODO: Can the max bid be equal to min ask at any moment?
    }



    @AfterAll
    static void tearDown() {

    }
}



//Not implemented yet.
@Disabled
class RecentTradesTest {
    static APICalls api;

    @BeforeAll
    static void setUp() {
        api = APICalls.getInstance();
    }


    @Test
    void fetchRecentTrades() {
        ResponseEntity<JsonNode> result = api.fetchRecentTrades("XBTUSD");
        System.out.println(result);
    }

    @Test
    void fetchRecentTradesWithLastStamp() {
        ResponseEntity<JsonNode> result = api.fetchRecentTrades("XBTUSD");

        System.out.println(result);
    }
    @AfterAll
    static void tearDown() {

    }
}


