package cn.itcast.travel.web.servlet;

import cn.itcast.travel.service.Userservice;
import cn.itcast.travel.service.impl.UserserviceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/activeUserServlet")
public class activeUserServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取激活码
        String code = request.getParameter("code");
        //调用service层进行
        Userservice service = new UserserviceImpl();
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

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
