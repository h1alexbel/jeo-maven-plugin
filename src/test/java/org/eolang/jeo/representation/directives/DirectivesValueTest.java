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
package org.eolang.jeo.representation.directives;

import org.eolang.jeo.matchers.SameXml;
import org.eolang.jeo.representation.DataType;
import org.eolang.jeo.representation.xmir.AllLabels;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.xembly.ImpossibleModificationException;
import org.xembly.Transformers;
import org.xembly.Xembler;

/**
 * Test case for {@link DirectivesValue}.
 * @since 0.3
 */
final class DirectivesValueTest {

    @Test
    void convertsInteger() throws ImpossibleModificationException {
        MatcherAssert.assertThat(
            "We expect that integer value is converted to the correct XMIR",
            new Xembler(new DirectivesValue(42, "access")).xml(),
            new SameXml(
                String.join(
                    "\n",
                    "<o base='org.eolang.jeo.int' name='access'>",
                    "  <o base='bytes' data='bytes'>00 00 00 00 00 00 00 2A</o>",
                    "</o>"
                )
            )
        );
    }

    @Test
    void convertsLabel() throws ImpossibleModificationException {
        MatcherAssert.assertThat(
            "Converts label to XML",
            new Xembler(
                new DirectivesValue(
                    new AllLabels().label("some-random")
                ),
                new Transformers.Node()
            ).xml(),
            new SameXml(
                "<o base='org.eolang.jeo.label'><o base='bytes' data='bytes'>73 6F 6D 65 2D 72 61 6E 64 6F 6D</o></o>"
            )
        );
    }

    @Test
    void decodesLabel() {
        MatcherAssert.assertThat(
            "Decodes label from XML",
            DataType.find("org.eolang.jeo.label").decode("73 6F 6D 65 2D 72 61 6E 64 6F 6D"),
            Matchers.equalTo(new AllLabels().label("some-random"))
        );
    }
}