package com.group.model;

import org.hyperledger.fabric.protos.common.Common;

public class Transaction {
    public Common.SignatureHeader header;
    public ChaincodeAction chaincodeAction;

    public Transaction() {
        chaincodeAction = new ChaincodeAction();
    }
}