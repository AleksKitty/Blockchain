package blockchain;

import blockchain.Encryption.Message;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class Blockchain {

    private final CopyOnWriteArrayList<Block> blocks;

    private int zeroesNumber;

    private CopyOnWriteArrayList<Message> messageList = new CopyOnWriteArrayList<>();

    public Blockchain() {
        this.blocks = new CopyOnWriteArrayList<>();
        zeroesNumber = 0;
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    public int getZeroesNumber() {
        return zeroesNumber;
    }

    boolean validateBlockChain(Block block) {
        if (blocks.size() == 0) {
            return true;
        }

        // already created
        Block lastBlock = blocks.get(blocks.size() - 1);
        if (lastBlock.getId() != block.getId() - 1) {
            return false;
        }

        return Objects.equals(lastBlock.getHash(), block.getPreviousHash())
                && block.getHash().startsWith(("0".repeat(Math.max(0, zeroesNumber))));
    }

    void correctZeroNumber(Block block) {
        if (block.getGeneratedMilliSeconds() < 50) {
            zeroesNumber += 1;
        } else if (zeroesNumber > 0) {
            zeroesNumber -= 1;
        }
        block.setN(zeroesNumber);
    }

    public CopyOnWriteArrayList<Message> getMessageList() {
        return messageList;
    }

    public void setMessageList(CopyOnWriteArrayList<Message> messageList) {
        this.messageList = messageList;
    }

    void printBlockchain() {
        int i = 0;
        Iterator<Block> it = blocks.iterator();
        while (it.hasNext() && i < 15) {
            System.out.println(it.next());
            i++;
        }
    }
}
