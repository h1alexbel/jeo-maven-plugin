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

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.xembly.Directive;
import org.xembly.Directives;

/**
 * An annotation value that is an array.
 * @since 0.6
 */
public final class DirectivesArrayAnnotationValue implements Iterable<Directive> {

    /**
     * The name of the annotation property.
     */
    private final String name;

    /**
     * The actual values.
     */
    private final List<Iterable<Directive>> values;

    /**
     * Constructor.
     * @param name The name of the annotation property.
     * @param children The actual values.
     */
    public DirectivesArrayAnnotationValue(
        final String name, final List<Iterable<Directive>> children
    ) {
        this.name = name;
        this.values = children;
    }

    @Override
    public Iterator<Directive> iterator() {
        return new DirectivesJeoObject(
            "annotation-property",
            Stream.concat(
                Stream.of(
                    new DirectivesValue("ARRAY"),
                    new DirectivesValue(this.name)
                ),
                this.values.stream()
            ).map(Directives::new).collect(Collectors.toList())
        ).iterator();
//        return new Directives()
//            .add("o").attr("base", new JeoFqn("annotation-property").fqn())
//            .append(new DirectivesValue("ARRAY"))
//            .append(new DirectivesValue(this.name))
//            .append(
//                this.values.stream()
//                    .map(Directives::new)
//                    .reduce(new Directives(), Directives::append)
//            )
//            .up()
//            .iterator();
    }
}