package DES;

/**
 * Created by Michele Iessi on 06/06/17.
 */
public class TripleDES {

    private DES des;

    public TripleDES() {
        this.des = new DES();
    }

    public String encrypt(String plainText, String key1, String key2) throws NoSuchFieldException, IllegalAccessException {

        String encryptedK1 = des.encrypt(plainText, key1);
        String decryptedK2 = des.decrypt(encryptedK1, key2);
        String cipheredText = des.encrypt(decryptedK2, key1);

        return cipheredText;

    }

    public String decrypt(String cipheredText, String key1, String key2) throws NoSuchFieldException, IllegalAccessException {

        String decryptedK1 = des.decrypt(cipheredText, key1);
        String encryptedK2 = des.encrypt(decryptedK1, key2);
        String plainText = des.decrypt(encryptedK2, key1);

        return plainText;
    }
}
