package com.abunko.services.saifer.services;

import com.abunko.services.saifer.exception.InvalidSignExeption;
import com.abunko.services.saifer.exception.SignerServerInteractionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.json.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SuppressWarnings("Duplicates")
public class SimpleOperationHelper {
    private static final Logger LOG = LoggerFactory.getLogger(SimpleOperationHelper.class);
    private String serviceBaseUrl;

    public SimpleOperationHelper(String aServeiceBaseUrl) {
        serviceBaseUrl = aServeiceBaseUrl;
    }

    public String createTicket() throws IOException {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(serviceBaseUrl).path("ticket");
        Response response = target.request().post(Entity.text(""));
        LOG.info("Create session response: " + response);
        if (response.getStatus() == 200) {
            try (InputStream is = response.readEntity(InputStream.class)) {
                JsonReader jr = Json.createReader(is);
                JsonObject responseJson = jr.readObject();
                return responseJson.getString("ticketUuid");
            }
        } else {
            final String EXCEPTION_MESSAGE = "Ошибка при создании сессии. ";
            String extendedFailureDescription = getFailureDescriptions(response).stream().collect(Collectors.joining(" "));
            throw new SignerServerInteractionException(EXCEPTION_MESSAGE + extendedFailureDescription);
        }
    }

    public void deleteTicket(String aTicketUuid) throws IOException {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(serviceBaseUrl).path("ticket");
        Response response = target.path(aTicketUuid).request().delete();
        LOG.info("Delete session response: " + response + ". Session: " + aTicketUuid);
        if (response.getStatus() != 200) {
            final String EXCEPTION_MESSAGE = "Ошибка при удалении сессии. ";
            String extendedFailureDescription = getFailureDescriptions(response).stream().collect(Collectors.joining(" "));
            throw new SignerServerInteractionException(EXCEPTION_MESSAGE + extendedFailureDescription);
        }
    }


    public void uploadTextData(String aData, String aTicketUuid) throws IOException {
        JsonObjectBuilder job = Json.createObjectBuilder();
        job.add("base64Data", aData);
        String jsonString = job.build().toString();

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(serviceBaseUrl).path("ticket").path(aTicketUuid).path("data");
        Response response = target.request().post(Entity.entity(jsonString, MediaType.APPLICATION_JSON_TYPE));
        LOG.info("Upload data response: " + response + ". Session: " + aTicketUuid);
        if (response.getStatus() != 200) {
            final String EXCEPTION_MESSAGE = "Ошибка при загрузке данных сессии. ";
            String extendedFailureDescription = getFailureDescriptions(response).stream().collect(Collectors.joining(" "));
            throw new SignerServerInteractionException(EXCEPTION_MESSAGE + extendedFailureDescription);
        }
    }

    public void uploadKey(String akey, String aTicketUuid) throws IOException {
        System.out.println(akey);
        JsonObjectBuilder job = Json.createObjectBuilder();
        job.add("base64Data", akey);
        String jsonString = job.build().toString();

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(serviceBaseUrl).path("ticket").path(aTicketUuid).path("keyStore");
        Response response = target.request().put(Entity.entity(jsonString, MediaType.APPLICATION_JSON_TYPE));
        LOG.info("Upload key store response: " + response + ". Session: " + aTicketUuid);
        if (response.getStatus() != 200) {
            System.out.println(response.getStatus());
            System.out.println(response.getEntity());
            final String EXCEPTION_MESSAGE = "Ошибка при загрузке ключевого контейнера. ";
            String extendedFailureDescription = getFailureDescriptions(response).stream().collect(Collectors.joining(" "));
            throw new SignerServerInteractionException(EXCEPTION_MESSAGE + extendedFailureDescription);
        }
    }

