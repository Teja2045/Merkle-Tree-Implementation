package utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class DigestUtils {
    final MessageDigest digest;

    public DigestUtils(String algoType) throws NoSuchAlgorithmException {
        digest = MessageDigest.getInstance(algoType);
    }

    public String getHash(String data) {
        final byte[] hashbytes = digest.digest(
                data.getBytes(StandardCharsets.UTF_8));
        String sha3Hex = bytesToHex(hashbytes);
        return sha3Hex;
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1)
                hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public List<String> getHashList(List<String> transactions) {
        List<String> hashes = new ArrayList<>();
        for (String tx : transactions) {
            String hash = getHash(tx);
            hashes.add(hash);
        }
        return hashes;
    }
}
