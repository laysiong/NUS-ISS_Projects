package com.example.demo.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

// both the front-end and back-end use the same AES encryption method 
// To enhance security, salt can be used (not implemented) 
// in both the front-end and back-end to obfuscate password hashes.
public class PasswordUtil {
	
	private static final String ALGORITHM = "AES";
	private static final byte[] KEY = "20240202team7666".getBytes(StandardCharsets.UTF_8); // 16 byte length key

    public static String encodePassword(String rawPassword) throws Exception {
    	 try {
             SecretKeySpec secretKey = new SecretKeySpec(KEY, ALGORITHM);
             Cipher cipher = Cipher.getInstance(ALGORITHM);
             cipher.init(Cipher.ENCRYPT_MODE, secretKey);
             byte[] encrypted = cipher.doFinal(rawPassword.getBytes());
             return Base64.getEncoder().encodeToString(encrypted);
         } catch (Exception e) {
             throw new RuntimeException("Error while encrypting password", e);
         }
    }

    public static String decryptPassword(String encodedPassword) throws Exception {
    	try {
            SecretKeySpec secretKey = new SecretKeySpec(KEY, ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decoded = Base64.getDecoder().decode(encodedPassword);
            byte[] decrypted = cipher.doFinal(decoded);
            return new String(decrypted);
        } catch (Exception e) {
            throw new RuntimeException("Error while decrypting password", e);
        }
    }
    
}


