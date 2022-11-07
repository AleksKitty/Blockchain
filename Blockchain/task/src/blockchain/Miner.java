package blockchain;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

public class Miner extends Thread {
    private final long id;
    private final Blockchain blockchain;

    public Miner(long id, Blockchain blockchain) {
        this.id = id;
        this.blockchain = blockchain;
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
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        String message = id + ": " + "A".repeat(Math.max(1, new Random().nextInt(5)));
        blockchain.getMessageList().add(message);
    }

    @Override
    public void run() {
        while (blockchain.getBlocks().size() < 5) {
            generateBlock();
        }
    }
}
