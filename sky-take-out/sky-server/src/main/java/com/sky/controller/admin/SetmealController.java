package com.sky.controller.admin;

import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/setmeal")
@Tag(name="套餐接口")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;

    /**
     * 新增套餐
     * @param setmealDTO
     * @return
     */
    @Operation(summary = "新增套餐")
    @PostMapping
    public Result addSetmeal(@RequestBody SetmealDTO setmealDTO){
        setmealService.insertSetmeal(setmealDTO);
        return Result.success();
    }

    /**
     * 分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    @Operation(summary = "分页查询")
    @GetMapping("/page")
    public Result<PageResult> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO){
        PageResult pageResult = setmealService.getSetmeals(setmealPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 批量删除套餐
     * @param ids
     * @return
     */
    @Operation(summary = "批量删除套餐")
    @DeleteMapping
    public Result deleteSetmeal(@RequestParam List<Long> ids){
        log.info("批量删除套餐：{}", ids);
        setmealService.deleteSetmealByIds(ids);
        return Result.success();
    }

    /**
     * 修改套餐
     * @param setmealDTO
     * @return
     */
    @Operation(summary = "修改套餐")
    @PutMapping
    public Result updateSetmeal(SetmealDTO setmealDTO){
        log.info("修改套餐:{}", setmealDTO);
        setmealService.updateSetmeal(setmealDTO);
        return Result.success();
    }

    /**
     * 根据Id查询套餐
     * @return
     */
    @Operation(summary = "根据Id查询套餐")
    @GetMapping("/{id}")
    public Result getSetmeal(@PathVariable Long id){
        log.info("根据Id查询套餐:{}",id);
        SetmealVO setmealVO= setmealService.getSetmealById(id);
        return Result.success(setmealVO);
    }

    /**
     * 修改套餐售卖状态
     * @param status
     * @param id
     * @return
     */
    @Operation(summary = "修改套餐售卖状态")
    @PostMapping("/status/{status}")
    public Result updateStatus(@PathVariable Integer status, Long id){
        setmealService.updateSetmealStatus(status, id);
        return Result.success();
    }
}
