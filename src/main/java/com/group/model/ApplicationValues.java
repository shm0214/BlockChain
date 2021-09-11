package com.group.model;

import java.util.Map;

public class ApplicationValues {
    private Map<String, String> ACLs;
    private String[] capabilities;

    public ApplicationValues() {
    }

    public Map<String, String> getACLs() {
        return ACLs;
    }

    public void setACLs(Map<String, String> ACLs) {
        this.ACLs = ACLs;
    }

    public String[] getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(String[] capabilities) {
        this.capabilities = capabilities;
    }
}
