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
package org.eolang.jeo;

import java.nio.file.Path;

/**
 * Transformation.
 * @since 0.6
 * @todo #784:90min Refactor Transformation, Translation and Translator.
 *  These abstractions are overcomplicated. Most probably, we can remove some of them.
 *  They were added to solve the immediate issues without thinking about the long-term
 *  architecture. Let's refactor them to make the code simpler and more maintainable.
 */
public interface Transformation {

    /**
     * The path to the file to be transformed.
     * @return Path to the file.
     */
    Path source();

    /**
     * The path to the transformed file.
     * @return Path to the transformed file.
     */
    Path target();

    /**
     * Transform the file.
     * @return Transformed file.
     */
    byte[] transform();
}