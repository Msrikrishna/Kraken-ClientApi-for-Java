package com.sri.KrakenJavaClientAPI.entity;
/*
    Model holding system status objects and necessary methods
 */
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.io.IOException;
import java.util.Arrays;

@JsonDeserialize(using = SystemStatus.class)
public class SystemStatus extends JsonDeserializer<SystemStatus> {

    public enum Status {
        ONLINE,
        MAINTENANCE,
        CANCEL_ONLY,
        POST_ONLY
    }

    Status status;
    String timeStamp; //Conversion has to be taken care of
    String[] error;


    @Override
    public SystemStatus deserialize(JsonParser p, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode node = p.readValueAsTree();
        TreeNode result = node.get("result");
        TreeNode error = node.get("error");
        ObjectMapper objectMapper = new ObjectMapper();
        SystemStatus s = new SystemStatus();
        s.setTimeStamp(objectMapper.treeToValue(result.get("timestamp"), String.class));
        s.setError(objectMapper.treeToValue(error, String[].class));
        String str = objectMapper.treeToValue(result.get("status"), String.class);
        switch (str) {
            case "online" :
                s.setStatus(Status.ONLINE);
                break;
            case "maintenance" :
                s.setStatus(Status.MAINTENANCE);
                break;
            case "cancel_only" :
                s.setStatus(Status.CANCEL_ONLY);
                break;
            case "post_only" :
                s.setStatus(Status.POST_ONLY);
                break;
            default:
                throw new IllegalArgumentException("Invalid status string: "+ str);
        }
        return s;
    }

    public SystemStatus() {
    }


    @Override
    public String toString() {
        return "SystemStatus{" +
                "status='" + status + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                ", error=" + Arrays.toString(error) +
                '}';
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String[] getError() {
        return error;
    }

    public void setError(String[] error) {
        this.error = error;
    }
}


