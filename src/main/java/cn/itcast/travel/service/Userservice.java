package cn.itcast.travel.service;

import cn.itcast.travel.domain.User;

public interface Userservice {
    boolean regist(User user);
    boolean active(String code);

    User login(User user);
}
