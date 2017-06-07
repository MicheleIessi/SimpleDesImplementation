package DES.FullDES;

import DES.FullDES.DES;

/**
 * Created by Michele Iessi on 06/06/17.
 */
public class TripleDES {

    private DES des1;
    private DES des2;
    private DES des3;

    public TripleDES(String key1, String key2) {
        this.des1 = new DES(key1);
        this.des2 = new DES(key2);
    }

    public String encrypt(String plainText, String key1, String key2) throws NoSuchFieldException, IllegalAccessException {

        String encryptedK1 = des1.encrypt(plainText);
        String decryptedK2 = des2.decrypt(encryptedK1);
        String cipheredText = des1.encrypt(decryptedK2);

        return cipheredText;

    }

    public String decrypt(String cipheredText, String key1, String key2) throws NoSuchFieldException, IllegalAccessException {

        String decryptedK1 = des1.decrypt(cipheredText);
        String encryptedK2 = des2.encrypt(decryptedK1);
        String plainText = des1.decrypt(encryptedK2);

        return plainText;
    }
}
