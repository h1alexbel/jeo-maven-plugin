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
package org.eolang.jeo.representation.asm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * XML method.
 * @since 0.1
 */
final class XmlMethod {

    /**
     * Method node.
     */
    private final Node node;

    /**
     * Constructor.
     * @param node Method node.
     */
    XmlMethod(final Node node) {
        this.node = node;
    }

    /**
     * Method access modifiers.
     * @return Access modifiers.
     */
    int access() {
        return Integer.parseInt(this.nameAttribute()[0]);
    }

    /**
     * Method name.
     * @return Name.
     */
    String name() {
        return String.valueOf(this.nameAttribute()[1]);
    }

    /**
     * Method descriptor.
     * @return Descriptor.
     */
    String descriptor() {
        return String.valueOf(this.nameAttribute()[2]);
    }

    /**
     * Method 'name' attribute.
     * @return 'name' attribute value.
     */
    private String[] nameAttribute() {
        final Node name = this.node.getAttributes().getNamedItem("name");
        final String content = name.getTextContent();
        return content.split("__");
    }

    /**
     * Method instructions.
     * @return Instructions.
     */
    List<XmlInstruction> instructions() {
        final List<XmlInstruction> result;
        final Optional<Node> sequence = this.sequence();
        if (sequence.isPresent()) {
            final Node seq = sequence.get();
            final List<XmlInstruction> instructions = new ArrayList<>();
            for (int index = 0; index < seq.getChildNodes().getLength(); ++index) {
                final Node instruction = seq.getChildNodes().item(index);
                if (XmlMethod.isInstruction(instruction)) {
                    instructions.add(new XmlInstruction(instruction));
                }
            }
            result = instructions;
        } else {
            result = Collections.emptyList();
        }
        return result;
    }

    /**
     * Find sequence node.
     * @return Sequence node.
     */
    private Optional<Node> sequence() {
        Optional<Node> result = Optional.empty();
        final NodeList children = this.node.getChildNodes();
        for (int index = 0; index < children.getLength(); index++) {
            final Node item = children.item(index);
            final NamedNodeMap attributes = item.getAttributes();
            if (attributes == null) {
                continue;
            }
            final Node base = attributes.getNamedItem("base");
            if (base == null) {
                continue;
            }
            if (base.getNodeValue().equals("seq")) {
                result = Optional.of(item);
                break;
            }
        }
        return result;
    }

    /**
     * Check if node is an instruction.
     * @param node Node.
     * @return True if node is an instruction.
     */
    private static boolean isInstruction(final Node node) {
        final boolean result;
        final NamedNodeMap attrs = node.getAttributes();
        if (attrs == null || attrs.getNamedItem("name") == null) {
            result = false;
        } else {
            result = true;
        }
        return result;
    }
}
