package com.flow.eda.oauth2.user;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface OauthUserMapper {

    @Select(
            "SELECT * FROM oauth_user WHERE username=#{username} AND client_id=#{clientId} AND status=1 LIMIT 1")
    OauthUser loadUserByUsername(String username, String clientId);
}
