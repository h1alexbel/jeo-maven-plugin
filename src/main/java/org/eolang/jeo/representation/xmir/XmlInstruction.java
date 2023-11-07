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
package org.eolang.jeo.representation.xmir;

import com.jcabi.log.Logger;
import com.jcabi.xml.XMLDocument;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;
import org.eolang.jeo.representation.HexData;
import org.eolang.jeo.representation.bytecode.BytecodeMethod;
import org.objectweb.asm.Label;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Bytecode instruction from XML.
 * @since 0.1
 */
@SuppressWarnings("PMD.TooManyMethods")
public final class XmlInstruction implements XmlCommand {

    /**
     * Instruction node.
     */
    @SuppressWarnings("PMD.AvoidFieldNameMatchingMethodName")
    private final Node node;

    /**
     * Constructor.
     * @param xml XML node as String.
     */
    public XmlInstruction(final String xml) {
        this(new XMLDocument(xml).node().getFirstChild());
    }

    /**
     * Constructor.
     * @param node Instruction node.
     */
    public XmlInstruction(final Node node) {
        this.node = node;
    }

    /**
     * Instruction code.
     * @return Code.
     */
    public int code() {
        return Integer.parseInt(
            this.node.getAttributes()
                .getNamedItem("name")
                .getNodeValue().split("-")[1]
        );
    }

    @Override
    public void writeTo(final BytecodeMethod method) {
        method.instruction(this.code(), this.arguments());
    }

    /**
     * Replace values of instruction arguments.
     * @param old Old value.
     * @param replacement Which value to set instead.
     */
    public void replaceArguementsValues(final String old, final String replacement) {
        final String oldname = new HexData(old).value();
        new XmlNode(this.node).children().forEach(
            child -> {
                final String content = child.text();
                if (oldname.equals(content)) {
                    child.withText(new HexData(replacement).value());
                }
            }
        );
    }

    /**
     * Instruction arguments.
     * @return Arguments.
     */
    public Object[] arguments() {
        return XmlInstruction.arguments(this.node);
    }

    @Override
    public boolean equals(final Object other) {
        final boolean result;
        if (this == other) {
            result = true;
        } else if (other == null || this.getClass() != other.getClass()) {
            result = false;
        } else {
            final XmlInstruction that = (XmlInstruction) other;
            result = this.equals(this.node, that.node);
        }
        return result;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.node);
    }

    @Override
    public String toString() {
        return new XMLDocument(this.node).toString();
    }

    /**
     * XML node.
     * @return XML node.
     * @todo #157:90min Hide internal node representation in XmlInstruction.
     *  This class should not expose internal node representation.
     *  We have to consider to add methods or classes in order to avoid
     *  exposing internal node representation.
     */
    @SuppressWarnings("PMD.AvoidFieldNameMatchingMethodName")
    Node node() {
        return this.node;
    }

    /**
     * Get opcode arguments.
     * @param node Node.
     * @return Arguments.
     * @todo #234:90min Refactor XmlInstruction#arguments method.
     *  This method is too complex and has too many responsibilities.
     *  Moreover, it uses static field LABELS. We have to refactor this method in order
     *  to avoid all that problems. Don't forget to remove PMD and checkstyle comments about
     *  suppressed warnings.
     */
    private static Object[] arguments(final Node node) {
        final NodeList children = node.getChildNodes();
        final Collection<Object> res = new ArrayList<>(children.getLength());
        for (int index = 0; index < children.getLength(); ++index) {
            final Node child = children.item(index);
            if (child.getNodeName().equals("o")) {
                final NamedNodeMap attributes = child.getAttributes();
                if (attributes.getNamedItem("base").getNodeValue().equals("int")) {
                    res.add(new HexString(child.getTextContent()).decodeAsInt());
                } else if (attributes.getNamedItem("base").getNodeValue().equals("label")) {
                    final String uid = child.getTextContent().strip().replace("\n", "");
                    if (XmlLabel.LABELS.containsKey(uid)) {
                        res.add(XmlLabel.LABELS.get(uid));
                    } else {
                        final Label value = new Label();
                        XmlLabel.LABELS.put(uid, value);
                        res.add(value);
                    }
                } else {
                    res.add(new HexString(child.getTextContent()).decode());
                }
            }
        }
        return res.toArray();
    }

    /**
     * Check if two nodes are equal.
     * @param first First node.
     * @param second Second node.
     * @return True if nodes are equal.
     */
    private boolean equals(final Node first, final Node second) {
        final boolean result;
        if (first.getNodeType() == second.getNodeType()) {
            if (first.getNodeType() == Node.TEXT_NODE) {
                result = first.getTextContent().trim().equals(second.getTextContent().trim());
            } else {
                result = this.areElementsEqual(first, second);
            }
        } else {
            result = false;
        }
        return result;
    }

    /**
     * Check if two elements are equal.
     * @param first First element.
     * @param second Second element.
     * @return True if elements are equal.
     */
    private boolean areElementsEqual(final Node first, final Node second) {
        return first.getNodeName().equals(second.getNodeName())
            && XmlInstruction.hasSameAttributes(first, second)
            && this.hasSameChildren(first, second);
    }

    /**
     * Check if two nodes have the same attributes.
     * @param first First node.
     * @param second Second node.
     * @return True if nodes have the same attributes.
     */
    private static boolean hasSameAttributes(final Node first, final Node second) {
        boolean result = true;
        final NamedNodeMap attributes = first.getAttributes();
        final int length = attributes.getLength();
        if (length == second.getAttributes().getLength()) {
            for (int index = 0; index < length; ++index) {
                final Node left = attributes.item(index);
                final Node right = second.getAttributes().getNamedItem(left.getNodeName());
                if (!XmlInstruction.areAttributesEqual(left, right)) {
                    result = false;
                    break;
                }
            }
        } else {
            result = false;
        }
        return result;
    }

    /**
     * Check if two attributes are equal.
     * @param first First attribute.
     * @param second Second attribute.
     * @return True if attributes are equal.
     */
    private static boolean areAttributesEqual(final Node first, final Node second) {
        final boolean result;
        if (first != null && second != null && first.getNodeName().equals(second.getNodeName())) {
            if (first.getNodeName().equals("name")) {
                result = first.getNodeValue().split("-")[0]
                    .equals(second.getNodeValue().split("-")[0]);
            } else {
                result = first.getNodeValue().equals(second.getNodeValue());
            }
        } else {
            result = false;
        }
        return result;
    }

    /**
     * Check if two nodes have the same children.
     * @param left Left node.
     * @param right Right node.
     * @return True if nodes have the same children.
     */
    private boolean hasSameChildren(final Node left, final Node right) {
        final List<Node> first = XmlInstruction.children(left);
        final List<Node> second = XmlInstruction.children(right);
        return first.size() == second.size()
            && IntStream.range(0, first.size())
            .allMatch(index -> this.equals(first.get(index), second.get(index)));
    }

    /**
     * Get children nodes.
     * @param root Root node.
     * @return Children nodes.
     */
    private static List<Node> children(final Node root) {
        final NodeList all = root.getChildNodes();
        final List<Node> res = new ArrayList<>(0);
        for (int index = 0; index < all.getLength(); ++index) {
            final Node item = all.item(index);
            final int type = item.getNodeType();
            if (type == Node.ELEMENT_NODE) {
                res.add(item);
            }
            if (type == Node.TEXT_NODE && !item.getTextContent().isBlank()) {
                res.add(item);
            }
        }
        return res;
    }
}
