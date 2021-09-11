package com.group.model;

public class Payload {
    public Header header;
    public Transaction transaction;

    public Payload() {
        header = new Header();
        transaction = new Transaction();
    }
}