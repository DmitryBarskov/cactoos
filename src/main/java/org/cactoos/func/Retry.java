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

import java.time.Duration;
import org.cactoos.Func;

/**
 * Func that will try a few times before throwing an exception.
 *
 * <p>There is no thread-safety guarantee.
 *
 * @param <X> Type of input
 * @param <Y> Type of output
 * @since 0.8
 */
public final class Retry<X, Y> implements Func<X, Y> {

    /**
     * Original func.
     */
    private final Func<X, Y> func;

    /**
     * Exit condition.
     */
    private final Func<Integer, Boolean> exit;

    /**
     * Wait between executions.
     */
    private final Duration wait;

    /**
     * Ctor.
     * @param fnc Func original
     */
    public Retry(final Func<X, Y> fnc) {
        // @checkstyle MagicNumberCheck (1 line)
        this(fnc, 3);
    }

    /**
     * Ctor.
     * @param fnc Func original
     * @param attempts Maximum number of attempts
     */
    public Retry(final Func<X, Y> fnc, final int attempts) {
        this(fnc, attempts, Duration.ZERO);
    }

    /**
     * Ctor.
     *
     * @param fnc Func original
     * @param attempts Maximum number of attempts
     * @param wait The executions of the function
     */
    public Retry(final Func<X, Y> fnc, final int attempts,
        final Duration wait) {
        this(fnc, attempt -> attempt >= attempts, wait);
    }

    /**
     * Ctor.
     *
     * @param fnc Func original
     * @param ext Exit condition, returns TRUE if there is no more reason to try
     */
    public Retry(final Func<X, Y> fnc, final Func<Integer, Boolean> ext) {
        this(fnc, ext, Duration.ZERO);
    }

    /**
     * Ctor.
     *
     * @param fnc Func original
     * @param ext Exit condition, returns TRUE if there is no more reason to try
     * @param wait The executions of the function
     */
    public Retry(final Func<X, Y> fnc, final Func<Integer, Boolean> ext,
        final Duration wait) {
        this.func = fnc;
        this.exit = ext;
        this.wait = wait;
    }

    @Override
    @SuppressWarnings("PMD.AvoidCatchingGenericException")
    public Y apply(final X input) throws Exception {
        int attempt = 0;
        Exception error = new IllegalArgumentException(
            "An immediate exit, didn't have a chance to try at least once"
        );
        while (!this.exit.apply(attempt)) {
            try {
                return this.func.apply(input);
            } catch (final InterruptedException ex) {
                Thread.currentThread().interrupt();
                error = ex;
                break;
                // @checkstyle IllegalCatchCheck (1 line)
            } catch (final Exception ex) {
                error = ex;
            }
            if (!this.wait.isZero() && !this.wait.isNegative()) {
                try {
                    Thread.sleep(this.wait.toMillis());
                } catch (final InterruptedException ex) {
                    error = ex;
                    break;
                }
            }
            ++attempt;
        }
        throw error;
    }

}
