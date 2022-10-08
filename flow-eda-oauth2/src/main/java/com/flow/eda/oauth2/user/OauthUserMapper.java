package com.flow.eda.oauth2.user;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface OauthUserMapper {

    @Insert(
            "INSERT INTO oauth_user(`username`, `password`, `client_id`, `authorities`, `phone`, `email`, `register_ip`, `status`, `create_date`, `update_date`) "
                    + "VALUES(#{username}, #{password}, #{clientId}, #{authorities}, #{phone}, #{email}, #{registerIp}, #{status}, #{createDate}, #{updateDate})")
    void insert(OauthUser oauthUser);

    @Select("SELECT * FROM oauth_user WHERE username=#{username} AND client_id=#{clientId} AND status=1 LIMIT 1")
    OauthUser loadUserByUsername(String username, String clientId);

    @Select("SELECT username FROM oauth_user WHERE username=#{username} AND status=1 LIMIT 1")
    Object existUsername(String username);

    @Update("UPDATE oauth_user SET username = #{username} , email = #{email} where username = #{username}")
    void update(OauthUser oauthUser);
}
