package com.abunko.wardeployment.model.saifer.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SaiferCreateSesionResponse {
    private String message;
    private String ticketUuid;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTicketUuid() {
        return ticketUuid;
    }

    public void setTicketUuid(String ticketUuid) {
        this.ticketUuid = ticketUuid;
    }

    @Override
    public String toString() {
        return "SaiferCreateSesionResponse{" +
                "message='" + message + '\'' +
                ", ticketUuid='" + ticketUuid + '\'' +
                '}';
    }
}
