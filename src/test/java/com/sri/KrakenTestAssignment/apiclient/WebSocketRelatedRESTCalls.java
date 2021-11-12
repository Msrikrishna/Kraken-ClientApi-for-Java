package com.sri.KrakenTestAssignment.apiclient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sri.KrakenTestAssignment.config.PrivateAPIConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


class WebSocketTokenTest  {
        static APICalls api;
        static PrivateAPIConfig config;

        @BeforeAll
         static void setUp() {
            api = APICalls.getInstance();
            //Add your Public, private keys as well as optionally the API OTP password here
            //config = new PrivateAPIConfig("", "", "");
        }

        @Test
        @DisplayName("Checks if it works with OTP or without")
        void checkTokenValidity() throws JsonProcessingException, InterruptedException {
            assertNotNull(config.getApiKey(), "Public key is not initialised");
            assertNotNull(config.getPrivateKey(), "Private key is not initialised");
            ResponseEntity<JsonNode> resultwithOTP =  api.getWebSocketsToken(config.getApiKey(), config.getPrivateKey(), config.getOtp());
            Thread.sleep(10);
            ResponseEntity<JsonNode> resultwithoutOTP =  api.getWebSocketsToken(config.getApiKey(), config.getPrivateKey());
            ObjectMapper o = new ObjectMapper();
            assert resultwithOTP.getStatusCode() == HttpStatus.OK || resultwithoutOTP.getStatusCode() == HttpStatus.OK;
            assert resultwithOTP.hasBody() && resultwithoutOTP.hasBody();
            boolean withOTP = resultwithOTP.getBody().has("result");
            boolean withOutOTP = resultwithoutOTP.getBody().has("result");
            assertEquals(withOTP || withOutOTP, true, "Expected to retrieve token with or without OTP");
            if (withOTP) assertNotEquals(o.treeToValue(resultwithOTP.getBody().get("result").get("token"), String.class), null, "Non string token received with OTP");
            if (withOutOTP) assertNotEquals(o.treeToValue(resultwithoutOTP.getBody().get("result").get("token"), String.class), null, "Non string token received without OTP");
        }


    }


