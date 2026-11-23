package vn.edu.uet.cinema.business;

import java.util.List;
import java.util.function.Function;

public record PageResult<T>(
        List<T> items,
        int page,
        int size,
        long totalItems,
        int totalPages) {

    public static final int DEFAULT_PAGE = 1;
    public static final int DEFAULT_SIZE = 20;
    public static final int MAX_SIZE = 100;

    public static int normalizePage(int page) {
        return Math.max(page, DEFAULT_PAGE);
    }

    public static int normalizeSize(int size) {
        if (size < 1) {
            return DEFAULT_SIZE;
        }
        return Math.min(size, MAX_SIZE);
    }

    public static <T> PageResult<T> of(List<T> items, int page, int size, long totalItems) {
        int totalPages = size > 0 ? (int) Math.ceil((double) totalItems / size) : 0;
        return new PageResult<>(items, page, size, totalItems, totalPages);
    }

    public <U> PageResult<U> map(Function<? super T, ? extends U> converter) {
        List<U> mapped = items.stream().<U>map(converter).toList();
        return new PageResult<>(mapped, page, size, totalItems, totalPages);
    }
}