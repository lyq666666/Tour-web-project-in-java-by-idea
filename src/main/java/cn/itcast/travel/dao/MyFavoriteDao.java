package cn.itcast.travel.dao;

import java.util.List;

/**
 * 我的喜欢myFavorite
 */
public interface MyFavoriteDao {
    public List<Integer> findUser_rid(int uid);
}
