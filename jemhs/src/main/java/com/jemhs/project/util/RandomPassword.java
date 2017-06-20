package com.jemhs.project.util;

import java.security.SecureRandom;

import org.apache.commons.lang3.RandomStringUtils;

public class RandomPassword {

	public static String generatePassword() {

		char[] possibleCharacters = (new String(
				"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!#$%&*()"))
						.toCharArray();
		String randomStr = RandomStringUtils.random(10, 0, possibleCharacters.length - 1, false, false,
				possibleCharacters, new SecureRandom());

		return randomStr;
	}
}
