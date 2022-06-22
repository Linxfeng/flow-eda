package com.flow.eda.oauth2.user;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface OauthUserMapper {

    @Insert(
            "INSERT INTO oauth_user(`username`, `password`, `client_id`, `authorities`, `phone`, `email`, `status`, `create_date`, `update_date`) "
                    + "VALUES(#{username}, #{password}, #{clientId}, #{authorities}, #{phone}, #{email}, #{status}, #{createDate}, #{updateDate})")
    void insert(OauthUser oauthUser);

    @Select(
            "SELECT * FROM oauth_user WHERE username=#{username} AND client_id=#{clientId} AND status=1 LIMIT 1")
    OauthUser loadUserByUsername(String username, String clientId);

    @Select("SELECT id FROM oauth_user WHERE username=#{username} AND status=1 LIMIT 1")
    Object existUsername(String username);
}
