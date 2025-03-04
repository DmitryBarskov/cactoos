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

import java.util.LinkedList;
import java.util.List;
import org.cactoos.Proc;
import org.cactoos.Scalar;
import org.cactoos.iterable.IterableOf;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.HasValue;

/**
 * Test case for {@link Or}.
 *
 * @since 0.8
 * @checkstyle JavadocMethodCheck (500 lines)
 * @checkstyle MagicNumber (500 line)
 */
@SuppressWarnings("PMD.TooManyMethods")
final class OrTest {

    @Test
    void allFalse() throws Exception {
        MatcherAssert.assertThat(
            new Or(
                new False(),
                new False(),
                new False(),
                new False(),
                new False()
            ),
            new HasValue<>(false)
        );
    }

    @Test
    void oneTrue() throws Exception {
        MatcherAssert.assertThat(
            new Or(
                new False(),
                new True(),
                new False(),
                new False(),
                new False()
            ),
            new HasValue<>(true)
        );
    }

    @Test
    void allTrue() throws Exception {
        MatcherAssert.assertThat(
            new Or(
                new IterableOf<Scalar<Boolean>>(
                    new True(),
                    new True(),
                    new True(),
                    new True(),
                    new True()
                )
            ),
            new HasValue<>(true)
        );
    }

    @Test
    void emptyIterator() throws Exception {
        MatcherAssert.assertThat(
            new Or(new IterableOf<Scalar<Boolean>>()),
            new HasValue<>(false)
        );
    }

    @Test
    void testProcIterable() throws Exception {
        final List<Integer> list = new LinkedList<>();
        new Or(
            (Proc<Integer>) list::add,
            new IterableOf<>(1, 2, 3, 4)
        ).value();
        MatcherAssert.assertThat(
            list,
            Matchers.contains(1, 2, 3, 4)
        );
    }

    @Test
    void testProcIterator() throws Exception {
        final List<Integer> list = new LinkedList<>();
        new Or(
            (Proc<Integer>) list::add,
            new IterableOf<>(1, 2, 3, 4)
        ).value();
        MatcherAssert.assertThat(
            list,
            Matchers.contains(1, 2, 3, 4)
        );
    }

    @Test
    void testProcVarargs() throws Exception {
        final List<Integer> list = new LinkedList<>();
        new Or(
            (Proc<Integer>) list::add,
            2, 3, 4
        ).value();
        MatcherAssert.assertThat(
            list,
            Matchers.contains(2, 3, 4)
        );
    }

    @Test
    void testFuncIterable() throws Exception {
        MatcherAssert.assertThat(
            new Or(
                input -> input > 0,
                new IterableOf<>(-1, 1, 0)
            ),
            new HasValue<>(true)
        );
    }

    @Test
    void testFuncIterator() throws Exception {
        MatcherAssert.assertThat(
            new Or(
                input -> input > 0,
                new IterableOf<>(-1, 1, 0)
            ),
            new HasValue<>(true)
        );
    }

    @Test
    void testFuncVarargs() throws Exception {
        MatcherAssert.assertThat(
            new Or(
                input -> input > 0,
                -1, -2, 0
            ),
            new HasValue<>(false)
        );
    }

    @Test
    void testMultipleFuncConditionTrue() throws Exception {
        MatcherAssert.assertThat(
            "Can't compare subject with true conditions",
            new Or(
                3,
                input -> input > 0,
                input -> input > 5,
                input -> input > 4
            ),
            new HasValue<>(true)
        );
    }

    @Test
    void testMultipleFuncConditionFalse() throws Exception {
        MatcherAssert.assertThat(
            "Can't compare subject with false conditions",
            new Or(
                "cactoos",
                input -> input.contains("singleton"),
                input -> input.contains("static")
            ),
            new HasValue<>(false)
        );
    }
}
