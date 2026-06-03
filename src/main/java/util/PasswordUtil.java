package util;

import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PasswordUtil {

    private static final int ITERATIONS = 10000;
    private static final int KEY_LENGTH = 256;

    public static String hashPassword(String password) {
        try {
            byte[] salt = new byte[16];
            new SecureRandom().nextBytes(salt);

            byte[] hash = createHash(password, salt);

            return Base64.getEncoder().encodeToString(salt)
                    + ":"
                    + Base64.getEncoder().encodeToString(hash);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean checkPassword(
            String inputPassword,
            String storedPassword) {

        try {
            String[] parts = storedPassword.split(":");

            byte[] salt =
                    Base64.getDecoder().decode(parts[0]);

            byte[] storedHash =
                    Base64.getDecoder().decode(parts[1]);

            byte[] inputHash =
                    createHash(inputPassword, salt);

            return java.security.MessageDigest.isEqual(
                    storedHash,
                    inputHash);

        } catch (Exception e) {
            return false;
        }
    }

    private static byte[] createHash(
            String password,
            byte[] salt)
            throws Exception {

        PBEKeySpec spec =
                new PBEKeySpec(
                        password.toCharArray(),
                        salt,
                        ITERATIONS,
                        KEY_LENGTH);

        SecretKeyFactory factory =
                SecretKeyFactory.getInstance(
                        "PBKDF2WithHmacSHA256");

        return factory.generateSecret(spec)
                .getEncoded();
    }
}