package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.dao.impl.UserDaoImpl;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.Userservice;
import cn.itcast.travel.util.MailUtils;
import cn.itcast.travel.util.UuidUtil;

public class UserserviceImpl implements Userservice {
    private UserDao userDao = new UserDaoImpl();


    @Override
    public boolean regist(User user) {

        //查询用户名对象
        User u = userDao.findByUsername(user.getUsername());
        //判断u是否为null
        if(u!=null)
        {//用户名已经被注册过了
            return false;
        }


        //2.保存用户信息
        //2.1设置激活码
        user.setCode(UuidUtil.getUuid());
//        System.out.println(user.getCode());
        //2.2设置激活状态（N代表未激活）
        user.setStatus("N");
        userDao.save(user);

        //激活邮件发送
        //邮件正文
        String content = "<a href='http://localhost/travel/user/active?code="+user.getCode()+"'>点击此处进行旅游网激活</a>";
        MailUtils.sendMail(user.getEmail(), content, "激活邮件");

        return true;


    }

    /*
    激活用户
     */
    @Override
    public boolean active(String code) {

        //1.根据激活码查询用户对象
        User user=userDao.findBycode(code);

        if(user !=null) {
            //2.调用dao修改激活状态
            userDao.updateStatus(user);
            return true;
        }
        else {
            //此激活码对应的用户不存在
            return false;
        }
    }

    @Override
    public User login(User user) {
        return userDao.findByUsernameAndPassword(user.getUsername(),user.getPassword());
    }
}
