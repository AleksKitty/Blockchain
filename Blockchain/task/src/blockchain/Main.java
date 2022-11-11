package blockchain;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class Main {
    static List<Miner> minerList = new CopyOnWriteArrayList<>();
    static Map<Miner, Object> minerListLocks = new ConcurrentHashMap<>();
    public static void main(String[] args) throws InterruptedException {
        Blockchain blockchain = new Blockchain();

        // start all threads
        for (int i = 0; i < 10; i++) {
            Miner miner = new Miner(i, blockchain);
            minerList.add(miner);
            minerListLocks.put(miner, new Object());
            miner.start();
        }

        // wait for all threads
        for (Miner miner: minerList) {
            miner.join();
        }

        // print result
        blockchain.printBlockchain();

        int sum = 0;
        for (Miner miner: minerList) {
            sum += miner.getCoin();
        }

        System.out.println("sum of coins " + sum);
    }
}
