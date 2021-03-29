package com.tdp.ms.autogestion.util;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.util.Base64Utils;

import com.tdp.ms.autogestion.exception.ErrorCategory;
import com.tdp.ms.autogestion.exception.GenericDomainException;

public class SecurityUtil {

	private static final String ALGORITHM = "AES";

	private static final byte[] keyValue = new byte[] { 'm', 'I', 'm', '0', 'v', '1', 's', 't', '4', 'r', 'C', 'o', 'n',
			'V', '3', 'r' };

	public static String decrypt(String encryptedValue) throws GenericDomainException {
		try {

			Key key = generateKey();
			Cipher c = Cipher.getInstance(ALGORITHM);
			c.init(Cipher.DECRYPT_MODE, key);
			byte[] decordedValue = Base64Utils.decodeFromString(encryptedValue);
			byte[] decValue = c.doFinal(decordedValue);
			return new String(decValue);			
		} catch (Exception e) {
			throw new GenericDomainException(ErrorCategory.UNEXPECTED, e.getLocalizedMessage());
		}
	}

	private static Key generateKey() throws GenericDomainException {
		return new SecretKeySpec(keyValue, ALGORITHM);		
	}
}
