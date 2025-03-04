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

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.cactoos.Scalar;
import org.cactoos.func.FuncOf;
import org.cactoos.iterable.IterableOf;
import org.cactoos.iterable.Mapped;
import org.cactoos.list.ListOf;
import org.cactoos.list.Synced;
import org.cactoos.proc.ProcNoNulls;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.Assertion;
import org.llorllale.cactoos.matchers.HasValue;

/**
 * Test case for {@link AndInThreads}.
 * @since 0.25
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 */
@SuppressWarnings({"PMD.TooManyMethods", "PMD.AvoidDuplicateLiterals", "unchecked"})
final class AndInThreadsTest {

    @Test
    void allTrue() throws Exception {
        new Assertion<>(
            "Each object must be True",
            new AndInThreads(
                new True(),
                new True(),
                new True()
            ),
            new HasValue<>(true)
        ).affirm();
    }

    @Test
    void oneFalse() throws Exception {
        new Assertion<>(
            "One object must be False",
            new AndInThreads(
                new True(),
                new False(),
                new True()
            ),
            new HasValue<>(false)
        ).affirm();
    }

    @Test
    void allFalse() throws Exception {
        new Assertion<>(
            "Each object must be False",
            new AndInThreads(
                new IterableOf<Scalar<Boolean>>(
                    new False(),
                    new False(),
                    new False()
                )
            ),
            new HasValue<>(false)
        ).affirm();
    }

    @Test
    void emptyIterable() throws Exception {
        new Assertion<>(
            "Must iterate over empty iterable",
            new AndInThreads(new IterableOf<>()),
            new HasValue<>(true)
        ).affirm();
    }

    @Test
    void iteratesList() {
        final List<String> list = new Synced<>(new ListOf<>());
        new Assertion<>(
            "Must iterate a list with a function",
            new AndInThreads(
                new Mapped<>(
                    new FuncOf<String, Scalar<Boolean>>(list::add, new True()),
                    new IterableOf<>("hello", "world")
                )
            ),
            new HasValue<>(true)
        ).affirm();
        new Assertion<>(
            "Iterable must contain elements in any order",
            list,
            new IsIterableContainingInAnyOrder<>(
                new ListOf<>(
                    new IsEqual<>("hello"),
                    new IsEqual<>("world")
                )
            )
        ).affirm();
    }

    @Test
    void worksWithFunc() throws Exception {
        MatcherAssert.assertThat(
            new AndInThreads(
                input -> input > 0,
                1, -1, 0
            ),
            new HasValue<>(false)
        );
    }

    @Test
    void worksWithIterableScalarBoolean() throws Exception {
        MatcherAssert.assertThat(
            new AndInThreads(
                new ListOf<Scalar<Boolean>>(
                    new Constant<>(true),
                    new Constant<>(true)
                )
            ).value(),
            Matchers.equalTo(true)
        );
    }

    @Test
    void worksWithExecServiceProcValues() throws Exception {
        final List<Integer> list = new Synced<>(new ListOf<>());
        final ExecutorService service = Executors.newSingleThreadExecutor();
        new AndInThreads(
            service,
            new ProcNoNulls<Integer>(list::add),
            1, 2
        ).value();
        MatcherAssert.assertThat(
            list,
            new IsIterableContainingInAnyOrder<>(
                new ListOf<>(
                    new IsEqual<>(1),
                    new IsEqual<>(2)
                )
            )
        );
    }

    @Test
    void worksWithExecServiceProcIterable() throws Exception {
        final List<Integer> list = new Synced<>(new ListOf<>());
        final ExecutorService service = Executors.newSingleThreadExecutor();
        new AndInThreads(
            service,
            new ProcNoNulls<Integer>(list::add),
            new ListOf<>(1, 2)
        ).value();
        MatcherAssert.assertThat(
            list,
            new IsIterableContainingInAnyOrder<>(
                new ListOf<>(
                    new IsEqual<>(1),
                    new IsEqual<>(2)
                )
            )
        );
    }

    @Test
    void worksWithExecServiceScalarBooleans() throws Exception {
        MatcherAssert.assertThat(
            new AndInThreads(
                Executors.newSingleThreadExecutor(),
                new Constant<>(false),
                new Constant<>(false)
            ).value(),
            Matchers.equalTo(false)
        );
    }

    @Test
    void worksWithExecServiceIterableScalarBoolean() throws Exception {
        MatcherAssert.assertThat(
            new AndInThreads(
                Executors.newSingleThreadExecutor(),
                new ListOf<Scalar<Boolean>>(
                    new Constant<>(true),
                    new Constant<>(false)
                )
            ).value(),
            Matchers.equalTo(false)
        );
    }

    @Test
    void worksWithEmptyIterableScalarBoolean() throws Exception {
        MatcherAssert.assertThat(
            new AndInThreads(
                new ListOf<>()
            ).value(),
            Matchers.equalTo(true)
        );
    }

}
