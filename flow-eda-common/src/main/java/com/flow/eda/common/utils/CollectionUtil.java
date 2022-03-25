package com.flow.eda.common.utils;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CollectionUtil {

    public static boolean isEmpty(Collection<?> collection) {
        return (collection == null || collection.isEmpty());
    }

    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    public static <T, R> List<R> map(
            Collection<T> collection, Function<? super T, ? extends R> mapper) {
        if (isEmpty(collection)) {
            return null;
        } else {
            return collection.stream().map(mapper).collect(Collectors.toList());
        }
    }

    public static <T> T findFirst(Collection<T> collection, Predicate<? super T> predicate) {
        if (isEmpty(collection)) {
            return null;
        } else {
            return collection.stream().filter(predicate).findFirst().orElse(null);
        }
    }

    public static <T> void forEach(Collection<T> collection, Consumer<? super T> action) {
        if (isNotEmpty(collection)) {
            collection.forEach(action);
        }
    }

    public static <T> List<T> filter(Collection<T> collection, Predicate<? super T> predicate) {
        if (isNotEmpty(collection)) {
            return collection.stream().filter(predicate).collect(Collectors.toList());
        }
        return null;
    }

    public static <T, R> List<R> filterMap(
            Collection<T> collection,
            Predicate<? super T> predicate,
            Function<? super T, ? extends R> mapper) {
        if (isNotEmpty(collection)) {
            return collection.stream().filter(predicate).map(mapper).collect(Collectors.toList());
        }
        return null;
    }

    public static <T> List<T> distinct(Collection<T> collection) {
        if (isNotEmpty(collection)) {
            return collection.stream().distinct().collect(Collectors.toList());
        }
        return null;
    }
}
