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
package org.cactoos.scalar;

import org.cactoos.iterable.IterableOf;
import org.cactoos.iterable.IterableOfInts;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.Assertion;
import org.llorllale.cactoos.matchers.HasValue;
import org.llorllale.cactoos.matchers.Throws;

/**
 * Tests for {@link FirstOf}.
 *
 * @since 0.32
 * @checkstyle JavadocMethodCheck (500 lines)
 */
final class FirstOfTest {

    @Test
    void returnsMatchingValue() {
        final int value = 1;
        new Assertion<>(
            "Must return the only matching element",
            new FirstOf<>(
                i -> i >= value,
                new IterableOfInts(0, value),
                () -> -1
            ),
            new HasValue<>(value)
        ).affirm();
    }

    @Test
    void returnsMatchingValueWithExceptionalFallback() {
        final int value = 2;
        new Assertion<>(
            "Exception was not thrown",
            new FirstOf<>(
                i -> i >= value,
                new IterableOfInts(0, value),
                () -> {
                    throw new IllegalArgumentException(
                        "This exception should not be thrown"
                    );
                }
            ),
            new HasValue<>(value)
        ).affirm();
    }

    @Test
    void returnsFirstValueForMultipleMatchingOnes() {
        final String value = "1";
        new Assertion<>(
            "Must return first matching element",
            new FirstOf<>(
                i -> !i.isEmpty(),
                new IterableOf<>("1", "2"),
                () -> ""
            ),
            new HasValue<>(value)
        ).affirm();
    }

    @Test
    void returnsFallbackIfNothingMatches() {
        final String value = "abc";
        new Assertion<>(
            "Must return fallback",
            new FirstOf<>(
                i -> i.length() > 2,
                new IterableOf<>("ab", "cd"),
                () -> value
            ),
            new HasValue<>(value)
        ).affirm();
    }

    @Test
    void throwsFallbackIfNothingMatches() {
        new Assertion<>(
            "Fallback was not thrown",
            new FirstOf<>(
                num -> num.equals(0),
                // @checkstyle MagicNumber (10 lines)
                new IterableOf<>(
                    1, 2, 3, 4, 5
                ),
                () -> {
                    throw new IllegalArgumentException(
                        String.format("Unable to found element with id %d", 0)
                    );
                }
            ),
            new Throws<>(IllegalArgumentException.class)
        ).affirm();
    }

    @Test
    void returnsFirstValueWithScalarFallback() {
        new Assertion<>(
            "Returns first value with scalar fallback",
            new FirstOf<>(
                new IterableOfInts(2, 1, 0),
                () -> -1
            ),
            new HasValue<>(2)
        ).affirm();
    }

    @Test
    void returnsFirstValueWithIntegerFallback() {
        new Assertion<>(
            "Returns first value with Integer fallback",
            new FirstOf<>(
                new IterableOfInts(2, 1, 0),
                -1
            ),
            new HasValue<>(2)
        ).affirm();
    }

    @Test
    void returnsFallbackWhenIterableEmpty() {
        new Assertion<>(
            "Returns fallback when iterable empty",
            new FirstOf<>(
                new IterableOf<>(),
                () -> -1
            ),
            new HasValue<>(-1)
        ).affirm();
    }
}
