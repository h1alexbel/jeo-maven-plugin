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

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;
import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import org.cactoos.Input;
import org.cactoos.io.ResourceOf;
import org.cactoos.text.TextOf;
import org.cactoos.text.UncheckedText;

/**
 * Java source class with ".java" extension.
 * @since 0.1
 */
@SuppressWarnings("JTCOP.RuleCorrectTestName")
final class JavaSourceClass {

    /**
     * Name of Java class.
     */
    private final String name;

    /**
     * Source of Java class.
     */
    private final Input java;

    /**
     * Constructor.
     * @param resource Resource of Java class.
     */
    JavaSourceClass(final String resource) {
        this(JavaSourceClass.filename(resource), new ResourceOf(resource));
    }

    /**
     * Constructor.
     * @param filename Name of Java class.
     * @param java Source of Java class.
     */
    private JavaSourceClass(final String filename, final Input java) {
        this.name = filename;
        this.java = java;
    }

    /**
     * Compile the Java class.
     * @return Bytecode of compiled class.
     */
    byte[] compile() {
        final JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        final BytecodeManager manager = new BytecodeManager(compiler);
        final boolean successful = compiler.getTask(
            null,
            manager,
            null,
            null,
            null,
            Collections.singleton(new SourceCode(this.name, this.java))
        ).call();
        if (successful) {
            return manager.bytecode();
        } else {
            throw new IllegalStateException(
                String.format("Compilation failed for class %s", this.name)
            );
        }
    }

    /**
     * Name of Java class.
     * @param resource Resource of Java class (full path in resources).
     * @return Name of Java class.
     */
    private static String filename(final String resource) {
        return resource.substring(resource.lastIndexOf('/') + 1);
    }

    /**
     * Compilation manager that stores the bytecode during compilation.
     * @since 0.1.0
     */
    private static final class BytecodeManager
        extends ForwardingJavaFileManager<StandardJavaFileManager> {

        /**
         * Bytecode.
         */
        private final AtomicReference<Bytecode> output;

        /**
         * Constructor.
         * @param compiler Java compiler.
         */
        private BytecodeManager(final JavaCompiler compiler) {
            this(
                compiler.getStandardFileManager(
                    null,
                    Locale.getDefault(),
                    Charset.defaultCharset()
                )
            );
        }

        /**
         * Constructor.
         * @param manager Standard Java file manager.
         */
        private BytecodeManager(final StandardJavaFileManager manager) {
            super(manager);
            this.output = new AtomicReference<>();
        }

        @Override
        public JavaFileObject getJavaFileForOutput(
            final Location location,
            final String classname,
            final JavaFileObject.Kind kind,
            final FileObject sibling
        ) {
            this.output.set(new Bytecode(classname));
            return this.output.get();
        }

        /**
         * Get the bytecode.
         * @return Bytecode.
         */
        byte[] bytecode() {
            return this.output.get().bytecode();
        }
    }

    /**
     * Java source code.
     * @since 0.1.0
     */
    private static final class SourceCode extends SimpleJavaFileObject {

        /**
         * Source code.
         */
        private final Input src;

        /**
         * Construct a SimpleJavaFileObject of the given kind and with the
         * given URI.
         * @param name Name of Java class.
         * @param input Source of Java class.
         */
        SourceCode(final String name, final Input input) {
            super(URI.create(name), Kind.SOURCE);
            this.src = input;
        }

        @Override
        public CharSequence getCharContent(final boolean ignore) {
            return new UncheckedText(new TextOf(this.src)).asString();
        }
    }

    /**
     * Bytecode of compiled class.
     * @since 0.1.0
     */
    private static final class Bytecode extends SimpleJavaFileObject {

        /**
         * Output stream.
         */
        private final ByteArrayOutputStream output;

        /**
         * Constructor.
         * @param name Name of Java class.
         */
        Bytecode(final String name) {
            super(URI.create(String.format("%s%s", name, Kind.CLASS.extension)), Kind.CLASS);
            this.output = new ByteArrayOutputStream();
        }

        @Override
        public OutputStream openOutputStream() {
            return this.output;
        }

        /**
         * Get the bytecode.
         * @return Bytecode.
         */
        byte[] bytecode() {
            return this.output.toByteArray();
        }
    }
}
