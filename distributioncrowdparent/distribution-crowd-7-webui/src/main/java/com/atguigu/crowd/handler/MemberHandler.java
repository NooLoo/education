package com.atguigu.crowd.handler;

import com.atguigu.crowd.api.MemberMangerRemoteService;
import com.atguigu.crowd.entity.MemberSignSuccessVO;
import com.atguigu.crowd.entity.MemberVO;
import com.atguigu.crowd.entity.ResultEntity;
import com.atguigu.crowd.util.CrowdConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
public class MemberHandler {

    @Autowired
    private MemberMangerRemoteService memberMangerRemoteService;

    /**
     * 退出登录
     */
    @RequestMapping("/member/logout")
    public String loginOut(HttpSession session){

        MemberSignSuccessVO memberSignSuccessVO = (MemberSignSuccessVO)session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_MESSAGE);
        String token = memberSignSuccessVO.getToken();

        ResultEntity<String> logout = memberMangerRemoteService.logout(token);

        session.invalidate();

        return "redirect:/index.html";
    }

    /**
     * 用户登录
     */
    @RequestMapping("/member/do/login")
    public String doLogin(MemberVO memberVO, Model model, HttpSession session) {

        String loginacct = memberVO.getLoginacct();
        String userpswd = memberVO.getUserpswd();

        ResultEntity<MemberSignSuccessVO> login = memberMangerRemoteService.login(loginacct, userpswd);

        //检查远程方法调用结果
        String result = login.getResult();

        if (ResultEntity.FAILED.equals(result)) {
            String message = login.getMessage();
            model.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,message);

            return "member-login";
        }

        MemberSignSuccessVO memberSignSuccessVO = login.getData();

        session.setAttribute(CrowdConstant.ATTR_NAME_LOGIN_MESSAGE,memberSignSuccessVO);

        return "member-center";
    }

}
