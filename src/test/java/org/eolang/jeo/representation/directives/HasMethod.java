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
package org.eolang.jeo.representation.directives;

import com.jcabi.xml.XMLDocument;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

/**
 * Matcher to check if the received XMIR document has a method inside a class with a given name.
 * @since 0.1.0
 */
public class HasMethod extends TypeSafeMatcher<String> {

    /**
     * Class name.
     */
    private final String clazz;

    /**
     * Method name.
     */
    private final String method;

    /**
     * Constructor.
     * @param method Method name.
     */
    HasMethod(final String method) {
        this("", method);
    }

    /**
     * Constructor.
     * @param clazz Class name.
     * @param method Method name.
     */
    private HasMethod(final String clazz, final String method) {
        this.clazz = clazz;
        this.method = method;
    }

    /**
     * Inside a class.
     * @param clazz Class name.
     * @return New matcher that checks class.
     */
    HasMethod inside(final String clazz) {
        return new HasMethod(clazz, this.method);
    }

    @Override
    protected boolean matchesSafely(final String item) {
        final XMLDocument document = new XMLDocument(item);
        return this.checks().stream().map(document::xpath).noneMatch(List::isEmpty);
    }

    @Override
    public void describeTo(final Description description) {
        final Description descr = description.appendText(
            "Received XMIR document doesn't comply with the following XPaths: ");
        this.checks().forEach(xpath -> descr.appendText("\n").appendText(xpath));
        descr.appendText("\nPlease, check the received XMIR document.");
    }

    /**
     * Checks.
     * @return List of XPaths to check.
     */
    private List<String> checks() {
        final String main = String.format(
            "/program/objects/o[@name='%s']/o[@name='%s']",
            this.clazz,
            this.method
        );
        return Stream.of(
            main.concat("/text()"),
            main.concat("/o[@base='seq']/text()"),
            main.concat("/o[@name='access']/@base"),
            main.concat("/o[@name='descriptor']/@base"),
            main.concat("/o[@name='signature']/@base"),
            main.concat("/o[@name='exceptions']/@base")
        ).collect(Collectors.toList());
    }
}
