package com.example.springboot.common;

import lombok.Data;

import java.util.List;

/**
 * Rabbit
 **/
@Data
public class Page<T> {
    private List<T> list;
    private int total;
}