    public void uploadCert(List<String> certs, String aTicketUuid) throws IOException {
        JsonArrayBuilder job = Json.createArrayBuilder();
        for (String cert : certs) {
            job.add(cert);
        }
        JsonArray build = job.build();
        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
        objectBuilder.add("recipientCertificates", build);
        String jsonString = objectBuilder.build().toString();

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(serviceBaseUrl).path("ticket").path(aTicketUuid).path("encryptor").path("certificates");
        Response response = target.request().post(Entity.entity(jsonString, MediaType.APPLICATION_JSON_TYPE));
        LOG.info("Upload client cert response: " + response + ". Session: " + aTicketUuid);
        if (response.getStatus() != 200) {
            System.out.println(response.getStatus());
            System.out.println(response.getEntity());
            final String EXCEPTION_MESSAGE = "Ошибка при загрузке сертификата пользователя контейнера. ";
            String extendedFailureDescription = getFailureDescriptions(response).stream().collect(Collectors.joining(" "));
            throw new SignerServerInteractionException(EXCEPTION_MESSAGE + extendedFailureDescription);
        }
    }


    public void decryptor(String akey, String aTicketUuid) throws IOException {
        JsonObjectBuilder job = Json.createObjectBuilder();
        job.add("keyStorePassword", akey);
        String jsonString = job.build().toString();

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(serviceBaseUrl).path("ticket").path(aTicketUuid).path("decryptor");
        Response response = target.request().post(Entity.entity(jsonString, MediaType.APPLICATION_JSON_TYPE));
        LOG.info("Decrypt customer crypto response: " + response + ". Session: " + aTicketUuid);
        if (response.getStatus() != 200) {
            final String EXCEPTION_MESSAGE = "Ошибка при загрузке данных сессии. ";
            String extendedFailureDescription = getFailureDescriptions(response).stream().collect(Collectors.joining(" "));
            throw new SignerServerInteractionException(EXCEPTION_MESSAGE + extendedFailureDescription);
        }
    }

    public void dsCreate(String akey, String aTicketUuid) throws IOException {
        JsonObjectBuilder job = Json.createObjectBuilder();
        job.add("keyStorePassword", akey);
        String jsonString = job.build().toString();

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(serviceBaseUrl).path("ticket").path(aTicketUuid).path("ds").path("creator");
        Response response = target.request().post(Entity.entity(jsonString, MediaType.APPLICATION_JSON_TYPE));
        LOG.info("DS create response: " + response + ". Session: " + aTicketUuid);
        if (response.getStatus() != 200) {
            final String EXCEPTION_MESSAGE = "Ошибка при загрузке данных сессии. ";
            String extendedFailureDescription = getFailureDescriptions(response).stream().collect(Collectors.joining(" "));
            throw new SignerServerInteractionException(EXCEPTION_MESSAGE + extendedFailureDescription);
        }
    }

    public void encrypt(String akey, String aTicketUuid) throws IOException {
        JsonObjectBuilder job = Json.createObjectBuilder();
        job.add("keyStorePassword", akey);
        String jsonString = job.build().toString();

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(serviceBaseUrl).path("ticket").path(aTicketUuid).path("encryptor");
        Response response = target.request().post(Entity.entity(jsonString, MediaType.APPLICATION_JSON_TYPE));
        LOG.info("Encrypt create response: " + response + ". Session: " + aTicketUuid);
        if (response.getStatus() != 200) {
            final String EXCEPTION_MESSAGE = "Ошибка при инициализации зашифрования. ";
            String extendedFailureDescription = getFailureDescriptions(response).stream().collect(Collectors.joining(" "));
            throw new SignerServerInteractionException(EXCEPTION_MESSAGE + extendedFailureDescription);
        }
    }

    public void setOptions(Map<String, String> aOptions, String aTicketUuid) throws IOException {
        final JsonObjectBuilder job = Json.createObjectBuilder();
        aOptions.entrySet().stream()
                .forEach(e -> job.add(e.getKey(), e.getValue()));
        String jsonString = job.build().toString();
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(serviceBaseUrl).path("ticket").path(aTicketUuid).path("option");
        Response response = target.request().put(Entity.entity(jsonString, MediaType.APPLICATION_JSON_TYPE));
        LOG.info("Set session option response: " + response + ". Session: " + aTicketUuid);
        if (response.getStatus() != 200) {
            final String EXCEPTION_MESSAGE = "Ошибка при установке параметров сессии. ";
            String extendedFailureDescription = getFailureDescriptions(response).stream().collect(Collectors.joining(" "));
            throw new SignerServerInteractionException(EXCEPTION_MESSAGE + extendedFailureDescription);
        }
    }


