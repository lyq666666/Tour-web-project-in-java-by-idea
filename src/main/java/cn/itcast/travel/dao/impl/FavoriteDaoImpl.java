package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.FavoriteDao;
import cn.itcast.travel.domain.Favorite;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Date;

public class FavoriteDaoImpl implements FavoriteDao {


    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());
    /**
     * 根据rid和uid查询Favorite对象
     * @param rid
     * @param uid
     * @return
     */
    @Override
    public Favorite findByRidAndUid(int rid, int uid) {
        Favorite favorite= null;
        try {
            String sql="select * from tab_favorite where rid = ? and uid = ?";

            favorite = template.queryForObject(sql,new BeanPropertyRowMapper<Favorite>(Favorite.class),rid,uid);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }


        return favorite;
    }

    /**
     * 根据rid查询此路线的收藏次数
     * @param rid
     */
    @Override
    public int findCountByrid(int rid) {
        int count= 0;
        try {
            String sql="select count(*) from tab_favorite where rid = ?";
            count = template.queryForObject(sql,Integer.class,rid);
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
        return count;
    }

    /**
     * 添加收藏记录到数据库中
     * @param rid
     * @param uid
     */
    @Override
    public void add(int rid, int uid) {
        String sql="insert into tab_favorite values(?,?,?)";

        template.update(sql,rid,new Date(),uid);



    }
}
