package com.example.springboot.controller;

import com.example.springboot.common.Result;
import com.sun.net.httpserver.Authenticator;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Rabbit 提供接口返回数据
 **/

@RestController
public class WebController {
    @RequestMapping(method = RequestMethod.GET)
    public Result hello(String name) {

//        return Result.success("泥嚎");
        return Result.success("hello " + name);
    }
    @PostMapping("/post")
    public Result post(Obj obj) {
        return Result.success(obj);
    }
    @PutMapping("/put")
    public Result put(@RequestBody Obj obj) {
        return Result.success(obj);
    }
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        return Result.success(id);
    }
//    @DeleteMapping("/delete")
//    public Result delete(@RequestBody List<Integer> ids) {
//        return Result.success(ids);
//    }
}