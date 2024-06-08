package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.SetmealVO;
import org.springframework.stereotype.Service;

import java.util.List;


public interface SetmealService {

    void insertSetmeal(SetmealDTO setmealDTO);

    PageResult getSetmeals(SetmealPageQueryDTO setmealPageQueryDTO);

    void deleteSetmealByIds(List<Long> ids);

    void updateSetmeal(SetmealDTO setmealDTO);

    SetmealVO getSetmealById(Long id);

    void updateSetmealStatus(Integer status, Long id);
}
