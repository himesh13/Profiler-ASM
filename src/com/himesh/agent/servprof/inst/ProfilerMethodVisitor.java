package com.himesh.agent.servprof.inst;

import com.himesh.agent.servprof.PrintTrace;
import com.himesh.agent.servprof.asm.MethodVisitor;
import com.himesh.agent.servprof.asm.Opcodes;
import com.himesh.agent.servprof.asm.Type;
import com.himesh.agent.servprof.utility.MethodKey;

public class ProfilerMethodVisitor extends StartEndMethodVisitor
{

	private String	name;

	private String	className;

	private String	args;

	public ProfilerMethodVisitor(MethodVisitor mv, int access, MethodKey methodKey, String methodDesc, String name,
			String className)
	{
		super(mv, access, methodKey, methodDesc);
		this.name = name;
		this.className = className;

		Type[] argumentTypes = Type.getArgumentTypes(methodDesc);

		args = "(";
		for (Type entry : argumentTypes)
		{

			args += entry.getInternalName() + ",";
		}
		if (args.length() > 2)
			args = args.substring(0, args.length() - 1);
		args += ")";
	}

	@Override
	protected void insertPrologue()
	{

		// System.out.println("In prologue");
		// String className =
		// Type.getArgumentTypes(methodDesc)[0].getInternalName();
		super.visitTypeInsn(Opcodes.NEW, "java/lang/StringBuilder");
		super.visitInsn(Opcodes.DUP);
		super.visitLdcInsn(">:" + className + "." + name + args + ":");
		super.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "(Ljava/lang/String;)V",
				false);
		super.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Thread", "currentThread", "()Ljava/lang/Thread;", false);
		super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Thread", "getId", "()J", false);
		super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "append",
				"(J)Ljava/lang/StringBuilder;", false);
		super.visitLdcInsn(":");
		super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "append",
				"(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
		super.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
		super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "append",
				"(J)Ljava/lang/StringBuilder;", false);
		super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;",
				false);
		super.visitMethodInsn(Opcodes.INVOKESTATIC, "com/himesh/agent/servprof/PrintTrace", "printToFile",
				"(Ljava/lang/String;)V", false);

		// super.visitLdcInsn("print this");
		// super.visitMethodInsn(Opcodes.INVOKESTATIC,
		// "com/takipi/samples/servprof/PrintTrace", "printToFile",
		// "(Ljava/lang/String;)V", false);
		// PrintTrace.printToFile("<,"+className+"."+name+Thread.currentThread().getId());
	}

	@Override
	protected void insertEpilogue()
	{

		// System.out.println("In Epilogue");
		super.visitTypeInsn(Opcodes.NEW, "java/lang/StringBuilder");
		super.visitInsn(Opcodes.DUP);
		super.visitLdcInsn("<:" + className + "." + name + args + ":");
		super.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "(Ljava/lang/String;)V",
				false);
		super.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Thread", "currentThread", "()Ljava/lang/Thread;", false);
		super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Thread", "getId", "()J", false);
		super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "append",
				"(J)Ljava/lang/StringBuilder;", false);
		super.visitLdcInsn(":");
		super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "append",
				"(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
		super.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
		super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "append",
				"(J)Ljava/lang/StringBuilder;", false);
		super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;",
				false);
		super.visitMethodInsn(Opcodes.INVOKESTATIC, "com/himesh/agent/servprof/PrintTrace", "printToFile",
				"(Ljava/lang/String;)V", false);

	}
}
