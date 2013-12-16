package mySTAT;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.Mac;
import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.security.InvalidKeyException;
import javax.crypto.BadPaddingException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.Charset;

/**
 *
 * @author Chris S.
 */
public class ProjectCrypto {
    private String passPhrase;
    final private byte[] AESKey = new byte[16];
    final private byte[] HMACKey = new byte[16];
    final private Charset UTF8Charset = Charset.forName("UTF-8");

    public boolean isPassphraseCorrect(String tagBase64Str, String compSHDataStr) {
        return getTag(compSHDataStr).equals(tagBase64Str);
    }
    
    public String getTag(String compSHDataStr) {
        final String tagBase64Str;
        final byte[] tagBytes;
        final byte[] cipherTextBytes;
        final String HMACType = "HmacSHA256";

        // TODO clean correct/cleanup try/catch
        try {
            SecretKeySpec SKSHMACKey = new SecretKeySpec(HMACKey, HMACType);

            Mac tag = Mac.getInstance(HMACType);
            tag.init(SKSHMACKey);

            cipherTextBytes = compSHDataStr.getBytes(UTF8Charset);
            tagBytes = tag.doFinal(cipherTextBytes);
            
            tagBase64Str = DatatypeConverter.printBase64Binary(tagBytes);
            
            return tagBase64Str;
        
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Error while creating tag: " + e);
            return null;
        } catch (InvalidKeyException e) {
            System.out.println("Bad pass phrase: " + e);
            return null;
        }
    }

    public void setPassphrase(String passPhraseStr) {
        passPhrase = passPhraseStr;
        final byte[] passPhraseBytes;
        final byte[] masterKey;
        
        // TODO clean correct/cleanup try/catch
        try {
            // use SHA-2 as weak KDF to generate 256 bits for AES/HMAC keys
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            passPhraseBytes = passPhrase.getBytes(UTF8Charset);
            masterKey = md.digest(passPhraseBytes);

            // store leftmost 128 bits as AES key
            System.arraycopy(masterKey, 0, AESKey, 0, 16);
            
            // store rightmost 128 bits as HMAC-SHA256 key
            System.arraycopy(masterKey, 16, HMACKey, 0, 16);
            
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Error while creating key: " + e);
        }
    }

    public String encrypt(String plainTextStr) {
        final byte[] cipherTextBytes;
        final Cipher cipher;
        final SecretKeySpec SKSAESKey;
        final int blockSize;
        final byte[] ivBytes;
        final SecureRandom random;
        final IvParameterSpec iv;
        final byte[] ivWithCipherTextBytes;
        final byte[] plainTextBytes = plainTextStr.getBytes(UTF8Charset);
        final String cipherTextBase64Str;

        // TODO clean correct/cleanup try/catch
        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SKSAESKey = new SecretKeySpec(AESKey, "AES");
            
            blockSize = cipher.getBlockSize();
            ivBytes = new byte[blockSize];
            
            random = SecureRandom.getInstance("SHA1PRNG");
            random.nextBytes(ivBytes);
            iv = new IvParameterSpec(ivBytes);
            
            cipher.init(Cipher.ENCRYPT_MODE, SKSAESKey, iv);
            
            cipherTextBytes = cipher.doFinal(plainTextBytes);
            ivWithCipherTextBytes = new byte[ivBytes.length + cipherTextBytes.length];
            
            System.arraycopy(ivBytes, 0, ivWithCipherTextBytes, 0, blockSize);
            System.arraycopy(cipherTextBytes,
                    0,
                    ivWithCipherTextBytes,
                    blockSize,
                    cipherTextBytes.length);
            
            cipherTextBase64Str = DatatypeConverter.printBase64Binary(ivWithCipherTextBytes);
            
            return cipherTextBase64Str;
            
        } catch (InvalidKeyException e) {
            System.out.println("Bad pass phrase: " + e);
            return null;
        } catch (GeneralSecurityException e) {
            System.out.println("Error while encrypting: " + e);
            return null;
        }
    }
    
    public String decrypt(String cipherTextBase64Str) {
        final Cipher cipher;
        final SecretKeySpec SKSAESKey;
        final int blockSize;
        final byte[] ivBytes;
        final byte[] ivWithCipherTextBytes;
        final IvParameterSpec iv;
        final byte[] cipherTextBytes;
        final String plainTextStr;
        
        // TODO clean correct/cleanup try/catch
        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SKSAESKey = new SecretKeySpec(AESKey, "AES");
            
            blockSize = cipher.getBlockSize();
            ivBytes = new byte[blockSize];
            
            ivWithCipherTextBytes = DatatypeConverter.parseBase64Binary(cipherTextBase64Str);
            
            System.arraycopy(ivWithCipherTextBytes, 0, ivBytes, 0, blockSize);
            iv = new IvParameterSpec(ivBytes);
            
            cipherTextBytes = new byte[ivWithCipherTextBytes.length - blockSize];
            
            System.arraycopy(ivWithCipherTextBytes,
                    blockSize,
                    cipherTextBytes,
                    0, 
                    (ivWithCipherTextBytes.length - blockSize));
            
            cipher.init(Cipher.DECRYPT_MODE, SKSAESKey, iv);

            plainTextStr = new String(cipher.doFinal(cipherTextBytes));
            
            return plainTextStr;
            
        } catch (InvalidKeyException e) {
            System.out.println("Bad pass phrase: " + e);
            return null;
        } catch (BadPaddingException e) {
            return null;
        } catch (GeneralSecurityException e) {
            System.out.println("Error while decrypting: " + e);
            return null;
        }
    }
}