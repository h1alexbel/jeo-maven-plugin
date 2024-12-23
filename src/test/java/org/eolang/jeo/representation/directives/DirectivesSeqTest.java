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

import com.jcabi.matchers.XhtmlMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.xembly.ImpossibleModificationException;
import org.xembly.Xembler;

/**
 * Directives sequence test cases.
 * Test cases for {@link DirectivesSeq}.
 * @since 0.6
 */
final class DirectivesSeqTest {

    @Test
    void convertsToNumberedSeq() throws ImpossibleModificationException {
        MatcherAssert.assertThat(
            "We expect to get a numbered sequence with two elements",
            new Xembler(
                new DirectivesSeq(
                    new DirectivesValue("1"), new DirectivesValue("2")
                )
            ).xml(),
            XhtmlMatchers.hasXPaths(
                "/o[contains(@base,'seq2') and @name='@']",
                "/o[contains(@base,'seq2') and @name='@']/o[contains(@base,'string')]/o[@base='org.eolang.bytes' and text()='31']",
                "/o[contains(@base,'seq2') and @name='@']/o[contains(@base,'string')]/o[@base='org.eolang.bytes' and text()='32']"
            )
        );
    }
}
