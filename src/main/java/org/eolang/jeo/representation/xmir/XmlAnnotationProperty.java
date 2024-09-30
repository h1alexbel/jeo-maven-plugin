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
package org.eolang.jeo.representation.xmir;

import java.util.List;
import java.util.stream.Collectors;
import org.eolang.jeo.representation.bytecode.AnnotationAnnotationValue;
import org.eolang.jeo.representation.bytecode.ArrayAnnotationValue;
import org.eolang.jeo.representation.bytecode.BytecodeAnnotationValue;
import org.eolang.jeo.representation.bytecode.EnumAnnotationValue;
import org.eolang.jeo.representation.bytecode.PlainAnnotationValue;

/**
 * Xmir annotation property.
 * @since 0.3
 */
public final class XmlAnnotationProperty {

    /**
     * Annotation property XML node.
     */
    private final XmlNode node;

    /**
     * Constructor.
     * @param xmlnode XML node.
     */
    public XmlAnnotationProperty(final XmlNode xmlnode) {
        this.node = xmlnode;
    }

    /**
     * Transform to bytecode.
     * @return Bytecode annotation property.
     */
    public BytecodeAnnotationValue bytecode() {
        final List<Object> params = this.params();
        switch (this.type()) {
            case "PLAIN":
                return new PlainAnnotationValue(
                    String.valueOf(params.get(0)),
                    params.get(1)
                );
            case "ARRAY":
                return new ArrayAnnotationValue(
                    String.valueOf(params.get(0)),
                    params.stream()
                        .skip(1)
                        .map(BytecodeAnnotationValue.class::cast)
//                        .map(XmlAnnotationProperty::bytecode)
                        .collect(Collectors.toList())
                );
            case "ANNOTATION":
                return new AnnotationAnnotationValue(
                    String.valueOf(params.get(0)),
                    String.valueOf(params.get(1)),
                    params.stream()
                        .skip(2)
                        .map(BytecodeAnnotationValue.class::cast)
                        .collect(Collectors.toList())
                );
//                return new BytecodeAnnotationProperty.Annotation(
//                    params.get(0).toString(),
//                    params.get(1).toString(),
//                    params.stream()
//                        .skip(2)
//                        .map(XmlAnnotationProperty.class::cast)
//                        .map(XmlAnnotationProperty::bytecode)
//                        .collect(Collectors.toList())
//                );
            case "ENUM":
                return new EnumAnnotationValue(
                    String.valueOf(params.get(0)),
                    String.valueOf(params.get(1)),
                    String.valueOf(params.get(2))
                );
//                return new BytecodeAnnotationProperty.Enum(
//                    params.get(0).toString(),
//                    params.get(1).toString(),
//                    params.get(2).toString()
//                );
            default:
                throw new IllegalArgumentException(
                    String.format("Unknown annotation property type: %s", this.type())
                );
        }
//        return BytecodeAnnotationProperty.byType(this.type(), params);
    }

    /**
     * Type of the property.
     * @return Type.
     */
    private String type() {
        return (String) new XmlOperand(
            this.node.children().collect(Collectors.toList()).get(0)
        ).asObject();
    }

    /**
     * Property parameters.
     * @return Parameters.
     */
    private List<Object> params() {
        return this.node.children()
            .skip(1)
            .map(XmlOperand::new)
            .map(XmlOperand::asObject)
            .collect(Collectors.toList());
    }
}
