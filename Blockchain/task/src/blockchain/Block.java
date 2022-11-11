package blockchain;

import blockchain.Encryption.Message;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Block {

    /**
     * used in hash
     * */
    private final long id;

    private final int minerId;

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

    private long generatedMilliSeconds;

    /**
     * number of zeroes for next blockchain
     * */
    private int N;

    private List<String> messagesData;

    public Block(long id, int minerId, String previousHash) {
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
        this.generatedMilliSeconds = endTime - startTime;
    }

    public long getId() {
        return id;
    }

    public int getMinerId() {
        return minerId;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public String getHash() {
        return hash;
    }

    public long getGeneratedMilliSeconds() {
        return generatedMilliSeconds;
    }

    public void setN(int n) {
        N = n;
    }

    public String printMessagesData() {
        if (messagesData.size() == 0) {
            return "\nNo transactions\n";
        }

        StringBuilder stringBuilder = new StringBuilder().append('\n');

        for (String message: messagesData) {
            stringBuilder.append(message).append('\n');
        }

        return String.valueOf(stringBuilder);
    }

    public void setMessagesData(List<Message> messages) {

        List<String> messagesData = new ArrayList<>();
        for (Message message: messages) {
            messagesData.add(new String(message.getMessageData()));
        }
        this.messagesData = messagesData;
    }

    @Override
    public String toString() {
        return "Block:" + '\n' +
                "Created by: miner" + minerId + '\n' +
                "miner" + minerId + " gets 100 VC" + '\n' +
                "Id: " + id + '\n' +
                "Timestamp: " + timeStamp + '\n' +
                "Magic number: " + magicNumber + '\n' +
                "Hash of the previous block: " + '\n' + previousHash + '\n' +
                "Hash of the block: " + '\n' + hash + '\n' +
                "Block data:" + printMessagesData() +
                "Block was generating for " + generatedMilliSeconds / 1000 + " seconds\n" +
                "N was increased to " + N + "\n";
    }
}
