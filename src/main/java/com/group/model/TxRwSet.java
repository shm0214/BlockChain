package com.group.model;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import org.hyperledger.fabric.protos.ledger.rwset.*;
import org.hyperledger.fabric.protos.ledger.rwset.kvrwset.KvRwset;


import java.util.ArrayList;

public class TxRwSet {
    private ArrayList<NsRwSet> nsRwSets;

    public TxRwSet() {
        nsRwSets = new ArrayList<>();
    }

    public ArrayList<NsRwSet> getNsRwSets() {
        return nsRwSets;
    }

    public void setNsRwSets(ArrayList<NsRwSet> nsRwSets) {
        this.nsRwSets = nsRwSets;
    }

    public boolean fromProtoBytes(ByteString protoBytes){
        try {
            Rwset.TxReadWriteSet protoMsg = Rwset.TxReadWriteSet.parseFrom(protoBytes);
            TxRwSet txRwSetTemp = txRwSetFromProtoMsg(protoMsg);
            nsRwSets = txRwSetTemp.nsRwSets;
            return true;
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        return false;
    }

    public TxRwSet txRwSetFromProtoMsg(Rwset.TxReadWriteSet protoMsg){
        TxRwSet txRwSet = new TxRwSet();
        for(Rwset.NsReadWriteSet nsRwSetProtoMsg : protoMsg.getNsRwsetList()){
            NsRwSet nsRwSet = nsRwSetFromProtoMsg(nsRwSetProtoMsg);
            if(nsRwSet == null){
                return null;
            }
            if(nsRwSet.getNamespace() == "lscc"){
                continue;
            }
            txRwSet.nsRwSets.add(nsRwSet);
        }
        return txRwSet;
    }

    public NsRwSet nsRwSetFromProtoMsg(Rwset.NsReadWriteSet protoMsg){
        try {
            NsRwSet nsRwSet = new NsRwSet();
            nsRwSet.setNamespace(protoMsg.getNamespace());
            KvRwset.KVRWSet kvrwSet = KvRwset.KVRWSet.parseFrom(protoMsg.getRwset());
            nsRwSet.setKvRwset(kvrwSet);
            return nsRwSet;
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        return null;
    }
}
