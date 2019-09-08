package com.abunko.services.saifer.controller;

import com.abunko.services.saifer.exception.InvalidSignExeption;
import com.abunko.services.saifer.model.saifer.request.EncriprSayferRequest;
import com.abunko.services.saifer.model.saifer.request.SaiferRequest;
import com.abunko.services.saifer.services.SaiferService;
import com.abunko.services.saifer.services.SimpleOperationHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController()
public class SaiferController {
    private static final Logger LOG = LoggerFactory.getLogger("saifer");

    @Value("${saifer.url}")
    private String saiferUri;
    private SimpleOperationHelper operationHelper = new SimpleOperationHelper(saiferUri);
    private SaiferService saiferService = new SaiferService();

    @PostMapping("/decriptData")
    public String decptAccInfo(@RequestBody SaiferRequest saiferRequest) throws IOException, InvalidSignExeption {
        System.out.println(saiferRequest);
        String dsBase64String = saiferService.getDsBase64String(saiferRequest.getDataBase64(), saiferRequest.getKeyStoreBase64(), saiferRequest.getKeyStorePass());
        String decriptionData = saiferService.getUserDataBAse64String(dsBase64String, saiferRequest.getDataBase64());
        return decriptionData;
    }

    @PostMapping("/sing")
    public String singData(@RequestBody SaiferRequest saiferRequest) throws IOException {
        String singDataBase64 = saiferService.getSingDataBase64(saiferRequest.getDataBase64(), saiferRequest.getKeyStoreBase64(), saiferRequest.getKeyStorePass());
        return singDataBase64;
    }

    @PostMapping("/encrypt")
    public String encryptData(@RequestBody EncriprSayferRequest saiferRequest) throws IOException {
        String singDataBase64 = saiferService.getSingDataBase64(saiferRequest.getDataBase64(), saiferRequest.getKeyStoreBase64(), saiferRequest.getKeyStorePass());
        String encryptData = saiferService.getEncryptData(singDataBase64, saiferRequest.getKeyStoreBase64(), saiferRequest.getKeyStorePass(), saiferRequest.getCert());
        return encryptData;
    }

}
