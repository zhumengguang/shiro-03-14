package cn.itcast.ssm.shiro;

import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @Author: zhumenguang
 * @Description: 认证之前进行，验证码效验
 * @Date: Created in 15:55 2018/3/19
 * @Modified By:
 */
public class CustomFormAuthenticationFilter extends FormAuthenticationFilter {


    //原FormAuthenticationFilter认证方法
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {

        //在这里进行验证码的效验，如果校验失败
        //从session中获取正确的验证码
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpSession session = httpServletRequest.getSession();
        //取出session的验证码（正确的验证码）
        String validateCode = (String) session.getAttribute("validateCode");

        //取出页面的验证码
        //输入验证码和session中的验证进行对比
        String randomcode = httpServletRequest.getParameter("randomcode");

        if(randomcode!=null&&validateCode!=null&&!randomcode.equals(validateCode)){

            httpServletRequest.setAttribute("shiroLoginFailure","randomCodeError");

            //拒绝访问，不在效验账号和密码
            return true;
        }


        return super.onAccessDenied(request, response);
    }
}
