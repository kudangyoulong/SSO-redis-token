package com.sso.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sso.model.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {

}
