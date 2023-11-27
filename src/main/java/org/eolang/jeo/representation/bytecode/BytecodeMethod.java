/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016-2023 Objectionary.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.eolang.jeo.representation.bytecode;

import java.util.ArrayList;
import java.util.List;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

/**
 * Bytecode method.
 * @since 0.1.0
 */
public final class BytecodeMethod {

    /**
     * ASM class writer.
     */
    private final ClassWriter writer;

    /**
     * Original class.
     */
    private final BytecodeClass clazz;

    /**
     * Method Instructions.
     */
    private final List<BytecodeEntry> instructions;

    /**
     * Method properties.
     */
    private final BytecodeMethodProperties properties;

    /**
     * Constructor.
     * @param name Method name.
     * @param writer ASM class writer.
     * @param clazz Original class.
     * @param modifiers Access modifiers.
     * @checkstyle ParameterNumberCheck (5 lines)
     */
    BytecodeMethod(
        final String name,
        final ClassWriter writer,
        final BytecodeClass clazz,
        final int... modifiers
    ) {
        this.writer = writer;
        this.clazz = clazz;
        this.instructions = new ArrayList<>(0);
        this.properties = new BytecodeMethodProperties(name, modifiers);
    }

    /**
     * Constructor.
     * @param name Method name.
     * @param writer ASM class writer.
     * @param clazz Original class.
     * @param modifiers Access modifiers.
     * @checkstyle ParameterNumberCheck (5 lines)
     */
    BytecodeMethod(
        final String name,
        final ClassWriter writer,
        final BytecodeClass clazz,
        final String descriptor,
        final int... modifiers
    ) {
        this(new BytecodeMethodProperties(name, descriptor, modifiers), writer, clazz);
    }

    /**
     * Constructor.
     * @param properties Method properties.
     * @param writer ASM class writer.
     * @param clazz Original class.
     */
    BytecodeMethod(BytecodeMethodProperties properties, ClassWriter writer, BytecodeClass clazz) {
        this.properties = properties;
        this.writer = writer;
        this.clazz = clazz;
        this.instructions = new ArrayList<>(0);

    }

    /**
     * Return to the original class.
     * @return Original class.
     * @checkstyle MethodNameCheck (3 lines)
     */
    @SuppressWarnings("PMD.ShortMethodName")
    public BytecodeClass up() {
        return this.clazz;
    }

    /**
     * Add label.
     * @param label Label.
     * @return This object.
     */
    public BytecodeMethod label(final Label label) {
        this.instructions.add(new BytecodeLabelEntry(label));
        return this;
    }

    /**
     * Add instruction.
     * @param opcode Opcode.
     * @param args Arguments.
     * @return This object.
     */
    public BytecodeMethod instruction(final int opcode, final Object... args) {
        this.instructions.add(new BytecodeInstructionEntry(opcode, args));
        return this;
    }

    /**
     * Generate bytecode.
     */
    void write() {
        final MethodVisitor visitor = this.properties.addMethod(this.writer);
        this.instructions.forEach(instruction -> instruction.writeTo(visitor));
        visitor.visitMaxs(0, 0);
        visitor.visitEnd();
    }
}
