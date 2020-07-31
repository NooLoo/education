package com.atguigu.crowd;

import com.atguigu.crowd.util.CrowdUtils;
import org.junit.Test;

public class test {

    @Test
    public void testSendCode(){
        String appcode="1090c626ea514269b8a250e5a01b6009";
        String randomCode=CrowdUtils.randomCode(4);
        String phoneNum="18858487813";
        CrowdUtils.sendShortMessage(appcode,randomCode,phoneNum);
    }
}
