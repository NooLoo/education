package com.atguigu.crowd.service.api;

import com.atguigu.crowd.entity.po.MemberPO;

public interface MemberService {

    Integer getLoginAcctCount(String loginAcct);

    void saveMemberPO(MemberPO memberPO);


    MemberPO getMemberbyLoginAcct(String loginAcct);
}
