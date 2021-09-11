package com.group.model;

import org.hyperledger.fabric.protos.peer.ProposalResponsePackage;

import java.util.ArrayList;
import java.util.List;

public class ChaincodeAction {
    public Proposal proposal;
    public Response response;
    public List<ProposalResponsePackage.Endorsement> endorses;

    public ChaincodeAction() {
        proposal = new Proposal();
        response = new Response();
    }
}