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
package it;

import com.github.lombrozo.jsmith.RandomJavaClass;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.tools.ToolProvider;
import org.eolang.jeo.representation.BytecodeRepresentation;
import org.eolang.jeo.representation.EoRepresentation;
import org.eolang.jeo.representation.bytecode.Bytecode;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Set of integration tests for Java source parsing and transformation.
 * The default pipeline for all the tests is:
 * - Generate random Java source code
 * - Compile it into bytecode
 * - Transform bytecode into XMIR
 * - Transform XMIR back into bytecode
 * - Compare the original bytecode with the transformed one.
 * @since 0.1
 * @todo #218:90min Enable java compilation test.
 *  Currently, the test is disabled because it fails. The reason is that:
 *  1. We loose some additional information from original code during transformation and bytecode is
 *  not equal to the original one. It's not critical for runtime, but we need to fix it.
 *  2. Some machines might now have java compiler installed.
 *  We need to fix both of these issues and enable the test.
 *  The test is {@link #transformsRandomJavaSourceCodeIntoEoAndBack}.
 */
class JavaSourceCompilationIT {

    @Test
    @Disabled
    void transformsRandomJavaSourceCodeIntoEoAndBack(@TempDir final Path temp) throws IOException {
        final Bytecode expected = JavaSourceCompilationIT.compile(temp, new RandomJavaClass());
        MatcherAssert.assertThat(
            "Bytecode is not equal to the original one, check that transformation is correct and does not change the bytecode",
            new EoRepresentation(
                new BytecodeRepresentation(expected).toEO()
            ).toBytecode(),
            Matchers.equalTo(expected)
        );
    }

    /**
     * Compile random java class into bytecode.
     * @param where Where to compile.
     * @param clazz Random java class.
     * @return Bytecode.
     */
    private static Bytecode compile(
        final Path where,
        final RandomJavaClass clazz
    ) throws IOException {
        final Path src = where.resolve(String.format("%s.java", clazz.name()));
        Files.write(src, clazz.src().getBytes(StandardCharsets.UTF_8));
        ToolProvider.getSystemJavaCompiler().run(System.in, System.out, System.err, src.toString());
        return new Bytecode(
            Files.readAllBytes(where.resolve(String.format("%s.class", clazz.name())))
        );
    }
}