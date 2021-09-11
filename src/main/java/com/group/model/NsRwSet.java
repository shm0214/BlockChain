package com.group.model;

import org.hyperledger.fabric.protos.ledger.rwset.kvrwset.*;

import java.util.ArrayList;

// NsRwSet encapsulates 'kvrwset.KVRWSet' proto message for a specific name space (chaincode)
public class NsRwSet {
    private String namespace;
    private KvRwset.KVRWSet kvRwset;
    private ArrayList<CollHashedRwSet> collHashedRwSets;

    public NsRwSet() {
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public KvRwset.KVRWSet getKvRwset() {
        return kvRwset;
    }

    public void setKvRwset(KvRwset.KVRWSet kvRwset) {
        this.kvRwset = kvRwset;
    }

    public ArrayList<CollHashedRwSet> getCollHashedRwSets() {
        return collHashedRwSets;
    }

    public void setCollHashedRwSets(ArrayList<CollHashedRwSet> collHashedRwSets) {
        this.collHashedRwSets = collHashedRwSets;
    }
}
