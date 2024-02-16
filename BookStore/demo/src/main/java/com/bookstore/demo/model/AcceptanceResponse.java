package com.bookstore.demo.model;

public class AcceptanceResponse {
    private boolean acceptance;
    
    public AcceptanceResponse() {
    }

    public AcceptanceResponse(boolean acceptance) {
        this.acceptance = acceptance;
    }

    public boolean isAcceptance() {
        return acceptance;
    }

    public void setAcceptance(boolean acceptance) {
        this.acceptance = acceptance;
    }
}
