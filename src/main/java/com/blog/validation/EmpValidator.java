package com.blog.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blog.model.Emp;

public class EmpValidator {
	
	private static final Logger logger = LoggerFactory.getLogger(EmpValidator.class);

	private static Pattern pattern;
	private static Matcher matcher;

	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,3})$";

	public static boolean validateEmail(final String hex) {
		logger.info("Validating email.");
		pattern = Pattern.compile(EMAIL_PATTERN);
		matcher = pattern.matcher(hex);
		//System.out.println(matcher.matches() + "\t" + "checking validate email method");
		boolean result = matcher.matches();
		
		logger.info("Email validated : {}", result);
		
		return result;
	}

	

	private static final String FIRST_NAME_PATTERN = "[a-zA-z]+([ _-][a-zA-Z]+)*";

	public static boolean validateFirstName(final String firstName) {
		//System.out.println("validate firstName");
		logger.info("Validating fIrstName.");
		pattern = Pattern.compile(FIRST_NAME_PATTERN);
		matcher = pattern.matcher(firstName);
		boolean result = matcher.matches();
		logger.info("First_name validated : {}", result);
		return result;

	}
	
	private static final String LAST_NAME_PATTERN="[a-zA-z]+([ _-][a-zA-Z]+)*";
	public static boolean validateLastName(final String LastName) {
		//System.out.println("valiadte LastName");
		logger.info("Validating LastName.");
		pattern = Pattern.compile(LAST_NAME_PATTERN);
		matcher = pattern.matcher(LastName);
		boolean result = matcher.matches();
		logger.info("Last_name validated : {}", result);
		return result;

		
		
		
	}
	
	
	
	
	

	private static final String USERNAME_PATTERN = "^[a-zA-Z0-9_.-]{3,}$";

	public static boolean validateUsername(final String userName) {
		logger.info("Validating userName.");
		//System.out.println("validate userName");
		pattern = Pattern.compile(USERNAME_PATTERN);
		matcher = pattern.matcher(userName);
		boolean result = matcher.matches();
		logger.info("userName validated : {}", result);
		return result;


	}

	private static final String PASSWORD_PATTERN = "^.{8,15}$";

	public static boolean validatePassword(final String password) {
		//System.out.println("validate password");
		logger.info("Validating pssword.");
		pattern = Pattern.compile(PASSWORD_PATTERN);
		matcher = pattern.matcher(password);
		boolean result =  matcher.matches();
		logger.info("password validated: {}", result);
		return result;


	}

}