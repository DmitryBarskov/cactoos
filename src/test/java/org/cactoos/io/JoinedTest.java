/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2017-2022 Yegor Bugayenko
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
package org.cactoos.io;

import java.io.IOException;
import org.cactoos.iterable.IterableOf;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.Assertion;
import org.llorllale.cactoos.matchers.HasContent;

/**
 * Unit tests for {@link Joined}.
 * @since 0.36
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 */
final class JoinedTest {
    /**
     * Must join inputs in the given order.
     * @throws IOException If an error occurs
     */
    @Test
    void joinsOk() {
        new Assertion<>(
            "Cannot properly join inputs",
            new Joined(
                new InputOf("first"),
                new InputOf("second"),
                new InputOf("third")
            ),
            new HasContent("firstsecondthird")
        ).affirm();
    }

    /**
     * Must join inputs of the iterable in the given order.
     */
    @Test
    void fromIterable() {
        new Assertion<>(
            "Can't join iterable of inputs",
            new Joined(
                new IterableOf<>(
                    new InputOf("ab"),
                    new InputOf("cde"),
                    new InputOf("fghi")
                )
            ),
            new HasContent("abcdefghi")
        ).affirm();
    }
}
