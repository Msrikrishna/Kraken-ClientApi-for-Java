package com.sri.KrakenJavaClientAPI.entity;

public class Spread {
    double bid;
    double bidVolume;
    double ask;
    double askVolume;
    double serverRecordedTime;
    double localRecordedTime;


    public Spread(double bid, double bidVolume, double ask, double askVolume, double serverRecordedTime, double localRecordedTime) {
        this.bid = bid;
        this.bidVolume = bidVolume;
        this.ask = ask;
        this.askVolume = askVolume;
        this.serverRecordedTime = serverRecordedTime;
        this.localRecordedTime = localRecordedTime;
    }

    @Override
    public String toString() {
        return "Spread{" +
                "bid=" + bid +
                ", bidVolume=" + bidVolume +
                ", ask=" + ask +
                ", askVolume=" + askVolume +
                ", recordLatencySec=" + (localRecordedTime - serverRecordedTime) +
                '}';
    }

    public double getServerRecordedTime() {
        return serverRecordedTime;
    }

    public void setServerRecordedTime(double serverRecordedTime) {
        this.serverRecordedTime = serverRecordedTime;
    }

    public double getLocalRecordedTime() {
        return localRecordedTime;
    }

    public void setLocalRecordedTime(double localRecordedTime) {
        this.localRecordedTime = localRecordedTime;
    }

    public double getBid() {
        return bid;
    }

    public void setBid(double bid) {
        this.bid = bid;
    }

    public double getBidVolume() {
        return bidVolume;
    }

    public void setBidVolume(double bidVolume) {
        this.bidVolume = bidVolume;
    }

    public double getAsk() {
        return ask;
    }

    public void setAsk(double ask) {
        this.ask = ask;
    }

    public double getAskVolume() {
        return askVolume;
    }

    public void setAskVolume(double askVolume) {
        this.askVolume = askVolume;
    }

}
