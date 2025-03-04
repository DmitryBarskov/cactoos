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
package org.cactoos.iterator;

import java.util.Objects;
import org.cactoos.list.ListOf;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.Assertion;
import org.llorllale.cactoos.matchers.HasValues;
import org.llorllale.cactoos.matchers.Throws;

/**
 * Test case for {@link Mapped}.
 * @since 0.47
 * @checkstyle MagicNumberCheck (500 lines)
 */
final class MatchedTest {
    @Test
    void failsWhenElementsNotMatch() {
        new Assertion<>(
            "All elements have correlation function 'equals'",
            () -> new ListOf<>(
                new Matched<>(
                    Objects::equals,
                    new IteratorOf<>(1),
                    new IteratorOf<>(0)
                )
            ),
            new Throws<>(
                new IsEqual<>(
                    "There is no correlation between `1` and `0`."
                ),
                IllegalStateException.class
            )
        ).affirm();
    }

    @Test
    void failsOnSizeMismatch() {
        new Assertion<>(
            "must fail if sizes of iterators are different",
            () -> new ListOf<>(
                new Matched<>(
                    Objects::equals,
                    new IteratorOf<>(1),
                    new IteratorOf<>()
                )
            ),
            new Throws<>(
                new IsEqual<>("Size mismatch of iterators"),
                IllegalStateException.class
            )
        ).affirm();
    }

    @Test
    void shouldProduceValuesOfFirstIterator() {
        new Assertion<>(
            "must match all items of first iterator",
            new ListOf<>(
                new Matched<>(
                    (Number first, Number second) -> first.intValue() == second.intValue(),
                    new IteratorOf<>(1f, 2f, 3f),
                    new IteratorOf<>(1L, 2L, 3L)
                )
            ),
            new HasValues<>(1f, 2f, 3f)
        ).affirm();
    }

}
