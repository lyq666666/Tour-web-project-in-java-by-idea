package cn.itcast.travel.service;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;

import java.util.List;

/**
 * 线路service
 */
public interface RouteService {
    /**
     *根据类别进行分页查询
     */
    public PageBean pageQuery(int cid, int currentPage, int pageSize, String rname);

    Route findone(String rid);
    List<Integer> findbyUid(int uid);


    public PageBean pageQueryMyfavorite(List<Integer> rid_list);

}
