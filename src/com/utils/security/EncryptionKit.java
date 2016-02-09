package com.utils.security;

import java.security.MessageDigest;
import java.security.SecureRandom;

public class EncryptionKit {
	private static SecureRandom random = new SecureRandom();
	public static String md5Encrypt(String srcStr){
		return encrypt("MD5", srcStr);
	}
	
	public static String sha1Encrypt(String srcStr){
		return encrypt("SHA-1", srcStr);
	}
	
	public static String sha256Encrypt(String srcStr){
		return encrypt("SHA-256", srcStr);
	}
	
	public static String sha384Encrypt(String srcStr){
		return encrypt("SHA-384", srcStr);
	}
	
	public static String sha512Encrypt(String srcStr){
		return encrypt("SHA-512", srcStr);
	}
	
	public static String encrypt(String algorithm, String srcStr) {
		try {
			
			MessageDigest md = MessageDigest.getInstance(algorithm);
			byte[] bytes = md.digest(srcStr.getBytes("utf-8"));
			return encodeHex(bytes);
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static String encodeHex(byte[] bytes){
		StringBuilder result = new StringBuilder();
		for (byte b :bytes) {
			String hex = Integer.toHexString(b&0xFF);
			if (hex.length() == 1)
				result.append("0");
			result.append(hex);
		}
		return result.toString();
	}

    public static byte[] decodeHex(String s){
         return decode(s.toCharArray());
    }

	public static String passWordencrypt(String srcStr, byte[] salt) {
		try {
			
			MessageDigest digest = MessageDigest.getInstance("SHA-1");

			if (salt != null) {
				digest.update(salt);
			}

			byte[] result = digest.digest(srcStr.getBytes());

			for (int i = 1; i < 1024; i++) {
				digest.reset();
				result = digest.digest(result);
			}
			return encodeHex(result);
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

    /**
     * 生成随机的Byte[]作为salt.
     * @return
     */
	public static byte[] generateSalt() {
		byte[] bytes = new byte[8];
		random.nextBytes(bytes);
		return bytes;
	}
	
	public static String generateSaltStr() {	
		return encodeHex(generateSalt());
	}

    /**
     * Converts an array of characters representing hexidecimal values into an
     * array of bytes of those same values. The returned array will be half the
     * length of the passed array, as it takes two characters to represent any
     * given byte. An exception is thrown if the passed char array has an odd
     * number of elements.
     *
     * @param data An array of characters containing hexidecimal digits
     * @return A byte array containing binary data decoded from
     *         the supplied char array.
     * @throws IllegalArgumentException if an odd number or illegal of characters
     *                                  is supplied
     */
    private static byte[] decode(char[] data) throws IllegalArgumentException {

        int len = data.length;

        if ((len & 0x01) != 0) {
            throw new IllegalArgumentException("Odd number of characters.");
        }

        byte[] out = new byte[len >> 1];

        // two characters form the hex value.
        for (int i = 0, j = 0; j < len; i++) {
            int f = toDigit(data[j], j) << 4;
            j++;
            f = f | toDigit(data[j], j);
            j++;
            out[i] = (byte) (f & 0xFF);
        }

        return out;
    }

    /**
     * Converts a hexadecimal character to an integer.
     *
     * @param ch    A character to convert to an integer digit
     * @param index The index of the character in the source
     * @return An integer
     * @throws IllegalArgumentException if ch is an illegal hex character
     */
    private static int toDigit(char ch, int index) throws IllegalArgumentException {
        int digit = Character.digit(ch, 16);
        if (digit == -1) {
            throw new IllegalArgumentException("Illegal hexadecimal charcter " + ch + " at index " + index);
        }
        return digit;
    }


}




