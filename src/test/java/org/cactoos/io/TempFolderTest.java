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

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import org.cactoos.iterable.IterableOf;
import org.cactoos.proc.ForEach;
import org.cactoos.proc.ProcOf;
import org.cactoos.text.Randomized;
import org.hamcrest.core.IsNot;
import org.junit.jupiter.api.Test;
import org.llorllale.cactoos.matchers.Assertion;
import org.llorllale.cactoos.matchers.IsTrue;

/**
 * Test case for {@link TempFolder}.
 *
 * @since 1.0
 * @checkstyle JavadocMethodCheck (500 lines)
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 */
final class TempFolderTest {

    @Test
    void createsDirectory() throws Exception {
        try (TempFolder folder = new TempFolder()) {
            final File dir = folder.value().toFile();
            new Assertion<>(
                "must create new directory",
                dir.exists() && dir.isDirectory(),
                new IsTrue()
            ).affirm();
        }
    }

    @Test
    void deletesDirectory() throws Exception {
        final TempFolder dir = new TempFolder(
            new Randomized('d', 'e', 'g').asString()
        );
        dir.close();
        new Assertion<>(
            "Can't delete folder while closing",
            !dir.value().toFile().exists(),
            new IsTrue()
        ).affirm();
    }

    @Test
    void deletesNonEmptyDirectory() throws Exception {
        final TempFolder temp = new TempFolder();
        final Path root = temp.value();
        new ForEach<>(
            new ProcOf<String>(
                name -> {
                    final Path dir = Files.createDirectories(
                        new File(root.toFile(), name).toPath()
                    );
                    new ForEach<>(
                        new ProcOf<String>(
                            filename -> {
                                new TempFile(
                                    () -> dir,
                                    filename,
                                    ""
                                ).value();
                            }
                        )
                    ).exec(
                        new IterableOf<>(
                            "file1.txt", "file2.txt", "file3.txt"
                        )
                    );
                }
            )
        ).exec(
            new IterableOf<>(
                "a", "b", "c", "d", "e"
            )
        );
        temp.close();
        new Assertion<>(
            "Can't delete not empty folder while closing",
            temp.value().toFile().exists(),
            new IsNot<>(new IsTrue())
        ).affirm();
    }

    @Test
    void createDirectoryWithDirectoriesAndFiles() throws Exception {
        final TempFolder temp = new TempFolder();
        final Path root = temp.value();
        new ForEach<>(
            new ProcOf<String>(
                name -> {
                    final Path dir = Files.createDirectories(
                        new File(root.toFile(), name).toPath()
                    );
                    new ForEach<>(
                        new ProcOf<String>(
                            filename -> {
                                new TempFile(
                                    () -> dir,
                                    filename,
                                    ""
                                ).value();
                            }
                        )
                    ).exec(
                        new IterableOf<>(
                            "1.txt", "2.txt", "3.txt"
                        )
                    );
                }
            )
        ).exec(
            new IterableOf<>(
                "1", "2", "3", "4", "5"
            )
        );
        new Assertion<>(
            "Directory contains files and sub directories",
            temp.value().toFile().exists(),
            new IsTrue()
        ).affirm();
        temp.close();
    }
}
