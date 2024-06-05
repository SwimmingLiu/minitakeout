package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.entity.Employee;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface EmployeeMapper {

    /**
     * 新增员工
     *
     * @param employee
     */
    @Insert("insert into employee(id_number,username, password, name, sex, phone, status, create_time, update_time, create_user, update_user) " +
            "values(#{idNumber}, #{username}, #{password}, #{name},#{sex}, #{phone}, #{status}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
    void insertEmployee(Employee employee);

    /**
     * 根据用户名查询员工
     *
     * @param username
     * @return
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);

    /**
     * 根据姓名查询员工
     *
     * @param name
     * @return
     */
    Page<Employee> getByName(String name);
}
