package com.abunko.services.saifer.model.saifer.request;

public class EncriprSayferRequest extends SaiferRequest {
    private String cert;

    public String getCert() {
        return cert;
    }

    public void setCert(String cert) {
        this.cert = cert;
    }

    @Override
    public String toString() {
        return "EncriprSayferRequest{" +
                "cert='" + cert + '\'' +
                '}';
    }
}
