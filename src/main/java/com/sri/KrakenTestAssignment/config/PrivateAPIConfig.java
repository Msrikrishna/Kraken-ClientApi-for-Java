package com.sri.KrakenTestAssignment.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

public class PrivateAPIConfig {
    @Value("${API_PUBLIC:dummy}")  //Set public key in your environmental variables
    protected String api_key ;

    @Value("${API_PRIVATE:dummy}")  //Set private key in your environmental variables
    protected String private_key ;

    @Value("${OTP:dummy}")
    protected String otp ;

    public PrivateAPIConfig(@Value("${API_PUBLIC:dummy}") String api_key,@Value("${API_PRIVATE:dummy}") String private_key,  @Value("${OTP:dummy}") String otp) {
            this.api_key = api_key;
            this.private_key = private_key;
            this.otp = otp;
    }



    public String getApiKey() {
        return api_key;
    }

    public void setApiKey(String api_key) {
        this.api_key = api_key;
    }

    public String getPrivateKey() {
        return private_key;
    }

    public void setPrivateKey(String private_key) {
        this.private_key = private_key;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}
