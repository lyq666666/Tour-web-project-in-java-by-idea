package cn.itcast.travel.web.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@WebServlet("/BaseServlet")
public class BaseServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // System.out.println("baseServlet的service方法被执行了...");

        //完成方法分发
        //1.获取请求路径
        String uri = req.getRequestURI();// /travel/user/add
        //System.out.println("请求uri" + uri);

        //2.获取方法名称
        String methodName = uri.substring(uri.lastIndexOf('/') + 1);
//        System.out.println("方法名称" + methodName);
        //打印this对象，应该是UserServlet对象
//        System.out.println(this);
        //3.获取方法对象Method
        try {
            Method method = this.getClass().getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);

            //4.执行方法
            /*//暴力反射
            method.setAccessible(true);
    */
            method.invoke(this, req, resp);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }


    }

    /**
     * 将传入的对象序列化为json，写回客户端
     */
    public void writeValue(Object o,HttpServletResponse response) throws IOException {
        ObjectMapper mapper =new ObjectMapper();
        response.setContentType("application/json;charset=utf-8");

        mapper.writeValue(response.getOutputStream(),o);

    }

    /**
     * 将传入的对象序列化为String类型的json数据，返回客户端
     *
     * @return
     */
    public String writeValueAsString(Object o,HttpServletResponse response) throws IOException {
        ObjectMapper mapper =new ObjectMapper();
        response.setContentType("application/json;charset=utf-8");

        return mapper.writeValueAsString(o);

    }

}
