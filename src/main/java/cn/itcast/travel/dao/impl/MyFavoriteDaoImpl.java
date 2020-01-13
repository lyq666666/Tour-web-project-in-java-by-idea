package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.MyFavoriteDao;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class MyFavoriteDaoImpl implements MyFavoriteDao {
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());
    @Override
    public List<Integer> findUser_rid(int uid) {
        List<Integer> rid_list=null;
        try {
            String sql = "select rid from tab_favorite where uid = ?";
           rid_list = template.queryForList(sql,Integer.class, uid);
        } catch (Exception e) {
            e.printStackTrace();
        }



        return rid_list;
    }
}
