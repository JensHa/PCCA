package de.fhrt.pcca.sha1;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Converter {

	public static String bytetoSHA1String(byte[] convertme) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return byteToHexString(md.digest(convertme));
	}

	public static String byteToHexString(byte[] bytes) {
		char[] hexArray = "0123456789abcdef".toCharArray();
		char[] hexChars = new char[bytes.length * 2];
		for (int j = 0; j < bytes.length; j++) {
			int v = bytes[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}

	public static String hexStringToBinString(String s) {
		String result = new BigInteger(s, 16).toString(2);
		int nullToadd = 160 - result.length();
		for (int i = 0; i < nullToadd; i++) {
			result = "0" + result;
		}
		return result;
	}

	public static String intToBinString(int s) {
		String result = Integer.toBinaryString(s);
		int nullToadd = 32 - result.length();
		for (int i = 0; i < nullToadd; i++) {
			result = "0" + result;
		}
		return result;
	}

	public static BigInteger binStringToBigint(String bin) {
		return new BigInteger(bin, 2);
	}

	public static String getChildId(String parentInBin, int childNo) {
		String conc = parentInBin + intToBinString(childNo);
		return bytetoSHA1String(conc.getBytes());
	}

	public static int noOfChildren(String parentInBin, double prop, int noOfChildren) {
		BigInteger parentInBigint = binStringToBigint(parentInBin);
		BigInteger divident = parentInBigint.mod(BigInteger.valueOf(4294967296L));

		BigDecimal value = new BigDecimal(divident).divide(BigDecimal.valueOf(4294967295L), 6,
				BigDecimal.ROUND_HALF_UP);
		if (value.compareTo(BigDecimal.ZERO) == -1) {
			System.out.println("");
		}
		if (value.compareTo(new BigDecimal(prop)) == -1) {
			return noOfChildren;
		} else {
			return 0;
		}
	}
}
