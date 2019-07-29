package com.abunko.wardeployment.controller;

import com.abunko.wardeployment.model.saifer.request.SaiferLoadSesionDataRequest;
import com.abunko.wardeployment.model.saifer.request.SesionParamsRequest;
import com.abunko.wardeployment.model.saifer.response.SaiferCreateSesionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

@RestController("/saifer")
public class SaiferController {
    @Autowired
    private RestTemplate restTemplate;

    private static String saiferUri = "http://10.1.26.2:9092";

    @GetMapping("/decript")
    public String decptAccInfo(String cert, String body) throws UnsupportedEncodingException {

        SaiferCreateSesionResponse createSesionResponse = saiferCreateSesion();
        String bodyBase64 = Base64.getEncoder().encodeToString(body.getBytes("windows-1251"));
        String msg = loadSesionData(bodyBase64, createSesionResponse.getTicketUuid());

        SaiferLoadSesionDataRequest sesionDataRequest;


        return new String();
    }

    private SaiferCreateSesionResponse saiferCreateSesion() {
        ResponseEntity<SaiferCreateSesionResponse> saiferCreateSesionResponseResponseEntity = restTemplate.postForEntity(saiferUri + "/api/v1/ticket", null, SaiferCreateSesionResponse.class);
        return saiferCreateSesionResponseResponseEntity.getBody();
    }

    private String loadSesionData(String bodyInBase64, String tiket) {
        String body = "{base64Data:" + bodyInBase64 + "+}";
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(saiferUri + "/ticket/" + tiket + "/data", body, String.class);
        return responseEntity.getBody();
    }

    private String setSesionParams(SesionParamsRequest sesionParamsRequest) {

    }


}
