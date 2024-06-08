package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.annotation.AutoFill;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.enumeration.OperationType;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;

    /**
     * 新增套餐
     * @param setmealDTO
     */
    @Override
    @Transactional
    @AutoFill(value = OperationType.INSERT)
    public void insertSetmeal(SetmealDTO setmealDTO) {
        // setmealDTO 转换为 setmeal 和 setmealDish
        Setmeal setmeal = new Setmeal();
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        // 1. 新增套餐
        setmealMapper.insertSetmeal(setmeal);
        // 2. 新增套餐菜品
        for (SetmealDish setmealDish : setmealDishes) {
            setmealDish.setSetmealId(setmeal.getId());
        }
        setmealDishMapper.insertSetmealDish(setmealDishes);
    }

    /**
     * 分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    @Override
    public PageResult getSetmeals(SetmealPageQueryDTO setmealPageQueryDTO) {
        // 设置页码范围
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
        // 获取套餐信息
        Page<SetmealVO> result = setmealMapper.getSetmeals(setmealPageQueryDTO);
        return new PageResult(result.getTotal(),result.getResult());
    }

    /**
     * 批量删除套餐
     * @param ids
     */
    @Override
    @Transactional
    public void deleteSetmealByIds(List<Long> ids) {
        // 判断套餐是否在售
        for (Long id : ids) {
            Setmeal setmeal = setmealMapper.getSetmealById(id);
            if (setmeal.getStatus() == StatusConstant.ENABLE){
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        }
        // 1. 删除套餐
        setmealMapper.deleteSetmealByIds(ids);
        // 2. 删除套餐菜品关联信息
        setmealDishMapper.deleteSetmealDishBySetmealId(ids);
    }

    /**
     * 修改套餐
     * @param setmealDTO
     */
    @Override
    @Transactional
    @AutoFill(value = OperationType.UPDATE)
    public void updateSetmeal(SetmealDTO setmealDTO) {
        // setmealDTO ==> setmeal
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        // 1. 修改 setmeal
        setmealMapper.updateSetmeal(setmeal);
        // 2. 删除 setmealDish
        setmealDishMapper.deleteSetmealDishBySetmealId(Arrays.asList(setmeal.getId()));
        // 3. 新增 setmealDish
        if (setmealDishes == null || setmealDishes.size() == 0){
            return;
        }
        setmealDishMapper.insertSetmealDish(setmealDishes);

    }

    /**
     * 根据id查询套餐
     * @param id
     * @return
     */
    @Override
    public SetmealVO getSetmealById(Long id) {
        // 查询 套餐和套餐菜品关系表
        Setmeal setmeal = setmealMapper.getSetmealById(id);
        List<SetmealDish> setMealDishes = setmealDishMapper.getsetMealDishBySetmealId(Arrays.asList(id));
        SetmealVO setmealVO = new SetmealVO();
        BeanUtils.copyProperties(setmeal,setmealVO);
        setmealVO.setSetmealDishes(setMealDishes);
        return setmealVO;
    }

    @Override
    public void updateSetmealStatus(Integer status, Long id) {
        Setmeal setmeal = Setmeal.builder().
                status(status).
                id(id).
                build();
        setmealMapper.updateSetmeal(setmeal);
    }
}
