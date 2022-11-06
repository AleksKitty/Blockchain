package blockchain;

import java.util.Date;

public class Block {

    /**
     * used in hash
     * */
    private final long id;

    private final long minerId;

    /**
     * used in hash
     * */
    private final long timeStamp;

    /**
     * used in hash
     * */
    private final String previousHash;

    /**
     * used in hash
     * */
    private long magicNumber;

    /**
     * generated hash
     * */
    private String hash;

    private long generatedSeconds;

    /**
     * number of zeroes for next blockchain
     * */
    private int N;

    public Block(long id, long minerId, String previousHash) {
        this.id = id;
        this.minerId = minerId;
        this.timeStamp = new Date().getTime();
        this.previousHash = previousHash;
    }

    void generateHashAndMagicNumber(int zeroesNumber) {
        StringBuilder prefix = new StringBuilder();
        prefix.append("0".repeat(Math.max(0, zeroesNumber)));

        long startTime = System.currentTimeMillis();
        String hash;
        long magicNumber = 0;
        do {
            hash = StringUtil.applySha256(id + timeStamp + previousHash + magicNumber);
            magicNumber++;
        } while (!hash.startsWith(String.valueOf(prefix)));
        long endTime = System.currentTimeMillis();

        this.magicNumber = magicNumber;
        this.hash = hash;
        this.generatedSeconds = (endTime - startTime) / 1000;
    }

    public synchronized long getId() {
        return id;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public String getHash() {
        return hash;
    }

    public long getGeneratedSeconds() {
        return generatedSeconds;
    }

    public void setN(int n) {
        N = n;
    }

    @Override
    public String toString() {
        return "Block:" + '\n' +
                "Created by miner # " + minerId + '\n' +
                "Id: " + id + '\n' +
                "Timestamp: " + timeStamp + '\n' +
                "Magic number: " + magicNumber + '\n' +
                "Hash of the previous block: " + '\n' + previousHash + '\n' +
                "Hash of the block: " + '\n' + hash + '\n' +
                "Block was generating for " + generatedSeconds + " seconds\n" +
                "N was increased to " + N + "\n";
    }
}
