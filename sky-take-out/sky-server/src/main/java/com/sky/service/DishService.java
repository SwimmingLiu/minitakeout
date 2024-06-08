package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService {
    void addDish(DishDTO dishDTO);

    PageResult getDishs(DishPageQueryDTO dishPageQueryDTO);

    void updateStatus(Long id, Integer status);

    void deleteDishByIds(List ids);

    DishVO getDishById(Long id);

    void updateDish(DishDTO dishDTO);

    List<Dish> getDishByCategoryId(Long categoryId);
}
