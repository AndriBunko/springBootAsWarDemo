package com.abunko.services.saifer.model.saifer.request;

public class SaiferRequest {
        private String dataBase64;
        private String keyStoreBase64;
        private String keyStorePass;
        private SesionParams sessionParams;

    public String getDataBase64() {
        return dataBase64;
    }

    public void setDataBase64(String dataBase64) {
        this.dataBase64 = dataBase64;
    }

    public String getKeyStoreBase64() {
        return keyStoreBase64;
    }

    public void setKeyStoreBase64(String keyStoreBase64) {
        this.keyStoreBase64 = keyStoreBase64;
    }

    public String getKeyStorePass() {
        return keyStorePass;
    }

    public void setKeyStorePass(String keyStorePass) {
        this.keyStorePass = keyStorePass;
    }

    public SesionParams getSessionParams() {
        return sessionParams;
    }

    public void setSessionParams(SesionParams sessionParams) {
        this.sessionParams = sessionParams;
    }

    @Override
    public String toString() {
        return "SaiferRequest{" +
                "dataBase64='" + dataBase64 + '\'' +
                ", keyStoreBase64='" + keyStoreBase64 + '\'' +
                ", keyStorePass='" + keyStorePass + '\'' +
                ", sessionParams=" + sessionParams +
                '}';
    }
}
