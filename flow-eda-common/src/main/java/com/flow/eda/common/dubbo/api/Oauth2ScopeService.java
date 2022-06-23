package com.flow.eda.common.dubbo.api;

/** oauth2权限认证服务 */
public interface Oauth2ScopeService {

    /**
     * 校验该token是否有对应的API访问权限
     *
     * @param token 用户token
     * @param uri 请求url
     * @return 返回是否有对应权限
     */
    boolean verifyPermissions(String token, String uri);
}
