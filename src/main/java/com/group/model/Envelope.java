package com.group.model;

import com.google.protobuf.ByteString;
public class Envelope {

    public Payload payload;
    public ByteString signature;

    public Envelope() {
        payload = new Payload();
    }
}
