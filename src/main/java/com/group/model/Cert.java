package com.group.model;

import com.google.protobuf.ByteString;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x500.style.IETFUtils;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.bouncycastle.jce.X509Principal;
import org.bouncycastle.util.io.pem.PemReader;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public class Cert {
    private String pem;
    private String cn;
    private String ou;
    private String org;

    public Cert() {
    }

    public Cert(ByteString certRaw){
        pem = certRaw.toString();
        InputStream stream = new ByteArrayInputStream(pem.getBytes(StandardCharsets.UTF_8));
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            X509Certificate cert = (X509Certificate) cf.generateCertificate(stream);
            X500Name x500Name = new JcaX509CertificateHolder(cert).getSubject();
            RDN cn = x500Name.getRDNs(BCStyle.CN)[0];
            this.cn = IETFUtils.valueToString(cn.getFirst().getValue());
            RDN ou = x500Name.getRDNs(BCStyle.OU)[0];
            this.ou = IETFUtils.valueToString(cn.getFirst().getValue());
            RDN org = x500Name.getRDNs(BCStyle.O)[0];
            this.org = IETFUtils.valueToString(cn.getFirst().getValue());
        } catch (CertificateException e) {
            e.printStackTrace();
        }
    }

    public String getPem() {
        return pem;
    }

    public void setPem(String pem) {
        this.pem = pem;
    }

    public String getCn() {
        return cn;
    }

    public void setCn(String cn) {
        this.cn = cn;
    }

    public String getOu() {
        return ou;
    }

    public void setOu(String ou) {
        this.ou = ou;
    }

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }
}
