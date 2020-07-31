package com.atguigu.crowd.service.impl;

import com.atguigu.crowd.entity.po.MemberPO;
import com.atguigu.crowd.entity.po.MemberPOExample;
import com.atguigu.crowd.mapper.MemberPOMapper;
import com.atguigu.crowd.service.api.MemberService;
import com.atguigu.crowd.util.CrowdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) //设置为只读
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberPOMapper memberPOMapper;

    public Integer getLoginAcctCount(String loginAcct) {

        MemberPOExample example = new MemberPOExample();
        example.createCriteria().andLoginacctEqualTo(loginAcct);

        return memberPOMapper.countByExample(example);
    }


    /**
     * 保存注册信息
     * @param memberPO 注册信息
     */
    @Transactional(readOnly = false,propagation= Propagation.REQUIRES_NEW, rollbackFor=Exception.class)
    public void saveMemberPO(MemberPO memberPO) {
        memberPOMapper.insert(memberPO);
    }


    /**
     * 获得登录账号信息
     */
    public MemberPO getMemberbyLoginAcct(String loginAcct) {

        // 1.创建MemberPOExample对象
        MemberPOExample example = new MemberPOExample();
        // 2.封装查询条件
        example.createCriteria().andLoginacctEqualTo(loginAcct);
        // 3.执行QBC查询
        List<MemberPO> memberList = memberPOMapper.selectByExample(example);
        // 4.判断查询结果集合的有效性
        if(CrowdUtils.collectionEffectiveCheck(memberList)) {
            // 5.如果查询结果集合有效，则返回第一个元素
            return memberList.get(0);
        }
        // 6.如果集合无效则返回null
        return null;
    }

}
