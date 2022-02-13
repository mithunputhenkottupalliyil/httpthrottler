package com.http.concurrentrequestor.core;


import java.util.Iterator;
import java.util.concurrent.*;

public class CompletionIterator<T> implements Iterable<T> {
    private int _count = 0;
    private final CompletionService<T> _completer;

    public CompletionIterator(ExecutorService executorService) {
        this._completer = new ExecutorCompletionService<>(executorService);
    }

    //submit sequential //todo concurrent submit
    public void submit(Callable<T> task) {
        _completer.submit(task);
        _count++;
    }


    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                return _count > 0;
            }

            @Override
            public T next() {
                Future<T> future;
                try {
                    future = _completer.take();
                    return future.get();
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException(ie);
                } catch (ExecutionException ee) {
                    return null;
                } finally {
                    _count--;
                }
            }
        };
    }
}
