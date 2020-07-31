package com.atguigu.crowd.api;

import com.atguigu.crowd.entity.ProjectVO;
import com.atguigu.crowd.entity.po.MemberPO;
import com.atguigu.crowd.entity.ResultEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "database-provider")
public interface DataBaseOperationRemoteService {

    /**
     *查看账号是否存在
     */
    @RequestMapping("/retrieve/loign/acct/count")
    ResultEntity<Integer> retrieveLoignAcctCount(@RequestParam("loginAcct") String loginAcct);


    /**
     * 保存用户信息
     * @param memberPO  传入的用户信息
     */
    @RequestMapping("/save/member/remote")
    ResultEntity<String> saveMemberRemote(@RequestBody MemberPO memberPO);


    /**
     * 登录用户检查
     * @param loginAcct  登录账号
     */
    @RequestMapping("/retrieve/member/by/login/acct")
    ResultEntity<MemberPO> retrieveMemberByLoginAcct(@RequestParam("loginAcct") String loginAcct);

    /**
     * 保存众筹项目
     * @param projectVO 项目所有信息
     * @param memberId
     */
    @RequestMapping("/save/project/remote/{memberId}")
    ResultEntity<String> saveProjectRemote(@RequestBody ProjectVO projectVO,@PathVariable("memberId") String memberId);

}
