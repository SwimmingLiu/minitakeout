package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }

    /**
     * 新增员工
     * @param employeeDTO
     */
    @Override
    public void addEmployee(EmployeeDTO employeeDTO) {
        // 创建员工对象
        Employee employee = new Employee();

        // 1. Copy EmpDTO ==> Emp对象
        BeanUtils.copyProperties(employeeDTO,employee);
        //设置账号的状态，默认正常状态 1表示正常 0表示锁定
        employee.setStatus(StatusConstant.ENABLE);
        // 对密码进行加密
        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));
        // 2. 增加创建时间和更新时间
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
        // 3. TODO 设置当前记录创建人id和修改人id
//        employee.setCreateUser(BaseContext.getCurrentId());//目前写个假数据，后期修改
//        employee.setUpdateUser(BaseContext.getCurrentId());
        // 4. 调用Mapper
        employeeMapper.insertEmployee(employee);
    }

    /**
     * 分页查询员工信息
     * @param employeePageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
        // 初始化分页查询结果
        PageResult pageResult = new PageResult();
        // 1. PageHelper 初始化
        PageHelper.startPage(employeePageQueryDTO.getPage(), employeePageQueryDTO.getPageSize());
        // 2. 查询数据
        Page<Employee> page = employeeMapper.getByName(employeePageQueryDTO.getName());
        // 3. 封装为PageResult
        long total = page.getTotal();
        pageResult.setRecords(page.getResult());
        pageResult.setTotal(total);
        return pageResult;
    }

    /**
     * 启用、禁用员工账号
     * @param id
     * @param status
     */
    @Override
    public void updateStatus(Long id, Integer status) {
        // 修改员工账号状态
        Employee employee = Employee.builder().
                id(id).
                status(status)
                .build();
        employeeMapper.update(employee);
    }

    /**
     * 根据员工id查询员工信息
     * @param id
     * @return
     */
    @Override
    public Employee getEmployeeById(Long id) {
        Employee employee = employeeMapper.getById(id);
        return employee;
    }

    /**
     * 修改员工信息
     * @param employeeDTO
     */
    @Override
    public void updateEmployee(EmployeeDTO employeeDTO) {
        // 1. Copy EmpDTO ==> Emp对象
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO,employee);
        // 2. 修改更新时间
//        employee.setUpdateTime(LocalDateTime.now());
        // 3. 修改更新用户
//        employee.setUpdateUser(BaseContext.getCurrentId());
        // 4. 调用Mapper
        employeeMapper.update(employee);
    }

}
