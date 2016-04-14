package de.bht.ebus.spotsome.util;


public final class AccessTokenGenerator {
	
	private static TokenGenerator tokenGenerator = new SecureRandomTokenGenerator();
	
	private AccessTokenGenerator() { }
	
	public static String generateAccessToken() {
		// TODO: Maybe check if new generated token exists in table access_token
		return tokenGenerator.generateToken();
	}

}
