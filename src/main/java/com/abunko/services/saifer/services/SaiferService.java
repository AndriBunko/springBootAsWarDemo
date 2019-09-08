package com.abunko.services.saifer.services;

import com.abunko.services.saifer.exception.InvalidSignExeption;
import org.springframework.stereotype.Service;

import javax.json.JsonArray;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Service
public class SaiferService {

    private SimpleOperationHelper simpleOperationHelper = new SimpleOperationHelper("http://10.1.26.2:9092/api/v1/");

    public String getUserDataBAse64String(String dsBase64, String dataBase64) throws IOException, InvalidSignExeption {
        String uuid;
        Map<String, String> options;
        String base64DigitalSignData;

        uuid = simpleOperationHelper.createTicket();
        try {
            simpleOperationHelper.uploadTextData(dataBase64, uuid);
            simpleOperationHelper.uploadDsBase64Data(dsBase64, uuid);
            // Установка параметров сессии
            options = new HashMap<>();
            options.put("signatureType", "attached");
            options.put("embedSignatureTs", "false");
            options.put("embedDataTs", "false");
            options.put("embedCertificateType", "signerAndCaCert");
            options.put("signatureTsVerifyOption", "ignore");
            options.put("dataTsVerifyOption", "ignore");
            options.put("tsAdditionalVerifying", "false");
            options.put("dataToSignQualifier", "notSignedBefore");
            options.put("duplicateSign", "false");
            options.put("caId", "iddDfs");
            options.put("cadesType", "undefined");
            options.put("nokkCompatible", "false");
            simpleOperationHelper.setOptions(options, uuid);
            simpleOperationHelper.verifyDigitalSign(uuid);
            System.out.println(simpleOperationHelper.getDigitalSignVerifyingResult(uuid));
            base64DigitalSignData = simpleOperationHelper.getBase64SignedData(uuid);
        }finally {

            simpleOperationHelper.deleteTicket(uuid);
        }
        return base64DigitalSignData;
    }

    public String getDsBase64String(String dataBase64, String keyStoreBase64, String keyStorePass) throws IOException {
        String uuid;
        Map<String, String> options;
        String base64DecryptorData = new String();
        // Создание сессии
        uuid = simpleOperationHelper.createTicket();
        try {
            simpleOperationHelper.uploadTextData(dataBase64, uuid);
            // Установка параметров сессии
            options = new HashMap<>();
            options.put("signatureType", "attached");
            options.put("embedSignatureTs", "false");
            options.put("embedDataTs", "false");
            options.put("embedCertificateType", "signerAndCaCert");
            options.put("signatureTsVerifyOption", "ignore");
            options.put("dataTsVerifyOption", "ignore");
            options.put("tsAdditionalVerifying", "false");
            options.put("dataToSignQualifier", "notSignedBefore");
            options.put("duplicateSign", "false");
            options.put("caId", "iddDfs");
            options.put("cadesType", "undefined");
            options.put("nokkCompatible", "false");
            simpleOperationHelper.setOptions(options, uuid);
            simpleOperationHelper.uploadKey(keyStoreBase64, uuid);
            simpleOperationHelper.decryptor(keyStorePass, uuid);
            String decryptorResult = simpleOperationHelper.getDecryptorResult(uuid);
            System.out.println(decryptorResult);
            base64DecryptorData = simpleOperationHelper.getBase64DecryptorData(uuid);
            System.out.println(base64DecryptorData);
        } finally {
            simpleOperationHelper.deleteTicket(uuid);
        }
        return base64DecryptorData;
    }


    public String getSingDataBase64(String dataBase64, String keyStoreBase64, String keyStorePass) throws IOException {
        String uuid;
        Map<String, String> options;
        String base64DigitalSign = new String();
        // Создание сессии
        uuid = simpleOperationHelper.createTicket();
        try {

            simpleOperationHelper.uploadTextData(dataBase64, uuid);
            // Установка параметров сессии
            options = new HashMap<>();
            options.put("signatureType", "attached");
            options.put("embedSignatureTs", "false");
            options.put("embedDataTs", "false");
            options.put("embedCertificateType", "signerAndCaCert");
            options.put("signatureTsVerifyOption", "ignore");
            options.put("dataTsVerifyOption", "ignore");
            options.put("tsAdditionalVerifying", "false");
            options.put("dataToSignQualifier", "notSignedBefore");
            options.put("duplicateSign", "false");
            options.put("caId", "iddDfs");
            options.put("cadesType", "undefined");
            options.put("nokkCompatible", "false");
            simpleOperationHelper.setOptions(options, uuid);
            simpleOperationHelper.uploadKey(keyStoreBase64, uuid);
            simpleOperationHelper.dsCreate(keyStorePass, uuid);
            base64DigitalSign = simpleOperationHelper.getBase64DigitalSign(uuid);
            System.out.println(base64DigitalSign);
        } finally {
            simpleOperationHelper.deleteTicket(uuid);
        }
        return base64DigitalSign;
    }


