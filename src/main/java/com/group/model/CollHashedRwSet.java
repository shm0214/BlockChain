package com.group.model;

import com.google.protobuf.ByteString;
import org.hyperledger.fabric.protos.ledger.rwset.kvrwset.*;

// CollHashedRwSet encapsulates 'kvrwset.HashedRWSet' proto message for a specific collection
public class CollHashedRwSet {
    private String collectionName;
    private KvRwset.HashedRWSet hashedRWSet;
    private ByteString pvtRwSetHash;

    public CollHashedRwSet() {
    }

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    public KvRwset.HashedRWSet getHashedRWSet() {
        return hashedRWSet;
    }

    public void setHashedRWSet(KvRwset.HashedRWSet hashedRWSet) {
        this.hashedRWSet = hashedRWSet;
    }

    public ByteString getPvtRwSetHash() {
        return pvtRwSetHash;
    }

    public void setPvtRwSetHash(ByteString pvtRwSetHash) {
        this.pvtRwSetHash = pvtRwSetHash;
    }
}
