package com.sri.KrakenTestAssignment.entity;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.IOException;
import java.util.Arrays;

@JsonDeserialize(using = OrderBookWrapper.class)
public class OrderBookWrapper extends JsonDeserializer<OrderBookWrapper> {
    abstract class Entry {   //Abstracts Bid and Asks objects
        public Double price;
        public Double volume;
        public Integer timestamp;
        public abstract String toString();
    }
    public class Bid extends Entry {
        public Bid() {
        }

        @Override
        public String toString() {
            return "Bid price : {" +
                    "price=" + price +
                    ", volume=" + volume +
                    ", timestamp=" + timestamp +
                    '}'+ "\n";
        }
    }
    public class Ask extends Entry{
        public Ask() {
        }

        @Override
        public String toString() {
            return "Ask Price {" +
                    "price=" + price +
                    ", volume=" + volume +
                    ", timestamp=" + timestamp +
                    '}' + "\n";
        }
    }

    Bid[] bids;
    Ask[] asks;
    int bidLength;
    int askLength;

    public OrderBookWrapper() {}

    public Bid[] getBids() {
        return bids;
    }

    public int getBidsLength() {
        return bids.length;
    }

    public int getAsksLength() {
        return asks.length;
    }

    public void setBids(Bid[] bids) {
        this.bids = bids;
    }

    public Ask[] getAsks() {
        return asks;
    }

    public void setAsks(Ask[] asks) {
        this.asks = asks;
    }

    @Override
    public String toString() {
        return "OrderBookWrapper{" +
                "bids=" + Arrays.toString(bids) + "\n" +
                ", asks=" + Arrays.toString(asks) + "\n" +
                '}';
    }

    @Override
    public OrderBookWrapper deserialize(JsonParser p, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode parent = p.readValueAsTree();
        JsonNode node = parent.get("result");

        ObjectMapper o = new ObjectMapper();
        OrderBookWrapper wrapper = new OrderBookWrapper();

        Object[][] asksList = o.treeToValue(node.findValue("asks"), Object[][].class);
        asks = new Ask[asksList.length];
        for (int i = 0; i < asksList.length; i++) {
            asks[i] = new Ask();
            asks[i].price = Double.parseDouble((String) asksList[i][0]) ;
            asks[i].timestamp = (Integer) asksList[i][2];
            asks[i].volume =  Double.parseDouble(String.valueOf(asksList[i][1])) ;
        }
        wrapper.setAsks(asks);
        Object[][] bidsList = o.treeToValue(node.findValue("bids"),Object[][].class);
        bids = new Bid[bidsList.length];
        for (int i = 0; i < bidsList.length; i++) {
            bids[i] = new Bid();
            bids[i].price = Double.parseDouble((String) bidsList[i][0]);
            bids[i].timestamp = (Integer) bidsList[i][2];
            bids[i].volume =  Double.parseDouble(String.valueOf(bidsList[i][1]));
        }
        wrapper.setBids(bids);

        return wrapper;
    }
}
