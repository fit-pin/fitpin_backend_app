package com.mzm.Fitpin.mapper;

import com.mzm.Fitpin.entity.UserForm;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserFormMapper {
    void insertUserForm(UserForm userForm);

    //@Select("SELECT fileName FROM userForm WHERE userEmail = #{userEmail}")
    String findFileNameByUserEmail(String userEmail);
}
