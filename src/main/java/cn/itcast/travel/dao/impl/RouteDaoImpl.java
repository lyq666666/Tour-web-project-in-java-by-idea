package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

public class RouteDaoImpl implements RouteDao {
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    /**
     * 根据cid查询总记录数
     *
     * @param cid
     * @param rname
     * @return
     */
    @Override
    public int findTotalCount(int cid,String rname) {
        //String sql = "select count(*) from tab_route where cid = ? and rname =?";
        //1.定义sql模板
        String sql = "select count(*) from tab_route where 1=1 ";
        StringBuilder sb = new StringBuilder(sql);

        List params = new ArrayList();//条件们
        //2.判断参数是否有值
        if(cid != 0){
            sb.append( " and cid = ? ");

            params.add(cid);//添加？对应的值
        }

//        if(rname.equals("null"))
//        {
//            rname="";
//        }
        System.out.println("current rname is :"+rname);
        if(rname != null && rname.length() > 0 && !rname.equals("null")){
            sb.append(" and rname like ? ");

            params.add("%"+rname+"%");
            System.out.println("####rname有值的呀####");
            System.out.println("####rname is :"+rname);
        }

        sql = sb.toString();
        System.out.println("sql is :"+sql);
        System.out.println("---参数输出如下:");
        for (Object param : params) {
            System.out.println(param);
        }


        return template.queryForObject(sql,Integer.class,params.toArray());
    }

    @Override
    public List<Route> findByPage(int cid, int start, int pageSize,String rname) {
       // String sql = "select * from tab_route where cid = ? and rname like ?  limit ? , ?";
        String sql = " select * from tab_route where 1 = 1 ";
        //1.定义sql模板
        StringBuilder sb = new StringBuilder(sql);

        List params = new ArrayList();//条件们
//        if(rname.equals("null"))
//        {
//            rname="";
//        }
        //2.判断参数是否有值
        if(cid != 0){
            sb.append( " and cid = ? ");

            params.add(cid);//添加？对应的值
        }

        if(rname != null && rname.length() > 0){
            sb.append(" and rname like ? ");

            params.add("%"+rname+"%");
        }
        sb.append(" limit ? , ? ");//分页条件

        sql = sb.toString();

        params.add(start);
        params.add(pageSize);

        //return template.query(sql,new BeanPropertyRowMapper<Route>(Route.class),cid,rname,start,pageSize);

        return template.query(sql,new BeanPropertyRowMapper<Route>(Route.class),params.toArray());
    }

    @Override
    public Route findone(int rid) {
        String sql="select * from tab_route where rid = ?";
        return template.queryForObject(sql,new BeanPropertyRowMapper<Route>(Route.class),rid);
    }

    @Override
    public int findRid(int uid) {
        return 0;
    }

    @Override
    public Route find_favByPage(Integer rid) {
        int Rid =rid.intValue();
        System.out.println("RouteDao中的rid："+Rid);
        Route route=new Route();
        try {
            String sql = "select * from tab_route where rid = ?";
            route=template.queryForObject(sql,new BeanPropertyRowMapper<Route>(Route.class),Rid);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return route;
    }

}