    public String getEncryptData(String dataBase64, String keyStoreBase64, String keyStorePass, String cert) throws IOException {
        String uuid;
        Map<String, String> options;
        String base64DigitalSign;
        // Создание сессии
        uuid = simpleOperationHelper.createTicket();
        try {

            simpleOperationHelper.uploadTextData(dataBase64, uuid);
            // Установка параметров сессии
            options = new HashMap<>();
            options.put("signatureType", "attached");
            options.put("embedSignatureTs", "false");
            options.put("embedDataTs", "false");
            options.put("embedCertificateType", "signerAndCaCert");
            options.put("signatureTsVerifyOption", "ignore");
            options.put("dataTsVerifyOption", "ignore");
            options.put("tsAdditionalVerifying", "false");
            options.put("dataToSignQualifier", "notSignedBefore");
            options.put("duplicateSign", "false");
            options.put("caId", "iddDfs");
            options.put("cadesType", "undefined");
            options.put("nokkCompatible", "false");
            simpleOperationHelper.setOptions(options, uuid);
            simpleOperationHelper.uploadKey(keyStoreBase64, uuid);
            ArrayList<String> certs = new ArrayList<>();
            certs.add(cert);
            simpleOperationHelper.uploadCert(certs, uuid);
            simpleOperationHelper.encrypt(keyStorePass, uuid);
            base64DigitalSign = simpleOperationHelper.getBase64EncryotedData(uuid);
            System.out.println(base64DigitalSign);
        } finally {
            simpleOperationHelper.deleteTicket(uuid);
        }
        return base64DigitalSign;
    }

//    public static String keyStorePass = "ofwCA9iF";
//    public static String keyStoreBase64 = "MIIDrjAcBgwrBgEEAYGXRgEBAQIwDAQEmKxy/AQE1H9WJASCA4zMjGNxJtmShgm5FN5bKDyq9Li0RDzp/8lcZYz0Vrah0aK3hlZDY2x5zWsdtvCMOicY1ynxsQe6l1CqcJ4myFWBaLKZZ2Sn2YT4zssF6FwrN4CxJmocXwIl40g5442ljGcy1WxiTKBSuY3DLC0sf60ls2mEeWFj9ZLPDXlexwZ487MMoBPOFFsbtKSPNUJuN9ls2a3hUltgQDMNZwLtwRN6W+dVg5yIZoDEX5qpjNXXdtm5f/QcqY8oJ7sD6kAGjm6ZExzJo0sP3e8URjZtAeBYbEKrSFuaPVi4+lqK+HgoHuDc9omv4qsaYJxta2SDtejwXSJXKhdAZ43nwie4uF0wURvPuR0/RaL0UB9Y4wtWP3SaNdP8yUwDLqSffZymmAtQmcmS6zisbcYJlNkrKROccCIYRgoimpr4Z9In5sO2UWYe6SAiXaMYGRkl64Pf5rSWZ123RuN+5xr2eaDuxSlsuyx7Kawgud1RG8+5HT9FotBXczyFVtXlh8LwXHHA5iHH5cOcgQi8mf+kxYTUVfoqeWsmV7h76GLhO6v7W36VvM3PI2GzDPvhV3TCuYHI6iIkqExpTieXCL2qCChLV+8lXCW/kWn1c5OZ2DqBvHpa/q/QbgqzovxIpeTwgGvLfSiLkZ7Fr1/8c3ooWk+ZvyPgFu/Ma/ReRhN1g02Qr73l1WKqzycAn/FoMdcbsctth7aAKsEWRZFvE991uNnL9uwN33W42cv27A3fdbjZy/bsDXYnRlUn/NzqzFR29x8k9Hy7yfqdVxgSAL2AE/6jgXOKGPrtLtZcim/igOHW9/ZeuleTEUAnj7eUHv9qSqyaCWpL0ZqPZu9KC2X+HAeBcUqGBZ9tc0U/uefYYkq3A90jzZ/3A8qzHjrrJVBi+MWArwQAwzVH1zA7ofhMrpMyREKphE3o896tSl6Hy0JZti41hUsxSJVjpp4OaTSQjIMl3P6UkvPlO17KFn2wdxONtxStlud7RgfYw76Jlirkgs2kYHAaJgpCo7uMNtFKp+tMTbFyvG22Qjinz8SmgyD0MYg/+5tyhaLpAy5wUINXzBBJIr4wnP5K9F0x0iTRoqNpH7TKlmgeRMnmsUW99sfa0kiAIcL2gVz+0MEfhUiL4OZ6//20GkSBlzEGgjnQSxiP/sp2GxkaOFsPgGdtLiPFbDp3xsni/NcpjU5GHYtiFGz/vWTYrjJ+oITkrgWlDA==";
//    public static String dataBase64 = "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";
//    public static String cert = "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";
//    public static void main(String[] args) throws IOException {
//        SaiferService saiferService = new SaiferService();
//        String singDataBase64 = saiferService.getSingDataBase64(Base64.getEncoder().encodeToString("1234".getBytes()), keyStoreBase64, keyStorePass);
//        String encryptData = saiferService.getEncryptData(singDataBase64, keyStoreBase64, keyStorePass, cert);
//        System.out.println(encryptData);
//    }
}