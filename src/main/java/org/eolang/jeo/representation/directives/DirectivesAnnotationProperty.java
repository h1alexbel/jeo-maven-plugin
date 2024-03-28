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

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.xembly.Directive;
import org.xembly.Directives;

/**
 * Annotation property as Xembly directives.
 * @since 0.3
 */
public final class DirectivesAnnotationProperty implements Iterable<Directive> {

    /**
     * Type of the property.
     */
    private final Type type;

    /**
     * Property parameters.
     */
    private final List<Object> params;

    /**
     * Constructor.
     * @param type Type of the property.
     * @param params Property parameters.
     */
    private DirectivesAnnotationProperty(final Type type, Object... params) {
        this(type, Arrays.asList(params));
    }

    /**
     * Constructor.
     * @param type Type of the property.
     * @param params Property parameters.
     */
    private DirectivesAnnotationProperty(final Type type, final List<Object> params) {
        this.type = type;
        this.params = params;
    }

    /**
     * Factory method for plain property.
     * @param params Parameters.
     * @return Property directives.
     */
    public static DirectivesAnnotationProperty plain(Object... params) {
        return new DirectivesAnnotationProperty(Type.PLAIN, params);
    }

    /**
     * Factory method for enum property.
     * @param name Name.
     * @param descriptor Descriptor.
     * @param value Value.
     * @return Property directives.
     */
    public static DirectivesAnnotationProperty enump(
        final String name, final String descriptor, final String value
    ) {
        return new DirectivesAnnotationProperty(Type.ENUM, name, descriptor, value);
    }

    /**
     * Factory method for array property.
     * @param name Name.
     * @return Property directives.
     */
    public static DirectivesAnnotationProperty array(final String name) {
        return new DirectivesAnnotationProperty(Type.ARRAY, name);
    }

    /**
     * Factory method for annotation property.
     * @param name Name.
     * @param descriptor Descriptor.
     * @return Property directives.
     */
    public static DirectivesAnnotationProperty annotation(
        final String name, final String descriptor
    ) {
        return new DirectivesAnnotationProperty(Type.ANNOTATION, name, descriptor);
    }

    @Override
    public Iterator<Directive> iterator() {
        final Directives directives = new Directives()
            .add("o").attr("base", "annotation-property")
            .append(new DirectivesData("type", this.type.toString()));
        this.params.stream().map(DirectivesData::new).forEach(directives::append);
        return directives.up().iterator();
    }

    /**
     * Property types.
     */
    private enum Type {
        /**
         * Plain property.
         */
        PLAIN,

        /**
         * Enum property.
         */
        ENUM,

        /**
         * Array property.
         */
        ARRAY,

        /**
         * Annotation property.
         */
        ANNOTATION
    }
}
