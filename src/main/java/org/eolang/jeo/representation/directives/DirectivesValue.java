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
import lombok.ToString;
import org.eolang.jeo.representation.HexData;
import org.xembly.Directive;

/**
 * Data Object Directive in EO language.
 *
 * @since 0.1.0
 * @todo #627:90min Remove 'line' attribute usages.
 *  We add 'line' attribute in many places to be able print XMIR representation as PHI expressions.
 *  Actually we shouldn't add any artificial attributes to the representation.
 *  When the following issue will be solved we should remove 'line' attribute from all places
 *  where it used:
 *  https://github.com/objectionary/eo/issues/3189
 */
@ToString
public final class DirectivesValue implements Iterable<Directive> {

    /**
     * Data.
     */
    private final HexData data;

    /**
     * Name.
     */
    private final String name;

    /**
     * Constructor.
     * @param data Data.
     * @param <T> Data type.
     */
    public <T> DirectivesValue(final T data) {
        this("", data);
    }

    /**
     * Constructor.
     *
     * @param <T> Data type.
     * @param name Name.
     * @param data Data.
     */
    public <T> DirectivesValue(final String name, final T data) {
        this(name, new HexData(data));
    }

    /**
     * Constructor.
     *
     * @param name Name.
     * @param data Data.
     */
    public DirectivesValue(final String name, final HexData data) {
        this.data = data;
        this.name = name;
    }

    @Override
    public Iterator<Directive> iterator() {
        return new DirectivesJeoObject(
            this.data.type(),
            this.name,
            new DirectivesBytes(this.data.value())
        ).iterator();
//        try {
//            final Directives directives = new Directives().add("o")
//                .attr("base", new JeoFqn(this.data.type()).fqn())
//                .attr("line", new Random().nextInt(Integer.MAX_VALUE));
//            if (!this.name.isEmpty()) {
//                directives.attr("name", this.name);
//            }
//            return directives.append(new DirectivesBytes(this.data.value())).up().iterator();
//        } catch (final IllegalArgumentException exception) {
//            throw new IllegalStateException(
//                String.format("Failed to create directives for %s", this), exception
//            );
//        }
    }
}
