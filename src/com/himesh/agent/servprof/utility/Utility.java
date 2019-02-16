package com.himesh.agent.servprof.utility;

public class Utility {

	//Sample Output Directory. Here is where all the traces will be put.
	private static String outputDirectory = "D:\\\\ProfilerOutput";

	public void printInfo(String arg) {
		System.out.println("[Profiler]" + arg);
	}

	public void printErr(String arg) {
		System.err.println("[Profiler][ERROR]" + arg);
	}

	public static void setOutputDirectory(String directory) {

		outputDirectory = directory;
	}

	public static String getOutputDirectory() {
		if (outputDirectory.equals("")) {
			outputDirectory = System.getProperty("user.dir");
		}
		return outputDirectory;
	}

}
