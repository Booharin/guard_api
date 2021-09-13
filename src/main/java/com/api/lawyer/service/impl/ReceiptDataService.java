package com.api.lawyer.service.impl;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class ReceiptDataService {

    private String VERIFICATION_URL_SANDBOX = "https://sandbox.itunes.apple.com/verifyReceipt";
    private String VERIFICATION_URL = "https://buy.itunes.apple.com/verifyReceipt";
    private String APP_SECRET = "1b200e162e4242f49623135250a1ed86";

    public JSONObject validateReceipt(String receiptData){
        CloseableHttpClient client = HttpClients.createDefault();
        try {
            JSONObject requestData = new JSONObject();
            requestData.put("receipt-data", receiptData);
            requestData.put("password", APP_SECRET);
            StringEntity requestEntity = new StringEntity(requestData.toString());

            HttpPost request = new HttpPost(VERIFICATION_URL_SANDBOX);
            request.addHeader("Content-Type", "text/plain");
            request.setEntity(requestEntity);

            CloseableHttpResponse response = client.execute(request);
            String responseBody = EntityUtils.toString(response.getEntity());
            return new JSONObject(responseBody);
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
