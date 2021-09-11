package com.group.model;

public class ConfigValues {
    private String consortium;
    private String hashingAlgorithm;
    private String[] ordererAddresses;
    private Integer blockDataHashWidth;
    private String[] capabilities;

    public ConfigValues() {
    }

    public String getConsortium() {
        return consortium;
    }

    public void setConsortium(String consortium) {
        this.consortium = consortium;
    }

    public String getHashingAlgorithm() {
        return hashingAlgorithm;
    }

    public void setHashingAlgorithm(String hashingAlgorithm) {
        this.hashingAlgorithm = hashingAlgorithm;
    }

    public String[] getOrdererAddresses() {
        return ordererAddresses;
    }

    public void setOrdererAddresses(String[] ordererAddresses) {
        this.ordererAddresses = ordererAddresses;
    }

    public Integer getBlockDataHashWidth() {
        return blockDataHashWidth;
    }

    public void setBlockDataHashWidth(Integer blockDataHashWidth) {
        this.blockDataHashWidth = blockDataHashWidth;
    }

    public String[] getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(String[] capabilities) {
        this.capabilities = capabilities;
    }
}
