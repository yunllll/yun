package com.example.springboot.mapper;

import com.example.springboot.entity.User;
import org.apache.ibatis.annotations.*;
;import java.util.List;

@Mapper
public interface UserMapper {
    @Insert("INSERT INTO `user` (username, age) VALUES (#{username}, #{age})")
    void insert(User user);

    @Update("UPDATE `user` set username=#{username}, age=#{age} where id = #{id}")
    void updateUser(User user);

    @Delete("DELETE from `user` where id = #{id}")
    void deleteUser(Integer id);

    @Select("SELECT * FROM `user`")
    List<User> inquireUser();

    @Select("SELECT * FROM `user` where id = #{id} Order by id asc ")
    User selectById(Integer id);

    @Select("SELECT * FROM `user` where username = #{username}")
    List <User> selectByName(String username);

    @Select("SELECT * FROM `user` where username = #{username} and age = #{age} order by age desc")
    List <User> selectByMore(@Param("username") String username, @Param("age") Integer age);

    @Select("SELECT * FROM `user` WHERE username LIKE CONCAT('%', #{username}, '%') AND age LIKE CONCAT('%', #{age}, '%') ORDER BY age DESC")
    List<User> selectByBlur(String username, Integer age);

    @Select("SELECT * FROM `user` WHERE username LIKE CONCAT('%', #{username}, '%') AND (age = #{age} OR #{age} IS NULL) ORDER BY id DESC LIMIT #{skipNum}, #{pageSize}")
    List<User> selectByPage(@Param("skipNum") Integer skipNum, @Param("pageSize") Integer pageSize, @Param("username") String username, @Param("age") Integer age);

}
