package blockchain;

import blockchain.Encryption.Message;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import static blockchain.Encryption.MineGenerator.verifySignature;
import static blockchain.Main.minerList;
import static blockchain.Main.minerListLocks;

public class Miner extends Thread {
    private final int id;
    private int coin = 100; // default value
    private final Blockchain blockchain;

    public Miner(int id, Blockchain blockchain) {
        this.id = id;
        this.blockchain = blockchain;
    }

    public int getCoin() {
        return coin;
    }

    public void setCoin(int coin) {
        this.coin = coin;
    }

    private void generateFirstBlock() {
        Block block = new Block(1, id, "0");
        block.generateHashAndMagicNumber(blockchain.getZeroesNumber());
        block.setN(1); // always less than minute
        addBlock(block);
    }

    void generateBlock() {
        List<Block> blocks = blockchain.getBlocks();
        if (blocks.size() == 0) {
            generateFirstBlock();
            return;
        }
        sendMessage(); // not for the first block

        Block previousBlock = blockchain.getBlocks().get(blocks.size() - 1);
        // current new block
        Block block = new Block(previousBlock.getId() + 1, id, previousBlock.getHash());

        block.generateHashAndMagicNumber(blockchain.getZeroesNumber());
        addBlock(block);
    }

    private void addBlock(Block block) {
        // check integrity and add
        // otherwise skip
        synchronized (blockchain) {
            if (blockchain.validateBlockChain(block)) {

                int minerId = block.getMinerId();
                Miner miner = minerList.get(minerId);

                // give reward
                synchronized (minerListLocks.get(this)) { // (blocked Petya)
                    miner.coin += 100;
                }

                // set messages to a block
                block.setMessagesData(blockchain.getMessageList());
                blockchain.getBlocks().add(block);
                blockchain.correctZeroNumber(block);

                // empty message list
                blockchain.setMessageList(new CopyOnWriteArrayList<>());
            }
        }
    }

    private void sendMessage() {
        try {
            Thread.sleep(700);

            int threadId = new Random().nextInt(minerList.size()); // receiver
            int money = new Random().nextInt(this.getCoin()) + 1;

            String messageData = makeStringMessage(threadId, money);
            Message message = new Message(messageData);

            if (threadId != id || money <= this.coin) { // we want to send to another thread
                // new message is bigger than the last
                // id of message is bigger than id of block
                // only if message is valid
                Message lastMessage = null;
                if (blockchain.getMessageList() != null && blockchain.getMessageList().size() > 0) {
                    lastMessage = blockchain.getMessageList().get(blockchain.getMessageList().size() - 1);
                }

                if ((lastMessage == null || message.getId() > lastMessage.getId())
                        && message.getId() > blockchain.getBlocks().get(blockchain.getBlocks().size() - 1).getId() &&
                        verifySignature(message.getMessageData(), message.getSignature(), message.getPublicKey())) {

                    if (sendMoney(threadId, money)) { // we had that amount
                        blockchain.getMessageList().add(message);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private boolean sendMoney(int threadId, int money) {
        boolean result = false;
        if (threadId != id) {
            Miner miner = minerList.get(threadId); // receiver (- 1 because list start with 0)

            synchronized (minerListLocks.get(miner)) { // only we can send money to miner (blocked Vasya)
                synchronized (minerListLocks.get(this)) { // (blocked Petya)
                    if (money <= this.coin) {
                        this.setCoin(this.coin - money);
                        miner.setCoin(miner.getCoin() + money);
                        result = true;
                    }
                }
            }
        }

        return result;
    }

    private String makeStringMessage(int minerId, int coins) {
        return "miner" + id + " sent " + coins + " VC to " + "miner" + minerId;
    }

    @Override
    public void run() {
        while (blockchain.getBlocks().size() < 15) {
            generateBlock();
        }
    }
}
