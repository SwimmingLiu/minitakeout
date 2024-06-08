package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
@Tag(name = "员工管理")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    @Operation(summary = "员工登录")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    @Operation(summary = "员工退出")
    public Result<String> logout() {
        return Result.success();
    }

    /**
     * 新增员工
     *
     * @param employeeDTO
     * @return
     */
    @PostMapping
    @Operation(summary = "新增员工")
    public Result addEmployee(@RequestBody EmployeeDTO employeeDTO) {
        log.info("新增员工: {}", employeeDTO);
        employeeService.addEmployee(employeeDTO);
        return Result.success();
    }

    /**
     * 分页查询员工信息
     * @return
     */
    @GetMapping("/page")
    @Operation(summary = "分页查询员工信息")
    public Result pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
        PageResult employeesByPage = employeeService.pageQuery(employeePageQueryDTO);
        return Result.success(employeesByPage);
    }


    /**
     * 启用、禁用员工账号
     * @param id
     * @param status
     * @return
     */
    @PostMapping("/status/{status}")
    @Operation(summary = "启用、禁用员工账号")
    public Result updateStatus(Long id, @PathVariable("status") Integer status){
        log.info("启用禁用员工账号：{},{}",status,id);
        employeeService.updateStatus(id, status);
        return Result.success();
    }

    /**
     * 根据id查询员工信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @Operation(summary = "根据id查询员工信息")
    public Result getEmployeeById(@PathVariable("id") Long id) {
        log.info("根据id查询员工信息：{}",id);
        Employee employee = employeeService.getEmployeeById(id);
        return Result.success(employee);
    }

    /**
     * 修改员工信息
     * @param employeeDTO
     * @return
     */
    @PutMapping
    @Operation(summary = "修改员工信息")
    public Result updateEmployee(@RequestBody EmployeeDTO employeeDTO) {
        log.info("修改员工信息: {}", employeeDTO);
        employeeService.updateEmployee(employeeDTO);
        return Result.success();
    }

}
