package com.group;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.group.model.BlockSummary;
import org.hyperledger.fabric.gateway.Contract;
import org.hyperledger.fabric.gateway.Gateway;
import org.hyperledger.fabric.gateway.Network;
import org.hyperledger.fabric.gateway.Wallet;
import org.hyperledger.fabric.gateway.Wallets;
import org.hyperledger.fabric.protos.common.Common;
import org.hyperledger.fabric.sdk.BlockInfo;
import org.hyperledger.fabric.sdk.Channel;

public class App {

    static {
        System.setProperty("org.hyperledger.fabric.sdk.service_discovery.as_localhost", "true");
    }

    // helper function for getting connected to the gateway
    public static Gateway connect() throws Exception {
        // Load a file system based wallet for managing identities.
        Path walletPath = Paths.get("wallet");
        Wallet wallet = Wallets.newFileSystemWallet(walletPath);
        // load a CCP
        // Path networkConfigPath = Paths.get("..", "..", "test-network", "organizations", "peerOrganizations", "org1.example.com", "connection-org1.yaml");
        Path networkConfigPath = Paths.get("/home/shihaoming/go/src/github.com/shm0214/fabric-samples/test-network/organizations/peerOrganizations/org1.example.com/connection-org1.yaml");

        Gateway.Builder builder = Gateway.createBuilder();
        builder.identity(wallet, "appUser").networkConfig(networkConfigPath).discovery(true);
        return builder.connect();
    }

    public static void main(String[] args) throws Exception {
        // enrolls the admin and registers the user
        try {
            EnrollAdmin.main(null);
            RegisterUser.main(null);
        } catch (Exception e) {
            System.err.println(e);
        }

        // connect to the network and invoke the smart contract
        try (Gateway gateway = connect()) {

            // get the network and contract
            Network network = gateway.getNetwork("mychannel");
            Contract contract = network.getContract("DigitalCoin");
            Channel channel = network.getChannel();
            int blockNumber = 0;
            BlockInfo blockInfo = channel.queryBlockByNumber(blockNumber);
            Common.Block block = blockInfo.getBlock();

            BlockSummary summary = new ParseBlock(block).getSummary();

            System.out.println(1);

//            Consumer<BlockEvent> consumer = network.addBlockListener(new Consumer<BlockEvent>() {
//                @Override
//                public void accept(BlockEvent blockEvent) {
//                    System.out.println(blockEvent.getBlockNumber());
//                    System.out.println(blockEvent.getTransActionsMetaData());
//                    System.out.println(blockEvent.getTransactionCount());
//                }
//            });


//            byte[] result;
//
//            System.out.println("Submit Transaction: InitLedger, function creates the initial set of assets on the ledger.");
//            contract.submitTransaction("InitLedger");
//
//            System.out.println("\n");
//            result = contract.evaluateTransaction("GetAllUsers");
//            System.out.println("Evaluate Transaction: GetAllUsers, result: " + new String(result));
//
//            System.out.println("\n");
//            result = contract.evaluateTransaction("GetAllCoins");
//            System.out.println("Evaluate Transaction: GetAllCoins, result: " + new String(result));
//
//            System.out.println("\n");
//            System.out.println("Submit Transaction: CreateCoin c0x5");
//            contract.submitTransaction("CreateCoin", "c0x5", "50", "50", "p0x101", "p0x000");
//
//            System.out.println("\n");
//            System.out.println("Evaluate Transaction: ReadCoin c0x5");
//            result = contract.evaluateTransaction("ReadCoin", "c0x5");
//            System.out.println("result: " + new String(result));
//
//            System.out.println("\n");
//            System.out.println("Evaluate Transaction: ReadUser p0x101");
//            result = contract.evaluateTransaction("ReadUser", "p0x101");
//            System.out.println("result: " + new String(result));
//
//            System.out.println("\n");
//            System.out.println("Evaluate Transaction: UserExists p0x106");
//            result = contract.evaluateTransaction("UserExists", "p0x106");
//            System.out.println("result: " + new String(result));
//
//            System.out.println("\n");
//            System.out.println("Evaluate Transaction: CoinExists c0x5");
//            result = contract.evaluateTransaction("CoinExists", "c0x5");
//            System.out.println("result: " + new String(result));
//
//            System.out.println("\n");
//            System.out.println("Submit Transaction: UpdateCoin c0x5, decrease the value to 40");
//            contract.submitTransaction("UpdateCoin", "c0x5", "40");
//
//            System.out.println("\n");
//            System.out.println("Evaluate Transaction: ReadCoin c0x5");
//            result = contract.evaluateTransaction("ReadCoin", "c0x5");
//            System.out.println("result: " + new String(result));
//
//            System.out.println("\n");
//            System.out.println("Evaluate Transaction: ReadUser p0x101");
//            result = contract.evaluateTransaction("ReadUser", "p0x101");
//            System.out.println("result: " + new String(result));
//
//            System.out.println("\n");
//            System.out.println("Submit Transaction: TransferCoin c0x5, 20 value to c0x6");
//            contract.submitTransaction("TransferCoin", "p0x101", "p0x103", "c0x5", "c0x6", "20");
//
//
//            System.out.println("\n");
//            System.out.println("Evaluate Transaction: ReadCoin c0x5");
//            result = contract.evaluateTransaction("ReadCoin", "c0x5");
//            System.out.println("result: " + new String(result));
//
//            System.out.println("\n");
//            System.out.println("Evaluate Transaction: ReadCoin c0x6");
//            result = contract.evaluateTransaction("ReadCoin", "c0x6");
//            System.out.println("result: " + new String(result));
//
//            System.out.println("\n");
//            System.out.println("Evaluate Transaction: ReadUser p0x101");
//            result = contract.evaluateTransaction("ReadUser", "p0x101");
//            System.out.println("result: " + new String(result));
//
//            System.out.println("\n");
//            System.out.println("Evaluate Transaction: ReadUser p0x103");
//            result = contract.evaluateTransaction("ReadUser", "p0x103");
//            System.out.println("result: " + new String(result));
        } catch (Exception e) {
            System.err.println(e);
        }

    }
}

