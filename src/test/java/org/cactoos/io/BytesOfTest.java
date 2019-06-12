/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2017-2019 Yegor Bugayenko
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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicBoolean;
import org.cactoos.Text;
import org.cactoos.iterable.Endless;
import org.cactoos.iterable.HeadOf;
import org.cactoos.text.Joined;
import org.cactoos.text.TextOf;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.llorllale.cactoos.matchers.MatcherOf;
import org.llorllale.cactoos.matchers.TextHasString;

/**
 * Test case for {@link BytesOf}.
 *
 * @since 0.12
 * @checkstyle JavadocMethodCheck (500 lines)
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 */
public final class BytesOfTest {

    @Test
    public void readsLargeInMemoryContent() throws Exception {
        final int multiplier = 5_000;
        final String body = "1234567890";
        MatcherAssert.assertThat(
            "Can't read large content from in-memory Input",
            new BytesOf(
                new InputOf(
                    new Joined(
                        "",
                        new HeadOf<>(
                            multiplier, new Endless<>(body)
                        )
                    )
                )
            ).asBytes().length,
            Matchers.equalTo(body.length() * multiplier)
        );
    }

    @Test
    public void readsInputIntoBytes() throws Exception {
        MatcherAssert.assertThat(
            "Can't read bytes from Input",
            new String(
                new BytesOf(
                    new InputOf("Hello, друг!")
                ).asBytes(),
                StandardCharsets.UTF_8
            ),
            Matchers.allOf(
                Matchers.startsWith("Hello, "),
                Matchers.endsWith("друг!")
            )
        );
    }

    @Test
    public void readsFromReader() throws Exception {
        final String source = "hello, друг!";
        MatcherAssert.assertThat(
            "Can't read string through a reader",
            new TextOf(
                new BytesOf(
                    new StringReader(source),
                    StandardCharsets.UTF_8,
                    // @checkstyle MagicNumberCheck (1 line)
                    16 << 10
                )
            ).asString(),
            Matchers.equalTo(source)
        );
    }

    @Test
    public void readsInputIntoBytesWithSmallBuffer() throws Exception {
        MatcherAssert.assertThat(
            "Can't read bytes from Input with a small reading buffer",
            new String(
                new BytesOf(
                    new InputOf(
                        new TextOf("Hello, товарищ!")
                    ),
                    2
                ).asBytes(),
                StandardCharsets.UTF_8
            ),
            Matchers.allOf(
                Matchers.startsWith("Hello,"),
                Matchers.endsWith("товарищ!")
            )
        );
    }

    @Test
    public void closesInputStream() throws Exception {
        final AtomicBoolean closed = new AtomicBoolean();
        final InputStream input = new ByteArrayInputStream(
            "how are you?".getBytes()
        );
        MatcherAssert.assertThat(
            "Can't close InputStream correctly",
            new TextOf(
                new InputOf(
                    new InputStream() {
                        @Override
                        public int read() throws IOException {
                            return input.read();
                        }
                        @Override
                        public void close() throws IOException {
                            input.close();
                            closed.set(true);
                        }
                    }
                ),
                StandardCharsets.UTF_8
            ).asString(),
            new MatcherOf<>(
                text -> {
                    return closed.get();
                }
            )
        );
    }

    @Test
    public void asBytes() throws Exception {
        final Text text = new TextOf("Hello!");
        MatcherAssert.assertThat(
            "Can't convert text into bytes",
            new BytesOf(
                new InputOf(text)
            ).asBytes(),
            Matchers.equalTo(
                new BytesOf(text.asString()).asBytes()
            )
        );
    }

    @Test
    public void printsStackTrace() {
        MatcherAssert.assertThat(
            "Can't print exception stacktrace",
            new TextOf(
                new BytesOf(
                    new IOException(
                        "It doesn't work at all"
                    )
                )
            ),
            new TextHasString(
                new Joined(
                    System.lineSeparator(),
                    "java.io.IOException: It doesn't work at all",
                    "\tat org.cactoos.io.BytesOfTest"
                )
            )
        );
    }

    @Test
    public void printsStackTraceFromArray() {
        MatcherAssert.assertThat(
            "Can't print exception stacktrace from array",
            new TextOf(
                new BytesOf(
                    new IOException("").getStackTrace()
                )
            ),
            new TextHasString("org.cactoos.io.BytesOfTest")
        );
    }

}
