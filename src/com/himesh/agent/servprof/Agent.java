package com.himesh.agent.servprof;

import java.io.FileNotFoundException;
import java.lang.instrument.Instrumentation;

import com.himesh.agent.servprof.inst.InstrumentationFilter;
import com.himesh.agent.servprof.utility.Utility;
import com.sun.tools.attach.VirtualMachine;


public class Agent {
	private static Transformer internalPremain(String agentArgs, Instrumentation inst) throws FileNotFoundException {
		
		
		Utility util = new Utility();
		
		util.printInfo("Starting Agent");
		
		// parse the options that were passed into the java agent
		Options options = Options.parse(agentArgs);

		// print options to console
		util.printInfo(options.print());

		// create a cut point filter based on Regex supplied through options
		InstrumentationFilter instFilter = new InstrumentationFilter(options.getClassPattern(),
				options.getMethodPattern());

		// initialize the bytecode instrumentation transformer
		Transformer transformer = new Transformer(options, instFilter);

		// connect our transformer into the bytecode transformation chain
		inst.addTransformer(transformer, true);

		return transformer;
	}
	
	public static void premain(String agentArgs,Instrumentation inst){
		try {
			internalPremain(agentArgs, inst);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	


	// this method is invoked when an agent is attached to a live JVM
	public static void agentmain(String agentArgs, Instrumentation inst) throws FileNotFoundException {
		System.out.println("TH profiling agent remotely attached.");

		// initialize a transformer
		Transformer transformer = internalPremain(agentArgs, inst);

		try {
			// gather the list of classes that have already been loaded into the
			// JVM
			Class<?> loadedClasses[] = inst.getAllLoadedClasses();

			for (Class<?> loadedClass : loadedClasses) {
				String internalName = loadedClass.getName();

				// if the class matches our filter, initiate a re-transform
				// sequence
				if (transformer.getInstFilter().shouldInstrumentClass(internalName)) {
					System.out.println("Retransforming loaded class " + internalName);
					inst.retransformClasses(loadedClass);
				}

			}
		} catch (Exception e) {
			System.err.println(e);
		}
	}


}
