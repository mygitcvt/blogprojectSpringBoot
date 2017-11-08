package com.blog.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BlogValidator {

	private static final Logger logger = LoggerFactory.getLogger(BlogValidator.class);
	private static Pattern pattern;
	private static Matcher matcher;

	private static final String TITLE_PATTERN = "[a-zA-z]+([ '_-][a-zA-Z]+)*";

	public static boolean validateTitlePattern(final String title) {
		logger.info("Validating title.");

		pattern = Pattern.compile(TITLE_PATTERN);
		matcher = pattern.matcher(title);

		boolean result = matcher.matches();
		logger.info("title validated : {}", result);
		return result;

	}

	private static final String POST_CONTENT_PATTERN = "[a-zA-z]+([ '-][a-zA-Z.]+)*";

	public static boolean validatePostContentPattern(final String postContent) {
		logger.info("Validating postcontent");

		pattern = Pattern.compile(POST_CONTENT_PATTERN);
		matcher = pattern.matcher(postContent);

		boolean result = matcher.matches();
		logger.info("postContent validated : {}", result);
		return result;

	}

}