    public String getBase64DigitalSign(String aTicketUuid) throws IOException {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(serviceBaseUrl).path("ticket").path(aTicketUuid).path("ds").path("base64Data");
        Response response = target.request().get();
        LOG.info("Get ds in base 64 response: " + response + ". Session: " + aTicketUuid);
        if (response.getStatus() == 200) {
            try (InputStream is = response.readEntity(InputStream.class)) {
                JsonReader jr = Json.createReader(is);
                JsonObject responseJson = jr.readObject();
                return responseJson.getString("base64Data");
            }
        } else {
            final String EXCEPTION_MESSAGE = "Ошибка при получении ЭЦП. ";
            String extendedFailureDescription = getFailureDescriptions(response).stream().collect(Collectors.joining(" "));
            throw new SignerServerInteractionException(EXCEPTION_MESSAGE + extendedFailureDescription);
        }
    }

    public String getBase64EncryotedData(String aTicketUuid) throws IOException {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(serviceBaseUrl).path("ticket").path(aTicketUuid).path("encryptor").path("base64Data");
        Response response = target.request().get();
        LOG.info("Get encrypted data in base 64 response: " + response + ". Session: " + aTicketUuid);
        if (response.getStatus() == 200) {
            try (InputStream is = response.readEntity(InputStream.class)) {
                JsonReader jr = Json.createReader(is);
                JsonObject responseJson = jr.readObject();
                return responseJson.getString("base64Data");
            }
        } else {
            final String EXCEPTION_MESSAGE = "Ошибка при получении зашифрованых даных. ";
            String extendedFailureDescription = getFailureDescriptions(response).stream().collect(Collectors.joining(" "));
            throw new SignerServerInteractionException(EXCEPTION_MESSAGE + extendedFailureDescription);
        }
    }

    public String getBase64SignedData(String aTicketUuid) throws IOException {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(serviceBaseUrl).path("ticket").path(aTicketUuid).path("ds").path("base64SignedData");
        Response response = target.request().get();
        LOG.info("Get encrypted data in base 64 response: " + response + ". Session: " + aTicketUuid);
        if (response.getStatus() == 200) {
            try (InputStream is = response.readEntity(InputStream.class)) {
                JsonReader jr = Json.createReader(is);
                JsonObject responseJson = jr.readObject();
                return responseJson.getString("base64Data");
            }
        } else {
            final String EXCEPTION_MESSAGE = "Ошибка при получении даных. ";
            String extendedFailureDescription = getFailureDescriptions(response).stream().collect(Collectors.joining(" "));
            throw new SignerServerInteractionException(EXCEPTION_MESSAGE + extendedFailureDescription);
        }
    }

    public String getBase64DecryptorData(String aTicketUuid) throws IOException {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(serviceBaseUrl).path("ticket").path(aTicketUuid).path("decryptor").path("base64Data");
        Response response = target.request().get();

        if (response.getStatus() == 200) {
            try (InputStream is = response.readEntity(InputStream.class)) {
                JsonReader jr = Json.createReader(is);
                JsonObject responseJson = jr.readObject();
                return responseJson.getString("base64Data");
            }
        } else {
            final String EXCEPTION_MESSAGE = "Ошибка при получении разшифрованих даных. ";
            String extendedFailureDescription = getFailureDescriptions(response).stream().collect(Collectors.joining(" "));
            throw new SignerServerInteractionException(EXCEPTION_MESSAGE + extendedFailureDescription);
        }
    }

