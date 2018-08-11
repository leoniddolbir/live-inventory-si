package com.smkw.commerce.util;

import org.apache.commons.lang.exception.ExceptionUtils;

public class ExceptionReporter {
	public static Throwable getRootCause(Throwable anEx) {
		return ExceptionUtils.getRootCause(anEx);
	}

	public static StringBuffer report(Throwable anEx, StringBuffer aBuff) {

		return aBuff.append(ExceptionUtils.getRootCauseMessage(anEx));
	}
}
