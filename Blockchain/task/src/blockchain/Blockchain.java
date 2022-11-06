package blockchain;

import java.util.*;

public class Blockchain {

    private final List<Block> blocks;

    private int zeroesNumber;

    public Blockchain() {
        this.blocks = Collections.synchronizedList(new LinkedList<>());
        zeroesNumber = 0;
    }

    public synchronized List<Block> getBlocks() {
        return blocks;
    }

    public int getZeroesNumber() {
        return zeroesNumber;
    }

    synchronized boolean validateBlockChain(Block block) {
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

     synchronized void correctZeroNumber(Block block) {
        if (block.getGeneratedSeconds() < 60) {
            zeroesNumber = zeroesNumber + 1;
        } else {
            zeroesNumber = zeroesNumber - 1;
        }

        block.setN(zeroesNumber);
    }

    void printBlockchain() {
        int i = 0;
        Iterator<Block> it = blocks.iterator();
        while (it.hasNext() && i < 5) {
            System.out.println(it.next());
            i++;
        }
    }
}
