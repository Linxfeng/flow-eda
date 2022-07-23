package com.flow.eda.web.flow;

import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FlowMapper {

    @Select("SELECT * FROM eda_flow WHERE id=#{id}")
    Flow findById(String id);

    @Select(
            "<script>SELECT `id`,`name` FROM eda_flow WHERE id IN "
                    + "<foreach collection='ids' item='id' open='(' separator=',' close=')'>#{id}</foreach></script>")
    List<Flow> findByIds(List<String> ids);

    @Select(
            "<script>SELECT * FROM eda_flow WHERE 1=1 "
                    + "<if test='username!=null'>AND `username`=#{username}</if>"
                    + "<if test='status!=null'>AND `status`=#{status}</if>"
                    + "<if test='name!=null'> AND `name` LIKE '%${name}%'</if>"
                    + " ORDER BY create_date DESC</script>")
    List<Flow> findByRequest(FlowRequest request);

    @Select("SELECT id FROM eda_flow WHERE username=#{username}")
    List<String> findIdsByUser(String username);

    @Insert(
            "INSERT INTO eda_flow (`id`,`name`,description,username,`status`,create_date,update_date) "
                    + "VALUES(#{id},#{name},#{description},#{username},#{status},#{createDate},#{updateDate})")
    void insert(Flow flow);

    @Update(
            "<script>UPDATE eda_flow SET <if test='name!=null'>`name`=#{name},</if>"
                    + "<if test='description!=null'>description=#{description},</if>"
                    + "update_date=#{updateDate} WHERE id=#{id}</script>")
    void update(Flow flow);

    @Delete(
            "<script>DELETE FROM eda_flow WHERE id in <foreach collection='ids' item='id' open='(' separator=',' close=')'>#{id}</foreach></script>")
    void deleteByIds(List<String> ids);

    @Update("UPDATE eda_flow SET `status`=#{status} WHERE id=#{id}")
    void updateStatus(String id, String status);
}
