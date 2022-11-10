package blockchain;

import blockchain.Encryption.Message;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import static blockchain.Encryption.MineGenerator.verifySignature;

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

                // verify messages

                // set messages to a block
                block.setMessagesData(blockchain.getMessageList());
                blockchain.getBlocks().add(block);
                blockchain.correctZeroNumber(block);

                // empty message list
                blockchain.setMessageList(new CopyOnWriteArrayList<>());
            }
        }
    }

    private void sendMessage(){
        try {
            Thread.sleep(500);
            String messageData = id + ": " + "A".repeat(Math.max(1, new Random().nextInt(5)));
            Message message = new Message(messageData);

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
                blockchain.getMessageList().add(message);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        while (blockchain.getBlocks().size() < 5) {
            generateBlock();
        }
    }
}
