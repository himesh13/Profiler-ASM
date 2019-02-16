package com.himesh.agent.servprof.inst;

import java.util.regex.Pattern;

// Filter to determine wether a particular class and method should be modified.
public class InstrumentationFilter {
	private final Pattern classPattern;
	private final Pattern methodPattern;

	//Set Regexes for class and method patterns.
	public InstrumentationFilter(String classPattern, String methodPattern) {
		this.classPattern = Pattern.compile(classPattern);
		this.methodPattern = Pattern.compile(methodPattern);
	}

	public boolean shouldInstrumentClass(String className) {
		return classPattern.matcher(className).matches();

	}

	public boolean shouldInstrumentMethod(String methodName) {
		return methodPattern.matcher(methodName).matches();
	}

}
