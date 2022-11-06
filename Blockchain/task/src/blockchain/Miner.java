package blockchain;

import java.util.List;

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
            if (blockchain.validateBlockChain(block)) { // also synchronised method

                blockchain.getBlocks().add(block);  // also synchronised method
                blockchain.correctZeroNumber(block); // also synchronised method
            }
        }
    }

    @Override
    public void run() {
        while (blockchain.getBlocks().size() < 5) {
            generateBlock();
        }
    }
}
