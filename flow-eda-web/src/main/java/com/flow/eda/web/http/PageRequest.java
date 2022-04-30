package com.flow.eda.web.http;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageRequest {
    /** 默认每页10条 */
    private int limit = 10;
    /** 默认从第1页开始 */
    private int page = 1;
}
