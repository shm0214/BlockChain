package com.group.model;

import org.checkerframework.checker.units.qual.A;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class TransactionSummary implements Serializable {
    private String channel;
    private String txId;
    private Date time;
    private String chaincode;
    private String function;
    private ArrayList<String> args;
    public class Response{
        private long status;
        private String message;
        private String data;

        public Response() {
        }

        public long getStatus() {
            return status;
        }

        public void setStatus(long status) {
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }
    }
    private Response response;
    private TxRwSet txRwSet;
    private boolean filter;
    private String ValidationCode;
    private SignInfo signer;
    private ArrayList<SignInfo> endorsers;

    public TransactionSummary() {
        response = new Response();
        txRwSet = new TxRwSet();
        signer = new SignInfo();
        endorsers = new ArrayList<>();
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getTxId() {
        return txId;
    }

    public void setTxId(String txId) {
        this.txId = txId;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getChaincode() {
        return chaincode;
    }

    public void setChaincode(String chaincode) {
        this.chaincode = chaincode;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public ArrayList<String> getArgs() {
        return args;
    }

    public void setArgs(ArrayList<String> args) {
        this.args = args;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public TxRwSet getTxRwSet() {
        return txRwSet;
    }

    public void setTxRwSet(TxRwSet txRwSet) {
        this.txRwSet = txRwSet;
    }

    public boolean isFilter() {
        return filter;
    }

    public void setFilter(boolean filter) {
        this.filter = filter;
    }

    public String getValidationCode() {
        return ValidationCode;
    }

    public void setValidationCode(String validationCode) {
        ValidationCode = validationCode;
    }

    public SignInfo getSigner() {
        return signer;
    }

    public void setSigner(SignInfo signer) {
        this.signer = signer;
    }

    public ArrayList<SignInfo> getEndorsers() {
        return endorsers;
    }

    public void setEndorsers(ArrayList<SignInfo> endorsers) {
        this.endorsers = endorsers;
    }
}
