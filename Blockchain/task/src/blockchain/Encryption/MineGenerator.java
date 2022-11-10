package blockchain.Encryption;

import java.security.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MineGenerator {

    private final KeyPairGenerator rsaGenerator;
    private PrivateKey privateKey;
    private PublicKey publicKey;

    private static final Map<Long, PrivateKey> privateKeysStorage = new ConcurrentHashMap<>();

    public MineGenerator() throws NoSuchAlgorithmException {
        rsaGenerator = KeyPairGenerator.getInstance("RSA");
        SecureRandom random = new SecureRandom();
        rsaGenerator.initialize(2048, random);
    }

    public void createKeys() {
        KeyPair pair = this.rsaGenerator.generateKeyPair();
        this.privateKey = pair.getPrivate();
        this.publicKey = pair.getPublic();
    }

    public PrivateKey getPrivateKey() {
        return this.privateKey;
    }

    public PublicKey getPublicKey() {
        return this.publicKey;
    }

    public static Map<Long, PrivateKey> getPrivateKeysStorage() {
        return privateKeysStorage;
    }

    public static PrivateKey generateKeys(Message message) throws GeneralSecurityException {
        // save private & public key
        MineGenerator mineGenerator = new MineGenerator();
        mineGenerator.createKeys();
        message.setPublicKey(mineGenerator.getPublicKey());

        return mineGenerator.getPrivateKey();
    }

    // to verify
     public static boolean verifySignature(byte[] data, byte[] signature, PublicKey publicKey) throws Exception {
        Signature sig = Signature.getInstance("SHA1withRSA");
        sig.initVerify(publicKey);
        sig.update(data);
        return sig.verify(signature);
    }
}
