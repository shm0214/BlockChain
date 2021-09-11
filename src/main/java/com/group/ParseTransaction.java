package com.group;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Timestamp;
import com.group.model.*;
import org.hyperledger.fabric.protos.common.Common;
import org.hyperledger.fabric.protos.peer.Chaincode;
import org.hyperledger.fabric.protos.peer.ProposalPackage;
import org.hyperledger.fabric.protos.peer.ProposalResponsePackage;
import org.hyperledger.fabric.protos.peer.TransactionPackage;
import org.hyperledger.fabric.protos.peer.TransactionPackage.*;
import org.hyperledger.fabric.protos.msp.Identities;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

public class ParseTransaction {
    private Common.Envelope envelope;
    private Envelope innerEnvelope;

    public ParseTransaction(Common.Envelope envelope) {
        this.envelope = envelope;
        this.innerEnvelope = new Envelope();
        innerEnvelope.signature = envelope.getSignature();
        try {
            Common.Payload payload = Common.Payload.parseFrom(envelope.getPayload());
            TransactionPackage.Transaction transaction = TransactionPackage.Transaction.parseFrom(payload.getData());
            unHeader(payload.getHeader());
            if (transaction.getActionsCount() < 1) {
                System.out.println("No transaction in block!");
                return;
            }
            unAction(transaction.getActions(0));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean unHeader(Common.Header header) {
        try {
            Common.ChannelHeader channelHeader = Common.ChannelHeader.parseFrom(header.getChannelHeader());
            innerEnvelope.payload.header.channelHeader = channelHeader;
            Common.SignatureHeader signatureHeader = Common.SignatureHeader.parseFrom(header.getSignatureHeader());
            innerEnvelope.payload.header.signatureHeader = signatureHeader;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean unAction(TransactionAction action) {
        try {
            ChaincodeActionPayload chaincodeActionPayload = ChaincodeActionPayload.parseFrom(action.getPayload());
            innerEnvelope.payload.transaction.chaincodeAction.endorses = chaincodeActionPayload.getAction().getEndorsementsList();
            ProposalPackage.ChaincodeProposalPayload chaincodeProposalPayload = ProposalPackage.ChaincodeProposalPayload.parseFrom(chaincodeActionPayload.getChaincodeProposalPayload());
            Chaincode.ChaincodeInvocationSpec chaincodeInvocationSpec = Chaincode.ChaincodeInvocationSpec.parseFrom(chaincodeProposalPayload.getInput());
            innerEnvelope.payload.transaction.chaincodeAction.proposal.input = chaincodeInvocationSpec.getChaincodeSpec();
            ProposalResponsePackage.ProposalResponsePayload proposalResponsePayload = ProposalResponsePackage.ProposalResponsePayload.parseFrom(chaincodeActionPayload.getAction().getProposalResponsePayload());
            innerEnvelope.payload.transaction.chaincodeAction.response.proposalHash = proposalResponsePayload.getProposalHash();
            ProposalPackage.ChaincodeAction chaincodeAction = ProposalPackage.ChaincodeAction.parseFrom(proposalResponsePayload.getExtension());
            innerEnvelope.payload.transaction.chaincodeAction.response.chaincodeAction = chaincodeAction;
            TxRwSet txRwSet = new TxRwSet();
            if (!txRwSet.fromProtoBytes(chaincodeAction.getResults())) {
                return false;
            }
            innerEnvelope.payload.transaction.chaincodeAction.response.rwSet = txRwSet;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public TransactionSummary getSummary(){
        TransactionSummary transactionSummary = new TransactionSummary();
        transactionSummary.setChannel(innerEnvelope.payload.header.channelHeader.getChannelId());
        transactionSummary.setTxId(innerEnvelope.payload.header.channelHeader.getTxId());
        Timestamp timestamp = innerEnvelope.payload.header.channelHeader.getTimestamp();
        transactionSummary.setTime(new Date(timestamp.getSeconds()*1000));
        transactionSummary.setChaincode(innerEnvelope.payload.transaction.chaincodeAction.proposal. input.getChaincodeId().getName());
        transactionSummary.setFunction(innerEnvelope.payload.transaction.chaincodeAction.proposal.input.getInput().getArgs(0).toStringUtf8());
        List<ByteString> args = innerEnvelope.payload.transaction.chaincodeAction.proposal.input.getInput().getArgsList();
        ArrayList<String> argsTemp = new ArrayList<>();
        for(ByteString arg:args){
            if(arg.equals(args.get(0))){
                continue;
            }
            argsTemp.add(arg.toStringUtf8());
        }
        transactionSummary.setArgs(argsTemp);
        transactionSummary.getResponse().setStatus(innerEnvelope.payload.transaction.chaincodeAction.response.chaincodeAction.getResponse().getStatus());
        transactionSummary.getResponse().setMessage(innerEnvelope.payload.transaction.chaincodeAction.response.chaincodeAction.getResponse().getMessage());
        transactionSummary.getResponse().setData(innerEnvelope.payload.transaction.chaincodeAction.response.chaincodeAction.getResponse().getPayload().toString());
        transactionSummary.setTxRwSet(innerEnvelope.payload.transaction.chaincodeAction.response.rwSet);
        Identities.SerializedIdentity serializedIdentity = null;
        try {
            serializedIdentity = Identities.SerializedIdentity.parseFrom(innerEnvelope.payload.header.signatureHeader.getCreator());
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        SignInfo signInfo = new SignInfo();
        signInfo.setMSPID(serializedIdentity.getMspid());
        signInfo.setSignature(Base64.getEncoder().encodeToString(innerEnvelope.signature.toByteArray()));
        signInfo.setCert(new Cert(serializedIdentity.getIdBytes()));
        transactionSummary.setSigner(signInfo);
        ArrayList<SignInfo> endorsers = new ArrayList<SignInfo>();
        for(ProposalResponsePackage.Endorsement endorsement : innerEnvelope.payload.transaction.chaincodeAction.endorses){
            Identities.SerializedIdentity serializedIdentity1 = null;
            try {
                serializedIdentity1 = Identities.SerializedIdentity.parseFrom(endorsement.getEndorser());
            } catch (InvalidProtocolBufferException e) {
                e.printStackTrace();
            }
            SignInfo signInfo1 = new SignInfo();
            signInfo1.setMSPID(serializedIdentity1.getMspid());
            signInfo1.setSignature(Base64.getEncoder().encodeToString(endorsement.getSignature().toByteArray()));
            signInfo1.setCert(new Cert(serializedIdentity1.getIdBytes()));
            endorsers.add(signInfo1);
        }
        transactionSummary.setEndorsers(endorsers);
        return transactionSummary;
    }
}
