package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.CategoryDao;
import cn.itcast.travel.dao.impl.CategoryDaoImpl;
import cn.itcast.travel.domain.Category;
import cn.itcast.travel.service.CategoryService;
import cn.itcast.travel.util.JedisUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CategoryServiceImpl implements CategoryService {

    CategoryDao category = new CategoryDaoImpl();

    @Override
    public List<Category> findAll() {
        //从redis中查询
        //1.1获取jedis客户端
        Jedis jedis = JedisUtil.getJedis();
        //1.2可使用sortedset进行排序查询
       // Set<String> categorys = jedis.zrange("category", 0, -1);
        //根据分数进行查询
        Set<Tuple> categorys = jedis.zrangeWithScores("category", 0, -1);
        //定义一个list数组存放最后返回的Category数据
        List<Category> cs = null;
        //判断查询的集合是否为空
        if (categorys == null || categorys.size() == 0) {
            System.out.println("从数据库中查询");
            //集合为空，从数据库中查询，并将数据存入redis中
            cs = category.findAll();
            for (int i = 0; i < cs.size(); i++) {
                jedis.zadd("category", cs.get(i).getCid(), cs.get(i).getCname());

            }

        } else {
            System.out.println("从redis中查询");
            //集合不为空，将set集合转换为list
            cs=new ArrayList<Category>();
            for (Tuple tuple : categorys) {
                Category category = new Category();
                category.setCname(tuple.getElement());
                category.setCid((int)tuple.getScore());
                cs.add(category);
            }
        }

        //返回
        return cs;
    }
}