    public void uploadDsBase64Data(String aBase64Data, String aTicketUuid) throws IOException {
        JsonObjectBuilder job = Json.createObjectBuilder();
        job.add("base64Data", aBase64Data);
        String jsonString = job.build().toString();
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(serviceBaseUrl).path("ticket").path(aTicketUuid).path("ds").path("data");
        Response response = target.request().post(Entity.entity(jsonString, MediaType.APPLICATION_JSON_TYPE));
        LOG.info("Upload ds in base 64 response: " + response + ". Session: " + aTicketUuid);
        if (response.getStatus() != 200) {
            final String EXCEPTION_MESSAGE = "Ошибка при загрузке данных ЭЦП. ";
            String extendedFailureDescription = getFailureDescriptions(response).stream().collect(Collectors.joining(" "));
            throw new SignerServerInteractionException(EXCEPTION_MESSAGE + extendedFailureDescription);
        }
    }

    public void verifyDigitalSign(String aTicketUuid) throws IOException {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(serviceBaseUrl).path("ticket").path(aTicketUuid).path("ds").path("verifier");
        Response response = target.request().post(Entity.text(""));
        LOG.info("Verify ds response: " + response + ". Session: " + aTicketUuid);
        if (response.getStatus() != 200) {
            final String EXCEPTION_MESSAGE = "Ошибка при проверке ЭЦП. ";
            String extendedFailureDescription = getFailureDescriptions(response).stream().collect(Collectors.joining(" "));
            throw new SignerServerInteractionException(EXCEPTION_MESSAGE + extendedFailureDescription);
        }
    }

    public String getDecryptorResult(String aTicketUuid) throws IOException {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(serviceBaseUrl).path("ticket").path(aTicketUuid).path("decryptor");
        Response response = target.request().get();
        LOG.info("Get decryptor result response: " + response + ". Session: " + aTicketUuid);
        if (response.getStatus() == 202) {
            return getDecryptorResult(aTicketUuid);
        } else if (response.getStatus() == 200) {
            try (InputStream is = response.readEntity(InputStream.class)) {
                JsonReader jr = Json.createReader(is);
                JsonObject responseJson = jr.readObject();
                return responseJson.getString("message");
            }
        } else {
            final String EXCEPTION_MESSAGE = "Ошибка разшифрования даных. ";
            String extendedFailureDescription = getFailureDescriptions(response).stream().collect(Collectors.joining(" "));
            throw new SignerServerInteractionException(EXCEPTION_MESSAGE + extendedFailureDescription);
        }
    }

    public JsonArray getDigitalSignVerifyingResult(String aTicketUuid) throws IOException, InvalidSignExeption {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(serviceBaseUrl).path("ticket").path(aTicketUuid).path("ds").path("verifier");
        Response response = target.request().get();
        LOG.info("Get verify ds response: " + response + ". Session: " + aTicketUuid);
        if (response.getStatus() == 200) {
            try (InputStream is = response.readEntity(InputStream.class)) {
                JsonReader jr = Json.createReader(is);
                JsonObject responseJson = jr.readObject();
                return responseJson.getJsonArray("verifyResults");
            }
        }
        if (response.getStatus() == 406) {
            try (InputStream is = response.readEntity(InputStream.class)) {
                JsonReader jr = Json.createReader(is);
                JsonObject responseJson = jr.readObject();
                throw new InvalidSignExeption("Цифровая подпись недействительна.", responseJson.getJsonArray("verifyResults"));
            }
        }
        final String EXCEPTION_MESSAGE = "Ошибка при получении результа проверки ЭЦП. ";
        String extendedFailureDescription = getFailureDescriptions(response).stream().collect(Collectors.joining(" "));
        throw new SignerServerInteractionException(EXCEPTION_MESSAGE + extendedFailureDescription);
    }

    private Collection<String> getFailureDescriptions(Response aResponse) throws IOException {
        List<String> descriptions = new ArrayList<>();
        if (aResponse.getMediaType().equals(MediaType.APPLICATION_JSON_TYPE)) {
            try (InputStream is = aResponse.readEntity(InputStream.class)) {
                JsonReader jr = Json.createReader(is);
                JsonObject responseJson = jr.readObject();
                if (responseJson.containsKey("message")) {
                    descriptions.add(responseJson.getString("message"));
                }
                if (responseJson.containsKey("failureCause")) {
                    descriptions.add(responseJson.getString("failureCause"));
                }
            }
        }
        return descriptions;
    }
}