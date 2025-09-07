/*
 * SPDX-FileCopyrightText: Copyright (c) 2016-2025 Objectionary.com
 * SPDX-License-Identifier: MIT
 */
package org.eolang.jeo;

import java.nio.file.Path;
import java.nio.file.Paths;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link BytecodeClasses}.
 * @since 0.14.0
 */
final class BytecodeClassesTest {

    @Test
    void convertsToString() {
        final Path path = Paths.get("src/test/resources/bytecode-classes");
        MatcherAssert.assertThat(
            "BytecodeClasses toString() should return the correct path",
            new BytecodeClasses(path).toString(),
            Matchers.equalTo(path.toString())
        );
    }
}
