package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.FavoriteService;
import cn.itcast.travel.service.RouteService;
import cn.itcast.travel.service.impl.FavoriteServiceImpl;
import cn.itcast.travel.service.impl.RouteServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/route/*")
public class RouteServlet extends BaseServlet {
    private RouteService routeService = new RouteServiceImpl();
    private FavoriteService favoriteService=new FavoriteServiceImpl();

    public void f() {
        System.out.println("hahaha");
    }

    /**
     * 分页查询
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void pageQuery(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.接收参数
        String currentPageStr = request.getParameter("currentPage");
        String pageSizeStr = request.getParameter("pageSize");
        String cidStr = request.getParameter("cid");
        //接受线路名称
        String rname = request.getParameter("rname");


        rname = new String(rname.getBytes("iso-8859-1"), "utf-8");
        //rname = new String(rname.getBytes("iso-8859-1"),"utf-8");

        if (rname.equals("null")) {
            rname = "";
        }
        System.out.println("rname is  :" + rname);
        //2.处理参数
        int cid = 0;//类别id
        if (cidStr != null && cidStr.length() > 0 && !"null".equals(cidStr)) {
            cid = Integer.parseInt(cidStr);
        }

        int currentPage = 0;//当前页码，第一次访问不传递此参数时，显示第一页
        if (currentPageStr != null && currentPageStr.length() > 0) {
            currentPage = Integer.parseInt(currentPageStr);
        } else {
            currentPage = 1;
        }


        int pageSize = 0;//每页显示条数,第一次访问不传递此参数时，每页显示5条记录
        if (pageSizeStr != null && pageSizeStr.length() > 0) {
            pageSize = Integer.parseInt(pageSizeStr);
        } else {
            pageSize = 5;
        }


        //调用service查询PageBean对象
        PageBean<Route> pageBean = routeService.pageQuery(cid, currentPage, pageSize, rname);

        //将pageBean对象序列化为json数据并返回客户端
        writeValue(pageBean, response);
    }


    /**
     * 根据id查询一个旅游线路的详细信息
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void findone(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1.获取rid值
        String rid = request.getParameter("rid");
        //System.out.println("---传送到servlet中的rid为： "+rid);
        //2.调用service查询route对象
        Route route = routeService.findone(rid);

        //3.转换为json数据写回客户端
        writeValue(route,response);

    }


    /**
     * 判断是否已经被收藏了
     * @param request
     * @param response
     */
    public void isFavorite(HttpServletRequest request, HttpServletResponse response)
    {
        //1.获取rid的值
        String rid =request.getParameter("rid");
        //2.获取当前用户
        User user = (User) request.getSession().getAttribute("user");
        int uid;//用户id值
        if(user==null)
        {
            //用户未登录
            uid=0;
        }
        else
        {
            uid=user.getUid();
        }
        //3调用FavoriteService查询是否被收藏
        boolean flag = favoriteService.isFavorite(rid, uid);

        //将flag标记写回客户端
        try {
            writeValue(flag,response);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 点击收藏按钮之后，将数据存入数据库
     * @param request
     * @param response
     */
    public void addFavorite(HttpServletRequest request, HttpServletResponse response)
    {
        //1.获取线路rid
        String rid = request.getParameter("rid");

        //2.获取当前登录的user：对应的uid
        User user = (User) request.getSession().getAttribute("user");
        int uid;//用户id值
        if(user==null)
        {
            //用户未登录
           return;
        }
        else
        {
            uid=user.getUid();
        }

        //调用service层
        favoriteService.add(rid,uid);


    }
    public void ShowMyfavorite(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //1.获取当前的user的uid
        User user = (User) request.getSession().getAttribute("user");

        int uid;//用户id值
        if(user==null)
        {
            //用户未登录
            return;
        }
        else
        {
            uid=user.getUid();
        }
        System.out.println("当前user的uid是："+uid);
        //2.根据uid查询对应用户中的favorite中的所有景点的rid list集合
        List<Integer> rid_list;
        rid_list = routeService.findbyUid(uid);
        System.out.println("rid_list:"+rid_list);
        //获取此rid list对应的所有数据,封装在pageBean中
        PageBean<Route> pageBean =routeService.pageQueryMyfavorite(rid_list);

        //3.将此用户的favorite数据list全部写回客户端
        writeValue(pageBean,response);
    }






}
