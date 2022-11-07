package com.sso.model.dtos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LoginDto {

    @ApiModelProperty(value = "用户名",required = true)
    private String username;

    @ApiModelProperty(value = "密码",required = true)
    private String password;

}
