package com.flow.eda.common.http;

import lombok.Getter;
import lombok.Setter;
import org.springframework.util.Assert;

import java.util.List;

@Getter
@Setter
public class PageResult<T> extends Result<List<T>> {
    private long total;
    private int page;
    private int limit;

    public static <T> PageResult<T> of(List<T> result) {
        Assert.notNull(result, "result must not be null!");
        PageResult<T> pageResult = new PageResult<>();
        pageResult.setResult(result);
        return pageResult;
    }

    public static <T> PageResult<T> of(long total, int page, List<T> result) {
        PageResult<T> pageResult = PageResult.of(result);
        pageResult.setTotal(total);
        pageResult.setPage(page);
        pageResult.setLimit(result.size());
        return pageResult;
    }
}
