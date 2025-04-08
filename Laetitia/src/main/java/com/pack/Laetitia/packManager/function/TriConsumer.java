package com.pack.Laetitia.packManager.function;


import java.util.Objects;
import java.util.function.Consumer;

@FunctionalInterface
public interface TriConsumer<T, U, V> {

    void accept(T t, U u, V v);

}
