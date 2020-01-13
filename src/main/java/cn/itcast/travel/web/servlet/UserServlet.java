package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.Userservice;
import cn.itcast.travel.service.impl.UserserviceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet("/user/*")
public class UserServlet extends BaseServlet {

    //service业务对象
   private UserserviceImpl service = new UserserviceImpl();


    /**
     * 注册
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void regist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //验证码校验
        //获取输入的验证码
        String check = request.getParameter("check");
        //从session中获取验证码图片中生成的验证码
        HttpSession session = request.getSession();
        String checkcode_server = (String) session.getAttribute("CHECKCODE_SERVER");

        //移除当前验证码，保证每个验证码只能用一次
        session.removeAttribute("CHECKCODE_SERVER");

        //比较
        if(checkcode_server==null||!checkcode_server.equalsIgnoreCase(check))
        {
            //验证码错误
            ResultInfo Info = new ResultInfo();
            Info.setFlag(false);
            Info.setErrorMsg("验证码错误");
            //将info对象序列化为json

            String json = writeValueAsString(Info,response);

            //将json数据写回客户端

            response.getWriter().write(json);
            return ;
        }



        //1.获取数据
        Map<String, String[]> map = request.getParameterMap();

        //2.封装对象
        User user =new User();
        try {
            //将map中的数据封装到user对象中
            BeanUtils.populate(user,map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        //3.调用service完成注册
       // Userservice service = new UserserviceImpl();
        boolean flag= service.regist(user);
        ResultInfo info = new ResultInfo();

        //4.响应结果
        if(flag)
        {//注册成功
            info.setFlag(true);
        }
        else
        {
            info.setFlag(false);
            info.setErrorMsg("注册失败！！！");

        }
        System.out.println("展示响应结果：flag为true则表示注册成功!!!");
        System.out.println(flag);
        //将info对象序列化为json

        String json = writeValueAsString(info,response);

        //将json数据写回客户端

        response.getWriter().write(json);

    }

    /**
     * 登录
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取用户名和密码数据
        final Map<String, String[]> map = request.getParameterMap();
        //封装user对象(将用户名与密码封装为一个简单版的user对象)
        User user = new User();
        try {
            BeanUtils.populate(user,map );
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }


        //调用service查询
       // UserserviceImpl service = new UserserviceImpl();
        User u=service.login(user);
        ResultInfo info = new ResultInfo();

        //4.判断
        //判断用户名和密码对应的用户是否存在
        if(u==null)
        {
            //用户名或密码错误
            info.setFlag(false);
            info.setErrorMsg("用户名或密码错误");

        }
        //判断用户是否激活
        if(u!=null && !"Y".equals(u.getStatus()))
        {
            //用户未激活
            info.setFlag(false);
            info.setErrorMsg("亲爱的，您尚未激活，请激活");

        }
        //登录成功
        if(u!=null && "Y".equals(u.getStatus()))
        {
            //登录成功则将user数据对象存入session中
            request.getSession().setAttribute("user",u);
            info.setFlag(true);
        }

        //响应数据
       writeValue(info,response);

    }
    /**
     * 查询一个对象
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void findone(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //从session中获取user对象
        Object user = request.getSession().getAttribute("user");

        //将user写回客户端
       writeValue(user,response);

    }
    /**
     * 退出
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void exit(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //销毁session，即消除了里面的user对象
        request.getSession().invalidate();
        //用户退出,重定向到登录页面
        response.sendRedirect(request.getContextPath()+"/login.html");

    }
    /**
     * 激活功能
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void active(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //获取激活码
        String code = request.getParameter("code");
        //调用service层进行
       // Userservice service = new UserserviceImpl();
        boolean flag = service.active(code);
        //3，判断标记
        String msg=null;
        if(flag)
        {//激活成功
            msg="激活成功，请<a href='login.html'>点击此处进行登录</a>";
        }
        else
        {
            //激活失败
            msg="激活失败，请联系后台管理员";

        }
        //将数据写回前端
        response.setContentType("text/html;charset=utf-8");
        response.getWriter().write(msg);
    }
}
