package com.sri.KrakenJavaClientAPI.apiclient;

import org.springframework.util.MultiValueMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Map;

public class Util {

    /*
    Creates a string such as "nonce=2312241244&otp=3435" from Map values
     */
    private static String urlEncodePayload(MultiValueMap<String, String> body) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> e: body.toSingleValueMap().entrySet()) {
            sb.append(URLEncoder.encode(e.getKey(), StandardCharsets.UTF_8)) ;
            sb.append("=");
            sb.append(URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8));
            sb.append("&");
        }
        sb.deleteCharAt(sb.length()-1);
        return sb.toString();
    }

    public static String getAPISignature(String urlPath, String nonce, MultiValueMap<String, String> body, String privateKey) {
        String apiSign = null;
        String data = urlEncodePayload(body);
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update((nonce + data).getBytes());
            Mac mac = Mac.getInstance("HmacSHA512");
            mac.init(new SecretKeySpec(Base64.getDecoder().decode(privateKey.getBytes()), "HmacSHA512"));
            mac.update(urlPath.getBytes());
            apiSign = new String(Base64.getEncoder().encode(mac.doFinal(md.digest())));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return apiSign;
    }

}
