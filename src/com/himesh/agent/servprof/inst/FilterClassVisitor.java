package com.himesh.agent.servprof.inst;

import com.himesh.agent.servprof.asm.ClassVisitor;
import com.himesh.agent.servprof.asm.MethodVisitor;
import com.himesh.agent.servprof.asm.Opcodes;

public abstract class FilterClassVisitor extends ClassVisitor {
	private final InstrumentationFilter instFilter;

	public FilterClassVisitor(ClassVisitor cv, InstrumentationFilter instFilter) {
		super(Opcodes.ASM5, cv);

		this.instFilter = instFilter;
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);

		//this method is called for each method (including c'tors) in the instrumented class
		
		//if the method does not meet a target cut point return null
		//this instructs the library to take the method as is without our interference
		if ((mv == null) || (!instFilter.shouldInstrumentMethod(name)) || (access & Opcodes.ACC_INTERFACE) != 0 || (access & Opcodes.ACC_NATIVE) != 0) {
			return mv;
		}

		return createMethodVisitor(mv, access, name, desc);
	}

	protected abstract MethodVisitor createMethodVisitor(MethodVisitor mv, int access, String name, String desc);
}
