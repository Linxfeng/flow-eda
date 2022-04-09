package com.flow.eda.common.utils;

import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MergeBuilder {

    public static <T> MergeSourceBuilder<T> source(List<T> source) {
        return new MergeSourceBuilder<>(source);
    }

    public static <T, K> MergeSourceKeyBuilder<T, K> source(
            List<T> source, Function<T, K> extractor) {
        return new MergeSourceKeyBuilder<>(source, extractor);
    }

    @AllArgsConstructor
    public static class MergeSourceBuilder<T> {
        private final List<T> source;

        public <K> MergeSourceKeyBuilder<T, K> sourceKeyExtractor(Function<T, K> extractor) {
            return new MergeSourceKeyBuilder<>(source, extractor);
        }
    }

    @AllArgsConstructor
    public static class MergeSourceKeyBuilder<T, K> {
        private final List<T> source;
        private final Function<T, K> sourceKeyExtractor;

        public <R> TargetSourceBuilder<T, K, R> target(Function<List<K>, List<R>> supplier) {
            List<K> keys =
                    source.stream().map(sourceKeyExtractor).distinct().collect(Collectors.toList());
            List<R> target = supplier.apply(keys);
            return new TargetSourceBuilder<>(source, sourceKeyExtractor, target);
        }

        public <R> TargetSourceBuilder<T, K, R> target(List<R> target) {
            return new TargetSourceBuilder<>(source, sourceKeyExtractor, target);
        }

        public <R> Merger<T, R> target(
                Function<List<K>, List<R>> supplier, Function<R, ?> extractor) {
            List<K> keys =
                    source.stream().map(sourceKeyExtractor).distinct().collect(Collectors.toList());
            List<R> target = supplier.apply(keys);
            return new Merger<T, R>(source, target, sourceKeyExtractor, extractor);
        }

        public <R> Merger<T, R> target(List<R> target, Function<R, ?> extractor) {
            return new Merger<T, R>(source, target, sourceKeyExtractor, extractor);
        }
    }

    @AllArgsConstructor
    public static class TargetSourceBuilder<T, K, R> {
        private final List<T> source;
        private final Function<T, K> sourceKeyExtractor;
        private final List<R> target;

        public Merger<T, R> targetKeyExtractor(Function<R, ?> extractor) {
            return new Merger<T, R>(source, target, sourceKeyExtractor, extractor);
        }
    }

    @AllArgsConstructor
    public static class Merger<T, R> {
        private final List<T> source;
        private final List<R> target;
        private final Function<T, ?> sourceKey;
        private final Function<R, ?> targetKey;

        public List<R> mergeT(BiConsumer<T, R> action) {
            if (source == null || target == null) {
                return target;
            }
            target.stream()
                    .filter(Objects::nonNull)
                    .forEach(
                            t ->
                                    source.stream()
                                            .filter(Objects::nonNull)
                                            .filter(
                                                    s ->
                                                            Objects.equals(
                                                                    sourceKey.apply(s),
                                                                    targetKey.apply(t)))
                                            .forEach(s -> action.accept(s, t)));
            return target;
        }

        public List<T> mergeS(BiConsumer<T, R> action) {
            if (target != null) {
                mergeT(action);
            }
            return source;
        }
    }
}
