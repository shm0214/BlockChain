package com.group.model;

import org.hyperledger.fabric.protos.orderer.etcdraft.Configuration.ConfigMetadata;


public class ConsensusInfo {
    private String type;
    private ConfigMetadata raftMetadata;
    private Integer maxMessageCount, absoluteMaxBytes, preferredMaxBytes;
    private String batchTimeOut;
    private String[] borkers, capabilities;

    public ConsensusInfo() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ConfigMetadata getRaftMetadata() {
        return raftMetadata;
    }

    public void setRaftMetadata(ConfigMetadata raftMetadata) {
        this.raftMetadata = raftMetadata;
    }

    public Integer getMaxMessageCount() {
        return maxMessageCount;
    }

    public void setMaxMessageCount(Integer maxMessageCount) {
        this.maxMessageCount = maxMessageCount;
    }

    public Integer getAbsoluteMaxBytes() {
        return absoluteMaxBytes;
    }

    public void setAbsoluteMaxBytes(Integer absoluteMaxBytes) {
        this.absoluteMaxBytes = absoluteMaxBytes;
    }

    public Integer getPreferredMaxBytes() {
        return preferredMaxBytes;
    }

    public void setPreferredMaxBytes(Integer preferredMaxBytes) {
        this.preferredMaxBytes = preferredMaxBytes;
    }

    public String getBatchTimeOut() {
        return batchTimeOut;
    }

    public void setBatchTimeOut(String batchTimeOut) {
        this.batchTimeOut = batchTimeOut;
    }

    public String[] getBorkers() {
        return borkers;
    }

    public void setBorkers(String[] borkers) {
        this.borkers = borkers;
    }

    public String[] getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(String[] capabilities) {
        this.capabilities = capabilities;
    }
}
