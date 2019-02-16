package com.himesh.agent.servprof;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.himesh.agent.servprof.utility.Utility;

//Prints the passed information to the specified file.

// Known Issue - Might create problems if the traces for the same thread span across multiple files.
public class PrintTrace {
	private static final long MAX_FILE_SIZE = 1000000;
	private static File currentFile, outputFolder;

	public static void printToFile(String given) {

		try {

//			System.out.println("Call in print traces");
			outputFolder = new File(Utility.getOutputDirectory());

			if (!outputFolder.exists()) {
				outputFolder.mkdirs();
			}

			SimpleDateFormat formatter = new SimpleDateFormat("dd MM yyyy HH mm ss");

			if (currentFile == null || (new FileInputStream(currentFile).getChannel().size() > MAX_FILE_SIZE)) {
				System.out.println("Creating New File : "+outputFolder + File.separator+ formatter.format(new Date()).replaceAll(" ","_"));
				currentFile = new File(outputFolder + File.separator+ "THP_"+formatter.format(new Date()).replaceAll(" ","_")+".txt");
			}

		//	System.out.println("Starting File Write for file "+currentFile +"with content "+given);
			FileWriter localFileWriter = new FileWriter(currentFile,true);
			BufferedWriter localBufferedWriter = new java.io.BufferedWriter(localFileWriter);
			PrintWriter localPrintWriter = new java.io.PrintWriter(localBufferedWriter);
			localPrintWriter.println(given);
			localPrintWriter.close();
			//System.out.println("Finished file write...");
			

		}

		catch (Exception e) {
			e.printStackTrace();
		}

	}

}
