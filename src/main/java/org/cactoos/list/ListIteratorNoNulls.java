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

package org.cactoos.list;

import java.util.ListIterator;

/**
 * A decorator of {@link ListIterator} that tolerates no NULLs.
 *
 * <p>There is no thread-safety guarantee.</p>
 *
 * @param <T> Element type
 * @since 0.39
 */
public final class ListIteratorNoNulls<T> implements ListIterator<T> {

    /**
     * ListIterator.
     */
    private final ListIterator<T> listiterator;

    /**
     * Ctor.
     *
     * @param src List iterator.
     */
    public ListIteratorNoNulls(final ListIterator<T> src) {
        this.listiterator = src;
    }

    @Override
    public boolean hasNext() {
        return this.listiterator.hasNext();
    }

    @Override
    public T next() {
        final T next = this.listiterator.next();
        if (next == null) {
            throw new IllegalStateException("Next item is NULL");
        }
        return next;
    }

    @Override
    public boolean hasPrevious() {
        return this.listiterator.hasPrevious();
    }

    @Override
    public T previous() {
        final T prev = this.listiterator.previous();
        if (prev == null) {
            throw new IllegalStateException("Previous item is NULL");
        }
        return prev;
    }

    @Override
    public int nextIndex() {
        return this.listiterator.nextIndex();
    }

    @Override
    public int previousIndex() {
        return this.listiterator.previousIndex();
    }

    @Override
    public void remove() {
        this.listiterator.remove();
    }

    @Override
    public void set(final T item) {
        if (item == null) {
            throw new IllegalArgumentException(
                "Item can't be NULL in #set(T)"
            );
        }
        this.listiterator.set(item);
    }

    @Override
    public void add(final T item) {
        if (item == null) {
            throw new IllegalArgumentException(
                "Item can't be NULL in #add(T)"
            );
        }
        this.listiterator.add(item);
    }

}
