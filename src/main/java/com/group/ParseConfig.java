package com.group;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.group.model.*;
import org.hyperledger.fabric.protos.common.Configtx;
import org.hyperledger.fabric.protos.common.Configuration;
import org.hyperledger.fabric.protos.msp.MspConfigPackage;
import org.hyperledger.fabric.protos.peer.Configuration.AnchorPeers;
import org.hyperledger.fabric.protos.peer.Configuration.AnchorPeer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ParseConfig {
    private Configtx.Config config;

    public ParseConfig(Configtx.Config config) {
        this.config = config;
    }


    public ConfigSummary getSummary(){
        ConfigSummary summary = new ConfigSummary();
        summary.setApplciationOrgs(getApplicationOrgs());
        summary.setConsensus(getConsensusInfo());
        summary.setApplicationValues(getApplicationValues());
        summary.setConsortiumOrgs(getConsortiumOrgs());
        summary.setValues(getValues());
        summary.setOrdererOrgs(getOrdererOrgs());
        return summary;
    }
    
    // 解析配置块中的基本参数信息
    public ConfigValues getValues(){
        ConfigValues configValues = new ConfigValues();
        try {
            Configuration.HashingAlgorithm  hashingAlgorithm = Configuration.HashingAlgorithm.parseFrom(config.getChannelGroup().getValuesMap().get("HashingAlgorithm").getValue());
            configValues.setHashingAlgorithm(hashingAlgorithm.getName());
            if(config.getChannelGroup().getValuesMap().containsKey("Consortium")) {
                Configuration.Consortium consortium = Configuration.Consortium.parseFrom(config.getChannelGroup().getValuesMap().get("Consortium").getValue());
                configValues.setConsortium(consortium.getName());
            }
            Configuration.OrdererAddresses ordererAddresses = Configuration.OrdererAddresses.parseFrom(config.getChannelGroup().getValuesMap().get("OrdererAddresses").getValue());
            configValues.setOrdererAddresses(ordererAddresses.getAddressesList().toArray(new String[0]));
            Configuration.Capabilities capabilities = Configuration.Capabilities.parseFrom(config.getChannelGroup().getValuesMap().get("Capabilities").getValue());
            ArrayList<String> values = new ArrayList<>();
            for(String key : capabilities.getCapabilitiesMap().keySet()){
                values.add(key);
            }
            configValues.setCapabilities(values.toArray(new String[0]));
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        return configValues;
    }

    // 解析配置块中的共识相关配置信息
    public ConsensusInfo getConsensusInfo(){
        ConsensusInfo consensusInfo = new ConsensusInfo();
        try {
            org.hyperledger.fabric.protos.orderer.Configuration.ConsensusType consensusType = org.hyperledger.fabric.protos.orderer.Configuration.ConsensusType.parseFrom(config.getChannelGroup().getGroupsMap().get("Orderer").getValuesMap().get("ConsensusType").getValue());
            consensusInfo.setType(consensusType.getType());
            switch (consensusType.getType()){
                case "etcdraft":
                    org.hyperledger.fabric.protos.orderer.etcdraft.Configuration.ConfigMetadata configMetadata = org.hyperledger.fabric.protos.orderer.etcdraft.Configuration.ConfigMetadata.parseFrom(consensusType.getMetadata());
                    consensusInfo.setRaftMetadata(configMetadata);
                    break;
                case "kafka":
                    org.hyperledger.fabric.protos.orderer.Configuration.KafkaBrokers kafkaBrokers = org.hyperledger.fabric.protos.orderer.Configuration.KafkaBrokers.parseFrom(config.getChannelGroup().getGroupsMap().get("Orderer").getValuesMap().get("KafkaBrokers").getValue());
                    consensusInfo.setBorkers(kafkaBrokers.getBrokersList().toArray(new String[0]));
            }
            org.hyperledger.fabric.protos.orderer.Configuration.BatchSize batchSize = org.hyperledger.fabric.protos.orderer.Configuration.BatchSize.parseFrom(config.getChannelGroup().getGroupsMap().get("Orderer").getValuesMap().get("BatchSize").getValue());
            consensusInfo.setAbsoluteMaxBytes(batchSize.getAbsoluteMaxBytes());
            consensusInfo.setMaxMessageCount(batchSize.getMaxMessageCount());
            consensusInfo.setPreferredMaxBytes(batchSize.getPreferredMaxBytes());
            org.hyperledger.fabric.protos.orderer.Configuration.BatchTimeout batchTimeout = org.hyperledger.fabric.protos.orderer.Configuration.BatchTimeout.parseFrom(config.getChannelGroup().getGroupsMap().get("Orderer").getValuesMap().get("BatchTimeout").getValue());
            consensusInfo.setBatchTimeOut(batchTimeout.getTimeout());
            Configuration.Capabilities capabilities = Configuration.Capabilities.parseFrom(config.getChannelGroup().getGroupsMap().get("Orderer").getValuesMap().get("Capabilities").getValue());
            ArrayList<String> values = new ArrayList<>();
            for(String key : capabilities.getCapabilitiesMap().keySet()){
                values.add(key);
            }
            consensusInfo.setCapabilities(values.toArray(new String[0]));
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        return consensusInfo;
    }

    // 解析 Application 节中的 Values 信息
    public ApplicationValues getApplicationValues(){
        ApplicationValues values = new ApplicationValues();
        try {
            Configuration.Capabilities capabilities = Configuration.Capabilities.parseFrom(config.getChannelGroup().getGroupsMap().get("Application").getValuesMap().get("Capabilities").getValue());
            ArrayList<String> value = new ArrayList<>();
            for(String key : capabilities.getCapabilitiesMap().keySet()){
                value.add(key);
            }
            values.setCapabilities(value.toArray(new String[0]));
            if(config.getChannelGroup().getGroupsMap().get("Application").getValuesMap().containsKey("ACLs")) {
                org.hyperledger.fabric.protos.peer.Configuration.ACLs acl = org.hyperledger.fabric.protos.peer.Configuration.ACLs.parseFrom(config.getChannelGroup().getGroupsMap().get("Application").getValuesMap().get("ACLs").getValue());
                HashMap<String, String> acls = new HashMap<>();
                for (String key : acl.getAclsMap().keySet()) {
                    acls.put(key, acl.getAclsMap().get(key).getPolicyRef());
                }
                values.setACLs(acls);
            }
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        return values;
    }

    // 解析配置块中的 orderer 组织
    public GroupOrg[] getOrdererOrgs(){
        ArrayList<GroupOrg> orgs = new ArrayList<>();
        Map<String, Configtx.ConfigGroup> groups = config.getChannelGroup().getGroupsMap().get("Orderer").getGroupsMap();
        for(String key : groups.keySet()){
            Configtx.ConfigGroup group = groups.get(key);
            ByteString mspValue = group.getValuesMap().get("MSP").getValue();
            GroupOrg org = getOrg(mspValue);
            if(group.getValuesMap().containsKey("Endpoints")){
                try {
                    Configuration.OrdererAddresses ordererAddresses = Configuration.OrdererAddresses.parseFrom(group.getValuesMap().get("Endpoints").getValue());
                    org.setEndpoints(ordererAddresses.getAddressesList().toArray(new String[0]));
                } catch (InvalidProtocolBufferException e) {
                    e.printStackTrace();
                }
            }
            orgs.add(org);
        }
        return orgs.toArray(new GroupOrg[0]);
    }

    // 解析配置块中的应用组织
    public GroupOrg[] getApplicationOrgs(){
        ArrayList<GroupOrg> orgs = new ArrayList<>();
        Map<String, Configtx.ConfigGroup> groups = config.getChannelGroup().getGroupsMap().get("Application").getGroupsMap();
        for(String key : groups.keySet()){
            Configtx.ConfigGroup group = groups.get(key);
            ByteString mspValue = group.getValuesMap().get("MSP").getValue();
            GroupOrg org = getOrg(mspValue);
            if(group.getValuesMap().containsKey("AnchorPeers")){
                try {
                    AnchorPeers anchorPeers = AnchorPeers.parseFrom(group.getValuesMap().get("AnchorPeers").getValue());
                    ArrayList<String> list = new ArrayList<>();
                    for(AnchorPeer anchorPeer: anchorPeers.getAnchorPeersList()){
                        String string = anchorPeer.getHost() + ':' + anchorPeer.getPort();
                        list.add(string);
                    }
                    org.setEndpoints(list.toArray(new String[0]));
                } catch (InvalidProtocolBufferException e) {
                    e.printStackTrace();
                }
            }
            orgs.add(org);
        }
        return orgs.toArray(new GroupOrg[0]);
    }

    // 解析系统配置块中的联盟组织
    public Map<String, GroupOrg[]> getConsortiumOrgs(){
        HashMap<String, GroupOrg[]> map = new HashMap<>();
        if(config.getChannelGroup().getGroupsMap().containsKey("Consortium")){
            for(String key : config.getChannelGroup().getGroupsMap().get("Consortium").getGroupsMap().keySet()){
                Configtx.ConfigGroup group = config.getChannelGroup().getGroupsMap().get("Consortium").getGroupsMap().get(key);
                ArrayList<GroupOrg> list = new ArrayList<>();
                for(Configtx.ConfigGroup o :  group.getGroupsMap().values()){
                    GroupOrg groupOrg = getOrg(o.getValuesMap().get("MSP").getValue());
                    list.add(groupOrg);
                }
                map.put(key, list.toArray(new GroupOrg[0]));
            }
        }
        return map;
    }

    public GroupOrg getOrg(ByteString mspValue){
        GroupOrg groupOrg = new GroupOrg();
        try {
            MspConfigPackage.MSPConfig mspConfig = MspConfigPackage.MSPConfig.parseFrom(mspValue);
            switch(mspConfig.getType()){
                case 0:
                    MspConfigPackage.FabricMSPConfig fabricMSPConfig = MspConfigPackage.FabricMSPConfig.parseFrom(mspConfig.getConfig());
                    groupOrg.setType(mspConfig.getType());
                    groupOrg.setTypeName("FABRIC");
                    groupOrg.setName(fabricMSPConfig.getName());
                    if(fabricMSPConfig.getAdminsCount() > 0){
                        groupOrg.setAdmin(fabricMSPConfig.getAdmins(0).toStringUtf8());
                    }
                    if (fabricMSPConfig.getRootCertsCount() > 0) {
                        groupOrg.setRootCert(fabricMSPConfig.getRootCerts(0).toStringUtf8());
                    }
                    if(fabricMSPConfig.getTlsRootCertsCount() > 0){
                        groupOrg.setTlsRootCert(fabricMSPConfig.getTlsRootCerts(0).toStringUtf8());
                    }
                    groupOrg.setRevocationList(fabricMSPConfig.getRevocationListList());
                    break;
                case 1:
                    MspConfigPackage.IdemixMSPConfig idemixMSPConfig = MspConfigPackage.IdemixMSPConfig.parseFrom(mspConfig.getConfig());
                    groupOrg.setType(mspConfig.getType());
                    groupOrg.setTypeName("IDEMIX");
                    groupOrg.setName(idemixMSPConfig.getName());
            }
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        return groupOrg;
    }

}
