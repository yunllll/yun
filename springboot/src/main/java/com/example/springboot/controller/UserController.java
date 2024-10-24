package com.example.springboot.controller;

import com.example.springboot.common.Page;
import com.example.springboot.common.Result;
import com.example.springboot.entity.User;
import com.example.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.yaml.snakeyaml.constructor.DuplicateKeyException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Rabbit
 **/
@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;
//    增
    @PostMapping("/add")
    public Result add(@RequestBody User user) {
        try {
            userService.insertUser(user);
        } catch(Exception e){
            if(e instanceof DuplicateKeyException) {
                return Result.error("插入数据库错误");
            }
            else{
                return Result.error("系统错误");
            }
        }
        return Result.success();
    }
//改
    @PutMapping("/update")
    public Result update(@RequestBody User user) {
        userService.updateUser(user);
        return Result.success();
    }
//删一个
    @DeleteMapping("/delete{id}")
    public Result delete(@PathVariable Integer id) {
        userService.deleteUser(id);
        return Result.success();
    }
//删很多个
    @DeleteMapping("/delete")
    public Result delete(@RequestBody List<Integer> ids) {
        userService.deleteUsers(ids);
        return Result.success();
    }
//查所有
    @GetMapping("/inquire")
    public Result inquire() {
        List<User> userList = userService.inquireUser();
        return Result.success(userList);
    }
//单条件查一个
    @GetMapping("/seleteById/{id}")
    public Result selectById(@PathVariable Integer id) {
        User user = userService.selectById(id);
        return Result.success(user);
    }

    @GetMapping("/seleteByName/{username}")
    public Result selectByName(@PathVariable String username) {
        List <User> userList = userService.selectByName(username);
        return Result.success(userList);
    }
//多条件查询
    @GetMapping("/seleteByMore")
    public Result selectByMore(@RequestParam String username,@RequestParam Integer age) {
        List <User> userList = userService.selectByMore(username,age);
        return Result.success(userList);
    }

    //模糊查询
    @GetMapping("/seleteByBlur")
    public Result selectByBlur(@RequestParam String username,@RequestParam Integer age) {
        List <User> userList = userService.selectByBlur(username,age);
        return Result.success(userList);
    }

    //模糊分页查询
    @GetMapping("/seleteByPage")
    public Result selectByPage(@RequestParam Integer pageNum,@RequestParam Integer pageSize,@RequestParam String username,@RequestParam Integer age) {
        Page<User> page = userService.selectByPage(pageNum,pageSize,username,age);
        return Result.success(page);
    }
}
