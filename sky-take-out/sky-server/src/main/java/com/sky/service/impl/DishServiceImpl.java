package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;

    /**
     * 添加菜品
     */
    @Override
    @Transactional
    public void addDish(DishDTO dishDTO) {
        // DishDTO转换成 Dish 和 DishFlavor
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        List<DishFlavor> flavors = dishDTO.getFlavors();
        // 1. 插入菜品
        dishMapper.insertDish(dish);
        long dishId = dish.getId();
        // 2. 判断是否有口味需求，有则插入口味
        if (flavors != null && flavors.size() > 0) {
            for (DishFlavor flavor : flavors) {
                flavor.setDishId(dishId);
            }
            dishFlavorMapper.insertDishFlavor(flavors);
        }
    }

    /**
     * 分页查询菜品
     *
     * @return
     */
    @Override
    public PageResult getDishs(DishPageQueryDTO dishPageQueryDTO) {
        // 设置PageSize
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        // 查询菜品信息
        Page<DishVO> result = dishMapper.getDishs(dishPageQueryDTO);
        return new PageResult(result.getTotal(), result.getResult());
    }

    /**
     * 修改售卖状态
     *
     * @param id
     * @param status
     */
    @Override
    public void updateStatus(Long id, Integer status) {
        // 构建Dish对象
        Dish dish = Dish.builder().
                id(id).
                status(status).build();
        // 直接修改整个Dish，方便复用代码
        dishMapper.update(dish);
    }

    /**
     * 批量删除菜品
     *
     * @param ids
     */
    @Override
    @Transactional
    public void deleteDishByIds(List ids) {
        // 判断是否存在在售的菜品，若存在则抛出异常
        List<Dish> dishs = dishMapper.getDishByIds(ids);
        for (Dish dish : dishs) {
            if (dish.getStatus() == StatusConstant.ENABLE) {
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }
        // 判断是否存在关联套餐，若存在则抛出异常
        List<SetmealDish> setmealIds = setmealDishMapper.getSetMealDishIdsByDishIds(ids);
        if (setmealIds != null && setmealIds.size() > 0) {
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }
        // 满足删除条件，执行删除菜品操作
        // 1. 删除菜品
        dishMapper.deteleByids(ids);
        // 2. 删除相关联的口味
        dishFlavorMapper.deleteByDishIds(ids);
    }

    /**
     * 按照Id查询菜品信息和口味信息
     *
     * @param id
     * @return
     */
    @Override
    public DishVO getDishById(Long id) {
        Dish dish = dishMapper.getDishById(id);
        List<DishFlavor> flavor = dishFlavorMapper.getFlavorByDishId(id);
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish, dishVO);
        dishVO.setFlavors(flavor);
        return dishVO;
    }

    /**
     * 修改菜品
     *
     * @param dishDTO
     */
    @Override
    @Transactional
    public void updateDish(DishDTO dishDTO) {
        Dish dish = new Dish();
        List<DishFlavor> flavors = dishDTO.getFlavors();
        BeanUtils.copyProperties(dishDTO, dish);
        // 1. 修改 Dish
        dishMapper.update(dish);
        // 2. 删除原有 DishFlavor
        dishFlavorMapper.deleteByDishIds(Arrays.asList(dishDTO.getId()));
        // 3. 新增 DishFlavor
        if (flavors == null || flavors.size() == 0)
            return;
        for (DishFlavor flavor : flavors) {
            flavor.setDishId(dishDTO.getId());
        }
        dishFlavorMapper.insertDishFlavor(flavors);
    }

    /**
     * 按照分类Id查询菜品信息
     * @param categoryId
     * @return
     */
    @Override
    public List<Dish> getDishByCategoryId(Long categoryId) {
        return dishMapper.getDishByCategoryId(categoryId);
    }


}
