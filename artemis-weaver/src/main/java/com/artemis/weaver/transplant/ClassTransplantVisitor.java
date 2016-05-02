package com.artemis.weaver.transplant;

import com.artemis.Weaver;
import com.artemis.meta.ClassMetadata;
import com.artemis.meta.MethodDescriptor;
import org.objectweb.asm.*;

public class ClassTransplantVisitor extends ClassVisitor {
	private final ClassReader source;
	private final ClassVisitor target;
	private final ClassMetadata meta;
	private final String name;

	public ClassTransplantVisitor(ClassReader source,
	                              ClassVisitor target,
	                              ClassMetadata meta,
	                              String name) {

		super(Opcodes.ASM5, target);
		this.source = source;
		this.target = target;
		this.meta = meta;
		this.name = name;
	}

	@Override
	public void visit(int version,
	                  int access,
	                  String name,
	                  String signature,
	                  String superName,
	                  String[] interfaces) {

		System.out.println("visit: " + name);
		super.visit(version, access, this.name, signature, superName, interfaces);
	}

	@Override
	public void visitSource(String source, String debug) {
		super.visitSource(null, debug);
	}

	@Override
	public void visitOuterClass(String owner, String name, String desc) {
		super.visitOuterClass(owner, this.name, desc);
	}

	@Override
	public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
		return super.visitAnnotation(desc, visible);
	}

	@Override
	public AnnotationVisitor visitTypeAnnotation(int typeRef,
	                                             TypePath typePath,
	                                             String desc,
	                                             boolean visible) {

		return super.visitTypeAnnotation(typeRef, typePath, desc, visible);
	}

	@Override
	public void visitAttribute(Attribute attr) {
		super.visitAttribute(attr);
	}

	@Override
	public void visitInnerClass(String name, String outerName, String innerName, int access) {
		innerName = this.name.replaceAll("^.*\\$", "");
		outerName = this.name.replaceAll("\\$.*", "");
		super.visitInnerClass(this.name, outerName, innerName, access);
	}

	@Override
	public FieldVisitor visitField(int access,
	                               String name,
	                               String desc,
	                               String signature,
	                               Object value) {

		return super.visitField(access, name, desc, signature, value);
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
		return new MethodBodyTransplanter(source.getClassName(), meta, mv);
	}
}
