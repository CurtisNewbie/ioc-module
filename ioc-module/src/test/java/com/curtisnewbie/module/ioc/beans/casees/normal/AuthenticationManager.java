package com.curtisnewbie.module.ioc.beans.casees.normal;

import com.curtisnewbie.module.ioc.annotations.Dependency;
import com.curtisnewbie.module.ioc.annotations.MBean;

/**
 * @author yongjie.zhuang
 */
@MBean
public class AuthenticationManager extends AbstractManager implements KnowWhoIAm {

    @Dependency
    private UserService userService;

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String toString() {
        return "AuthenticationManager{" +
                "userService=" + userService +
                '}';
    }
}
