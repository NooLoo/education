package com.atguigu.crowd.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberVO {

    //用户名
    private String loginacct;

    //用户密码
    private String userpswd;

    //手机号
    private String phoneNum;

    //验证码
    private String randomCode;

}
