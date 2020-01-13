package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.impl.UserserviceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.text.Keymap;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet("/loginServlet")
public class loginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
        UserserviceImpl service = new UserserviceImpl();
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
        ObjectMapper mapper = new ObjectMapper();

        response.setContentType("application/json;charset=utf-8");
        mapper.writeValue(response.getOutputStream(),info);
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
