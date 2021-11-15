package com.sri.KrakenJavaClientAPI.entity;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.IOException;
import java.util.Arrays;

@JsonDeserialize(using = TickerInfo.class)

public class TickerInfo extends JsonDeserializer<TickerInfo> {
    String[] ask;
    String[] bid;
    String[] closed;
    String[] volume;
    Integer[] numTrades;
    String[] low;
    String[] high;
    String todaysOpeningPrice;



    String[] error;
    public TickerInfo(String[] ask, String[] bid, String[] closed, String[] volume, Integer[] numTrades, String[] low, String[] high, String todaysOpeningPrice) {
        this.ask = ask;
        this.bid = bid;
        this.closed = closed;
        this.volume = volume;
        this.numTrades = numTrades;
        this.low = low;
        this.high = high;
        this.todaysOpeningPrice = todaysOpeningPrice;
    }

    public TickerInfo() {}


    @Override
    public TickerInfo deserialize(JsonParser p, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode parent = p.readValueAsTree();
        JsonNode errorNode = parent.get("error");
        JsonNode node = parent.get("result");

        ObjectMapper o = new ObjectMapper();
        TickerInfo s = new TickerInfo();
        s.setAsk(o.treeToValue(node.findValue("a"), String[].class));
        s.setBid(o.treeToValue(node.findValue("b"), String[].class));
        s.setClosed(o.treeToValue(node.findValue("c"), String[].class));
        s.setVolume(o.treeToValue(node.findValue("v"), String[].class));
        s.setNumTrades(o.treeToValue(node.findValue("t"), Integer[].class));
        s.setLow(o.treeToValue(node.findValue("l"), String[].class));
        s.setHigh(o.treeToValue(node.findValue("h"), String[].class));
        s.setTodaysOpeningPrice(o.treeToValue(node.findValue("o"), String.class));
        s.setError(o.treeToValue(errorNode, String[].class));
        return s;
    }

    public String[] getAsk() {
        return ask;
    }

    public void setAsk(String[] ask) {
        this.ask = ask;
    }

    public String[] getBid() {
        return bid;
    }

    public void setBid(String[] bid) {
        this.bid = bid;
    }

    public String[] getClosed() {
        return closed;
    }

    public void setClosed(String[] closed) {
        this.closed = closed;
    }

    public String[] getVolume() {
        return volume;
    }

    public void setVolume(String[] volume) {
        this.volume = volume;
    }

    public Integer[] getNumTrades() {
        return numTrades;
    }

    public void setNumTrades(Integer[] numTrades) {
        this.numTrades = numTrades;
    }

    public String[] getLow() {
        return low;
    }

    public void setLow(String[] low) {
        this.low = low;
    }

    public String[] getHigh() {
        return high;
    }

    public void setHigh(String[] high) {
        this.high = high;
    }

    public String getTodaysOpeningPrice() {
        return todaysOpeningPrice;
    }

    public void setTodaysOpeningPrice(String todaysOpeningPrice) {
        this.todaysOpeningPrice = todaysOpeningPrice;
    }
    public String[] getError() {
        return error;
    }

    public void setError(String[] error) {
        this.error = error;
    }
    @Override
    public String toString() {
        return "TickerInfo{" +
                "ask=" + Arrays.toString(ask) +
                ", bid=" + Arrays.toString(bid) +
                ", closed=" + Arrays.toString(closed) +
                ", volume=" + Arrays.toString(volume) +
                ", numTrades=" + Arrays.toString(numTrades) +
                ", low=" + Arrays.toString(low) +
                ", high=" + Arrays.toString(high) +
                ", todaysOpeningPrice='" + todaysOpeningPrice + '\'' +
                '}';
    }


}
