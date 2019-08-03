package com.abunko.services.saifer.controller;

import com.abunko.services.saifer.model.saifer.request.SaiferRequest;
import com.abunko.services.saifer.model.saifer.request.SesionParams;
import com.abunko.services.saifer.model.saifer.response.SaiferCreateSesionResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

@RestController()
public class SaiferController {
    @Autowired
    private RestTemplate restTemplate;
    @Value("${saifer.url}")
    private static String saiferUri;

    @PostMapping("/decript")
    public String decptAccInfo(SaiferRequest saiferRequest) throws UnsupportedEncodingException {


        SaiferCreateSesionResponse sesionResponse = saiferCreateSesion();
        String tiket = sesionResponse.getTicketUuid();

        loadSesionData(saiferRequest.getDataBase64(), tiket);
        setSesionParams(saiferRequest.getSesionParams(), tiket);
        loadKeyStoreToSaifer(saiferRequest.getKeyStoreBase64(), tiket);
        decrypt(saiferRequest.getKeyStorePass(), tiket);
        String decriptionData = getDecriptionData(tiket);
        clouseSesion(tiket);

        return decriptionData;
    }

    private SaiferCreateSesionResponse saiferCreateSesion() {
        ResponseEntity<SaiferCreateSesionResponse> saiferCreateSesionResponseResponseEntity = restTemplate.postForEntity(saiferUri + "/api/v1/ticket", null, SaiferCreateSesionResponse.class);
        return saiferCreateSesionResponseResponseEntity.getBody();
    }

    private String loadSesionData(String bodyInBase64, String tiket) {
        String body = "{\"base64Data\":\"" + bodyInBase64 + "\"}";
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(saiferUri + "/ticket/" + tiket + "/data", body, String.class);
        return responseEntity.getBody();
    }

    private ResponseEntity<String> setSesionParams(SesionParams sesionParams, String tiket) {
        String sesionParamsJson = getSesionParamJson(sesionParams);


        /*String sesionParam = "{\n" +
                "\"signatureType\":\"attached\",\n" +
                "\"embedSignatureTs\":\"false\",\n" +
                "\"embedDataTs\":\"false\",\n" +
                "\"embedCertificateType\":\"signerAndCaCert\",\n" +
                "\"signatureTsVerifyOption\":\"ignore\",\n" +
                "\"dataTsVerifyOption\":\"ignore\",\n" +
                "\"tsAdditionalVerifying\":\"false\",\n" +
                "\"dataToSignQualifier\":\"notSignedBefore\",\n" +
                "\"duplicateSign\":\"false\",\n" +
                "\"caId\":\"iddDfs\",\n" +
                "\"cadesType\":\"undefined\",\n" +
                "\"nokkCompatible\":\"false\"\n" +
                "}";*/

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
        HttpEntity<String> request = new HttpEntity<>(sesionParamsJson, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(saiferUri + "/ticket/" + tiket + "/option", HttpMethod.PUT, request, String.class);
        return responseEntity;
    }



    public ResponseEntity<String> loadKeyStoreToSaifer(String keyStoreInBase64, String tiket) {
        byte[] bytes = Base64.getMimeDecoder().decode(keyStoreInBase64);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/octet-stream");
        HttpEntity<byte[]> request = new HttpEntity<>(bytes, headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(saiferUri + "/ticket/" + tiket + "/keyStore", HttpMethod.PUT, request, String.class);
        return responseEntity;

    }

    private String decrypt(String keyStorePass, String tiket) {
        String body = "{\"keyStorePassword\":\"" + keyStorePass + "\"}";
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(saiferUri + "/ticket/" + tiket + "/decryptor", body, String.class);
        return responseEntity.getBody();
    }

    private String getDecriptionData(String tiket) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(saiferUri + "/ticket/" + tiket + "/decryptor/base64Data", String.class);
        return responseEntity.getBody();
    }

    private String clouseSesion(String tiket) {
        ResponseEntity<String> exchange = restTemplate.exchange(saiferUri + "/ticket/" + tiket, HttpMethod.DELETE, null, String.class);
        return exchange.getBody();
    }

    private String getSesionParamJson(SesionParams sesionParams) {
        ObjectMapper mapper = new ObjectMapper();
        String sesionParamsJson = null;
        try {
            sesionParamsJson = mapper.writeValueAsString(sesionParams);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return sesionParamsJson;
    }

}
