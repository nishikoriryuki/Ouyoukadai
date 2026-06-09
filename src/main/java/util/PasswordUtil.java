package util;

import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PasswordUtil {

    // ハッシュ生成時の繰り返し回数
    private static final int ITERATIONS = 10000;

    // ハッシュの長さ（bit）
    private static final int KEY_LENGTH = 256;

    /**
     * パスワードをハッシュ化する
     *
     * 保存形式：
     * salt:hash
     */
    public static String hashPassword(String password) {

        try {

            // ランダムなソルトを生成
            byte[] salt = new byte[16];
            new SecureRandom().nextBytes(salt);

            // パスワードとソルトからハッシュを生成
            byte[] hash = createHash(password, salt);

            // 「ソルト:ハッシュ」の形式で返却
            return Base64.getEncoder().encodeToString(salt)
                    + ":"
                    + Base64.getEncoder().encodeToString(hash);

        } catch (Exception e) {

            throw new RuntimeException(e);
        }
    }

    /**
     * 入力されたパスワードと
     * 保存済みパスワードを照合する
     */
    public static boolean checkPassword(
            String inputPassword,
            String storedPassword) {

        try {

            // 保存データをソルトとハッシュに分割
            String[] parts = storedPassword.split(":");

            byte[] salt =
                    Base64.getDecoder().decode(parts[0]);

            byte[] storedHash =
                    Base64.getDecoder().decode(parts[1]);

            // 入力パスワードを同じソルトでハッシュ化
            byte[] inputHash =
                    createHash(inputPassword, salt);

            // ハッシュ値を比較
            return java.security.MessageDigest.isEqual(
                    storedHash,
                    inputHash);

        } catch (Exception e) {

            return false;
        }
    }

    /**
     * パスワードとソルトから
     * PBKDF2方式でハッシュを生成する
     */
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

        // PBKDF2 + HmacSHA256 を利用
        SecretKeyFactory factory =
                SecretKeyFactory.getInstance(
                        "PBKDF2WithHmacSHA256");

        return factory.generateSecret(spec)
                      .getEncoded();
    }
}