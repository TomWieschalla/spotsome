package de.bht.ebus.spotsome.util;

import java.math.BigInteger;
import java.security.SecureRandom;

public class SecureRandomTokenGenerator implements TokenGenerator {

	private final SecureRandom random;

	public SecureRandomTokenGenerator() {
		random = new SecureRandom();
	}
	
	@Override
	public String generateToken() {
		return new BigInteger(130, random).toString(32);
	}

}