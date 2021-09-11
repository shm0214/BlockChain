package com.group.model;

public class SignInfo {
    private String MSPID;
    private Cert cert;
    private String signature;

    public SignInfo() {
    }

    public String getMSPID() {
        return MSPID;
    }

    public void setMSPID(String MSPID) {
        this.MSPID = MSPID;
    }

    public Cert getCert() {
        return cert;
    }

    public void setCert(Cert cert) {
        this.cert = cert;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}
