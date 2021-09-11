package com.group.model;

import java.util.Map;

public class ConfigSummary {
    private GroupOrg[] ordererOrgs;
    private Map<String, GroupOrg[]> consortiumOrgs;
    private GroupOrg[] applciationOrgs;
    private ApplicationValues applicationValues;
    private ConfigValues values;
    private ConsensusInfo consensus;

    public ConfigSummary() {
    }

    public GroupOrg[] getOrdererOrgs() {
        return ordererOrgs;
    }

    public void setOrdererOrgs(GroupOrg[] ordererOrgs) {
        this.ordererOrgs = ordererOrgs;
    }

    public Map<String, GroupOrg[]> getConsortiumOrgs() {
        return consortiumOrgs;
    }

    public void setConsortiumOrgs(Map<String, GroupOrg[]> consortiumOrgs) {
        this.consortiumOrgs = consortiumOrgs;
    }

    public GroupOrg[] getApplciationOrgs() {
        return applciationOrgs;
    }

    public void setApplciationOrgs(GroupOrg[] applciationOrgs) {
        this.applciationOrgs = applciationOrgs;
    }

    public ApplicationValues getApplicationValues() {
        return applicationValues;
    }

    public void setApplicationValues(ApplicationValues applicationValues) {
        this.applicationValues = applicationValues;
    }

    public ConfigValues getValues() {
        return values;
    }

    public void setValues(ConfigValues values) {
        this.values = values;
    }

    public ConsensusInfo getConsensus() {
        return consensus;
    }

    public void setConsensus(ConsensusInfo consensus) {
        this.consensus = consensus;
    }
}
