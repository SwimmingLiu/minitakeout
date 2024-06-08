package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.mapper.DishMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/dish")
@Slf4j
@Tag(name = "菜品接口")
public class DishController {
    @Autowired
    private DishService dishService;
    @Autowired
    private DishMapper dishMapper;

    /**
     * 新增菜品
     *
     * @param dishDTO
     * @return
     */
    @Operation(summary = "新增菜品")
    @PostMapping
    public Result addDish(@RequestBody DishDTO dishDTO) {
        log.info("新增菜品：{}", dishDTO);
        dishService.addDish(dishDTO);
        return Result.success();
    }

    /**
     * 分页查询菜品
     *
     * @param dishPageQueryDTO
     * @return
     */
    @Operation(summary = "查询菜品")
    @GetMapping("/page")
    public Result<PageResult> pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        log.info("查询菜品:{}", dishPageQueryDTO);
        PageResult pageResult = dishService.getDishs(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 批量删除菜品
     * @param ids
     * @return
     */
    @Operation(summary = "批量删除菜品")
    @DeleteMapping
    public Result deleteDishByIds(@RequestParam List ids) {
        log.info("批量删除菜品Id: {}", ids);
        dishService.deleteDishByIds(ids);
        return Result.success();
    }

    /**
     * 按照Id查询菜品信息
     * @param id
     * @return
     */
    @Operation(summary = "按照Id查询菜品信息")
    @GetMapping("/{id}")
    public Result<DishVO> getDishById(@PathVariable Long id){
        log.info("按照Id查询菜品信息: {}", id);
        DishVO dishvo = dishService.getDishById(id);
        return Result.success(dishvo);
    }

    /**
     * 修改菜品
     * @param dishDTO
     * @return
     */
    @Operation(summary = "修改菜品")
    @PutMapping
    public Result updateDish(@RequestBody DishDTO dishDTO){
        log.info("修改菜品: {}",dishDTO);
        dishService.updateDish(dishDTO);
        return Result.success();
    }

    /**
     * 修改售卖状态
     *
     * @param status
     * @param id
     * @return
     */
    @Operation(summary = "修改售卖状态")
    @PostMapping("/status/{status}")
    public Result updateStatus(@PathVariable Integer status, Long id) {
        log.info("修改售卖状态:{},{}", id, status);
        dishService.updateStatus(id, status);
        return Result.success();
    }

    /**
     * 按照分类Id查询菜品信息
     * @param categoryId
     * @return
     */
    @Operation(summary = "按照分类Id查询菜品信息")
    @GetMapping("/list")
    public Result list(Long categoryId){
        log.info("按照分类Id查询菜品信息: {}", categoryId);
        List<Dish> dishs = dishService.getDishByCategoryId(categoryId);
        return Result.success(dishs);
    }

}
