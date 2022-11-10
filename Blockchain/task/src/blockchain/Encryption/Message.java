package blockchain.Encryption;

import java.security.*;

public class Message {
    private static int idCounter = 1;

    private final long id;
    private final byte[] messageData;

    private final byte[] signature;

    private PublicKey publicKey;

    public Message(String data) throws Exception {
        id = idCounter++;

        PrivateKey privateKey = MineGenerator.generateKeys(this); // first we need keys
        MineGenerator.getPrivateKeysStorage().put(id, privateKey);
        messageData = data.getBytes();
        signature = sign(data, privateKey);
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public long getId() {
        return id;
    }

    public byte[] getMessageData() {
        return messageData;
    }

    public byte[] getSignature() {
        return signature;
    }

    // The method that signs the data using the private key
    public byte[] sign(String data, PrivateKey privateKey) throws Exception{
        Signature signature = Signature.getInstance("SHA1withRSA");
        signature.initSign(privateKey);
        signature.update(data.getBytes());
        return signature.sign();
    }
}
