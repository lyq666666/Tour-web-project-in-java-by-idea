package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.*;
import cn.itcast.travel.dao.impl.*;
import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.RouteImg;
import cn.itcast.travel.domain.Seller;
import cn.itcast.travel.service.RouteService;

import java.util.ArrayList;
import java.util.List;

public class RouteServiceImpl implements RouteService {
    private RouteDao routeDao = new RouteDaoImpl();
    private RouteImgDao routeImgDao=new RouteImageDaoImpl();
   private SellerDao sellerDao =new SellerDaoImpl();
   private FavoriteDao favoriteDao =new FavoriteDaoImpl();
   private MyFavoriteDao myFavoriteDao =new MyFavoriteDaoImpl();
    @Override
    public PageBean pageQuery(int cid, int currentPage, int pageSize, String rname) {


        //封装PageBean
        PageBean<Route> pb = new PageBean<Route>();

        //设置当前页码
        pb.setCurrentPage(currentPage);

        //设置每页显示条数
        pb.setPageSize(pageSize);

        //设置总记录数
        int totalcount = routeDao.findTotalCount(cid,rname);
        pb.setTotalCount(totalcount);
        //设置当页显示的数据集合
        //start是当前的开始位置
        int start = (currentPage - 1) * pageSize;
        List<Route> list = routeDao.findByPage(cid, start, pageSize,rname);
        pb.setList(list);

        //设置总页数
        int totalPage = totalcount % pageSize == 0 ? totalcount / pageSize : (totalcount / pageSize) + 1;
        pb.setTotalPage(totalPage);


        return pb;
    }





    @Override
    public Route findone(String rid) {
        //根据id去查询route表中的route对象
        Route route =routeDao.findone(Integer.parseInt(rid));
       // System.out.println("当前的路线的rid:"+route.getRid());
        //根据route 的id查询此旅游商品的图片集合信息
        List<RouteImg> routeImgList = routeImgDao.findByRid(route.getRid());
        //将图片集合设置到route对象中
        route.setRouteImgList(routeImgList);


        //根据route的sid(商家id)查询商家对象
        Seller seller = sellerDao.findById(route.getSid());

        //将商家对象设置到route对象中
        route.setSeller(seller);

        //查询此线路被收藏的次数
        int count = favoriteDao.findCountByrid(route.getRid());
        route.setCount(count);

        return route;
    }

    @Override
    public List<Integer> findbyUid(int uid) {
        List<Integer> rid_list =null;
        rid_list = myFavoriteDao.findUser_rid(uid);

        return rid_list;
    }

    @Override
    public PageBean pageQueryMyfavorite(List<Integer> rid_list) {
        //封装PageBean
        PageBean<Route> pb = new PageBean<Route>();

        List<Route> list=new ArrayList<Route>();
        //设置当页显示的数据集合
        for (Integer rid : rid_list) {
            int Rid=rid.intValue();
            list.add(routeDao.findone(Rid));

        }
        pb.setList(list);


        return pb;

    }
}
