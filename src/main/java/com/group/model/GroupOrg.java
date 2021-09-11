package com.group.model;

import com.google.protobuf.ByteString;

import java.util.List;

public class GroupOrg {

    private Integer type;
    private String typeName, name, rootCert, tlsRootCert, admin;
    private List<ByteString> revocationList;
    private String[] endpoints;

    public GroupOrg() {
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRootCert() {
        return rootCert;
    }

    public void setRootCert(String rootCert) {
        this.rootCert = rootCert;
    }

    public String getTlsRootCert() {
        return tlsRootCert;
    }

    public void setTlsRootCert(String tlsRootCert) {
        this.tlsRootCert = tlsRootCert;
    }

    public String getAdmin() {
        return admin;
    }

    public void setAdmin(String admin) {
        this.admin = admin;
    }

    public List<ByteString> getRevocationList() {
        return revocationList;
    }

    public void setRevocationList(List<ByteString> revocationList) {
        this.revocationList = revocationList;
    }

    public String[] getEndpoints() {
        return endpoints;
    }

    public void setEndpoints(String[] endpoints) {
        this.endpoints = endpoints;
    }
}
