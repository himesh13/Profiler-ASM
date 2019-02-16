package com.himesh.agent.servprof;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import com.himesh.agent.servprof.asm.ClassReader;
import com.himesh.agent.servprof.asm.ClassVisitor;
import com.himesh.agent.servprof.asm.ClassWriter;
import com.himesh.agent.servprof.asm.Opcodes;
import com.himesh.agent.servprof.inst.InstrumentationFilter;
import com.himesh.agent.servprof.inst.ProfilerClassVisitor;
import com.himesh.agent.servprof.utility.Utility;

public class Transformer implements ClassFileTransformer
{

	private final InstrumentationFilter	instFilter;

	private final Options				options;

	File								outputByteCodeFolder;

	private final static String			outputByteCodeFolderPath	= "/path/to/data/output/folder";

	private final String packageInclusions = "com/example/package";
	public Transformer(Options options, InstrumentationFilter instFilter)
	{
		this.instFilter = instFilter;
		this.options = options;
		outputByteCodeFolder = new File(outputByteCodeFolderPath);

		if (!outputByteCodeFolder.exists())
		{
			outputByteCodeFolder.mkdirs();
		}

	}

	@Override
	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
			ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException
	{

		// if the class being loaded / redefined does not match our target cut
		// points return null
		// this instructs the JVM to continue loading that class as is
		if (!className.contains(packageInclusions)
				|| className.contains("com/himesh/agent/servprof/PrintTrace"))
		{

			return null;
		}

		// initiate a reader which will scan the loaded byte code
		ClassReader cr = new ClassReader(classfileBuffer);

		if ((cr.getAccess() & Opcodes.ACC_INTERFACE) != 0)
		{
			return null;
		}

		// create a writer which will receive data from the reader and write
		// that into a new bytecode raw byte[] buffer

		ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);

		// every instruction read by the class reader will be passed on to us
		// before it goes into the writer
		// this gives us the ability to manipulate, or "instrument" the class's
		// code with our own

		// Coded for future use.Not used right now.
		// ClassVisitor tnv = new ThreadNameSetterClassVisitor(cw, instFilter,
		// className);

		ClassVisitor cv = new ProfilerClassVisitor(cw, instFilter, className);

		// initiate a Visitor pattern between the reader and writer, passing our
		// visitor through the chain
		cr.accept(cv, 0);

		// the writer has now completed generating the new class, convert to raw
		// bytecode

		try
		{
//			System.out.println(outputByteCodeFolder + "//" + className + ".class");
			File classFile = new File(outputByteCodeFolder + "//" + className + ".class");
			if (classFile.exists())
			{
				classFile.delete();
			}

			classFile.getParentFile().mkdirs();
			classFile.createNewFile();
			FileOutputStream fileOutputStream = new FileOutputStream(classFile);
			fileOutputStream.write(cw.toByteArray());
			fileOutputStream.close();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return cw.toByteArray();
	}

	public Options getOptions()
	{

		return options;
	}

	public InstrumentationFilter getInstFilter()
	{

		return instFilter;
	}

}
