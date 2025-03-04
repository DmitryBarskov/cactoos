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
package org.cactoos.time;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.Assertion;
import org.llorllale.cactoos.matchers.HasValue;

/**
 * Tests for DateOf.
 * @since 1.0
 * @checkstyle JavadocMethodCheck (500 lines)
 * @checkstyle MagicNumberCheck (500 lines)
 */
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public class DateOfTest {

    @Test
    public final void testParsingIsoFormattedStringToDate() {
        new Assertion<>(
            "must parse a Date with default/ISO format.",
            new DateOf("2017-12-13T14:15:16.000000017Z"),
            new HasValue<>(
                Date.from(
                    LocalDateTime.of(
                        2017, 12, 13, 14, 15, 16, 17
                    ).toInstant(ZoneOffset.UTC)
                )
            )
        ).affirm();
    }

    @Test
    public final void testParsingCustomFormattedStringToDate() {
        new Assertion<>(
            "must parse a Date with custom format.",
            new DateOf(
                "2017-12-13 14:15:16.000000017",
                "yyyy-MM-dd HH:mm:ss.n"
            ),
            new HasValue<>(
                Date.from(
                    LocalDateTime.of(
                        2017, 12, 13, 14, 15, 16, 17
                    ).toInstant(ZoneOffset.UTC)
                )
            )
        ).affirm();
    }

    @Test
    public final void testParsingCustomFormattedStringWithoutTimeToDate() {
        new Assertion<>(
            "must parse a Date with custom format.",
            new DateOf(
                "2018-01-01",
                "yyyy-MM-dd"
            ),
            new HasValue<>(
                Date.from(
                    LocalDateTime.of(
                        2018, 01, 01, 0, 0, 0, 0
                    ).toInstant(ZoneOffset.UTC)
                )
            )
        ).affirm();
    }

    @Test
    public final void testParsingCustomFormatterStringToDate() {
        new Assertion<>(
            "must parse a Date with custom format.",
            new DateOf(
                "2017-12-13 14:15:16.000000017",
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.n")
            ),
            new HasValue<>(
                Date.from(
                    LocalDateTime.of(
                        2017, 12, 13, 14, 15, 16, 17
                    ).toInstant(ZoneOffset.UTC)
                )
            )
        ).affirm();
    }

}
