package com.group.model;

import com.google.protobuf.ByteString;
import org.hyperledger.fabric.protos.peer.ProposalPackage;

public class Response {
    public ByteString proposalHash;
    public ProposalPackage.ChaincodeAction chaincodeAction;
    public TxRwSet rwSet;

    public Response() {
        rwSet = new TxRwSet();
    }
}