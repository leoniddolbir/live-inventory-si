package com.smkw.commerce.util;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

/**
 * There is a single instance of the class <code>IdGenerator</code>, accessed
 * through the static method {@link #getCurrent()}.
 * <p>
 * CrossLogic Corp. 2002-2003
 * 
 * @pattern Singleton
 * 
 * @generatedBy CodePro Studio at 3/10/03 12:44 PM
 * 
 * @author ldolbir
 * 
 * @version $Revision: 1.2 $
 */
public class IdGenerator implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3821280802096531633L;
	/**
	 * The unique instance of this class.
	 */
	private static final IdGenerator current;
	private String valueBeforeMD5 = "";
	private String valueAfterMD5 = "";
	private static Random myRand;
	private static SecureRandom mySecureRand;
	private static String s_id;
	/*
	 * Static block to take care of one time secureRandom seed. It takes a few
	 * seconds to initialize SecureRandom. You might want to consider removing
	 * this static block or replacing it with a "time since first loaded" seed
	 * to reduce this time. This block will run only once per JVM instance.
	 */
	static {
		current = new IdGenerator();
		mySecureRand = new SecureRandom();
		mySecureRand.setSeed(System.currentTimeMillis());
		long secureInitializer = mySecureRand.nextLong();
		myRand = new Random(secureInitializer);
		try {
			s_id = InetAddress.getLocalHost().toString();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Prevent instances of this class from being created.
	 */
	private IdGenerator() {
		super();
	}

	/**
	 * Return the unique instance of this class.
	 * 
	 * @return the unique instance of this class
	 */
	public static IdGenerator getCurrent() {
		return current;
	}

	/**
	 * Resolve the singleton object.
	 * 
	 * @return the resolved singleton object
	 * 
	 * @throws ObjectStreamException
	 *             if the singleton could not be resolved.
	 */
	private Object readResolve() throws ObjectStreamException {
		return current;
	}

	/*
	 * Method to generate the random GUID
	 */
	private void getRandomGUID(boolean secure) {
		MessageDigest md5 = null;
		StringBuffer sbValueBeforeMD5 = new StringBuffer();
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace(System.out);
		}
		try {
			long time = System.currentTimeMillis();
			long rand = 0;
			if (secure) {
				rand = mySecureRand.nextLong();
			} else {
				rand = myRand.nextLong();
			}
			// This StringBuffer can be a long as you need; the MD5
			// hash will always return 128 bits. You can change
			// the seed to include anything you want here.
			// You could even stream a file through the MD5 making
			// the odds of guessing it at least as great as that
			// of guessing the contents of the file!
			sbValueBeforeMD5.append(s_id);
			sbValueBeforeMD5.append(":");
			sbValueBeforeMD5.append(Long.toString(time));
			sbValueBeforeMD5.append(":");
			sbValueBeforeMD5.append(Long.toString(rand));
			valueBeforeMD5 = sbValueBeforeMD5.toString();
			md5.update(valueBeforeMD5.getBytes());
			byte[] array = md5.digest();
			StringBuffer sb = new StringBuffer();
			for (int j = 0; j < array.length; ++j) {
				int b = array[j] & 0xFF;
				if (b < 0x10)
					sb.append('0');
				sb.append(Integer.toHexString(b));
			}
			valueAfterMD5 = sb.toString();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
	}

	/*
	 * Convert to the standard format for GUID (Useful for SQL Server
	 * UniqueIdentifiers, etc.) Example: C2FEEEAC-CFCD-11D1-8B05-00600806D9B6
	 */
	public String getRandomGUID() {
		String raw = null;
		synchronized (this) {
			this.getRandomGUID(true);
			raw = valueAfterMD5.toUpperCase();
		}
		StringBuffer sb = new StringBuffer();
		sb.append(raw.substring(0, 8));
		sb.append("-");
		sb.append(raw.substring(8, 12));
		sb.append("-");
		sb.append(raw.substring(12, 16));
		sb.append("-");
		sb.append(raw.substring(16, 20));
		sb.append("-");
		sb.append(raw.substring(20));
		return sb.toString();
	}

	/**
	 * Generate secure string of specified length using MD5
	 */
	public String getRandomString(int length) {
		String aString = null;
		synchronized (this) {
			this.getRandomGUID(true);
			aString = valueAfterMD5.toUpperCase().substring(0, length);
		}
		return aString;
	}

	/**
	 * Generate secure (if set to true) int value Avoid reserved values
	 * Integer.MIN_VALUE, Integer.MAX_VALUE, 0, -1
	 * 
	 * @param boolean secure
	 * @return int
	 */
	public int getRandomInt(boolean secure) {
		int rand = 0;
		if (secure) {
			rand = mySecureRand.nextInt();
		} else {
			rand = myRand.nextInt();
		}
		if ((rand == Integer.MIN_VALUE) || (rand == Integer.MAX_VALUE) || (rand == 0) || (rand == -1))
			rand = this.getRandomInt(secure);
		return rand;
	}

	/**
	 * Generate secure (if set to true) int value
	 * 
	 * @param boolean secure
	 * @return int
	 */
	public long getRandomLong(boolean secure) {
		long rand = 0;
		if (secure) {
			rand = mySecureRand.nextLong();
		} else {
			rand = myRand.nextLong();
		}
		return rand;
	}
}