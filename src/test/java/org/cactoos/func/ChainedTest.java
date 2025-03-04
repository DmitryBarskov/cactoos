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
package org.cactoos.func;

import org.cactoos.Func;
import org.cactoos.iterable.Filtered;
import org.cactoos.iterable.IterableOf;
import org.cactoos.iterable.Mapped;
import org.cactoos.scalar.LengthOf;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.Assertion;
import org.llorllale.cactoos.matchers.HasValue;

/**
 * Test case for {@link Chained}.
 *
 * @since 0.7
 * @checkstyle JavadocMethodCheck (500 lines)
 * @checkstyle MagicNumber (500 line)
 */
final class ChainedTest {

    @Test
    void withoutIterable() throws Exception {
        new Assertion<>(
            "Must work without iterable",
            new LengthOf(
                new Filtered<>(
                    input -> input.endsWith("12"),
                    new Mapped<>(
                        new Chained<>(
                            input -> input.concat("1"),
                            (String input) -> input.concat("2")
                        ),
                        new IterableOf<>("public", "final", "class")
                    )
                )
            ),
            new HasValue<>(3L)
        ).affirm();
    }

    @Test
    void withIterable() throws Exception {
        new Assertion<>(
            "Must work with iterable",
            new LengthOf(
                new Filtered<>(
                    input -> !input.startsWith("st"),
                    new Mapped<>(
                        new Chained<>(
                            input -> input.concat("1"),
                            new IterableOf<Func<String, String>>(
                                input -> input.concat("2"),
                                input -> input.replaceAll("a", "b")
                            ),
                            String::trim
                        ),
                        new IterableOf<>("private", "static", "String")
                    )
                )
            ),
            new HasValue<>(2L)
        ).affirm();
    }
}
