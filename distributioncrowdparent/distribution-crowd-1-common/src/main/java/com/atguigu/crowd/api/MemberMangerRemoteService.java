package com.atguigu.crowd.api;

import com.atguigu.crowd.entity.MemberSignSuccessVO;
import com.atguigu.crowd.entity.MemberVO;
import com.atguigu.crowd.entity.ResultEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "member-manager")
public interface MemberMangerRemoteService {

    /**
     * 登出功能
     */
    @RequestMapping("/member/logout")
    public ResultEntity<String> logout(@RequestParam("token") String token);

    /**
     * 登录功能
     */
    @RequestMapping("/member/login")
    public ResultEntity<MemberSignSuccessVO> login(@RequestParam("loginacct") String loginacct, @RequestParam("userpswd") String userpswd);

    /**
     * 注册功能
     */
    @RequestMapping("/member/manage/register")
    public ResultEntity<String> register(@RequestBody MemberVO memberVO);


    /**
     * 发送手机验证码
     * @param phoneNum  所要发送的手机号
     */
    @RequestMapping("/member/manage/send/code")
    public ResultEntity<String> sendCode(@RequestParam("phoneNum") String phoneNum);

}
