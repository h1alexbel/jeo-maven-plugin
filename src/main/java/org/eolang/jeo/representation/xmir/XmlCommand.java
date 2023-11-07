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
package org.eolang.jeo.representation.xmir;

import org.eolang.jeo.representation.bytecode.BytecodeMethod;

/**
 * XML representation of bytecode instruction or a label.
 * @since 0.1
 * @todo #226:90min Rename XmlCommand to XmlInstruction.
 *  XmlCommand is a bad name for this class. It is not a command, it is an instruction in general.
 *  Since instruction could be a label or a bytecode instruction, it is better to rename this class
 *  to XmlInstruction. After that, rename all usages of the old XmlInstruction to XmlCommand.
 */
public interface XmlCommand {

    /**
     * Write instruction to the bytecode method.
     * @param method Bytecode Method where instruction should be written.
     */
    void writeTo(BytecodeMethod method);
}