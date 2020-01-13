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

@WebServlet("/registUserServlet")
public class registUserServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(Info);

            //将json数据写回客户端
            response.setContentType("application/json;charset=utf-8");
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
        Userservice service = new UserserviceImpl();
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
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(info);

        //将json数据写回客户端
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(json);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
