package blockchain;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Blockchain blockchain = new Blockchain();

        List<Miner> minerList = new ArrayList<>();

        // start all threads
        for (int i = 0; i < 10; i++) {
            Miner miner = new Miner(i, blockchain);
            minerList.add(miner);
            miner.start();
        }

        // wait for all threads
        for (Miner miner: minerList) {
            miner.join();
        }

        // print result
        blockchain.printBlockchain();
    }
}
