package com.atguigu.crowd;

import com.atguigu.crowd.util.CrowdUtils;
import org.junit.Test;

public class test {

    @Test
    public void testSendCode(){
        String appcode="1090c626ea514269b8a250e5a01b6009";
        String randomCode=CrowdUtils.randomCode(6);
        String phoneNum="13587038526";
        CrowdUtils.sendShortMessage(appcode,randomCode,phoneNum);
    }
}
