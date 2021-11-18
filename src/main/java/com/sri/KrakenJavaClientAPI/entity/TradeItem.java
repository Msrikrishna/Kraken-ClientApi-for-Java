package com.sri.KrakenJavaClientAPI.entity;



/*
    Entity to hold information of a executable trade
 */
public class TradeItem {
    String market;
    String buyOrSell;
    Double volumeBase;
    Double price;
    String type = "limit";

    public TradeItem(String market, String buyOrSell) {
        this.market = market;
        this.buyOrSell = buyOrSell;
    }

    public TradeItem(String market, String buyOrSell, Double volumeBase, Double price, String type) {
        this.market = market;
        this.buyOrSell = buyOrSell;
        this.volumeBase = volumeBase;
        this.price = price;
        this.type = type;
    }

    @Override
    public String toString() {
        return "TradeItem{" +
                "market='" + market + '\'' +
                ", buyOrSell='" + buyOrSell + '\'' +
                ", volumeBase=" + volumeBase +
                ", price=" + price +
                ", type='" + type + '\'' +
                '}';
    }

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public String getBuyOrSell() {
        return buyOrSell;
    }

    public void setBuyOrSell(String buyOrSell) {
        this.buyOrSell = buyOrSell;
    }

    public Double getVolumeBase() {
        return volumeBase;
    }

    public void setVolumeBase(Double volumeBase) {
        this.volumeBase = volumeBase;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
