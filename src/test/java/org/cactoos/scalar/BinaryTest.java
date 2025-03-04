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

import java.util.concurrent.atomic.AtomicInteger;
import org.hamcrest.core.IsEqual;
import org.junit.Test;
import org.llorllale.cactoos.matchers.Assertion;
import org.llorllale.cactoos.matchers.HasValue;
import org.llorllale.cactoos.matchers.Throws;

/**
 * Test case for {@link Binary}.
 * @since 1.0
 * @checkstyle JavadocMethodCheck (500 lines)
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 */
public final class BinaryTest {

    @Test
    public void conditionTrue() {
        final AtomicInteger counter = new AtomicInteger(0);
        final Binary binary = new Binary(
            new True(),
            counter::incrementAndGet
        );
        new Assertion<>(
            "Binary has to return true",
            binary,
            new HasValue<>(true)
        ).affirm();
        final int expected = 1;
        new Assertion<>(
            "Binary has to invoke increment method",
            counter.get(),
            new IsEqual<>(expected)
        ).affirm();
    }

    @Test
    public void conditionFalse() {
        final AtomicInteger counter = new AtomicInteger(0);
        final Binary binary = new Binary(
            new False(),
            counter::incrementAndGet
        );
        new Assertion<>(
            "Binary has to return false",
            binary,
            new HasValue<>(false)
        ).affirm();
        final int expected = 0;
        new Assertion<>(
            "Binary must not to invoke increment method",
            counter.get(),
            new IsEqual<>(expected)
        ).affirm();
    }

    @Test
    public void throwsException() {
        final String msg = "Custom exception message";
        final Binary binary = new Binary(
            new True(),
            () -> {
                throw new IllegalArgumentException(msg);
            }
        );
        new Assertion<>(
            "Binary has to throw exception",
            binary,
            new Throws<>(msg, IllegalArgumentException.class)
        ).affirm();
    }
}
