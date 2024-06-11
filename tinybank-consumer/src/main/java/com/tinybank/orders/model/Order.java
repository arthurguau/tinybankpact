package com.tinybank.orders.model;

import java.util.List;

public class Order {
    private int partyId;
    private List<Account> accounts;

    public int getPartyId() {
        return partyId;
    }

    public void setPartyId(int id) {
        this.partyId = id;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }
}
