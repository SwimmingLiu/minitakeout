package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealDishMapper {


    List<SetmealDish> getSetMealDishIdsByDishIds(List ids);

    void insertSetmealDish(List<SetmealDish> setmealDishes);

    void deleteSetmealDishBySetmealId(List<Long> setmealIds);

    List<SetmealDish> getsetMealDishBySetmealId(List<Long> SetmealIds);

}
