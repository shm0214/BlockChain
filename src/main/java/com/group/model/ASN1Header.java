package com.group.model;

import org.bouncycastle.asn1.*;

public class ASN1Header extends ASN1Object {
    private ASN1Integer number;
    private BEROctetString previousHash, dataHash;

    public ASN1Integer getNumber() {
        return number;
    }

    public void setNumber(ASN1Integer number) {
        this.number = number;
    }

    public BEROctetString getPreviousHash() {
        return previousHash;
    }

    public void setPreviousHash(BEROctetString previousHash) {
        this.previousHash = previousHash;
    }

    public BEROctetString getDataHash() {
        return dataHash;
    }

    public void setDataHash(BEROctetString dataHash) {
        this.dataHash = dataHash;
    }

    public ASN1Header(ASN1Integer number, BEROctetString previousHash, BEROctetString dataHash) {
        this.number = number;
        this.previousHash = previousHash;
        this.dataHash = dataHash;
    }

    @Override
    public ASN1Primitive toASN1Primitive() {
        ASN1EncodableVector vector = new ASN1EncodableVector();
        vector.add(number);
        vector.add(previousHash);
        vector.add(dataHash);
        return new DERSequence(vector);
    }
}
