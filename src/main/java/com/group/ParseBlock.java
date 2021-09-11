package com.group;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.group.model.ASN1Header;
import com.group.model.BlockSummary;
import com.group.model.TransactionSummary;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.BEROctetString;
import org.hyperledger.fabric.protos.common.Common;
import org.hyperledger.fabric.protos.common.Configtx;
import org.hyperledger.fabric.protos.peer.TransactionPackage;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;

public class ParseBlock {
    private Common.Block block;
    private long blockNumber;
    private byte[] previousHash, dataHash;

    public ParseBlock(Common.Block block) {
        this.block = block;
    }

    public long getBlockNum() {
        blockNumber = block.getHeader().getNumber();
        return blockNumber;
    }

    public String getBlockHash() {
        getPreviousHash();
        getDataHash();
        try {
            ASN1Header header =
                    new ASN1Header(
                            new ASN1Integer(blockNumber), new BEROctetString(previousHash), new BEROctetString(dataHash));
            byte[] a = header.getEncoded();
            MessageDigest messageDigest;
            try {
                messageDigest = MessageDigest.getInstance("SHA-256");
                messageDigest.update(a);
                byte[] b = messageDigest.digest();
                return Base64.getEncoder().encodeToString(b);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getDataHash() {
        dataHash = block.getHeader().getDataHash().toByteArray();
        return Base64.getEncoder().encodeToString(dataHash);
    }

    public String getPreviousHash() {
        previousHash = block.getHeader().getPreviousHash().toByteArray();
        return Base64.getEncoder().encodeToString(previousHash);
    }

    public String getCommitHash() {
        if (block.getMetadata().getMetadataCount() > Common.BlockMetadataIndex.COMMIT_HASH_VALUE) {
            ByteString byteString = block.getMetadata().getMetadata(Common.BlockMetadataIndex.COMMIT_HASH_VALUE);
            Common.BlockMetadata metadata = null;
            try {
                metadata = Common.BlockMetadata.parseFrom(byteString);
            } catch (InvalidProtocolBufferException e) {
                e.printStackTrace();
            }
            return Base64.getEncoder().encodeToString(metadata.getMetadata(0).toByteArray());
        } else {
            return null;
        }
    }

    public long getMetaDataLastConfig() {
        try {
            if (block.getMetadata().getMetadataCount() > Common.BlockMetadataIndex.LAST_CONFIG_VALUE) {
                ByteString byteString = block.getMetadata().getMetadata(Common.BlockMetadataIndex.LAST_CONFIG_VALUE);
                Common.Metadata metadata = Common.Metadata.parseFrom(byteString);
                ByteString index = metadata.getValue();
                Common.LastConfig lastConfig = Common.LastConfig.parseFrom(index);
                return lastConfig.getIndex();
            } else {
                return -1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public Configtx.Config getConfig() {
        try {
            if (block.getData().getDataCount() == 1) {
                ByteString byteString = block.getData().getData(0);
                Common.Envelope envelope = Common.Envelope.parseFrom(byteString);
                Common.Payload payload = Common.Payload.parseFrom(envelope.getPayload());
                Configtx.ConfigEnvelope configEnvelope = Configtx.ConfigEnvelope.parseFrom(payload.getData());
                return configEnvelope.getConfig();
            } else {
                System.out.println("no metadata for last config");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getChannel() {
        if (block.getData().getDataCount() < 1) {
            return "invalid block";
        }
        try {
            Common.Envelope envelope = Common.Envelope.parseFrom(block.getData().getData(0));
            Common.Payload payload = Common.Payload.parseFrom(envelope.getPayload());
            Common.ChannelHeader channelHeader = Common.ChannelHeader.parseFrom(payload.getHeader().getChannelHeader());
            return channelHeader.getChannelId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Boolean> getMetaDataTransFilter() {
        ArrayList<Boolean> txFilters = new ArrayList<Boolean>();
        if (block.getMetadata().getMetadataCount() > Common.BlockMetadataIndex.TRANSACTIONS_FILTER_VALUE) {
            ByteString filter = block.getMetadata().getMetadata(Common.BlockMetadataIndex.TRANSACTIONS_FILTER_VALUE);
            for (Byte f : filter) {
                boolean tf = false;
                if (f == TransactionPackage.TxValidationCode.VALID_VALUE) {
                    tf = true;
                }
                txFilters.add(tf);
            }
            return txFilters;
        }
        return null;
    }

    public ArrayList<TransactionPackage.TxValidationCode> getMetaDataTransValidationCode() {
        ArrayList<TransactionPackage.TxValidationCode> txFilters = new ArrayList<>();
        if (block.getMetadata().getMetadataCount() > Common.BlockMetadataIndex.TRANSACTIONS_FILTER_VALUE) {
            ByteString filter = block.getMetadata().getMetadata(Common.BlockMetadataIndex.TRANSACTIONS_FILTER_VALUE);
            for (Byte f : filter) {
                txFilters.add(TransactionPackage.TxValidationCode.values()[f]);
            }
            return txFilters;
        }
        return null;
    }

    public ArrayList<Common.Envelope> getTransactions() {
        ArrayList<Common.Envelope> envelopes = new ArrayList<>();
        for (ByteString data : block.getData().getDataList()) {
            try {
                Common.Envelope envelope = Common.Envelope.parseFrom(data);
                envelopes.add(envelope);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return envelopes;
    }

    public BlockSummary getSummary() {
        BlockSummary summary = new BlockSummary();
        summary.setBlockNum(getBlockNum());
        summary.setChannel(getChannel());
        summary.setHash(getBlockHash());
        summary.setPreHash(getPreviousHash());
        summary.setLastConfig(getMetaDataLastConfig());
        Configtx.Config config = getConfig();
        if (config == null) {
            summary.setType(0);
            ArrayList<Boolean> filters = getMetaDataTransFilter();
            ArrayList<TransactionPackage.TxValidationCode> codes = getMetaDataTransValidationCode();
            ArrayList<Common.Envelope> transactions = getTransactions();
            for (int i = 0; i < transactions.size(); i++) {
                Common.Envelope transaction = transactions.get(i);
                ParseTransaction parseTransaction = new ParseTransaction(transaction);
                TransactionSummary transactionSummary = parseTransaction.getSummary();
                if (filters.size() > i) {
                    transactionSummary.setFilter(filters.get(i));
                }
                if (codes.size() > i) {
                    transactionSummary.setValidationCode(codes.get(i).name());
                }
                summary.getTransactions().add(transactionSummary);
            }
            summary.setTransCount(filters.size());
            summary.setCommitHash(getCommitHash());
        } else {
            summary.setType(1);
            ParseConfig parseConfig = new ParseConfig(config);
            summary.setConfigSummary(parseConfig.getSummary());
        }
        return summary;
    }
}
