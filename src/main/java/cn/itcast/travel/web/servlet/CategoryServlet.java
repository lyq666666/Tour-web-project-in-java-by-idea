package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.Category;
import cn.itcast.travel.service.CategoryService;
import cn.itcast.travel.service.impl.CategoryServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/category/*")
public class CategoryServlet extends BaseServlet {

    private CategoryService service = new CategoryServiceImpl();



    /**
     * 查寻所有目录

     * @throws ServletException
     * @throws IOException
     */

    public void f()
    {
        System.out.println("hahhah");
    }
    public void findAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.带哦用service查询所有
        List<Category> cs = service.findAll();


        //2.序列化为json数据，并将数据返回客户端
        writeValue(cs,response);
    }
}
