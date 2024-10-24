package com.example.springboot.service;
import com.example.springboot.common.Page;
import com.example.springboot.entity.User;
import com.example.springboot.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Rabbit
 **/
@Service
public class UserService {

    @Autowired
    UserMapper userMapper;

    public void insertUser(User user) {
        userMapper.insert(user);
    }
    public void updateUser(User user) {
        userMapper.updateUser(user);
    }
    public void deleteUser(Integer id) {
        userMapper.deleteUser(id);
    }
    public void deleteUsers(List<Integer> ids) {
        for (Integer id : ids) {
            userMapper.deleteUser(id);
        }
    }
    public List<User> inquireUser() {
       return userMapper.inquireUser();
    }

    public User selectById(Integer id) {
        return userMapper.selectById(id);
    }

    public List<User> selectByName(String userName) {
        return userMapper.selectByName(userName);
    }

    public List<User> selectByMore(String userName, Integer id) {
        return userMapper.selectByMore(userName, id);
    }

    public List<User> selectByBlur(String userName, Integer id) {
        return userMapper.selectByBlur(userName, id);
    }

    public Page<User> selectByPage(Integer pageNum,Integer pageSize,String userName, Integer age) {
        Integer skipNum = (pageNum-1)*pageSize;
        Page<User> page = new Page<>();
        List <User> userList = userMapper.selectByPage(skipNum,pageSize,userName,age);
        page.setList(userList);
        page.setTotal(userList.size());
        return page;
    }
}
