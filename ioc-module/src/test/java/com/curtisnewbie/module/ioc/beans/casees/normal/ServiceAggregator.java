package com.curtisnewbie.module.ioc.beans.casees.normal;

import com.curtisnewbie.module.ioc.annotations.Dependency;
import com.curtisnewbie.module.ioc.annotations.MBean;

/**
 * @author yongjie.zhuang
 */
@MBean
public class ServiceAggregator implements KnowWhoIAm {

    @Dependency
    private AuthenticationManager authenticationManager;

    @Dependency
    private UserServiceImpl userServiceImpl;

    @Dependency
    private Service service;

    public UserServiceImpl getUserServiceImpl() {
        return userServiceImpl;
    }

    public void setUserServiceImpl(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public AuthenticationManager getAuthenticationManager() {
        return authenticationManager;
    }

    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public String toString() {
        return "ServiceAggregator{" +
                "authenticationManager=" + authenticationManager +
                ", userServiceImpl=" + userServiceImpl +
                ", service=" + service +
                '}';
    }
}
