package com.flow.eda.web.http;

import com.flow.eda.common.http.Result;
import com.github.pagehelper.Page;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.Assert;

import java.util.List;

@Getter
@Setter
public class PageResult<T> extends Result<List<T>> {
    /** 总条数 */
    private long total;
    /** 页数，从1开始 */
    private int page;
    /** 每页限制条数 */
    private int limit;

    @SuppressWarnings("rawtypes")
    public static <T> PageResult<T> of(List<T> result) {
        Assert.notNull(result, "result must not be null!");
        PageResult<T> pageResult = new PageResult<>();
        pageResult.setResult(result);
        pageResult.setPage(((Page) result).getPageNum());
        pageResult.setLimit(((Page) result).getPageSize());
        pageResult.setTotal(((Page) result).getTotal());
        return pageResult;
    }

    /** 逻辑分页 */
    public static <T> PageResult<T> ofPage(List<T> result, PageRequest request) {
        Assert.notNull(result, "result must not be null!");
        PageResult<T> pageResult = new PageResult<>();
        pageResult.setPage(request.getPage());
        pageResult.setLimit(request.getLimit());
        pageResult.setTotal(result.size());
        if (result.isEmpty()) {
            pageResult.setResult(result);
            return pageResult;
        }
        // 逻辑分页
        int fromIndex = (pageResult.getPage() - 1) * pageResult.getLimit();
        int toIndex = fromIndex + pageResult.getLimit();
        if (toIndex > pageResult.getTotal()) {
            toIndex = (int) pageResult.getTotal();
        }
        pageResult.setResult(result.subList(fromIndex, toIndex));
        return pageResult;
    }
}
