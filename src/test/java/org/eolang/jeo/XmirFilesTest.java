/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016-2024 Objectionary.com
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
package org.eolang.jeo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.eolang.jeo.representation.bytecode.BytecodeClass;
import org.eolang.jeo.representation.bytecode.BytecodeProgram;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Test case for {@link XmirFiles}.
 *
 * @since 0.1.0
 */
final class XmirFilesTest {

    @Test
    void retrievesObjectsSuccessfully(@TempDir final Path temp) throws IOException {
        final int expected = 2;
        final Path directory = temp.resolve(Paths.get("xmirs"));
        Files.createDirectories(directory);
        Files.write(
            directory.resolve("first.xmir"),
            new BytecodeProgram(new BytecodeClass("First")).xml().toString()
                .getBytes(StandardCharsets.UTF_8)
        );
        Files.write(
            directory.resolve("second.xmir"),
            new BytecodeProgram(new BytecodeClass("Second")).xml().toString()
                .getBytes(StandardCharsets.UTF_8)
        );
        MatcherAssert.assertThat(
            String.format("Objects were not retrieved, we expected '%d' objects", expected),
            new XmirFiles(temp).all().collect(Collectors.toList()),
            Matchers.hasSize(expected)
        );
    }

    @Test
    void retrievesEmptyObjectsIfFolderIsEmpty(@TempDir final Path temp) throws IOException {
        Files.createDirectories(temp.resolve("some-path"));
        MatcherAssert.assertThat(
            "Objects were not retrieved, we expected empty list",
            new XmirFiles(temp).all().collect(Collectors.toList()),
            Matchers.empty()
        );
    }

    @Test
    void throwsExceptionIfFolderDoesNotExist(@TempDir final Path temp) {
        Assertions.assertThrows(
            IllegalStateException.class,
            () -> new XmirFiles(temp.resolve("missing")).all(),
            "Exception was not thrown when folder does not exist"
        );
    }

    @Test
    void usesDifferentSubfolder(@TempDir final Path temp) throws IOException {
        final Path generated = temp.resolve("generated-sources");
        final Path path = generated.resolve("opeo-xmir");
        Files.createDirectories(path);
        Files.write(
            path.resolve("opeo-class.xmir"),
            new BytecodeProgram(new BytecodeClass("OpeoClass")).xml().toString()
                .getBytes(StandardCharsets.UTF_8)
        );
        final Stream<Path> all = new XmirFiles(path).all();
        MatcherAssert.assertThat(
            String.format(
                "Objects were not retrieved, we expected exactly one object was read from %s",
                path
            ),
            all.collect(Collectors.toList()),
            Matchers.hasSize(1)
        );
    }
}
