package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishFlavorMapper {

    /**
     * 插入菜品口味
     * @param flavors
     */
    void insertDishFlavor(List<DishFlavor> flavors);

    /**
     * 根据dishIds删除菜品
     * @param dishIds
     */
    void deleteByDishIds(List dishIds);

    /**
     * 按照Id查询菜品信息
     * @param dishId
     * @return
     */
    @Select("select * from dish_flavor where dish_id = #{dishId}")
    List<DishFlavor> getFlavorByDishId(Long dishId);
}
