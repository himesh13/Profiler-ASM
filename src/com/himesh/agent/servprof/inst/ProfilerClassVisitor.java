package com.himesh.agent.servprof.inst;

import com.himesh.agent.servprof.asm.ClassVisitor;
import com.himesh.agent.servprof.asm.MethodVisitor;
import com.himesh.agent.servprof.utility.MethodKey;




public class ProfilerClassVisitor extends FilterClassVisitor {
	private final String className;

	public ProfilerClassVisitor(ClassVisitor cv, InstrumentationFilter instFilter, String className) {
		super(cv, instFilter);
//		System.out.println("In PCV Constructor");
		this.className = className;
	}


	@Override
	protected MethodVisitor createMethodVisitor(MethodVisitor mv, int access, String name, String desc) {
		
//		System.out.println("Returning new PCV");
		return new ProfilerMethodVisitor(mv, access, new MethodKey(className, name), desc,name,className);
	}

}
