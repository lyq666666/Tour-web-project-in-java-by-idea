package cn.itcast.travel.dao;

import cn.itcast.travel.domain.User;

public interface UserDao {

    //根据用户名查找用户对象
    public User findByUsername(String username);

    //保存用户信息
    public void save(User user);

    User findBycode(String code);

    void updateStatus(User user);


    User findByUsernameAndPassword(String username, String password);
}
