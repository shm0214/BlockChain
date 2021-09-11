package com.group.model;

import java.io.Serializable;
import java.util.ArrayList;

public class BlockSummary implements Serializable {
    private long blockNum;
    private String hash;
    private String preHash;
    private String channel;
    // 0 transaction 1 config
    private int type;
    private int transCount;
    private String commitHash;
    private long lastConfig;
    private ArrayList<TransactionSummary> transactions;
    private ConfigSummary configSummary;

    public BlockSummary() {
        transactions = new ArrayList<>();
    }

    public long getBlockNum() {
        return blockNum;
    }

    public void setBlockNum(long blockNum) {
        this.blockNum = blockNum;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getPreHash() {
        return preHash;
    }

    public void setPreHash(String preHash) {
        this.preHash = preHash;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getTransCount() {
        return transCount;
    }

    public void setTransCount(int transCount) {
        this.transCount = transCount;
    }

    public String getCommitHash() {
        return commitHash;
    }

    public void setCommitHash(String commitHash) {
        this.commitHash = commitHash;
    }

    public long getLastConfig() {
        return lastConfig;
    }

    public void setLastConfig(long lastConfig) {
        this.lastConfig = lastConfig;
    }

    public ArrayList<TransactionSummary> getTransactions() {
        return transactions;
    }

    public void setTransactions(ArrayList<TransactionSummary> transactions) {
        this.transactions = transactions;
    }

    public ConfigSummary getConfigSummary() {
        return configSummary;
    }

    public void setConfigSummary(ConfigSummary configSummary) {
        this.configSummary = configSummary;
    }
}
